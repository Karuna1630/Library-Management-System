package com.example.librarymanagementsystem.dao;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import com.example.librarymanagementsystem.utils.PasswordHashUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAO {
    // SQL query for inserting a new user
    private static final String INSERT_USER = "INSERT INTO users(full_name, user_email, password, confirmPassword, role, profile_picture, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    // Registers a new user in the database and returns the generated user ID
    public static int registerUser(User user) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            // Set current timestamp for created_at and updated_at
            Date currentDate = new Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());

            // Set parameters for the insert query
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getConfirmPassword());
            ps.setString(5, user.getRole().name());
            ps.setBytes(6, user.getImage());
            ps.setTimestamp(7, timestamp);
            ps.setTimestamp(8, timestamp);

            int affectedRows = ps.executeUpdate();

            // Check if the insert was successful and retrieve the generated user ID
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            // Log and wrap SQL exceptions for debugging
            e.printStackTrace();
            throw new RuntimeException("Error registering user", e);
        }
        // Return -1 if registration fails
        return -1;
    }

    // Retrieves a user by their email address
    public static User getUserByEmail(String email) {
        final String SELECT_USER_BY_EMAIL = "SELECT user_id, full_name, user_email, password, role, profile_picture " +
                "FROM users WHERE user_email = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Populate User object with database data
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("user_email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(User.Role.valueOf(rs.getString("role")));
                    user.setImage(rs.getBytes("profile_picture"));
                    return user;
                }
            }
        } catch (SQLException e) {
            // Log SQL errors for debugging
            System.err.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        }
        // Return null if no user is found
        return null;
    }

    // Authenticates a user by email and password
    public static User loginUser(String email, String password) {
        // Fetch user by email
        User user = getUserByEmail(email);
        // Verify password using PasswordHashUtil
        if (user != null && PasswordHashUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        // Return null if authentication fails
        return null;
    }

    // Updates user information in the database
    public static boolean updateUser(User user) {
        final String UPDATE_USER = "UPDATE users SET full_name = ?, password = ?, profile_picture = ?, updated_at = ? WHERE user_email = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {

            // Set current timestamp for updated_at
            Date currentDate = new Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPassword());
            // Handle null profile picture
            if (user.getImage() != null) {
                ps.setBytes(3, user.getImage());
            } else {
                ps.setNull(3, Types.BLOB);
            }
            ps.setTimestamp(4, timestamp);
            ps.setString(5, user.getEmail());

            // Return true if the update was successful
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            // Log SQL errors for debugging
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves all users from the database
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, full_name, user_email, role, profile_picture, created_at FROM users";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Populate User object for each record
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("user_email"));
                user.setRole(User.Role.valueOf(rs.getString("role")));
                user.setImage(rs.getBytes("profile_picture"));
                user.setCreated_at(rs.getDate("created_at"));
                users.add(user);
            }
        } catch (SQLException e) {
            // Log and wrap SQL exceptions
            e.printStackTrace();
            throw new RuntimeException("Error fetching users", e);
        }
        return users;
    }

    // Deletes a user by their ID
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            // Return true if the deletion was successful
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            // Log and wrap SQL exceptions
            e.printStackTrace();
            throw new RuntimeException("Error deleting user", e);
        }
    }

    // Retrieves users by their role
    public List<User> getUsersByRole(User.Role role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id FROM users WHERE role = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, role.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Populate minimal User object with ID only
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            // Log and wrap SQL exceptions
            e.printStackTrace();
            throw new RuntimeException("Error fetching users by role", e);
        }
        return users;
    }

    // Retrieves a user by their ID
    public static User getUserById(int userId) {
        final String SELECT_USER_BY_ID = "SELECT user_id, full_name, user_email, role, profile_picture, created_at " +
                "FROM users WHERE user_id = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Populate User object with database data
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("user_email"));
                    user.setRole(User.Role.valueOf(rs.getString("role")));
                    user.setImage(rs.getBytes("profile_picture"));
                    user.setCreated_at(rs.getDate("created_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            // Log SQL errors for debugging
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        // Return null if no user is found
        return null;
    }
}