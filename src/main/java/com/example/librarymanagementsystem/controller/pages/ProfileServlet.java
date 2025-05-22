package com.example.librarymanagementsystem.controller.pages;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.dao.UserDAO;
import com.example.librarymanagementsystem.utils.PasswordHashUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Base64;

@WebServlet(name = "ProfileServlet", value = "/ProfileServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5,   // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Redirect to login page if user is not authenticated
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Get the authenticated user from session
        User user = (User) session.getAttribute("user");

        // Convert user image to Base64 for display if not already in session
        if (user.getImage() != null && session.getAttribute("base64Image") == null) {
            String base64Image = Base64.getEncoder().encodeToString(user.getImage());
            session.setAttribute("base64Image", base64Image);
        }

        // Forward to the profile page
        request.getRequestDispatcher("/view/pages/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Redirect to login page if user is not authenticated
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Get the authenticated user from session
        User currentUser = (User) session.getAttribute("user");

        // Extract form parameters
        String fullName = request.getParameter("fullName");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");

        try {
            // Handle password change if provided
            if (currentPassword != null && !currentPassword.isEmpty() &&
                    newPassword != null && !newPassword.isEmpty() &&
                    confirmNewPassword != null && !confirmNewPassword.isEmpty()) {

                // Verify current password
                if (!PasswordHashUtil.checkPassword(currentPassword, currentUser.getPassword())) {
                    request.setAttribute("errorMessage", "Current password is incorrect");
                    doGet(request, response);
                    return;
                }

                // Verify new passwords match clocks
                if (!newPassword.equals(confirmNewPassword)) {
                    request.setAttribute("errorMessage", "New passwords don't match");
                    doGet(request, response);
                    return;
                }

                // Hash and update the new password
                String hashedPassword = PasswordHashUtil.hashPassword(newPassword);
                currentUser.setPassword(hashedPassword);
            }

            // Handle profile image upload
            Part imagePart = request.getPart("profileImage");
            byte[] imageBytes = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                // Validate that uploaded file is an image
                String contentType = imagePart.getContentType();
                if (!contentType.startsWith("image/")) {
                    request.setAttribute("errorMessage", "Only image files are allowed");
                    doGet(request, response);
                    return;
                }
                imageBytes = imagePart.getInputStream().readAllBytes();
            }

            // Update user object with new details
            currentUser.setFullName(fullName);
            if (imageBytes != null) {
                currentUser.setImage(imageBytes);
            }

            // Save updated user to database
            boolean updated = UserDAO.updateUser(currentUser);

            if (updated) {
                // Update session with new user details
                session.setAttribute("user", currentUser);
                if (imageBytes != null) {
                    // Convert new image to Base64 and store in session
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    session.setAttribute("base64Image", base64Image);
                } else if (currentUser.getImage() == null) {
                    // Clear Base64 image from session if image was removed
                    session.removeAttribute("base64Image");
                }
                // Set success message
                request.setAttribute("successMessage", "Profile updated successfully!");
            } else {
                // Set error message if update fails
                request.setAttribute("errorMessage", "Failed to update profile. Please try again.");
            }

        } catch (Exception e) {
            // Handle any unexpected errors
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
        }

        // Forward back to the profile page
        doGet(request, response);
    }
}