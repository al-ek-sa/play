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

@WebServlet("/api/admin/area")
public class AdminServlet extends HttpServlet {

    @Inject
    private AdminService adminService;

    @Inject
    private Validator validator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        UUID userId = (UUID) request.getSession(false)
                .getAttribute("userId");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write(
                    "{\"error\":\"Необходимо авторизоваться\"}"
            );

            return;
        }

        AreaDTO areaDTO;

        try {
            areaDTO = objectMapper.readValue(
                    request.getReader(),
                    AreaDTO.class
            );
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                    "{\"error\":\"Некорректный JSON\"}"
            );
            return;
        }

        Set<ConstraintViolation<AreaDTO>> violations =
                validator.validate(areaDTO);

        if (!violations.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            String error = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .orElse("Ошибка валидации");

            objectMapper.writeValue(
                    response.getWriter(),
                    new ErrorResponse(error)
            );

            return;
        }

        try {
            adminService.saveArea(areaDTO, userId);

            response.setStatus(HttpServletResponse.SC_OK);

            response.getWriter().write(
                    "{\"message\":\"Область успешно обновлена\"}"
            );

        } catch (SecurityException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            objectMapper.writeValue(
                    response.getWriter(),
                    new ErrorResponse(e.getMessage())
            );

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            objectMapper.writeValue(
                    response.getWriter(),
                    new ErrorResponse(e.getMessage())
            );

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            objectMapper.writeValue(
                    response.getWriter(),
                    new ErrorResponse("Внутренняя ошибка сервера")
            );
        }
    }

    public record ErrorResponse(String error) {}
}