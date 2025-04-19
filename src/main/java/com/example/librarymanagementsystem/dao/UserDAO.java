package com.example.librarymanagementsystem.dao;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;

import java.sql.*;
import java.util.Date;

public class UserDAO {
    private static final String INSERT_USER = "INSERT INTO users(full_name, user_email, password, confirmPassword, role, profile_picture, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static int registerUser(User user) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            // Set current timestamp
            Date currentDate = new Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());

            // Set parameters
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getConfirmPassword());
            ps.setString(5, user.getRole().name());
            ps.setBytes(6, user.getImage());
            ps.setTimestamp(7, timestamp);
            ps.setTimestamp(8, timestamp);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error registering user", e);
        }
        return -1;
    }

    public static User loginUser(String email, String password) {
        final String SELECT_USER_BY_EMAIL_PASSWORD = "SELECT user_id, full_name, user_email, password, role, profile_picture " + "FROM users WHERE user_email = ? AND password = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_EMAIL_PASSWORD)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User authenticatedUser = new User();
                    authenticatedUser.setId(rs.getInt("user_id"));
                    authenticatedUser.setFullName(rs.getString("full_name"));
                    authenticatedUser.setEmail(rs.getString("user_email"));
                    authenticatedUser.setRole(User.Role.valueOf(rs.getString("role")));
                    authenticatedUser.setImage(rs.getBytes("profile_picture"));
                    return authenticatedUser;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            throw new RuntimeException("Database error during authentication", e);
        }
        return null;
    }
}