<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.User" %>
<html>
<head>
    <title>UniShelf - University Library Management System</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/navbar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="${pageContext.request.contextPath}/assets/js/navbar.js"></script>
</head>
<body>
<nav class="navbar">
    <div class="navbar-container">
        <div class="navbar-brand">
            <i class="fas fa-book-open"></i>
            <span>UniShelf</span>
        </div>

        <div class="navbar-menu">
            <ul class="nav-links">
                <li><a href="<%= request.getContextPath() %>/index.jsp" class="nav-link">Home</a></li>
                <li><a href="<%= request.getContextPath() %>/view/pages/books.jsp" class="nav-link">Books</a></li>
                <li><a href="<%= request.getContextPath() %>/view/pages/aboutus.jsp" class="nav-link">About Us</a></li>
                <li><a href="<%= request.getContextPath() %>/view/pages/contact.jsp" class="nav-link">Contact Us</a></li>
            </ul>
        </div>

        <div class="navbar-actions">
            <%
                User user = (User) session.getAttribute("user");
                if (user == null) {
            %>
            <a href="<%= request.getContextPath() %>/LoginServlet" class="btn btn-outline">Login</a>
            <a href="<%= request.getContextPath() %>/RegisterServlet" class="btn btn-primary">Sign Up</a>
            <% } else { %>
            <div class="profile-dropdown">
                <div class="profile-toggle">
                    <% if (session.getAttribute("base64Image") != null) { %>
                    <img src="data:image/jpeg;base64,<%= session.getAttribute("base64Image") %>"
                         alt="Profile"
                         class="profile-image"
                         onerror="this.src='<%= request.getContextPath() %>/assets/images/default-profile.svg'">
                    <% } else { %>
                    <div class="profile-initial"><%= user.getFullName().charAt(0) %></div>
                    <% } %>
                    <span class="profile-name"><%= user.getFullName() %></span>
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="dropdown-content">
                    <a href="<%= request.getContextPath() %>/view/pages/profile.jsp">
                        <i class="fas fa-user"></i> Profile
                    </a>
                    <a href="<%= request.getContextPath() %>/LogoutServlet">
                        <i class="fas fa-sign-out-alt"></i> Logout
                    </a>
                </div>
            </div>
            <% } %>

            <a href="<%= request.getContextPath() %>/view/auth/reserve.jsp" class="cart-icon">
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