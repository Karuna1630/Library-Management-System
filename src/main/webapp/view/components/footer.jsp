<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 18-04-2025
  Time: 01:55 am
  To change this template use File | Settings | File Templates.
--%>
<%-- Reusable footer component for UniShelf - University Library Management System --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Note: <html>, <head>, and <body> tags are unnecessary here as this is a fragment included in other JSP pages --%>
<html>
<head>
    <%-- Page title (not needed for a footer component) --%>
    <title>UniShelf - University Library Management System</title>
    <%-- External stylesheet for Font Awesome icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <%-- Custom stylesheet for footer styling --%>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/footer.css">
    <%-- Responsive viewport setting (not needed for a footer component) --%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%-- Footer section with branding, links, and contact information --%>
<footer class="footer">
    <div class="footer-container">
        <%-- Branding and social media links --%>
        <div class="footer-section">
            <h3 class="footer-heading">UniShelf</h3>
            <p class="footer-text">Your modern university library management system for seamless book reservations and academic resources.</p>
            <div class="social-links">
                <a href="#" class="social-link"><i class="fab fa-facebook-f"></i></a>
                <a href="#" class="social-link"><i class="fab fa-twitter"></i></a>
                <a href="#" class="social-link"><i class="fab fa-instagram"></i></a>
                <a href="#" class="social-link"><i class="fab fa-linkedin-in"></i></a>
            </div>
        </div>

        <%-- Quick navigation links --%>
        <div class="footer-section">
            <h3 class="footer-heading">Quick Links</h3>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/index.jsp" class="footer-link">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/view/pages/books.jsp" class="footer-link">Books</a></li>
                <li><a href="${pageContext.request.contextPath}/view/pages/aboutus.jsp" class="footer-link">About Us</a></li>
                <li><a href="${pageContext.request.contextPath}/view/pages/contact.jsp" class="footer-link">Contact Us</a></li>
            </ul>
        </div>

        <%-- Contact information --%>
        <div class="footer-section">
            <h3 class="footer-heading">Contact Us</h3>
            <ul class="footer-contact">
                <li><i class="fas fa-map-marker-alt"></i> Itahari International Collage, Dulari, Morang</li>
                <li><i class="fas fa-phone"></i> +977 987456321</li>
                <li><i class="fas fa-envelope"></i> iic@edu.np</li>
            </ul>
        </div>
    </div>

    <%-- Footer bottom with copyright information --%>
    <div class="footer-bottom">
        <p>&copy; 2025 UniShelf Library Management System. All rights reserved.</p>
    </div>
</footer>
</body>
</html>