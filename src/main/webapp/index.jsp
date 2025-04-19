<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>UniShelf - University Library Management System</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/assets/css/index.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%@include file="view/components/navbar.jsp" %>

<div class="hero-section">
  <div class="hero-content">
    <h1>Discover <span class="highlight">Knowledge</span> at Your Fingertips</h1>
    <p class="subtitle">Your modern university library management system for seamless book reservations and academic resources.</p>
    <div class="hero-buttons">
      <a href="./view/pages/books.jsp" class="btn primary-btn"><i class="fas fa-book"></i> Explore Collection</a>
      <a href="LoginServlet" class="btn secondary-btn"><i class="fas fa-user"></i> Sign In</a>
    </div>
  </div>
  <div class="hero-wave">
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320">
      <path fill="#ffffff" fill-opacity="1" d="M0,96L48,112C96,128,192,160,288,160C384,160,480,128,576,122.7C672,117,768,139,864,149.3C960,160,1056,160,1152,144C1248,128,1344,96,1392,80L1440,64L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path>
    </svg>
  </div>
</div>

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

<section class="new-arrivals-section">
  <div class="section-header">
    <h2>Latest Additions</h2>
    <p class="section-subtitle">Fresh resources for your academic success</p>
  </div>
  <div class="books-container">
    <!-- These would ideally be populated dynamically from your database -->
    <div class="book-card">
      <div class="book-cover-container">
        <div class="book-cover"><i class="fas fa-book"></i></div>
        <div class="book-tag">New</div>
      </div>
      <div class="book-info">
        <h3>Advanced Java Programming</h3>
        <p class="book-author">By John Smith</p>
        <div class="book-meta">
          <span><i class="fas fa-star"></i> 4.8</span>
          <span><i class="fas fa-bookmark"></i> Computer Science</span>
        </div>
        <a href="book-details.jsp?id=1" class="btn small-btn">View Details</a>
      </div>
    </div>
    <div class="book-card">
      <div class="book-cover-container">
        <div class="book-cover"><i class="fas fa-book"></i></div>
        <div class="book-tag">New</div>
      </div>
      <div class="book-info">
        <h3>Modern Database Systems</h3>
        <p class="book-author">By Mary Johnson</p>
        <div class="book-meta">
          <span><i class="fas fa-star"></i> 4.6</span>
          <span><i class="fas fa-bookmark"></i> IT</span>
        </div>
        <a href="book-details.jsp?id=2" class="btn small-btn">View Details</a>
      </div>
    </div>
    <div class="book-card">
      <div class="book-cover-container">
        <div class="book-cover"><i class="fas fa-book"></i></div>
        <div class="book-tag">Popular</div>
      </div>
      <div class="book-info">
        <h3>Full-Stack Web Development</h3>
        <p class="book-author">By David Brown</p>
        <div class="book-meta">
          <span><i class="fas fa-star"></i> 4.9</span>
          <span><i class="fas fa-bookmark"></i> Development</span>
        </div>
        <a href="book-details.jsp?id=3" class="btn small-btn">View Details</a>
      </div>
    </div>
    <div class="book-card">
      <div class="book-cover-container">
        <div class="book-cover"><i class="fas fa-book"></i></div>
        <div class="book-tag">Featured</div>
      </div>
      <div class="book-info">
        <h3>AI & Machine Learning Principles</h3>
        <p class="book-author">By Sarah Wilson</p>
        <div class="book-meta">
          <span><i class="fas fa-star"></i> 4.7</span>
          <span><i class="fas fa-bookmark"></i> AI</span>
        </div>
        <a href="book-details.jsp?id=4" class="btn small-btn">View Details</a>
      </div>
    </div>
  </div>
  <div class="section-action">
    <a href="./view/pages/books.jsp" class="btn outline-btn">View All Books <i class="fas fa-arrow-right"></i></a>
  </div>
</section>

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

<%@include file="view/components/footer.jsp" %>

</body>
</html>