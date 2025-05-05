package com.example.librarymanagementsystem.controller.auth;

import com.example.librarymanagementsystem.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB - Maximum size of the entire request
)
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the registration JSP page
        request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        String name = request.getParameter("full_name");
        String email = request.getParameter("user_email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");


        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Name is required");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email is required");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Password is required");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        // Process profile image upload
        Part imagePart = request.getPart("image");
        byte[] imageBytes = null;

        if (imagePart != null && imagePart.getSize() > 0) {
            // Validate image type
            String contentType = imagePart.getContentType();
            if (!contentType.startsWith("image/")) {
                request.setAttribute("errorMessage", "Only image files are allowed");
                request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
                return;
            }

            imageBytes = imagePart.getInputStream().readAllBytes();
        }


        // Register user
        int userID = AuthService.register(name, email, password, confirmPassword, role, imageBytes);

        if (userID > 0) {
            // Registration successful - redirect to login with success message
            response.sendRedirect(request.getContextPath() + "/LoginServlet?message=Registration+successful.+Please+login.");
        } else {
            // Registration failed
            request.setAttribute("errorMessage", "Registration failed. Please try again.");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
        }
    }
}