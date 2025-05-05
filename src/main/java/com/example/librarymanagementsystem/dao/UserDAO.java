package com.example.librarymanagementsystem.dao;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import com.example.librarymanagementsystem.utils.PasswordHashUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static User getUserByEmail(String email) {
        final String SELECT_USER_BY_EMAIL = "SELECT user_id, full_name, user_email, password, role, profile_picture " +
                "FROM users WHERE user_email = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
            System.err.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static User loginUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && PasswordHashUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }


    public static boolean updateUser(User user) {
        final String UPDATE_USER = "UPDATE users SET full_name = ?, password = ?, profile_picture = ?, updated_at = ? WHERE user_email = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {

            Date currentDate = new Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(currentDate.getTime());

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPassword());
            if (user.getImage() != null) {
                ps.setBytes(3, user.getImage());
            } else {
                ps.setNull(3, Types.BLOB);
            }
            ps.setTimestamp(4, timestamp);
            ps.setString(5, user.getEmail());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, full_name, user_email, role, profile_picture, created_at FROM users";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("user_email"));
                user.setRole(User.Role.valueOf(rs.getString("role")));
                user.setImage(rs.getBytes("profile_picture")); // Add this line
                user.setCreated_at(rs.getDate("created_at"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching users", e);
        }
        return users;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting user", e);
        }
    }

    public List<User> getUsersByRole(User.Role role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id FROM users WHERE role = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, role.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching users by role", e);
        }
        return users;
    }

    public static User getUserById(int userId) {
        final String SELECT_USER_BY_ID = "SELECT user_id, full_name, user_email, role, profile_picture, created_at " +
                "FROM users WHERE user_id = ?";

        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}