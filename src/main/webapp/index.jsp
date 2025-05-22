<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.Book,java.util.List,java.util.Calendar" %>
<%-- Home page for UniShelf - University Library Management System --%>
<html>
<head>
  <%-- Page title and metadata --%>
  <title>UniShelf - University Library Management System</title>
  <%-- External stylesheet for Font Awesome icons --%>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <%-- Custom stylesheet for index page styling --%>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/index.css">
  <%-- Responsive viewport setting --%>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%-- Include navigation bar component --%>
<%@include file="view/components/navbar.jsp" %>

<%-- Hero section with call-to-action buttons --%>
<div class="hero-section">
  <div class="hero-content">
    <h1>Discover <span class="highlight">Knowledge</span> at Your Fingertips</h1>
    <p class="subtitle">Your modern university library management system for seamless book reservations and academic resources.</p>
    <div class="hero-buttons">
      <a href="BookListServlet" class="btn primary-btn"><i class="fas fa-book"></i> Explore Collection</a>
      <a href="LoginServlet" class="btn secondary-btn"><i class="fas fa-user"></i> Sign In</a>
    </div>
  </div>
  <%-- Decorative wave SVG for visual effect --%>
  <div class="hero-wave">
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320">
      <path fill="#ffffff" fill-opacity="1" d="M0,96L48,112C96,128,192,160,288,160C384,160,480,128,576,122.7C672,117,768,139,864,149.3C960,160,1056,160,1152,144C1248,128,1344,96,1392,80L1440,64L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path>
    </svg>
  </div>
</div>

<%-- Features section highlighting system capabilities --%>
<section class="features-section">
  <div class="section-header">
    <h2>Smart Library Services</h2>
    <p class="section-subtitle">Everything you need for an enhanced learning experience</p>
  </div>
  <div class="features-container">
    <div class="feature-card">
      <div class="feature-icon-container">
        <i class="fas fa-search feature-icon"></i>
      </div>
      <h3>Intelligent Search</h3>
      <p>Find books, journals, and research papers quickly with our advanced search system.</p>
    </div>
    <div class="feature-card">
      <div class="feature-icon-container">
        <i class="fas fa-calendar-check feature-icon"></i>
      </div>
      <h3>Simple Reservations</h3>
      <p>Reserve materials online and receive notifications when they're ready for pickup.</p>
    </div>
    <div class="feature-card">
      <div class="feature-icon-container">
        <i class="fas fa-history feature-icon"></i>
      </div>
      <h3>Digital Tracking</h3>
      <p>Monitor your borrowing history and manage returns with a personalized dashboard.</p>
    </div>
    <div class="feature-card">
      <div class="feature-icon-container">
        <i class="fas fa-bell feature-icon"></i>
      </div>
      <h3>Smart Alerts</h3>
      <p>Get timely notifications about due dates, new arrivals, and available reservations.</p>
    </div>
  </div>
</section>

<%-- New arrivals section displaying latest books --%>
<section class="new-arrivals-section">
  <div class="section-header">
    <h2>Latest Additions</h2>
    <p class="section-subtitle">Fresh resources for your academic success</p>
  </div>
  <div class="books-container">
    <%-- Iterate over latest books from request attribute --%>
    <%
      List<Book> latestBooks = (List<Book>) request.getAttribute("latestBooks");
      if (latestBooks != null) {
        for (Book book : latestBooks) {
          // Escape HTML to prevent XSS attacks
          String title = book.getTitle() != null ? book.getTitle().replace("\"", "&quot;") : "";
          String author = book.getAuthor() != null ? book.getAuthor().replace("\"", "&quot;") : "";
          String category = book.getCategory() != null ? book.getCategory().replace("\"", "&quot;") : "";
          // Determine book tag based on publication year
          String bookTag = "New";
          if (book.getPublicationYear() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(book.getPublicationYear());
            int pubYear = cal.get(Calendar.YEAR);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (pubYear != currentYear) {
              bookTag = "Classic";
            }
          }
    %>
    <div class="book-card">
      <div class="book-cover-container">
        <div class="book-cover">
          <%-- Display book cover image with fallback --%>
          <img src="<%= request.getContextPath() %>/BookCoverServlet?id=<%= book.getBookId() %>" alt="<%= title %> cover" style="width:100%; height:100%; object-fit:cover;" onerror="this.src='<%= request.getContextPath() %>/assets/images/fallback-cover.jpg';">
        </div>
        <div class="book-tag"><%= bookTag %></div>
      </div>
      <div class="book-info">
        <h3><%= title %></h3>
        <p class="book-author">By <%= author %></p>
        <div class="book-meta">
          <span><i class="fas fa-bookmark"></i> <%= category %></span>
          <span><i class="fas fa-box"></i> <%= book.getStock() %> in stock</span>
        </div>
        <a href="book-details.jsp?id=<%= book.getBookId() %>" class="btn small-btn">View Details</a>
      </div>
    </div>
    <%
        }
      }
    %>
  </div>
  <div class="section-action">
    <%-- Link to view full book collection --%>
    <a href="BookListServlet" class="btn outline-btn">View All Books <i class="fas fa-arrow-right"></i></a>
  </div>
</section>

<%-- How-it-works section explaining user workflow --%>
<section class="how-it-works">
  <div class="section-header">
    <h2>How UniShelf Works</h2>
    <p class="section-subtitle">Four simple steps to access our resources</p>
  </div>
  <div class="steps-container">
    <div class="step">
      <div class="step-number">1</div>
      <h3>Create Account</h3>
      <p>Register with your university credentials to set up your personal library profile</p>
    </div>
    <div class="step-connector"></div>
    <div class="step">
      <div class="step-number">2</div>
      <h3>Browse Collection</h3>
      <p>Search our extensive catalog by title, author, subject, or category</p>
    </div>
    <div class="step-connector"></div>
    <div class="step">
      <div class="step-number">3</div>
      <h3>Reserve Materials</h3>
      <p>Place holds on available resources with just a few clicks</p>
    </div>
    <div class="step-connector"></div>
    <div class="step">
      <div class="step-number">4</div>
      <h3>Pick Up & Return</h3>
      <p>Visit the library at your convenience to collect and return items</p>
    </div>
  </div>
</section>

<%-- Include footer component --%>
<%@include file="view/components/footer.jsp" %>
</body>
</html>