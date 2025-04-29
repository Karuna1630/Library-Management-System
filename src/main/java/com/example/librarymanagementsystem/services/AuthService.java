package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.dao.UserDAO;
import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.utils.PasswordHashUtil;

public class AuthService {
    public static int register(String name, String email, String password, String confirmPassword, String role, byte[] image) {
        if (!password.equals(confirmPassword)) {
            return -1;
        }

        String hashedPassword = PasswordHashUtil.hashPassword(password);
        String hashedConfirmPassword = PasswordHashUtil.hashPassword(confirmPassword);

        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setConfirmPassword(hashedConfirmPassword);
        user.setRole(User.Role.valueOf(role));
        user.setImage(image);

        return UserDAO.registerUser(user);
    }

    public static User login(String email, String password) {
        try {
            User user = UserDAO.getUserByEmail(email);
            if (user != null && PasswordHashUtil.checkPassword(password, user.getPassword())) {
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User validateRememberMeToken(String email) {
        return UserDAO.getUserByEmail(email);
    }
}