<%--
  Created by IntelliJ IDEA.
  User: USER-PC
  Date: 4/17/2025
  Time: 3:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%-- Login page for UniShelf - University Library Management System --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- Page title and metadata --%>
    <title>UniShelf - University Library Management System</title>
    <%-- External stylesheet for Font Awesome icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <%-- Custom stylesheet for login page styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/login.css">
    <%-- Custom JavaScript for login form functionality (e.g., password toggle) --%>
    <script src="${pageContext.request.contextPath}/assets/js/login.js"></script>
</head>
<body>
<%-- Main container for the login page --%>
<div class="page-container">
    <div class="form-container">
        <%-- Header with system name and logo icon --%>
        <div class="form-header">
            <h1>UniShelf</h1>
            <div class="logo">
                <i class="fas fa-book-reader"></i>
            </div>
        </div>

        <%-- Login form submitting to LoginServlet --%>
        <form class="loginForm" id="loginForm"
              action="${pageContext.request.contextPath}/LoginServlet"
              method="POST">
            <h2>Welcome Back</h2>
            <p>Sign in to access your library account.</p>
            <%-- Display error message for failed login attempts --%>
            <%
                if ("POST".equalsIgnoreCase(request.getMethod()) && request.getAttribute("errorMessage") != null) {
            %>
            <div class="error-message show">
                <i class="fas fa-exclamation-circle error-icon"></i>
                <span class="error-text"><%= request.getAttribute("errorMessage") %></span>
            </div>
            <% } %>
            <%-- Display success message (e.g., after registration redirect) --%>
            <%
                if ("GET".equalsIgnoreCase(request.getMethod()) && request.getAttribute("successMessage") != null) {
            %>
            <div class="error-message show success">
                <i class="fas fa-check-circle error-icon"></i>
                <span class="error-text"><%= request.getAttribute("successMessage") %></span>
            </div>
            <% } %>

            <%-- Email input field --%>
            <div class="form-group">
                <label for="user_email">Email Address</label>
                <div class="input-container">
                    <i class="fas fa-envelope input-icon"></i>
                    <input type="email" id="user_email" name="user_email" placeholder="Enter your email" required>
                </div>
            </div>

            <%-- Password input field with toggle visibility icon --%>
            <div class="form-group">
                <label for="password">Password</label>
                <div class="password-input-container">
                    <i class="fas fa-lock input-icon"></i>
                    <input type="password" id="password" name="password" placeholder="Enter your password" required>
                    <i id="togglePassword" class="fas fa-eye toggle-icon" onclick="togglePassword('password', 'togglePassword')"></i>
                </div>
            </div>

            <%-- Remember me checkbox and forgot password link --%>
            <div class="remember-forgot">
                <div class="remember-me">
                    <input type="checkbox" id="rememberMe" name="rememberMe">
                    <label for="rememberMe">Remember me</label>
                </div>
                <a href="/forgot-password" class="forgot-link">Forgot Password?</a>
            </div>

            <%-- Submit button for login form --%>
            <button type="submit" class="btn">Sign In</button>

            <%-- Link to registration page for new users --%>
            <div class="bottom-link">
                Don't have an account? <a href="RegisterServlet">Create Account</a>
            </div>
        </form>

        <%-- Footer with copyright information --%>
        <div class="form-footer">
            <p>© 2025 Library Management System. All rights reserved.</p>
        </div>
    </div>
    <%-- Hidden fallback message container for JavaScript use --%>
    <div id="fallbackMessage" class="fallback-message" style="display: none;"></div>
</div>
<%-- JavaScript to pass server-side messages to client-side script --%>
<script>
    // Pass messages to JavaScript for client-side handling
    const successMessage = '<%= (request.getAttribute("successMessage") != null && !((String)request.getAttribute("successMessage")).isEmpty()) ? request.getAttribute("successMessage") : "" %>';
    const errorMessage = '<%= (request.getAttribute("errorMessage") != null && !((String)request.getAttribute("errorMessage")).isEmpty()) ? request.getAttribute("errorMessage") : "" %>';
</script>
</body>
</html>