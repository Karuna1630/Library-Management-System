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
    <meta charset="UTF-8">
    <title>Navbar</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/navbar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-left">
        <div class="logo">UniSelf</div>
        <!-- Search bar wrapped in a form -->
        <form action="<%= request.getContextPath() %>/view/pages/books.jsp" method="get">
            <input type="text" class="search-bar" name="query" placeholder="Search books">
        </form>
    </div>

    <div class="navbar-center">
        <ul class="nav-links">
            <li><a href="<%= request.getContextPath() %>/index.jsp">Home</a></li>
            <li><a href="<%= request.getContextPath() %>/view/pages/books.jsp">Books</a></li>
            <li><a href="<%= request.getContextPath() %>/view/pages/aboutus.jsp">About Us</a></li>

            <%
                String role = (String) session.getAttribute("role");
                if ("member".equals(role)) {
            %>
            <li><a href="<%= request.getContextPath() %>/view/pages/profile.jsp">Profile</a></li>
            <% } else if ("admin".equals(role)) { %>
            <li><a href="<%= request.getContextPath() %>/view/dashboard/admin-dashboard.jsp">Admin</a></li>
            <% } %>
        </ul>
    </div>

    <div class="navbar-right">
        <%
            String username = (String) session.getAttribute("username");
            if (username == null) {
        %>
        <a href="<%= request.getContextPath() %>/view/auth/login.jsp" class="auth-btn">Login</a>
        <a href="<%= request.getContextPath() %>/view/auth/register.jsp" class="auth-btn">Sign Up</a>
        <% } else { %>
        <a href="<%= request.getContextPath() %>/view/auth/logout.jsp" class="auth-btn">Logout</a>
        <% } %>

        <a href="<%= request.getContextPath() %>/view/auth/reserve.jsp" class="cart-btn"><i class="fa-solid fa-store"></i></a>
    </div>
</nav>
</body>
</html>

