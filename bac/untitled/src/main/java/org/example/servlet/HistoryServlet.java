package org.example.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.HistoryService;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@WebServlet("/internal/history/*")
public class HistoryServlet extends HttpServlet {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_LIMIT = 5;
    private static final int MAX_LIMIT = 100;

    @Inject
    private HistoryService historyService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UUID userId = getAuthenticatedUserId(request, response);

        if (userId == null) {
            return;
        }

        String path = request.getPathInfo();

        switch (path == null ? "" : path) {
            case "/version" -> version(request, response, userId);
            case "/result" -> result(request, response, userId);
            case "/time" -> time(request, response, userId);
            default -> writeError(
                    response,
                    HttpServletResponse.SC_NOT_FOUND,
                    "Endpoint not found"
            );
        }
    }

    private void version(HttpServletRequest request, HttpServletResponse response, UUID userId) throws IOException {

        try {
            int version = getRequiredInt(request, "version");

            Pagination pagination = getPagination(request);

            StringWriter writer = new StringWriter();

            long total = historyService.findByVersion(
                    version,
                    pagination.offset(),
                    pagination.limit(),
                    userId,
                    writer
            );

            writeCsvResponse(
                    response,
                    writer.toString(),
                    pagination,
                    total
            );

        } catch (IllegalArgumentException e) {
            writeError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private void result(HttpServletRequest request, HttpServletResponse response, UUID userId) throws IOException {
        try {
            boolean hit = getRequiredBoolean(request);

            Pagination pagination = getPagination(request);

            StringWriter writer = new StringWriter();

            long total = historyService.findByResult(
                    hit,
                    pagination.offset(),
                    pagination.limit(),
                    userId,
                    writer
            );

            writeCsvResponse(
                    response,
                    writer.toString(),
                    pagination,
                    total
            );

        } catch (IllegalArgumentException e) {
            writeError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private void time(HttpServletRequest request, HttpServletResponse response, UUID userId) throws IOException {

        try {
            LocalDateTime from = getRequiredDateTime(request, "from");
            LocalDateTime to = getRequiredDateTime(request, "to");

            if (from.isAfter(to)) {
                throw new IllegalArgumentException(
                        "Начальная дата не может быть позже конечной"
                );
            }

            Pagination pagination = getPagination(request);

            StringWriter writer = new StringWriter();

            long total = historyService.findByTime(
                    from,
                    to,
                    pagination.offset(),
                    pagination.limit(),
                    userId,
                    writer
            );

            writeCsvResponse(
                    response,
                    writer.toString(),
                    pagination,
                    total
            );

        } catch (IllegalArgumentException e) {
            writeError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private UUID getAuthenticatedUserId(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var session = request.getSession(false);

        if (session == null) {
            writeError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Необходимо авторизоваться"
            );

            return null;
        }

        Object sessionUserId = session.getAttribute("userId");

        if (!(sessionUserId instanceof UUID userId)) {
            writeError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Необходимо авторизоваться"
            );

            return null;
        }

        return userId;
    }

    private int getRequiredInt(
            HttpServletRequest request,
            String parameter
    ) {

        String value = request.getParameter(parameter);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Параметр '" + parameter + "' обязателен"
            );
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Параметр '" + parameter + "' должен быть целым числом"
            );
        }
    }

    private boolean getRequiredBoolean(HttpServletRequest request) {

        String value = request.getParameter("result");

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Параметр '" + "result" + "' обязателен"
            );
        }

        if ("true".equalsIgnoreCase(value)) {
            return true;
        }

        if ("false".equalsIgnoreCase(value)) {
            return false;
        }

        throw new IllegalArgumentException(
                "Параметр '" + "result" + "' должен быть true или false"
        );
    }

    private LocalDateTime getRequiredDateTime(
            HttpServletRequest request,
            String parameter
    ) {

        String value = request.getParameter(parameter);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Параметр '" + parameter + "' обязателен"
            );
        }

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Неверный формат параметра '" + parameter
                            + "'. Используйте yyyy-MM-ddTHH:mm:ss"
            );
        }
    }

    private Pagination getPagination(
            HttpServletRequest request
    ) {

        int page = request.getParameter("page") == null
                ? DEFAULT_PAGE
                : getRequiredInt(request, "page");

        int limit = request.getParameter("limit") == null
                ? DEFAULT_LIMIT
                : getRequiredInt(request, "limit");

        if (page < 1) {
            throw new IllegalArgumentException(
                    "Номер страницы должен быть больше 0"
            );
        }

        if (limit < 1 || limit > MAX_LIMIT) {
            throw new IllegalArgumentException(
                    "Размер страницы должен находиться в диапазоне 1–100"
            );
        }

        long calculatedOffset = (long) (page - 1) * limit;

        if (calculatedOffset > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Слишком большой номер страницы"
            );
        }

        return new Pagination(
                page,
                limit,
                (int) calculatedOffset
        );
    }

    private void writeCsvResponse(
            HttpServletResponse response,
            String csv,
            Pagination pagination,
            long total
    ) throws IOException {

        long totalPages = total == 0
                ? 0
                : (total + pagination.limit() - 1) / pagination.limit();

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"history.csv\""
        );

        response.setHeader(
                "X-Total-Count",
                String.valueOf(total)
        );

        response.setHeader(
                "X-Page",
                String.valueOf(pagination.page())
        );

        response.setHeader(
                "X-Limit",
                String.valueOf(pagination.limit())
        );

        response.setHeader(
                "X-Total-Pages",
                String.valueOf(totalPages)
        );

        response.setHeader(
                "X-Offset",
                String.valueOf(pagination.offset())
        );

        response.getWriter().write(csv);
    }

    private void writeError(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String safeMessage = message == null
                ? "Unknown error"
                : message;

        response.getWriter().write(
                "{\"message\":\"" + escapeJson(safeMessage) + "\"}"
        );
    }

    private String escapeJson(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private record Pagination(
            int page,
            int limit,
            int offset
    ) {
    }
}