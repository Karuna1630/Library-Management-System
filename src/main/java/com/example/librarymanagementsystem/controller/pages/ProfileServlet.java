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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Ensure image is loaded in session if it exists in user object
        User user = (User) session.getAttribute("user");
        if (user.getImage() != null && session.getAttribute("base64Image") == null) {
            String base64Image = Base64.getEncoder().encodeToString(user.getImage());
            session.setAttribute("base64Image", base64Image);
        }

        request.getRequestDispatcher("/view/pages/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String fullName = request.getParameter("fullName");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");

        try {
            // Process password change if fields are filled
            if (currentPassword != null && !currentPassword.isEmpty() &&
                    newPassword != null && !newPassword.isEmpty() &&
                    confirmNewPassword != null && !confirmNewPassword.isEmpty()) {

                // Verify current password
                if (!PasswordHashUtil.checkPassword(currentPassword, currentUser.getPassword())) {
                    request.setAttribute("errorMessage", "Current password is incorrect");
                    doGet(request, response);
                    return;
                }

                // Verify new passwords match
                if (!newPassword.equals(confirmNewPassword)) {
                    request.setAttribute("errorMessage", "New passwords don't match");
                    doGet(request, response);
                    return;
                }

                // Update password
                String hashedPassword = PasswordHashUtil.hashPassword(newPassword);
                currentUser.setPassword(hashedPassword);
            }

            // Process profile image if uploaded
            Part imagePart = request.getPart("profileImage");
            byte[] imageBytes = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                String contentType = imagePart.getContentType();
                if (!contentType.startsWith("image/")) {
                    request.setAttribute("errorMessage", "Only image files are allowed");
                    doGet(request, response);
                    return;
                }
                imageBytes = imagePart.getInputStream().readAllBytes();
            }

            // Update user object
            currentUser.setFullName(fullName);
            if (imageBytes != null) {
                currentUser.setImage(imageBytes);
            }

            // Save to database
            boolean updated = UserDAO.updateUser(currentUser);

            // In the success update section:
            if (updated) {
                // Update session
                session.setAttribute("user", currentUser);
                if (imageBytes != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    session.setAttribute("base64Image", base64Image);
                } else if (currentUser.getImage() == null) {
                    // Clear the image if it was removed
                    session.removeAttribute("base64Image");
                }
                request.setAttribute("successMessage", "Profile updated successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to update profile. Please try again.");
            }

        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
        }

        doGet(request, response);
    }}