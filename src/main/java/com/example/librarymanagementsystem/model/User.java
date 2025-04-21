package com.example.librarymanagementsystem.model;


import javax.management.relation.Role;
import java.util.Arrays;
import java.util.Date;

public class User {
    public enum Role{admin, user}
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;
    private Role role;
    private byte[] image;
    private Date created_at;

    public User() {
    }

    public User(String fullName, String email, String password, String confirmPassword, Role role, Date created_at) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.created_at = created_at;
    }

    public User(int id, String fullName, String email, String password, String confirmPassword, Role role, Date created_at) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.created_at = created_at;
    }

    public User(String fullName, String email, String password, String confirmPassword, Role role, byte[] image, Date created_at) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.image = image;
        this.created_at = created_at;
    }

    public User(int id, String fullName, String email, String password, String confirmPassword, Role role, byte[] image, Date created_at) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.image = image;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

}



