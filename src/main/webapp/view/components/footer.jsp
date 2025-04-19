<%--
  Created by IntelliJ IDEA.
  User: USER-PC
  Date: 4/16/2025
  Time: 10:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UniShelf - University Library Management System</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/footer.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<footer class="footer">
    <div class="footer-container">
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

        <div class="footer-section">
            <h3 class="footer-heading">Quick Links</h3>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/index.jsp" class="footer-link">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/view/pages/books.jsp" class="footer-link">Books</a></li>
                <li><a href="${pageContext.request.contextPath}/view/pages/aboutus.jsp" class="footer-link">About Us</a></li>
            </ul>
        </div>

        <div class="footer-section">
            <h3 class="footer-heading">Contact Us</h3>
            <ul class="footer-contact">
                <li><i class="fas fa-map-marker-alt"></i> Itahari International Collage, Dulari, Morang</li>
                <li><i class="fas fa-phone"></i> +977 987456321</li>
                <li><i class="fas fa-envelope"></i> iic@edu.np</li>
            </ul>
        </div>
    </div>

    <div class="footer-bottom">
        <p>&copy; 2025 UniShelf Library Management System. All rights reserved.</p>
    </div>
</footer>
</body>
</html>
