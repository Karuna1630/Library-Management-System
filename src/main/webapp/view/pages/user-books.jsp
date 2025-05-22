<%-- Set page content type and encoding, and import necessary Java classes --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.Reservation" %>
<%@ page import="com.example.librarymanagementsystem.model.Borrow" %>
<%@ page import="com.example.librarymanagementsystem.model.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <%-- Page title for SEO and browser tab display --%>
    <title>My Books | UniShelf</title>
    <%-- Include Font Awesome for icons --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <%-- Shared CSS for site-wide styling --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <%-- Custom CSS for user-books page styling --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user-books.css">
    <%-- Ensure responsive design for mobile and desktop --%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%-- Include reusable navigation bar component --%>
<%@include file="../components/navbar.jsp" %>

<%-- Display toast notifications for success or error messages --%>
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

<%-- Hero section to introduce the user's books page --%>
<section class="user-books-hero">
    <div class="container">
        <h1><i class="fas fa-book-reader"></i> My Books</h1>
        <p>Manage your reserved and borrowed academic resources with ease</p>
    </div>
</section>

<%-- Main section for displaying reserved and borrowed books --%>
<section class="user-books-grid">
    <div class="container">
        <%-- Reservations section header --%>
        <div class="section-header">
            <h2><i class="fas fa-bookmark"></i> Reserved Books</h2>
        </div>
        <div class="grid-container">
            <%
                List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                Boolean reservationFulfilled = (Boolean) session.getAttribute("reservationFulfilled");
                if (reservationFulfilled != null) {
                    session.removeAttribute("reservationFulfilled");
                }
                if (successMessage != null) {
                    session.removeAttribute("successMessage");
            %>
            <%-- Additional toast for success message in reservations section --%>
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
            <%-- Additional toast for error message in reservations section --%>
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
            <%-- Card for each reserved book --%>
            <div class="user-book-card">
                <div class="book-cover">
                    <%-- Display book cover if available, otherwise show placeholder --%>
                    <% if (book.getImage() != null && book.getImage().length > 0) { %>
                    <img src="${pageContext.request.contextPath}/BookCoverServlet?id=<%= book.getBookId() %>"
                         alt="Book Cover"
                         style="width: 100%; height: 100%; object-fit: cover;">
                    <% } else { %>
                    <div class="book-placeholder">
                        <i class="fas fa-book"></i>
                    </div>
                    <% } %>
                    <span class="book-status status-reserved">Reserved</span>
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
                    <%-- Form to cancel reservation --%>
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
            <%-- Empty state for no reservations --%>
            <div class="empty-state">
                <i class="fas fa-book-open"></i>
                <p>No active reservations. Explore our collection to reserve a book!</p>
                <a href="${pageContext.request.contextPath}/BookListServlet" class="btn btn-primary">Browse Books</a>
            </div>
            <%
                }
            %>
        </div>

        <%-- Borrowed books section header --%>
        <div class="section-header">
            <h2><i class="fas fa-book"></i> Borrowed Books</h2>
        </div>
        <div class="grid-container">
            <%
                List<Borrow> borrows = (List<Borrow>) request.getAttribute("borrows");
                if (borrows != null && !borrows.isEmpty()) {
                    for (Borrow borrow : borrows) {
                        Book book = borrow.getBook();
            %>
            <%-- Card for each borrowed book --%>
            <div class="user-book-card">
                <div class="book-cover">
                    <%-- Display book cover if available, otherwise show placeholder --%>
                    <% if (book.getImage() != null && book.getImage().length > 0) { %>
                    <img src="${pageContext.request.contextPath}/BookCoverServlet?id=<%= book.getBookId() %>"
                         alt="Book Cover"
                         style="width: 100%; height: 100%; object-fit: cover;">
                    <% } else { %>
                    <div class="book-placeholder">
                        <i class="fas fa-book"></i>
                    </div>
                    <% } %>
                    <span class="book-status status-borrowed">Borrowed</span>
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
                    <%-- Form to return borrowed book --%>
                    <div class="book-actions">
                        <form action="${pageContext.request.contextPath}/ReturnServlet" method="POST">
                            <input type="hidden" name="borrowId" value="<%= borrow.getBorrowId() %>">
                            <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                            <button type="submit" class="btn btn-outline">Return Book</button>
                        </form>
                    </div>
                </div>
            </div>
            <%
                }
            } else {
            %>
            <%-- Empty state for no borrowed books --%>
            <div class="empty-state">
                <i class="fas fa-book-open"></i>
                <p>No borrowed books. Start borrowing from our library today!</p>
                <a href="${pageContext.request.contextPath}/BookListServlet" class="btn btn-primary">Browse Books</a>
            </div>
            <%
                }
            %>
        </div>
    </div>
</section>

<%-- Include reusable footer component --%>
<%@include file="../components/footer.jsp" %>

<%-- JavaScript for toast notifications and reservation fulfillment handling --%>
<script>
    // Close toast notifications when clicking the close button
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

    // Pass reservationFulfilled flag as a JavaScript variable
    const reservationFulfilled = <%= reservationFulfilled != null %>;

    // Check if a reservation was fulfilled and prompt a page reload
    if (reservationFulfilled) {
        // Display a toast notification for fulfilled reservation
        const toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container';
        toastContainer.innerHTML = `
            <div class="toast success show">
                <i class="fas fa-info-circle"></i>
                A reserved book is now available and borrowed for you! Reloading page in 3 seconds...
                <button class="toast-close">×</button>
            </div>
        `;
        document.body.appendChild(toastContainer);

        // Auto-reload page after 3 seconds to reflect updated status
        setTimeout(() => {
            window.location.reload();
        }, 3000);
    }
</script>
</body>
</html>