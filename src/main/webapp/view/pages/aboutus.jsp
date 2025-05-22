<%-- Set page content type and encoding, and declare Java as the scripting language --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- Page title for SEO and browser tab display --%>
    <title>About UniShelf - University Library Management System</title>
    <%-- Include Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <%-- Custom CSS for About page styling --%>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/aboutus.css">
    <%-- Shared CSS for consistent site-wide styling --%>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/index.css">
    <%-- Ensure responsive design for mobile and desktop --%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%-- Include reusable navigation bar component --%>
<%@include file="../components/navbar.jsp" %>

<%-- Hero section to introduce UniShelf --%>
<section class="about-hero-section">
    <div class="about-hero-content">
        <h1>About UniShelf</h1>
        <p class="subtitle">Transforming library experiences for modern universities</p>
    </div>
</section>

<%-- Mission section to describe UniShelf's purpose and goals --%>
<section class="mission-section">
    <div class="container">
        <%-- Section header for visual hierarchy --%>
        <div class="section-header">
            <h2>Our Mission</h2>
            <p class="section-subtitle">What drives us every day</p>
        </div>
        <div class="mission-container">
            <div class="mission-content">
                <h3>Empowering Academic Success Through Technology</h3>
                <p>UniShelf was founded in 2015 with a simple goal: to modernize university library systems and make academic resources more accessible to students and faculty alike. We believe that technology should remove barriers to knowledge, not create them.</p>
                <p>Our platform bridges the gap between traditional library services and the digital expectations of today's academic community. By combining intuitive design with powerful functionality, we've created a solution that serves both small colleges and large university systems.</p>
                <%-- avtalent = "Call to action to encourage user engagement" --%>
                <div class="section-action">
                    <a href="./contact.jsp" class="btn primary-btn">Contact Our Team</a>
                </div>
            </div>
            <%-- Image to enhance visual appeal of the mission section --%>
            <div class="mission-image">
                <img src="https://images.unsplash.com/photo-1523240795612-9a054b0db644?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80" alt="University library">
            </div>
        </div>
    </div>
</section>

<%-- Include reusable footer component --%>
<%@include file="../components/footer.jsp" %>

</body>
</html>