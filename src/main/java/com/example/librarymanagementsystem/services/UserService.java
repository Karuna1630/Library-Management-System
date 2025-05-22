package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.dao.UserDAO;
import com.example.librarymanagementsystem.model.User;
import java.util.List;

// Service class for managing user-related operations
public class UserService {
    // Initialize UserDAO for database interactions
    private UserDAO userDao = new UserDAO();

    // Retrieves all users from the database
    public List<User> getAllUsers() {
        try {
            // Delegate to UserDAO to fetch all users
            return userDao.getAllUsers();
        } catch (Exception e) {
            // Log errors for debugging and wrap in RuntimeException
            System.err.println("Error getting users: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    // Deletes a user by their ID, with protection against deleting the last admin
    public boolean deleteUser(int userId) {
        try {
            // Prevent deleting the last admin
            if (isLastAdmin(userId)) {
                throw new IllegalStateException("Cannot delete the last admin user");
            }
            // Delegate to UserDAO to perform deletion
            return userDao.deleteUser(userId);
        } catch (Exception e) {
            // Log errors for debugging and wrap in RuntimeException
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    // Checks if the user is the last admin in the system
    private boolean isLastAdmin(int userId) {
        // Fetch all admin users
        List<User> admins = userDao.getUsersByRole(User.Role.admin);
        // Return true if there is only one admin and it matches the given userId
        return admins.size() == 1 && admins.get(0).getId() == userId;
    }

    // Retrieves a user by their ID
    public User getUserById(int userId) {
        // Delegate to UserDAO to fetch user by ID
        return UserDAO.getUserById(userId);
    }
}