package com.example.librarymanagementsystem.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalidate the session and clear all attributes
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear any authentication cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID") ||
                        cookie.getName().equals("rememberMe")) {
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

        // Redirect to login page with success message
        response.sendRedirect(request.getContextPath() + "/LoginServlet?logout=true");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}