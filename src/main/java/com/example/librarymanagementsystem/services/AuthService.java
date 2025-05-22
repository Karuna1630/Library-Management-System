package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.dao.UserDAO;
import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.utils.PasswordHashUtil;

// Service class for handling user authentication and registration
public class AuthService {

    // Registers a new user with hashed passwords
    public static int register(String name, String email, String password, String confirmPassword, String role, byte[] image) {
        // Check if passwords match before proceeding
        if (!password.equals(confirmPassword)) {
            return -1; // Return -1 to indicate password mismatch
        }

        // Hash both password and confirmPassword for security
        String hashedPassword = PasswordHashUtil.hashPassword(password);
        String hashedConfirmPassword = PasswordHashUtil.hashPassword(confirmPassword);

        // Create and populate User object
        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setConfirmPassword(hashedConfirmPassword);
        user.setRole(User.Role.valueOf(role));
        user.setImage(image);

        // Delegate to UserDAO to save the user and return the generated user ID
        return UserDAO.registerUser(user);
    }

    // Authenticates a user by email and password
    public static User login(String email, String password) {
        try {
            // Fetch user by email
            User user = UserDAO.getUserByEmail(email);
            // Verify password and return user if valid
            if (user != null && PasswordHashUtil.checkPassword(password, user.getPassword())) {
                return user;
            }
            // Return null if authentication fails
            return null;
        } catch (Exception e) {
            // Log errors for debugging
            e.printStackTrace();
            return null;
        }
    }

    // Validates a remember-me token by retrieving user by email
    public static User validateRememberMeToken(String email) {
        // Delegate to UserDAO to fetch user by email
        return UserDAO.getUserByEmail(email);
    }

    // Checks if an email is already registered
    public static boolean isEmailExists(String email) {
        try {
            // Fetch user by email
            User user = UserDAO.getUserByEmail(email);
            // Return true if user exists
            return user != null;
        } catch (Exception e) {
            // Log errors for debugging
            System.err.println("Error checking if email exists: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}