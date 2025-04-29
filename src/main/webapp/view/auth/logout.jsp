<%--
  Created by IntelliJ IDEA.
  User: USER-PC
  Date: 4/16/2025
  Time: 10:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logging Out - UniShelf</title>
    <meta http-equiv="refresh" content="2;url=<%= request.getContextPath() %>/LoginServlet?logout=true">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/Logout.css">
</head>
<body>
<div class="logout-message">
    <p>Logging you out...</p>
</div>
</body>
</html>
