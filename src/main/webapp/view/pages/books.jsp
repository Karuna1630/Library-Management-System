<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
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

<!-- Toast Notifications -->
    <%
    String errorMessage = (String) session.getAttribute("errorMessage");
    String successMessage = (String) session.getAttribute("successMessage");
    if (successMessage != null) {
        session.removeAttribute("successMessage");
%>
<div class="toast-container">
    <div class="toast success show">
        <i class="fas fa-check-circle"></i>
        <%= successMessage %>
        <button class="toast-close">×</button>
    </div>
</div>
    <%
    }
    if (errorMessage != null) {
        session.removeAttribute("errorMessage");
%>
<div class="toast-container">
    <div class="toast error show">
        <i class="fas fa-exclamation-circle"></i>
        <%= errorMessage %>
        <button class="toast-close">×</button>
    </div>
</div>
    <%
    }
    Set<Integer> borrowedBooks = (Set<Integer>) session.getAttribute("borrowedBooks");
    Set<Integer> reservedBooks = (Set<Integer>) session.getAttribute("reservedBooks");
%>

<!-- Hero Section -->
<section class="books-hero">
    <div class="container">
        <h1>Our Book Collection</h1>
        <p>Browse and reserve from our extensive academic library</p>
    </div>
</section>

<!-- Filter Section -->
<section class="books-filter">
    <div class="filter-container container">
        <form action="${pageContext.request.contextPath}/BookListServlet" method="get" class="search-box">
            <input type="text" name="search" placeholder="Search by title, author..." value="${searchQuery}">
            <i class="fas fa-search"></i>
            <button type="submit" style="display:none;"></button>
        </form>
        <form action="${pageContext.request.contextPath}/BookListServlet" method="get" class="filter-select-container">
            <select name="category" class="filter-select" onchange="this.form.submit()">
                <option value="All Categories" ${category == null || category == 'All Categories' ? 'selected' : ''}>All Categories</option>
                <option value="Computer Science" ${category == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                <option value="Engineering" ${category == 'Engineering' ? 'selected' : ''}>Engineering</option>
                <option value="Mathematics" ${category == 'Mathematics' ? 'selected' : ''}>Mathematics</option>
                <option value="Literature" ${category == 'Literature' ? 'selected' : ''}>Literature</option>
            </select>
            <input type="hidden" name="search" value="${searchQuery}">
        </form>
        <form action="${pageContext.request.contextPath}/BookListServlet" method="get" class="filter-select-container">
            <select name="availability" class="filter-select" onchange="this.form.submit()">
                <option value="All Availability" ${availability == null || availability == 'All Availability' ? 'selected' : ''}>All Availability</option>
                <option value="Available Now" ${availability == 'Available Now' ? 'selected' : ''}>Available Now</option>
                <option value="Currently Reserved" ${availability == 'Currently Reserved' ? 'selected' : ''}>Currently Reserved</option>
            </select>
            <input type="hidden" name="search" value="${searchQuery}">
            <input type="hidden" name="category" value="${category}">
        </form>
    </div>
</section>

<!-- Books Grid -->
<section class="books-grid">
    <div class="grid-container container">
        <%
            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
                    boolean isBorrowedByUser = borrowedBooks != null && borrowedBooks.contains(book.getBookId());
                    boolean isReservedByUser = reservedBooks != null && reservedBooks.contains(book.getBookId());
        %>
        <div class="book-card">
            <div class="book-cover">
                <% if (book.getImage() != null && book.getImage().length > 0) { %>
                <img src="${pageContext.request.contextPath}/BookCoverServlet?id=<%= book.getBookId() %>"
                     alt="Book Cover"
                     style="width: 100%; height: 200px; object-fit: cover;">
                <% } else { %>
                <i class="fas fa-book"></i>
                <% } %>
                <span class="book-status <%= book.getStock() > 0 ? "status-available" : "status-borrowed" %>">
                    <%= book.getStock() > 0 ? "Available" : "Reserved" %>
                </span>
            </div>
            <div class="book-info">
                <h3 class="book-title"><%= book.getTitle() %></h3>
                <p class="book-author">By <%= book.getAuthor() %></p>
                <div class="book-meta">
                    <span><i class="fas fa-bookmark"></i> <%= book.getCategory() %></span>
                    <span><i class="fas fa-calendar-alt"></i> <%= book.getPublicationYear() != null ? book.getPublicationYear() : "N/A" %></span>
                </div>
                <div class="book-actions">
                    <% if (isBorrowedByUser) { %>
                    <button class="btn btn-borrow" disabled>Borrowed</button>
                    <% } else if (isReservedByUser) { %>
                    <button class="btn btn-borrow" disabled>Reserved</button>
                    <% } else if (book.getStock() > 0) { %>
                    <form action="${pageContext.request.contextPath}/BorrowServlet" method="POST">
                        <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                        <button type="submit" class="btn btn-borrow">Borrow Book</button>
                    </form>
                    <% } else { %>
                    <form action="${pageContext.request.contextPath}/ReserveServlet" method="POST">
                        <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                        <button type="submit" class="btn btn-borrow">Reserve Book</button>
                    </form>
                    <% } %>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p>No books available matching your criteria.</p>
        <%
            }
        %>
    </div>
</section>

<%@include file="../components/footer.jsp" %>

<script>
    // Close toast notifications
    document.querySelectorAll('.toast-close').forEach(button => {
        button.addEventListener('click', () => {
            button.parentElement.classList.remove('show');
        });
    });

    // Auto-hide toasts after 5 seconds
    setTimeout(() => {
        document.querySelectorAll('.toast.show').forEach(toast => {
            toast.classList.remove('show');
        });
    }, 5000);

    // Submit search form on Enter key press
    document.querySelector('.search-box input').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            this.parentElement.submit();
        }
    });
</script>