package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.dto.AreaDTO;
import org.example.service.AdminService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * сервлет только для действий администраторов (добавить новую область)
 */
@WebServlet("/api/admin/area")
public class AdminServlet extends HttpServlet {
    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();

    @Inject
    private AdminService adminService;

    @Inject
    private Validator validator;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

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

        AreaDTO areaDTO;

        try {
            areaDTO = OBJECT_MAPPER.readValue(
                    request.getReader(),
                    AreaDTO.class
            );
        } catch (IOException e) {
            sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Некорректный JSON"
            );
            return;
        }

        Set<ConstraintViolation<AreaDTO>> violations =
                validator.validate(areaDTO);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .orElse("Ошибка валидации");

            sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    errorMessage
            );
            return;
        }

        try {
            adminService.saveArea(areaDTO, userId);

            response.setStatus(HttpServletResponse.SC_OK);

            OBJECT_MAPPER.writeValue(
                    response.getWriter(),
                    new MessageResponse(
                            "Область успешно обновлена"
                    )
            );

        } catch (SecurityException e) {
            sendError(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    e.getMessage()
            );

        } catch (IllegalArgumentException e) {
            sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );

        } catch (Exception e) {
            sendError(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Внутренняя ошибка сервера"
            );
        }
    }

    private void sendError(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        response.setStatus(status);

        OBJECT_MAPPER.writeValue(
                response.getWriter(),
                new ErrorResponse(message)
        );
    }

    public record ErrorResponse(String error) {}

    public record MessageResponse(String message) {}
}