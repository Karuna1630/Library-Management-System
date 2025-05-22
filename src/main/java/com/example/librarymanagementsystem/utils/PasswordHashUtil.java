package com.example.librarymanagementsystem.utils;

import org.mindrot.jbcrypt.BCrypt;

// Utility class for password hashing and verification using BCrypt
public class PasswordHashUtil {

    // Hashes a password using BCrypt with a generated salt
    public static String hashPassword(String password) {
        // Generate a salted hash of the password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verifies a password against its hashed version
    public static boolean checkPassword(String password, String hashedPassword) {
        // Check if the provided password matches the hashed password
        return BCrypt.checkpw(password, hashedPassword);
    }
}