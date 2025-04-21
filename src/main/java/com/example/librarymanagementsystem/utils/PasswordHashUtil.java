package com.example.librarymanagementsystem.utils;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashUtil {
    //Hashing the password using BCrypt
    public static String PasswordHashUtil(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    //Check the password
    public static boolean checkPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }
}
