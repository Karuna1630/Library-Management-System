package com.example.librarymanagementsystem.model;



public class User {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private int role = 1;

    public User() {
    }

    public User(String fullName, String email, String password) {
        fullName = fullName;
        email = email;
        password = password;
    }

public User(int id, String fullName, String email, String password) {
    this.id = id;
    fullName = fullName;
    email = email;
    password = password;
}

public User(String fullName, String email, String password, int role) {
    fullName = fullName;
    email = email;
    password = password;
    this.role = role;
}

public User(int id, String fullName, String email, String password, int role) {
    this.id = id;
    fullName = fullName;
    email = email;
    password = password;
    this.role = role;
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
        fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
    // Helper methods for role checks
    public boolean isSuperAdmin() {
        return role == 0;
    }

    public boolean isRegularUser() {
        return role == 1;
    }

    public boolean isAdmin() {
        return role == 2;
    }

    // Helper method to check if user has admin-level permissions
    public boolean hasAdminPermissions() {
        return role == 0 || role == 2; // Super admin or regular admin
    }

    // Helper method to check if user can upload images
    public boolean canUploadImages() {
        return role == 1 || role == 2; // Regular user or regular admin
    }
    //displaying the string representation of the User object
    @Override
    public String toString() {
        return "User [id=" + id + ", fullName=" + fullName + ", email=" + email + ", role=" + role + "]";
    }
}



