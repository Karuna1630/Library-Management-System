<%--
  Created by IntelliJ IDEA.
  User: USER-PC
  Date: 4/16/2025
  Time: 10:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UniShelf - University Library Management System</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/register.css">
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</head>
<body>
<div class="page-container">
    <div class="form-container">
        <div class="form-header">
            <h1>UniSelf</h1>
            <div class="logo">
                <i class="fas fa-book-reader"></i>
            </div>
        </div>

        <form class="registerForm" id="registerForm"
              action="<%= request.getContextPath()%>/RegisterServlet"
              method="POST"
              enctype="multipart/form-data"
              onsubmit="return validateForm('registerForm')">
            <h2>Create an Account</h2>
            <p>Your journey to smarter reading starts here.</p>

            <div class="form-group">
                <label for="full_name">UserName</label>
                <div class="input-container">
                    <i class="fas fa-user input-icon"></i>
                    <input type="text" id="full_name" name="full_name" placeholder="Enter your full name" required>
                </div>
            </div>

            <div class="form-group">
                <label for="user_email">Email Address</label>
                <div class="input-container">
                    <i class="fas fa-envelope input-icon"></i>
                    <input type="email" id="user_email" name="user_email" placeholder="Enter your email" required>
                </div>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <div class="password-input-container">
                    <i class="fas fa-lock input-icon"></i>
                    <input type="password" id="password" name="password" placeholder="Choose a password" required>
                    <i id="togglePassword" class="fas fa-eye toggle-icon" onclick="togglePassword('password', 'togglePassword')"></i>
                </div>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <div class="password-input-container">
                    <i class="fas fa-lock input-icon"></i>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm password" required>
                    <i id="toggleConfirmPassword" class="fas fa-eye toggle-icon" onclick="togglePassword('confirmPassword', 'toggleConfirmPassword')"></i>
                </div>
            </div>

            <!--  Role Selection Dropdown -->
            <div class="form-group">
                <label for="role">Role:</label>
                <div class="input-container">
                    <i class="fas fa-user-tag input-icon"></i>
                    <select id="role" name="role" class="form-select" required>
                        <option value="user">User</option>
                        <option value="admin">Administrator</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="image">Profile Picture</label>
                <div class="file-input-container">
                    <label for="image" class="file-input-label">
                        <i class="fas fa-upload"></i>
                        <span>Choose a file</span>
                    </label>
                    <input type="file" id="image" name="image" accept="image/*" onchange="previewImage(event)">
                </div>

                <div class="image-preview-container">
                    <img id="imagePreview"
                         class="image-preview"
                         src="${pageContext.request.contextPath}/assets/image/default-profile.jpg"
                         alt="Profile Preview" />
                    <p class="preview-text">Upload an image to change the default profile picture</p>
                </div>
            </div>

            <button type="submit" class="btn">Register</button>

            <div class="bottom-link">
                Already have an account? <a href="LoginServlet">Sign In</a>
            </div>
        </form>

        <div class="form-footer">
            <p>&copy; 2025 Library Management System. All rights reserved.</p>
        </div>
    </div>
</div>
</body>
</html>