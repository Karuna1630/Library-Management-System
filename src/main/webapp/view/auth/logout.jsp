<%-- Logout page for UniShelf - University Library Management System --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- Page title --%>
    <title>Logging Out</title>
    <%-- Custom stylesheet for index page styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/index.css">
    <%-- Custom stylesheet for logout page styling --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/Logout.css">
</head>
<body>
<%-- Include JavaScript for toast notifications and other utilities --%>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
<script>
    // Show logout toast and redirect to homepage
    document.addEventListener('DOMContentLoaded', function() {
        // Display success toast message
        showToast('You have been successfully logged out.', 'success');
        // Redirect to homepage after 3 seconds
        setTimeout(function() {
            window.location.href = "<%= request.getContextPath() %>/index.jsp";
        }, 3000);
    });
</script>
</body>
</html>