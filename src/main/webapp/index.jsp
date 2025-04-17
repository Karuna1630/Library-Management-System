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
    <h1>Welcome to <span class="highlight">UniShelf</span></h1>
    <p class="subtitle">Your one-stop solution for book reservations and student resources.</p>
    <div class="hero-buttons">
      <a href="browse.jsp" class="btn primary-btn"><i class="fas fa-book"></i> Browse Books</a>
      <a href="login.jsp" class="btn secondary-btn"><i class="fas fa-sign-in-alt"></i> Member Login</a>
    </div>
  </div>
</div>

<section class="features-section">
  <h2>Our Services</h2>
  <div class="features-container">
    <div class="feature-card">
      <i class="fas fa-search feature-icon"></i>
      <h3>Search Collection</h3>
      <p>Explore our vast collection of books, journals, and research papers.</p>
    </div>
    <div class="feature-card">
      <i class="fas fa-calendar-check feature-icon"></i>
      <h3>Easy Reservations</h3>
      <p>Reserve books online and pick them up at your convenience.</p>
    </div>
    <div class="feature-card">
      <i class="fas fa-history feature-icon"></i>
      <h3>Manage Loans</h3>
      <p>Track your borrowing history and manage returns efficiently.</p>
    </div>
    <div class="feature-card">
      <i class="fas fa-bell feature-icon"></i>
      <h3>Notifications</h3>
      <p>Get timely alerts about due dates and available reservations.</p>
    </div>
  </div>
</section>

<section class="new-arrivals-section">
  <h2>New Arrivals</h2>
  <div class="books-container">
    <!-- These would ideally be populated dynamically from your database -->
    <div class="book-card">
      <div class="book-cover"><i class="fas fa-book"></i></div>
      <h3>Programming in Java</h3>
      <p>By John Smith</p>
      <a href="book-details.jsp?id=1" class="btn small-btn">View Details</a>
    </div>
    <div class="book-card">
      <div class="book-cover"><i class="fas fa-book"></i></div>
      <h3>Database Systems</h3>
      <p>By Mary Johnson</p>
      <a href="book-details.jsp?id=2" class="btn small-btn">View Details</a>
    </div>
    <div class="book-card">
      <div class="book-cover"><i class="fas fa-book"></i></div>
      <h3>Web Development</h3>
      <p>By David Brown</p>
      <a href="book-details.jsp?id=3" class="btn small-btn">View Details</a>
    </div>
    <div class="book-card">
      <div class="book-cover"><i class="fas fa-book"></i></div>
      <h3>AI Fundamentals</h3>
      <p>By Sarah Wilson</p>
      <a href="book-details.jsp?id=4" class="btn small-btn">View Details</a>
    </div>
  </div>
</section>

<section class="how-it-works">
  <h2>How It Works</h2>
  <div class="steps-container">
    <div class="step">
      <div class="step-number">1</div>
      <h3>Create Account</h3>
      <p>Register and set up your library profile</p>
    </div>
    <div class="step">
      <div class="step-number">2</div>
      <h3>Browse Books</h3>
      <p>Search for books by title, author, or category</p>
    </div>
    <div class="step">
      <div class="step-number">3</div>
      <h3>Reserve</h3>
      <p>Reserve available books for pickup</p>
    </div>
    <div class="step">
      <div class="step-number">4</div>
      <h3>Pick Up & Return</h3>
      <p>Collect at the library and return on time</p>
    </div>
  </div>
</section>

<section class="stats-section">
  <div class="stat-item">
    <i class="fas fa-book-open stat-icon"></i>
    <div class="stat-number">10,000+</div>
    <div class="stat-label">Books</div>
  </div>
  <div class="stat-item">
    <i class="fas fa-users stat-icon"></i>
    <div class="stat-number">5,000+</div>
    <div class="stat-label">Members</div>
  </div>
  <div class="stat-item">
    <i class="fas fa-university stat-icon"></i>
    <div class="stat-number">50+</div>
    <div class="stat-label">Categories</div>
  </div>
  <div class="stat-item">
    <i class="fas fa-exchange-alt stat-icon"></i>
    <div class="stat-number">1,000+</div>
    <div class="stat-label">Monthly Loans</div>
  </div>
</section>

<section class="testimonials-section">
  <h2>What Our Users Say</h2>
  <div class="testimonials-container">
    <div class="testimonial">
      <div class="quote"><i class="fas fa-quote-left"></i></div>
      <p>UniShelf has completely transformed how I access library resources. The reservation system is so convenient!</p>
      <div class="user">- John D., Computer Science Student</div>
    </div>
    <div class="testimonial">
      <div class="quote"><i class="fas fa-quote-left"></i></div>
      <p>As a professor, I appreciate how easy it is to recommend books to my students through this platform.</p>
      <div class="user">- Dr. Emily R., Faculty Member</div>
    </div>
  </div>
</section>

<section class="cta-section">
  <h2>Ready to Get Started?</h2>
  <p>Join thousands of students and faculty who use UniShelf daily for their academic needs.</p>
  <a href="register.jsp" class="btn primary-btn large-btn">Sign Up Now</a>
</section>

</body>
</html>