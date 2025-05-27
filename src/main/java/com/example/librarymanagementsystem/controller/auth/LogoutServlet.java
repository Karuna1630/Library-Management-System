package com.example.librarymanagementsystem.controller.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    // Constant for "Remember Me" cookie name
    private static final String REMEMBER_ME_COOKIE = "rememberMe";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check for existing session and invalidate it
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Terminate the user session
        }

        // Clear the "Remember Me" cookie by setting its value to empty and max age to 0
        Cookie cookie = new Cookie(REMEMBER_ME_COOKIE, "");
        cookie.setMaxAge(0); // Immediately expire the cookie
        cookie.setPath("/"); // Ensure cookie is accessible across the app
        response.addCookie(cookie);

        // Redirect to the main index page after logout
        response.sendRedirect(request.getContextPath() + "/IndexServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Delegate POST requests to the same logic as GET
        doGet(request, response);
    }
}