<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Access Denied - UniShelf</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/access-denied.css">
</head>
<body>
<div class="page-container">
    <div class="access-denied-container">
        <div class="error-header">
            <i class="fas fa-exclamation-triangle"></i>
            <h1>403 - Access Denied</h1>
        </div>

        <div class="error-content">
            <p>You don't have permission to access this page.</p>
            <p>Please contact the administrator if you believe this is an error.</p>

            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/LoginServlet" class="btn">
                    <i class="fas fa-sign-in-alt"></i> Return to Login
                </a>
                <a href="${pageContext.request.contextPath}/" class="btn">
                    <i class="fas fa-home"></i> Go to Homepage
                </a>
            </div>
        </div>

        <div class="footer">
            <p>&copy; 2025 UniShelf - Library Management System</p>
        </div>
    </div>
</div>
</body>
</html>