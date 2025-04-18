<%--
  Created by IntelliJ IDEA.
  User: USER-PC
  Date: 4/16/2025
  Time: 10:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<html>
<head>
    <title>UniShelf - University Library Management System</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/navbar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<nav class="navbar">
    <div class="navbar-container">
        <div class="navbar-brand">
            <i class="fas fa-book-open"></i>
            <span>UniShelf</span>
        </div>

        <div class="navbar-search">
            <form action="<%= request.getContextPath() %>/view/pages/books.jsp" method="get">
                <div class="search-container">
                    <input type="text" name="query" placeholder="Search books..." class="search-input">
                    <button type="submit" class="search-button">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
            </form>
        </div>

        <div class="navbar-menu">
            <ul class="nav-links">
                <li><a href="<%= request.getContextPath() %>/index.jsp" class="nav-link">Home</a></li>
                <li><a href="<%= request.getContextPath() %>/view/pages/books.jsp" class="nav-link">Books</a></li>
                <li><a href="<%= request.getContextPath() %>/view/pages/aboutus.jsp" class="nav-link">About Us</a></li>

                <%
                    String role = (String) session.getAttribute("role");
                    if ("member".equals(role)) {
                %>
                <li><a href="<%= request.getContextPath() %>/view/pages/profile.jsp" class="nav-link">Profile</a></li>
                <% } else if ("admin".equals(role)) { %>
                <li><a href="<%= request.getContextPath() %>/view/dashboard/admin-dashboard.jsp" class="nav-link">Dashboard</a></li>
                <% } %>
            </ul>
        </div>

        <div class="navbar-actions">
            <%
                String username = (String) session.getAttribute("username");
                if (username == null) {
            %>
            <a href="<%= request.getContextPath() %>/view/auth/login.jsp" class="btn btn-outline">Login</a>
            <a href="<%= request.getContextPath() %>/view/auth/register.jsp" class="btn btn-primary">Sign Up</a>
            <% } else { %>
            <a href="<%= request.getContextPath() %>/view/auth/logout.jsp" class="btn btn-outline">Logout</a>
            <% } %>

            <a href="<%= request.getContextPath() %>/view/auth/reservation.jsp" class="cart-icon">
                <i class="fas fa-store"></i>
            </a>
        </div>

        <button class="navbar-toggle">
            <i class="fas fa-bars"></i>
        </button>
    </div>
</nav>
</body>
</html>