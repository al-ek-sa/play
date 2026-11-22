package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * сервлет-контроллер
 */
@WebServlet("/api/*")
public class ControllerServlet extends HttpServlet {

    private static final String HISTORY_SERVLET = "/internal/history";
    private static final String ADMIN_SERVLET = "/internal/admin";
    private static final String AUTH_SERVLET = "/internal/auth";
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

            case "/auth/register", "/auth/login", "/auth/logout" -> {
                if (!method.equals("POST")) {
                    sendMethodNotAllowed(response, "POST");
                    return;
                }

                String authPath = path.substring("/auth".length());

                forward(request, response, AUTH_SERVLET + authPath);
            }

            case "/points", "/points/check", "/points/batch" -> {
                if (!method.equals("POST")) {
                    sendMethodNotAllowed(response, "POST");
                    return;
                }

                forward(request, response, POINTS_SERVLET + path);
            }

            case "/admin/area" -> {
                if (!method.equals("POST")) {
                    sendMethodNotAllowed(response, "POST");
                    return;
                }

                forward(
                        request,
                        response,
                        ADMIN_SERVLET + "/area"
                );
            }

            case "/history/export" -> {
                if (!method.equals("GET")) {
                    sendMethodNotAllowed(response, "GET");
                    return;
                }

                forward(request, response, HISTORY_EXPORT_SERVLET);
            }

            case "/history/version", "/history/result", "/history/time" -> {
                if (!method.equals("POST")) {
                    sendMethodNotAllowed(response, "POST");
                    return;
                }

                forward(
                        request,
                        response,
                        HISTORY_SERVLET + path.substring("/history".length())
                );
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

        request.setAttribute("internalDispatch", true);

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