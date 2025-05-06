<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.Book" %>
<%@ page import="java.util.List" %>
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
            <option>Currently Reserved</option>
        </select>
    </div>
</section>

<!-- Books Grid -->
<section class="books-grid">
    <div class="grid-container container">
        <%
            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
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
                    <% if (book.getStock() > 0) { %>
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
        <p>No books available at the moment.</p>
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
</script>
</body>
</html>