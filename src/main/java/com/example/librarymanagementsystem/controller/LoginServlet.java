package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // For log out
        String logout = request.getParameter("logout");
        if (logout != null && logout.equals("true")) {
            request.setAttribute("successMessage", "You have been logged out successfully.");
        }

        // Check if user is already logged in
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("user") != null) {
            User user = (User) existingSession.getAttribute("user");
            redirectBasedOnRole(user, request, response);
            return;
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

            // Set session attributes
            newSession.setAttribute("user", authenticatedUser);
            newSession.setAttribute("userEmail", authenticatedUser.getEmail());
            newSession.setAttribute("userRole", authenticatedUser.getRole().toString());

            // Set session timeout (30 minutes)
            newSession.setMaxInactiveInterval(30 * 60);

            // Store image in session if exists
            if (authenticatedUser.getImage() != null) {
                String base64Image = java.util.Base64.getEncoder().encodeToString(authenticatedUser.getImage());
                newSession.setAttribute("base64Image", base64Image);
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
}