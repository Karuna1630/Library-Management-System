<%-- Set page content type and encoding for proper rendering --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- Page title for SEO and browser tab display --%>
    <title>Contact Us | UniShelf</title>
    <%-- Include Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <%-- Shared CSS for site-wide styling --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <%-- Custom CSS for contact page styling --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/contact.css">
    <%-- Ensure responsive design for mobile and desktop --%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%-- Include reusable navigation bar component --%>
<%@include file="../components/navbar.jsp" %>

<%-- Hero section to introduce the contact page --%>
<section class="contact-hero">
    <div class="container">
        <h1>Contact Our Team</h1>
        <p>Have questions or feedback? We're here to help you with any library-related inquiries.</p>
    </div>
</section>

<%-- Main content section with contact methods and form --%>
<section class="contact-content">
    <div class="container">
        <div class="contact-container">
            <%-- Contact methods section for direct communication options --%>
            <div class="contact-methods">
                <h2>Reach Us Directly</h2>

                <div class="contact-method">
                    <div class="contact-icon">
                        <i class="fas fa-envelope"></i>
                    </div>
                    <div class="contact-method-content">
                        <h3>Email Support</h3>
                        <p>library-support@unishelf.edu</p>
                        <p>Response time: 24 hours</p>
                    </div>
                </div>

                <div class="contact-method">
                    <div class="contact-icon">
                        <i class="fas fa-phone-alt"></i>
                    </div>
                    <div class="contact-method-content">
                        <h3>Phone Assistance</h3>
                        <p>+1 (555) 123-4567</p>
                        <p>Mon-Fri, 9AM-5PM</p>
                    </div>
                </div>

                <div class="contact-method">
                    <div class="contact-icon">
                        <i class="fas fa-building"></i>
                    </div>
                    <div class="contact-method-content">
                        <h3>Visit Us</h3>
                        <p>Main Library Building<br>
                            Room 205, Help Desk<br>
                            University Campus</p>
                    </div>
                </div>
            </div>

            <%-- Contact form for users to send messages --%>
            <div class="contact-form">
                <h2>Send Us a Message</h2>
                <form>
                    <div class="form-group">
                        <label for="name" class="form-label">Your Name</label>
                        <input type="text" id="name" class="form-input" required>
                    </div>

                    <div class="form-group">
                        <label for="email" class="form-label">Email Address</label>
                        <input type="email" id="email" class="form-input" required>
                    </div>

                    <div class="form-group">
                        <label for="subject" class="form-label">Subject</label>
                        <input type="text" id="subject" class="form-input" required>
                    </div>

                    <div class="form-group">
                        <label for="message" class="form-label">Message</label>
                        <textarea id="message" class="form-textarea" required></textarea>
                    </div>

                    <button type="submit" class="btn primary-btn submit-btn">
                        <i class="fas fa-paper-plane"></i> Send Message
                    </button>
                </form>
            </div>
        </div>
    </div>
</section>

<%-- Include reusable footer component --%>
<%@include file="../components/footer.jsp" %>
</body>
</html>