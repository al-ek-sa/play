package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/*")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void service(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {
            case "/points" -> {
                request.getRequestDispatcher("/internal/area-check")
                        .forward(request, response);
            }

            case "/history/export" -> {
                request.getRequestDispatcher("/internal/history-export")
                        .forward(request, response);
            }

            default -> response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Unknown API endpoint"
            );
        }
    }
}