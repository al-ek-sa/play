package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.dto.HitRequestDto;
import org.example.service.HitPackageService;
import org.example.service.HitService;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebServlet("/internal/area-check/*")
public class AreaCheckServlet extends HttpServlet {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int MAX_BATCH_SIZE = 500;

    @Inject
    private HitService hitService;

    @Inject
    private HitPackageService hitPackageService;

    @Inject
    private Validator validator;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        prepareResponse(response);

        var session = request.getSession(false);

        if (session == null) {
            sendError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Необходимо авторизоваться"
            );
            return;
        }

        Object sessionUserId = session.getAttribute("userId");

        if (!(sessionUserId instanceof UUID userId)) {
            sendError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Необходимо авторизоваться"
            );
            return;
        }

        String path = request.getPathInfo();

        if (path == null || path.isEmpty() || "/".equals(path)) {
            handleSingle(request, response, userId);
            return;
        }

        switch (path) {
            case "/check", "/points/check", "/points" ->
                    handleSingle(request, response, userId);

            case "/batch", "/points/batch" ->
                    handleBatch(request, response, userId);

            default -> sendError(response, HttpServletResponse.SC_NOT_FOUND,
                    "Endpoint не найден: " + path);
        }
    }

    private void handleSingle(
            HttpServletRequest request,
            HttpServletResponse response,
            UUID userId
    ) throws IOException {

        HitRequestDto dto;

        try {
            dto = OBJECT_MAPPER.readValue(request.getReader(), HitRequestDto.class);
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Некорректный JSON");
            return;
        }

        if (dto == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Тело запроса не может быть null");
            return;
        }

        Set<ConstraintViolation<HitRequestDto>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            sendValidationErrors(response, violations);
            return;
        }

        UUID idempotencyKey;
        try {
            idempotencyKey = parseIdempotencyKey(request);
        } catch (IllegalArgumentException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Некорректный Idempotency-Key: ожидается UUID");
            return;
        }

        try {
            HitService.HitResult result = hitService.hit(
                    dto, userId, idempotencyKey, 0
            );

            response.setStatus(HttpServletResponse.SC_CREATED);
            OBJECT_MAPPER.writeValue(response.getWriter(), result);

        } catch (IllegalStateException e) {
            sendError(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }
    }

    private void handleBatch(
            HttpServletRequest request,
            HttpServletResponse response,
            UUID userId
    ) throws IOException {

        List<HitRequestDto> requests;

        try {
            requests = OBJECT_MAPPER.readValue(
                    request.getReader(),
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(
                            List.class, HitRequestDto.class
                    )
            );
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Некорректный JSON: ожидается массив точек");
            return;
        }

        if (requests == null || requests.isEmpty()) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Список точек пуст");
            return;
        }

        if (requests.size() > MAX_BATCH_SIZE) {
            sendError(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
                    "Размер пакета не должен превышать " + MAX_BATCH_SIZE + " точек");
            return;
        }

        for (int i = 0; i < requests.size(); i++) {
            HitRequestDto dto = requests.get(i);

            if (dto == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Точка с индексом " + i + " не может быть null");
                return;
            }

            Set<ConstraintViolation<HitRequestDto>> violations = validator.validate(dto);

            if (!violations.isEmpty()) {
                sendValidationErrors(response, violations);
                return;
            }
        }

        UUID idempotencyKey;
        try {
            idempotencyKey = parseIdempotencyKey(request);
        } catch (IllegalArgumentException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Некорректный Idempotency-Key: ожидается UUID");
            return;
        }

        try {
            hitPackageService.save(requests, userId, idempotencyKey, 0);

            response.setStatus(HttpServletResponse.SC_CREATED);
            OBJECT_MAPPER.writeValue(
                    response.getWriter(),
                    new BatchResponse(true, requests.size(),
                            "Пакет успешно обработан")
            );

        } catch (IllegalStateException e) {
            sendError(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }
    }

    private UUID parseIdempotencyKey(HttpServletRequest request) {
        String header = request.getHeader("Idempotency-Key");

        if (header == null || header.isBlank()) {
            return UUID.randomUUID();
        }

        return UUID.fromString(header.trim());
    }

    private void sendValidationErrors(
            HttpServletResponse response,
            Set<? extends ConstraintViolation<?>> violations
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        List<String> errors = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        OBJECT_MAPPER.writeValue(response.getWriter(),
                new ValidationErrorResponse(errors));
    }

    private void sendError(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        response.setStatus(status);
        OBJECT_MAPPER.writeValue(response.getWriter(), new ErrorResponse(message));
    }

    private void prepareResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    public record ErrorResponse(String error) {}

    public record ValidationErrorResponse(List<String> errors) {}

    public record BatchResponse(
            boolean success,
            int processed,
            String message
    ) {}
}