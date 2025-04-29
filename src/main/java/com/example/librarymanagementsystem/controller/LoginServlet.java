package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final String REMEMBER_ME_COOKIE = "rememberMe";
    private static final int COOKIE_AGE = 30 * 24 * 60 * 60; // 30 days in seconds

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("user") != null) {
            User user = (User) existingSession.getAttribute("user");
            redirectBasedOnRole(user, request, response);
            return;
        }

        // Check for remember me cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REMEMBER_ME_COOKIE.equals(cookie.getName())) {
                    String email = cookie.getValue();
                    User rememberedUser = AuthService.validateRememberMeToken(email);
                    if (rememberedUser != null) {
                        // Create new session
                        HttpSession newSession = request.getSession(true);
                        newSession.setAttribute("user", rememberedUser);
                        redirectBasedOnRole(rememberedUser, request, response);
                        return;
                    }
                }
            }
        }

        // Check for success message from registration
        String message = request.getParameter("message");
        if (message != null && !message.isEmpty()) {
            request.setAttribute("successMessage", message);
        }

        request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("user_email");
        String password = request.getParameter("password");
        boolean rememberMe = "on".equals(request.getParameter("rememberMe"));

        // Validate inputs
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and password are required");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        User authenticatedUser = AuthService.login(email, password);

        if (authenticatedUser != null) {
            // Create new session and invalidate any existing one
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create new session
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("user", authenticatedUser);

            // Set "Remember Me" cookie if requested
            if (rememberMe) {
                Cookie cookie = new Cookie(REMEMBER_ME_COOKIE, authenticatedUser.getEmail());
                cookie.setMaxAge(COOKIE_AGE);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
                System.out.println("DEBUG: Cookie set for: " + authenticatedUser.getEmail());
            }

            redirectBasedOnRole(authenticatedUser, request, response);
        } else {
            request.setAttribute("errorMessage", "Invalid email or password");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
        }
    }

    private void redirectBasedOnRole(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (user.getRole() == User.Role.admin) {
            response.sendRedirect(request.getContextPath() + "/AdminServlet?action=dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
