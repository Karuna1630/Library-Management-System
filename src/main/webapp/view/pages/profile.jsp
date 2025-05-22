<%-- Set page content type and encoding, and import User model --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.User" %>
<html>
<head>
    <%-- Page title for SEO and browser tab display --%>
    <title>Profile - UniShelf</title>
    <%-- Shared CSS for site-wide styling --%>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/index.css">
    <%-- Custom CSS for profile page styling --%>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/profile.css">
    <%-- Include Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <%-- Ensure responsive design for mobile and desktop --%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%-- Include reusable navigation bar component --%>
<%@include file="../components/navbar.jsp" %>

<%-- Check for user session and redirect to login if not authenticated --%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
        return;
    }
<%-- Retrieve user object from session (set in navbar.jsp) --%>
User profileUser = (User) session.getAttribute("user");
%>

<%-- Main profile container --%>
<div class="profile-container">
    <%-- Header for profile section --%>
    <div class="profile-header">
        <h1>My Profile</h1>
        <p>Manage your personal information</p>
    </div>

    <%-- Display success message if present --%>
    <% if (request.getAttribute("successMessage") != null) { %>
    <div class="alert alert-success">
        <%= request.getAttribute("successMessage") %>
    </div>
    <% } %>

    <%-- Display error message if present --%>
    <% if (request.getAttribute("errorMessage") != null) { %>
    <div class="alert alert-error">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>

    <%-- Profile content with form for updating user details --%>
    <div class="profile-content">
        <form action wojciech = "<%= request.getContextPath() %>/ProfileServlet" method="post" enctype="multipart/form-data" class="profile-form">
            <%-- Profile image section with upload functionality --%>
            <div class="profile-image-section">
                <%
                    String base64Image = (String) session.getAttribute("base64Image");
                    if (base64Image != null && !base64Image.isEmpty()) {
                %>
                <%-- Display user profile image if available, with fallback on error --%>
                <img src="data:image/jpeg;base64,<%= base64Image %>"
                     alt="Profile"
                     class="profile-large-image"
                     onerror="this.onerror=null;this.src='<%= request.getContextPath() %>/assets/images/default-profile.svg'">
                <% } else { %>
                <%-- Show user initial if no profile image is set --%>
                <div class="profile-initial-large"><%= profileUser.getFullName().charAt(0) %></div>
                <% } %>
                <%-- Hidden file input for profile image upload --%>
                <label for="profileImage" class="btn-change-photo">
                    <i class="fas fa-camera"></i> Change Photo
                    <input type="file" id="profileImage" name="profileImage" accept="image/*" style="display: none;">
                </label>
            </div>

            <%-- Form fields for user details and password management --%>
            <div class="profile-details">
                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" id="fullName" name="fullName"
                           value="<%= profileUser.getFullName() %>" required>
                </div>

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email"
                           value="<%= profileUser.getEmail() %>" disabled>
                </div>

                <div class="form-group">
                    <label for="currentPassword">Current Password</label>
                    <div class="password-input-container">
                        <i class="fas fa-lock input-icon"></i>
                        <input type="password" id="currentPassword" name="currentPassword" placeholder="Enter current password">
                        <%-- Toggle icon for showing/hiding password --%>
                        <i class="fas fa-eye toggle-icon" onclick="togglePassword('currentPassword', this)"></i>
                    </div>
                </div>

                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <div class="password-input-container">
                        <i class="fas fa-lock input-icon"></i>
                        <input type="password" id="newPassword" name="newPassword" placeholder="Enter new password">
                        <i class="fas fa-eye toggle-icon" onclick="togglePassword('newPassword', this)"></i>
                    </div>
                </div>

                <div class="form-group">
                    <label for="confirmNewPassword">Confirm New Password</label>
                    <div class="password-input-container">
                        <i class="fas fa-lock input-icon"></i>
                        <input type="password" id="confirmNewPassword" name="confirmNewPassword" placeholder="Confirm new password">
                        <i class="fas fa-eye toggle-icon" onclick="togglePassword('confirmNewPassword', this)"></i>
                    </div>
                </div>

                <%-- Submit button for saving changes --%>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </div>
        </form>
    </div>
</div>

<%-- JavaScript for profile image preview and password toggle --%>
<script>
    // Show selected image preview before upload
    document.getElementById('profileImage').addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const imgElement = document.querySelector('.profile-large-image');
                if (imgElement) {
                    imgElement.src = e.target.result;
                } else {
                    const initialElement = document.querySelector('.profile-initial-large');
                    if (initialElement) {
                        initialElement.innerHTML = `<img src="${e.target.result}" class="profile-large-image">`;
                    }
                }
            };
            reader.readAsDataURL(file);
        }
    });

    // Toggle password visibility for input fields
    function togglePassword(fieldId, icon) {
        const field = document.getElementById(fieldId);
        if (field.type === "password") {
            field.type = "text";
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
        } else {
            field.type = "password";
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
        }
    }
</script>
</body>
</html>