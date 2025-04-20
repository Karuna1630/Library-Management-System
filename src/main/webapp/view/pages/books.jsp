<%--
  Created by IntelliJ IDEA.
  User: USER-PC
  Date: 4/16/2025
  Time: 10:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Collection | UniShelf</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/books.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%@include file="../components/navbar.jsp" %>

<!-- Hero Section -->
<section class="books-hero">
    <div class="container">
        <h1>Our Book Collection</h1>
        <p>Browse and borrow from our extensive academic library</p>
    </div>
</section>

<!-- Filter Section -->
<section class="books-filter">
    <div class="filter-container container">
        <div class="search-box">
            <input type="text" placeholder="Search by title, author, or ISBN...">
            <i class="fas fa-search"></i>
        </div>
        <select class="filter-select">
            <option>All Categories</option>
            <option>Computer Science</option>
            <option>Engineering</option>
            <option>Mathematics</option>
            <option>Literature</option>
        </select>
        <select class="filter-select">
            <option>All Availability</option>
            <option>Available Now</option>
            <option>Currently Borrowed</option>
        </select>
    </div>
</section>

<!-- Books Grid -->
<section class="books-grid">
    <div class="grid-container container">
        <!-- Book 1 -->
        <div class="book-card">
            <div class="book-cover">
                <i class="fas fa-book"></i>
                <span class="book-status status-available">Available</span>
            </div>
            <div class="book-info">
                <h3 class="book-title">Introduction to Algorithms</h3>
                <p class="book-author">By Thomas H. Cormen</p>
                <div class="book-meta">
                    <span><i class="fas fa-bookmark"></i> Computer Science</span>
                    <span><i class="fas fa-calendar-alt"></i> 2022 Edition</span>
                </div>
                <div class="book-actions">
                    <button class="btn btn-borrow">Borrow Book</button>
                    <button class="btn btn-details">Details</button>
                </div>
            </div>
        </div>

        <!-- Book 2 -->
        <div class="book-card">
            <div class="book-cover">
                <i class="fas fa-book"></i>
                <span class="book-status status-borrowed">Due 05/30</span>
            </div>
            <div class="book-info">
                <h3 class="book-title">Database System Concepts</h3>
                <p class="book-author">By Abraham Silberschatz</p>
                <div class="book-meta">
                    <span><i class="fas fa-bookmark"></i> Computer Science</span>
                    <span><i class="fas fa-calendar-alt"></i> 7th Edition</span>
                </div>
                <div class="book-actions">
                    <button class="btn btn-borrow" disabled>Borrowed</button>
                    <button class="btn btn-details">Details</button>
                </div>
            </div>
        </div>

        <!-- Book 3 -->
        <div class="book-card">
            <div class="book-cover">
                <i class="fas fa-book"></i>
                <span class="book-status status-available">Available</span>
            </div>
            <div class="book-info">
                <h3 class="book-title">Clean Code</h3>
                <p class="book-author">By Robert C. Martin</p>
                <div class="book-meta">
                    <span><i class="fas fa-bookmark"></i> Software Engineering</span>
                    <span><i class="fas fa-calendar-alt"></i> 1st Edition</span>
                </div>
                <div class="book-actions">
                    <button class="btn btn-borrow">Borrow Book</button>
                    <button class="btn btn-details">Details</button>
                </div>
            </div>
        </div>

        <!-- Book 4 -->
        <div class="book-card">
            <div class="book-cover">
                <i class="fas fa-book"></i>
                <span class="book-status status-available">Available</span>
            </div>
            <div class="book-info">
                <h3 class="book-title">The Pragmatic Programmer</h3>
                <p class="book-author">By Andrew Hunt</p>
                <div class="book-meta">
                    <span><i class="fas fa-bookmark"></i> Programming</span>
                    <span><i class="fas fa-calendar-alt"></i> 2nd Edition</span>
                </div>
                <div class="book-actions">
                    <button class="btn btn-borrow">Borrow Book</button>
                    <button class="btn btn-details">Details</button>
                </div>
            </div>
        </div>
    </div>
</section>

<%@include file="../components/footer.jsp" %>
</body>
</html>
