<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.User" %>
<html>
<head>
    <title>Profile - UniShelf</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/index.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%@include file="../components/navbar.jsp" %>

<%
    // Check if user exists in session without redeclaring the variable
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
        return;
    }
    // Get the user object that was already declared in navbar.jsp
    User profileUser = (User) session.getAttribute("user");
%>

<div class="profile-container">
    <div class="profile-header">
        <h1>My Profile</h1>
        <p>Manage your personal information</p>
    </div>

    <% if (request.getAttribute("successMessage") != null) { %>
    <div class="alert alert-success">
        <%= request.getAttribute("successMessage") %>
    </div>
    <% } %>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <div class="alert alert-error">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>

    <div class="profile-content">
        <form action="<%= request.getContextPath() %>/ProfileServlet" method="post" enctype="multipart/form-data" class="profile-form">
            <div class="profile-image-section">
                <% if (session.getAttribute("base64Image") != null) { %>
                <img src="data:image/jpeg;base64,<%= session.getAttribute("base64Image") %>"
                     alt="Profile"
                     class="profile-large-image"
                     onerror="this.src='<%= request.getContextPath() %>/assets/images/default-profile.svg'">
                <% } else { %>
                <div class="profile-initial-large"><%= profileUser.getFullName().charAt(0) %></div>
                <% } %>
                <label for="profileImage" class="btn-change-photo">
                    <i class="fas fa-camera"></i> Change Photo
                    <input type="file" id="profileImage" name="profileImage" accept="image/*" style="display: none;">
                </label>
            </div>

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

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
    // Show selected image preview
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