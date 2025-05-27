package com.example.librarymanagementsystem.controller.auth;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    // Constants for "Remember Me" cookie
    private static final String REMEMBER_ME_COOKIE = "rememberMe";
    private static final int COOKIE_AGE = 30 * 24 * 60 * 60; // 30 days in seconds

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Clear any existing messages to avoid stale data
        request.removeAttribute("successMessage");
        request.removeAttribute("errorMessage");

        // Check if user is already logged in
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("user") != null) {
            User user = (User) existingSession.getAttribute("user");
            // Redirect based on user role if already logged in
            redirectBasedOnRole(user, request, response);
            return;
        }

        // Check for "Remember Me" cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REMEMBER_ME_COOKIE.equals(cookie.getName())) {
                    String email = cookie.getValue();
                    // Validate cookie and auto-login user
                    User rememberedUser = AuthService.validateRememberMeToken(email);
                    if (rememberedUser != null) {
                        // Create new session for remembered user
                        HttpSession newSession = request.getSession(true);
                        newSession.setAttribute("user", rememberedUser);
                        redirectBasedOnRole(rememberedUser, request, response);
                        return;
                    }
                }
            }
        }

        // Check for success message from registration (e.g., "Account created")
        String message = request.getParameter("message");
        if (message != null && !message.isEmpty()) {
            request.setAttribute("successMessage", message.replace("+", " "));
        }

        // Forward to login page
        request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get login form parameters
        String email = request.getParameter("user_email");
        String password = request.getParameter("password");
        boolean rememberMe = "on".equals(request.getParameter("rememberMe"));

        // Clear any existing messages
        request.removeAttribute("successMessage");
        request.removeAttribute("errorMessage");

        // Validate email and password inputs
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and password are required");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
            return;
        }

        // Attempt to authenticate user
        User authenticatedUser = AuthService.login(email, password);

        if (authenticatedUser != null) {
            // Invalidate any existing session
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create new session and store user
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("user", authenticatedUser);

            // Set "Remember Me" cookie if requested
            if (rememberMe) {
                Cookie cookie = new Cookie(REMEMBER_ME_COOKIE, authenticatedUser.getEmail());
                cookie.setMaxAge(COOKIE_AGE);
                cookie.setPath("/"); // Cookie accessible across the app
                cookie.setHttpOnly(true); // Enhance security by preventing client-side access
                response.addCookie(cookie);
                System.out.println("DEBUG: Cookie set for: " + authenticatedUser.getEmail());
            }

            // Redirect based on user role
            redirectBasedOnRole(authenticatedUser, request, response);
        } else {
            // Return error if authentication fails
            request.setAttribute("errorMessage", "Invalid email or password");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
        }
    }

    // Helper method to redirect users based on their role
    private void redirectBasedOnRole(User user, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (user.getRole() == User.Role.admin) {
            // Redirect admins to the admin dashboard
            response.sendRedirect(request.getContextPath() + "/AdminServlet?action=dashboard");
        } else {
            // Redirect regular users to the main index page
            response.sendRedirect(request.getContextPath() + "/IndexServlet");
        }
    }
}