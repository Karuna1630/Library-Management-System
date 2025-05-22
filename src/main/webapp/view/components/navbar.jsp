<%--
  Created by IntelliJ IDEA.
  User: [Author]
  Date: [Date]
  Time: [Time]
  To change this template use File | Settings | File Templates.
--%>
<%-- Reusable navigation bar component for UniShelf - University Library Management System --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.User" %>
<%-- Note: <html>, <head>, and <body> tags are unnecessary here as this is a fragment included in other JSP pages --%>
<html>
<head>
    <%-- Page title (not needed for a navbar component) --%>
    <title>UniShelf - University Library Management System</title>
    <%-- Custom stylesheet for navbar styling --%>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/navbar.css">
    <%-- External stylesheet for Font Awesome icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <%-- Responsive viewport setting (not needed for a navbar component) --%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%-- Custom JavaScript for navbar functionality (e.g., toggle menu) --%>
    <script src="${pageContext.request.contextPath}/assets/js/navbar.js"></script>
</head>
<body>
<%-- Navigation bar with branding, menu, and user actions --%>
<nav class="navbar">
    <div class="navbar-container">
        <%-- Branding section with logo and system name --%>
        <div class="navbar-brand">
            <i class="fas fa-book-open"></i>
            <span>UniShelf</span>
        </div>

        <%-- Main navigation menu --%>
        <div class="navbar-menu">
            <ul class="nav-links">
                <li><a href="<%= request.getContextPath() %>/IndexServlet" class="nav-link">Home</a></li>
                <li><a href="<%= request.getContextPath() %>/BookListServlet" class="nav-link">Books</a></li>
                <li><a href="<%= request.getContextPath() %>/AboutUsServlet" class="nav-link">About Us</a></li>
                <li><a href="<%= request.getContextPath() %>/ContactServlet" class="nav-link">Contact Us</a></li>
            </ul>
        </div>

        <%-- User actions based on authentication status --%>
        <div class="navbar-actions">
            <%
                User user = (User) session.getAttribute("user");
                if (user == null) {
            %>
            <%-- Display login and sign-up buttons for unauthenticated users --%>
            <a href="<%= request.getContextPath() %>/LoginServlet" class="btn btn-outline">Login</a>
            <a href="<%= request.getContextPath() %>/RegisterServlet" class="btn btn-primary">Sign Up</a>
            <% } else {
                // Convert user image to Base64 for display if not already in session
                String base64Image = (String) session.getAttribute("base64Image");
                if (base64Image == null && user.getImage() != null) {
                    base64Image = java.util.Base64.getEncoder().encodeToString(user.getImage());
                    session.setAttribute("base64Image", base64Image);
                }
            %>
            <%-- Profile dropdown for authenticated users --%>
            <div class="profile-dropdown">
                <div class="profile-toggle">
                    <% if (base64Image != null) { %>
                    <%-- Display user profile image with fallback --%>
                    <img src="data:image/jpeg;base64,<%= base64Image %>"
                         alt="Profile"
                         class="profile-image"
                         onerror="this.onerror=null;this.src='<%= request.getContextPath() %>/assets/images/default-profile.svg'">
                    <% } else { %>
                    <%-- Display user initial if no profile image is available --%>
                    <div class="profile-initial"><%= user.getFullName().charAt(0) %></div>
                    <% } %>
                    <span class="profile-name"><%= user.getFullName() %></span>
                    <i class="fas fa-chevron-down"></i>
                </div>
                <%-- Dropdown menu with profile and logout options --%>
                <div class="dropdown-content">
                    <a href="<%= request.getContextPath() %>/ProfileServlet">
                        <i class="fas fa-user"></i> Profile
                    </a>
                    <a href="<%= request.getContextPath() %>/LogoutServlet">
                        <i class="fas fa-sign-out-alt"></i> Logout
                    </a>
                </div>
            </div>
            <% } %>

            <%-- Link to user's book reservations or cart --%>
            <a href="<%= request.getContextPath() %>/UserBooksServlet" class="cart-icon">
                <i class="fas fa-store"></i>
            </a>
        </div>

        <%-- Mobile menu toggle button --%>
        <button class="navbar-toggle">
            <i class="fas fa-bars"></i>
        </button>
    </div>
</nav>
</body>
</html>