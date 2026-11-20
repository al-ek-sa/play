package org.example.servlet;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.entity.UserEntity;
import org.example.service.AuthService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/internal/auth/*")
public class AuthServlet extends HttpServlet {

    @Inject
    private AuthService authService;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String path = request.getPathInfo();

        switch (path == null ? "" : path) {
            case "/register" -> register(request, response);
            case "/login" -> login(request, response);
            case "/logout" -> logout(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String path = request.getPathInfo();

        if ("/me".equals(path)) {
            getCurrentUser(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void register(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        try {
            JsonObject body = readJson(request);

            String username = body.getString("username");
            String password = body.getString("password");

            validateCredentials(username, password);

            UserEntity user = authService.register(username, password);

            response.setStatus(HttpServletResponse.SC_CREATED);
            writeUserResponse(response, user);

        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    private void login(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        try {
            JsonObject body = readJson(request);

            String username = body.getString("username");
            String password = body.getString("password");

            UserEntity user = authService.login(username, password);

            HttpSession session = request.getSession();
            request.changeSessionId();

            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            response.setStatus(HttpServletResponse.SC_OK);
            writeUserResponse(response, user);

        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    e.getMessage()
            );
        }
    }

    private void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void getCurrentUser(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UUID userId = (UUID) session.getAttribute("userId");

        UserEntity user = authService.findById(userId);

        if (user == null || !user.isEnabled()) {
            session.invalidate();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        writeUserResponse(response, user);
    }

    private JsonObject readJson(HttpServletRequest request)
            throws IOException {

        try (JsonReader reader = Json.createReader(request.getReader())) {
            return reader.readObject();
        }
    }

    private void validateCredentials(String username, String password) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (username.length() > 50) {
            throw new IllegalArgumentException("Username is too long");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters"
            );
        }
    }

    private void writeUserResponse(
            HttpServletResponse response,
            UserEntity user
    ) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject json = Json.createObjectBuilder()
                .add("id", user.getId().toString())
                .add("username", user.getUsername())
                .add("role", user.getRole().toString())
                .build();

        response.getWriter().write(json.toString());
    }
}