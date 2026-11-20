package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/internal/admin/*")
public class AdminServlet extends HttpServlet {

    @Override
     public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

         String path = request.getPathInfo();

         if (path == null || path.equals("/")) {
             response.sendError(HttpServletResponse.SC_NOT_FOUND);
             return;
         }

         switch (path){
             case "/users" -> getUsers(request, response);
             case "/" ->
             default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
         }
     }

     private void getUsers(HttpServletRequest request, HttpServletResponse response){

     }
}
