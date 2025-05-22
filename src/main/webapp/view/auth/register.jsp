<%-- Registration page for UniShelf - University Library Management System --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- Page title and metadata --%>
    <title>UniShelf - University Library Management System</title>
    <%-- External stylesheet for Font Awesome icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <%-- Custom stylesheet for registration page styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/register.css">
    <%-- External jQuery library for client-side scripting --%>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <%-- Custom JavaScript for form validation and image preview --%>
    <script src="${pageContext.request.contextPath}/assets/js/register.js?v=1.0"></script>
</head>
<body>
<%-- Main container for the registration page --%>
<div class="page-container">
    <div class="form-container">
        <%-- Header with system name and logo icon --%>
        <div class="form-header">
            <h1>UniShelf</h1>
            <div class="logo">
                <i class="fas fa-book-reader"></i>
            </div>
        </div>

        <%-- Registration form submitting to RegisterServlet with file upload support --%>
        <form class="registerForm" id="registerForm"
              action="${pageContext.request.contextPath}/RegisterServlet"
              method="POST"
              enctype="multipart/form-data"
              onsubmit="return validateRegisterForm('registerForm')">
            <h2>Create an Account</h2>
            <p>Your journey to smarter reading starts here.</p>
            <%-- Display error message for failed registration attempts --%>
            <%
                if ("POST".equalsIgnoreCase(request.getMethod()) && request.getAttribute("errorMessage") != null) {
            %>
            <div class="error-message show">
                <i class="fas fa-exclamation-circle error-icon"></i>
                <span class="error-text"><%= request.getAttribute("errorMessage") %></span>
            </div>
            <% } %>

            <%-- Username input field --%>
            <div class="form-group">
                <label for="full_name">UserName</label>
                <div class="input-container">
                    <i class="fas fa-user input-icon"></i>
                    <input type="text" id="full_name" name="full_name" placeholder="Enter your full name" required>
                    <span id="full_name-error" class="error-message"></span>
                </div>
            </div>

            <%-- Email input field --%>
            <div class="form-group">
                <label for="user_email">Email Address</label>
                <div class="input-container">
                    <i class="fas fa-envelope input-icon"></i>
                    <input type="email" id="user_email" name="user_email" placeholder="Enter your email" required>
                    <span id="email-error" class="error-message"></span>
                </div>
            </div>

            <%-- Password input field with toggle visibility icon --%>
            <div class="form-group">
                <label for="password">Password</label>
                <div class="password-input-container">
                    <i class="fas fa-lock input-icon"></i>
                    <input type="password" id="password" name="password" placeholder="Choose a password" required>
                    <i id="togglePassword" class="fas fa-eye toggle-icon" onclick="togglePassword('password', 'togglePassword')"></i>
                    <span id="password-error" class="error-message"></span>
                </div>
            </div>

            <%-- Confirm password input field with toggle visibility icon --%>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <div class="password-input-container">
                    <i class="fas fa-lock input-icon"></i>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm password" required>
                    <i id="toggleConfirmPassword" class="fas fa-eye toggle-icon" onclick="togglePassword('confirmPassword', 'toggleConfirmPassword')"></i>
                    <span id="confirm-password-error" class="error-message"></span>
                </div>
            </div>

            <%-- Role selection dropdown --%>
            <div class="form-group">
                <label for="role">Role:</label>
                <div class="input-container">
                    <i class="fas fa-user-tag input-icon"></i>
                    <select id="role" name="role" class="form-select" required>
                        <option value="" disabled selected>Select your role</option>
                        <option value="user">User</option>
                        <option value="admin">Administrator</option>
                    </select>
                    <span id="role-error" class="error-message"></span>
                </div>
            </div>

            <%-- Profile picture upload field with preview --%>
            <div class="form-group">
                <label for="image">Profile Picture</label>
                <div class="file-input-container">
                    <label for="image" class="file-input-label">
                        <i class="fas fa-upload"></i>
                        <span>Choose a file</span>
                    </label>
                    <input type="file" id="image" name="image" accept="image/*" onchange="previewImage()">
                </div>

                <%-- Preview area for uploaded profile picture --%>
                <div class="image-preview-container">
                    <img id="imagePreview"
                         class="image-preview"
                         src="${pageContext.request.contextPath}/assets/image/default-profile.jpg"
                         alt="Profile Preview" />
                    <p class="preview-text">Upload an image to change the default profile picture</p>
                </div>
            </div>

            <%-- Submit button for registration form --%>
            <button type="submit" class="btn">Register</button>

            <%-- Link to login page for existing users --%>
            <div class="bottom-link">
                Already have an account? <a href="${pageContext.request.contextPath}/LoginServlet">Sign In</a>
            </div>
        </form>

        <%-- Footer with copyright information --%>
        <div class="form-footer">
            <p>© 2025 Library Management System. All rights reserved.</p>
        </div>
    </div>
</div>
</body>
</html>