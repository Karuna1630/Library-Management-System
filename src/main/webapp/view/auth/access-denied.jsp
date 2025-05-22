<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Error page displayed when a user attempts to access a restricted resource --%>
<html>
<head>
    <%-- Page title and metadata --%>
    <title>Access Denied - UniShelf</title>
    <%-- External stylesheet for Font Awesome icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <%-- Custom stylesheet for access-denied page styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/access-denied.css">
</head>
<body>
<%-- Main container for the access denied page --%>
<div class="page-container">
    <div class="access-denied-container">
        <%-- Header section with error icon and title --%>
        <div class="error-header">
            <i class="fas fa-exclamation-triangle"></i>
            <h1>403 - Access Denied</h1>
        </div>

        <%-- Content section with error message and instructions --%>
        <div class="error-content">
            <p>You don't have permission to access this page.</p>
            <p>Please contact the administrator if you believe this is an error.</p>

            <%-- Action buttons to redirect users --%>
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/LoginServlet" class="btn">
                    <i class="fas fa-sign-in-alt"></i> Return to Login
                </a>
                <a href="${pageContext.request.contextPath}/" class="btn">
                    <i class="fas fa-home"></i> Go to Homepage
                </a>
            </div>
        </div>

        <%-- Footer with copyright information --%>
        <div class="footer">
            <p>&copy; 2025 UniShelf - Library Management System</p>
        </div>
    </div>
</div>
</body>
</html>