package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/*")
public class ControllerServlet extends HttpServlet {

    private static final String POINTS_SERVLET = "/internal/area-check";
    private static final String HISTORY_EXPORT_SERVLET = "/internal/history-export";

    @Override
    protected void service(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.isBlank() || path.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String method = request.getMethod();

        switch (path) {
            case "/points", "/points/check", "/points/batch" ->
                    forward(request, response, POINTS_SERVLET);

            case "/history/export" -> {
                if (!method.equals("GET")) {
                    sendMethodNotAllowed(response, "GET");
                    return;
                }

                forward(request, response, HISTORY_EXPORT_SERVLET);
            }

            default -> response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Unknown API endpoint: " + path
            );
        }
    }

    private void forward(
            HttpServletRequest request,
            HttpServletResponse response,
            String destination
    ) throws ServletException, IOException {

        request.getRequestDispatcher(destination)
                .forward(request, response);
    }

    private void sendMethodNotAllowed(
            HttpServletResponse response,
            String allowedMethod
    ) throws IOException {

        response.setHeader("Allow", allowedMethod);
        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Allowed method: " + allowedMethod
        );
    }
}
