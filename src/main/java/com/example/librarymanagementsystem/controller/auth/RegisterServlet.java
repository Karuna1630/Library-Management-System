package com.example.librarymanagementsystem.controller.Auth;

import com.example.librarymanagementsystem.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5,  // 5MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB
)
public class RegisterServlet extends HttpServlet {
    // Regex patterns for input validation
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{3,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Clear any existing error messages
        request.removeAttribute("errorMessage");
        // Forward to the registration page
        request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Clear any existing error messages
        request.removeAttribute("errorMessage");

        // Extract form parameters
        String name = request.getParameter("full_name");
        String email = request.getParameter("user_email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");

        // Validate full name
        if (name == null || name.trim().isEmpty() || !NAME_PATTERN.matcher(name.trim()).matches()) {
            request.setAttribute("errorMessage", "Name must be at least 3 characters and contain only letters and spaces");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        // Validate email format
        if (email == null || email.trim().isEmpty() || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            request.setAttribute("errorMessage", "Please enter a valid email");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        // Check if email is already in use
        if (AuthService.isEmailExists(email)) {
            request.setAttribute("errorMessage", "Email is already in use");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        // Validate password strength
        if (password == null || password.trim().isEmpty() || !PASSWORD_PATTERN.matcher(password).matches()) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters, include uppercase, lowercase, number, and special character");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        // Verify password confirmation
        if (confirmPassword == null || confirmPassword.trim().isEmpty() || !password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        // Validate role selection
        if (role == null || role.trim().isEmpty() || !role.equalsIgnoreCase("user") && !role.equalsIgnoreCase("admin")) {
            request.setAttribute("errorMessage", "Please select a valid role");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        // Handle image upload and validation
        Part imagePart = request.getPart("image");
        byte[] imageBytes = null;
        if (imagePart != null && imagePart.getSize() > 0) {
            String contentType = imagePart.getContentType();
            // Ensure uploaded file is an image
            if (!contentType.startsWith("image/")) {
                request.setAttribute("errorMessage", "Only image files are allowed");
                request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
                return;
            }
            // Check image size limit
            if (imagePart.getSize() > 5 * 1024 * 1024) {
                request.setAttribute("errorMessage", "Image size must be less than 5MB");
                request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
                return;
            }
            imageBytes = imagePart.getInputStream().readAllBytes();
        }

        try {
            // Attempt to register the user
            int userID = AuthService.register(name, email, password, confirmPassword, role, imageBytes);
            if (userID > 0) {
                // Redirect to login page with success message
                response.sendRedirect(request.getContextPath() + "/LoginServlet?message=Registration+successful.+Please+login.");
            } else {
                // Handle registration failure
                request.setAttribute("errorMessage", "Registration failed. Unexpected error.");
                request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Log and handle any unexpected errors
            e.printStackTrace();
            request.setAttribute("errorMessage", "Registration failed. Please try again.");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
        }
    }
}