package com.example.librarymanagementsystem.services;

import com.example.librarymanagementsystem.dao.UserDAO;
import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.utils.PasswordHashUtil;

public class AuthService {
    public static int register(String name, String email, String password, String confirmPassword, String role, byte[] image) {
//        password = PasswordHashUtil.hashPassword(password);
        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
        user.setRole(User.Role.valueOf(role));
        user.setImage(image);

        return UserDAO.registerUser(user);
    }

    public static User login(String email, String password) {
        try {
            return UserDAO.loginUser(email, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // if(uer !=null){
    }

}
