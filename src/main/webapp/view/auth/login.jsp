<%--
  Created by IntelliJ IDEA.
  User: USER-PC
  Date: 4/17/2025
  Time: 3:15 PM
  To change this template use File | Settings | File Templates.
--%>

<<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UniShelf - University Library Management System</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/login.css">
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</head>
<body>
<div class="page-container">
    <div class="form-container">
        <div class="form-header">
            <h1>UniShelf</h1>
            <div class="logo">
                <i class="fas fa-book-reader"></i>
            </div>
        </div>

        <form class="loginForm" id="loginForm"
              action="${pageContext.request.contextPath}/LoginServlet"
              method="POST"
              onsubmit="return validateLoginForm('loginForm')">
            <h2>Welcome Back</h2>
            <p>Sign in to access your library account.</p>

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
                    <input type="password" id="password" name="password" placeholder="Enter your password" required>
                    <i id="togglePassword" class="fas fa-eye toggle-icon" onclick="togglePassword('password', 'togglePassword')"></i>
                </div>
            </div>

            <div class="remember-forgot">
                <div class="remember-me">
                    <input type="checkbox" id="rememberMe" name="rememberMe">
                    <label for="rememberMe">Remember me</label>
                </div>
                <a href="/forgot-password" class="forgot-link">Forgot Password?</a>
            </div>

            <button type="submit" class="btn">Sign In</button>

            <div class="bottom-link">
                Don't have an account? <a href="RegisterServlet">Create Account</a>
            </div>
        </form>

        <div class="form-footer">
            <p>&copy; 2025 Library Management System. All rights reserved.</p>
        </div>
    </div>
</div>
</body>
</html>