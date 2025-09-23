package com.recenter.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Re-Center Hello World</title>");
        out.println("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">");
        out.println("<meta charset=\"UTF-8\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container mt-5\">");
        out.println("<h1>🎉 Hello World from Re-Center!</h1>");
        out.println("<div class=\"alert alert-success\">");
        out.println("<p>✅ Servlet работает на Tomcat 9!</p>");
        out.println("<p>🕒 Текущее время: " + new java.util.Date() + "</p>");
        out.println("<p>🐘 Tomcat Version: 9.x</p>");
        out.println("<p>☕ Java Version: " + System.getProperty("java.version") + "</p>");
        out.println("</div>");
        out.println("<a href=\"/re-center/\" class=\"btn btn-primary\">На главную</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}