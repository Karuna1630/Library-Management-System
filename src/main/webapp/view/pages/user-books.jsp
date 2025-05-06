<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.Reservation" %>
<%@ page import="com.example.librarymanagementsystem.model.Borrow" %>
<%@ page import="com.example.librarymanagementsystem.model.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <title>My Books | UniShelf</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user-books.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%@include file="../components/navbar.jsp" %>

<!-- Hero Section -->
<section class="user-books-hero">
    <div class="container">
        <h1><i class="fas fa-books"></i> My Books</h1>
        <p>View and manage your reserved and borrowed academic resources</p>
    </div>
</section>

<!-- User Books Grid -->
<section class="user-books-grid">
    <div class="grid-container container">
        <!-- Reservations Section -->
        <h2>Reserved Books</h2>
        <%
            List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
            String errorMessage = (String) session.getAttribute("errorMessage");
            String successMessage = (String) session.getAttribute("successMessage");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
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
            if (reservations != null && !reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    Book book = reservation.getBook();
        %>
        <div class="user-book-card">
            <div class="book-cover">
                <% if (book.getImage() != null && book.getImage().length > 0) { %>
                <img src="${pageContext.request.contextPath}/BookCoverServlet?id=<%= book.getBookId() %>"
                     alt="Book Cover"
                     style="width: 100%; height: 200px; object-fit: cover;">
                <% } else { %>
                <i class="fas fa-book"></i>
                <% } %>
                <span class="book-status status-available">Reserved</span>
            </div>
            <div class="book-info">
                <h3 class="book-title"><%= book.getTitle() %></h3>
                <p class="book-author">By <%= book.getAuthor() %></p>
                <div class="book-meta">
                    <span><i class="fas fa-bookmark"></i> <%= book.getCategory() %></span>
                    <span><i class="fas fa-calendar-alt"></i> <%= book.getPublicationYear() != null ? book.getPublicationYear() : "N/A" %></span>
                </div>
                <div class="book-meta">
                    <span><i class="fas fa-clock"></i> Reserved on <%= dateFormat.format(reservation.getReservationDate()) %></span>
                    <span><i class="fas fa-calendar-check"></i> Until <%= dateFormat.format(reservation.getReservedUntil()) %></span>
                </div>
                <div class="book-actions">
                    <form action="${pageContext.request.contextPath}/ReserveServlet" method="POST">
                        <input type="hidden" name="reservationId" value="<%= reservation.getReservationId() %>">
                        <input type="hidden" name="action" value="cancel">
                        <button type="submit" class="btn btn-outline">Cancel Reservation</button>
                    </form>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p>You have no active reservations.</p>
        <%
            }
        %>

        <!-- Borrows Section -->
        <h2>Borrowed Books</h2>
        <%
            List<Borrow> borrows = (List<Borrow>) request.getAttribute("borrows");
            if (borrows != null && !borrows.isEmpty()) {
                for (Borrow borrow : borrows) {
                    Book book = borrow.getBook();
        %>
        <div class="user-book-card">
            <div class="book-cover">
                <% if (book.getImage() != null && book.getImage().length > 0) { %>
                <img src="${pageContext.request.contextPath}/BookCoverServlet?id=<%= book.getBookId() %>"
                     alt="Book Cover"
                     style="width: 100%; height: 200px; object-fit: cover;">
                <% } else { %>
                <i class="fas fa-book"></i>
                <% } %>
                <span class="book-status status-borrowed"><%= borrow.getStatus() %></span>
            </div>
            <div class="book-info">
                <h3 class="book-title"><%= book.getTitle() %></h3>
                <p class="book-author">By <%= book.getAuthor() %></p>
                <div class="book-meta">
                    <span><i class="fas fa-bookmark"></i> <%= book.getCategory() %></span>
                    <span><i class="fas fa-calendar-alt"></i> <%= book.getPublicationYear() != null ? book.getPublicationYear() : "N/A" %></span>
                </div>
                <div class="book-meta">
                    <span><i class="fas fa-clock"></i> Borrowed on <%= dateFormat.format(borrow.getBorrowDate()) %></span>
                    <span><i class="fas fa-calendar-check"></i> Due by <%= dateFormat.format(borrow.getDueDate()) %></span>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p>You have no borrowed books.</p>
        <%
            }
        %>
        <a href="${pageContext.request.contextPath}/BookListServlet" class="btn btn-primary">Browse Books</a>
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