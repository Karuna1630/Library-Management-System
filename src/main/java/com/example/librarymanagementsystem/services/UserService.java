package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.dao.UserDAO;
import com.example.librarymanagementsystem.model.User;
import java.util.List;

public class UserService {
    private UserDAO userDao = new UserDAO();

    public List<User> getAllUsers() {
        try {
            return userDao.getAllUsers();
        } catch (Exception e) {
            System.err.println("Error getting users: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    public boolean deleteUser(int userId) {
        try {
            // Prevent deleting the last admin
            if (isLastAdmin(userId)) {
                throw new IllegalStateException("Cannot delete the last admin user");
            }
            return userDao.deleteUser(userId);
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    private boolean isLastAdmin(int userId) {
        List<User> admins = userDao.getUsersByRole(User.Role.admin);
        return admins.size() == 1 && admins.get(0).getId() == userId;
    }
    public User getUserById(int userId) {
        return UserDAO.getUserById(userId);
    }
}