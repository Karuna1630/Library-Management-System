<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.example.librarymanagementsystem.model.Book" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Dashboard | UniShelf</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
</head>
<body>
<%@ include file="../components/navbar.jsp" %>

<%
    // Get the current logged-in admin user once at the top
    User currentAdmin = (User) session.getAttribute("user");
    List<User> userList = (List<User>) request.getAttribute("users");
%>

<div class="admin-container">
    <aside class="admin-sidebar">
        <div class="admin-profile">
            <% if (session.getAttribute("base64Image") != null) { %>
            <img src="data:image/jpeg;base64,<%= session.getAttribute("base64Image") %>" alt="Admin Profile" class="profile-img">
            <% } else { %>
            <div class="profile-initial"><%= currentAdmin.getFullName().charAt(0) %></div>
            <% } %>
            <h3><%= currentAdmin.getFullName() %></h3>
            <p>Administrator</p>
        </div>

        <nav class="admin-menu">
            <ul>
                <li class="active"><a href="#dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                <li><a href="#books"><i class="fas fa-book"></i> Manage Books</a></li>
                <li><a href="#borrowings"><i class="fas fa-exchange-alt"></i> Borrow Records</a></li>
                <li><a href="#users"><i class="fas fa-users"></i> Manage Users</a></li>
            </ul>
        </nav>
    </aside>

    <main class="admin-main">
        <div class="admin-header">
            <h1>Admin Dashboard</h1>
            <div class="admin-stats">
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #3a86ff20; color: #3a86ff;">
                        <i class="fas fa-book"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Total Books</h3>
                        <p>1,245</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #8338ec20; color: #8338ec;">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Active Users</h3>
                        <p>568</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #ff006e20; color: #ff006e;">
                        <i class="fas fa-exchange-alt"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Current Borrows</h3>
                        <p>327</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background-color: #fb560720; color: #fb5607;">
                        <i class="fas fa-clock"></i>
                    </div>
                    <div class="stat-info">
                        <h3>Overdue Books</h3>
                        <p>42</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Books Management Section -->
        <section id="books" class="admin-section">
            <div class="section-header">
                <h2><i class="fas fa-book"></i> Manage Books</h2>
                <button class="btn btn-primary" id="addBookBtn">
                    <i class="fas fa-plus"></i> Add New Book
                </button>
            </div>

            <div class="book-management">
                <div class="book-form-container">
                    <form id="bookForm" action="${pageContext.request.contextPath}/BookServlet" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="addBook">
                        <input type="hidden" id="bookId" name="bookId">
                        <div class="form-group">
                            <label for="bookTitle">Title</label>
                            <input type="text" id="bookTitle" name="title" required>
                        </div>
                        <div class="form-group">
                            <label for="bookAuthor">Author</label>
                            <input type="text" id="bookAuthor" name="author" required>
                        </div>
                        <div class="form-group">
                            <label for="bookYear">Publication Year</label>
                            <input type="date" id="bookYear" name="publicationYear" required>
                        </div>
                        <div class="form-group">
                            <label for="bookCategory">Category</label>
                            <select id="bookCategory" name="category" required>
                                <option value="">Select Category</option>
                                <option value="Computer Science">Computer Science</option>
                                <option value="Engineering">Engineering</option>
                                <option value="Mathematics">Mathematics</option>
                                <option value="Literature">Literature</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="bookStock">Stock Quantity</label>
                            <input type="number" id="bookStock" name="stock" min="1" value="1" required>
                        </div>

                        <div class="form-group">
                            <label for="bookImage">Book Cover Image</label>
                            <input type="file" id="bookImage" name="image" accept="image/*">
                            <div class="image-preview" id="imagePreview" style="display: none;">
                                <img id="previewImage" src="#" alt="Preview" style="max-width: 200px; max-height: 200px; margin-top: 10px;">
                            </div>

                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary">Save Book</button>
                                <button type="button" class="btn btn-outline" id="cancelBookBtn">Cancel</button>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="books-table-container">
                    <div class="table-actions">
                        <div class="search-box">
                            <input type="text" placeholder="Search books...">
                            <i class="fas fa-search"></i>
                        </div>
                        <select class="filter-select">
                            <option>All Categories</option>
                            <option>Computer Science</option>
                            <option>Engineering</option>
                            <option>Mathematics</option>
                        </select>
                    </div>

                    <table class="books-table">
                        <thead>
                        <tr>
                            <th>Cover</th>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Year</th>
                            <th>Category</th>
                            <th>Available</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            List<Book> books = (List<Book>) request.getAttribute("books");
                            if (books != null) {
                                for (Book book : books) {
                        %>
                        <tr>
                            <td>
                                <div class="book-cover-container-admin">
                                    <% if (book.getImage() != null && book.getImage().length > 0) { %>
                                    <img src="${pageContext.request.contextPath}/BookCoverServlet?id=<%= book.getBookId() %>"
                                         alt="Book Cover"
                                         class="book-cover-small">
                                    <% } else { %>
                                    <div class="book-cover-placeholder">
                                        <i class="fas fa-book"></i>
                                    </div>
                                    <% } %>
                                </div>
                            </td>
                            <td><%= book.getBookId() %></td>
                            <td><%= book.getTitle() %></td>
                            <td><%= book.getAuthor() %></td>
                            <td><%= book.getPublicationYear() %></td>
                            <td><%= book.getCategory() %></td>
                            <td><%= book.getStock() %></td>
                            <td>
                                <button class="btn-icon edit-btn" onclick="editBook(<%= book.getBookId() %>)">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="btn-icon delete-btn" onclick="deleteBook(<%= book.getBookId() %>)">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>

        <!-- Borrow Records Section -->
        <section id="borrowings" class="admin-section">
            <div class="section-header">
                <h2><i class="fas fa-exchange-alt"></i> Borrow Records</h2>
                <div class="filter-options">
                    <select class="filter-select">
                        <option>All Status</option>
                        <option>Borrowed</option>
                        <option>Returned</option>
                        <option>Overdue</option>
                    </select>
                    <input type="date" class="filter-date">
                </div>
            </div>

            <table class="borrow-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Book Title</th>
                    <th>User</th>
                    <th>Borrow Date</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1001</td>
                    <td>Introduction to Algorithms</td>
                    <td>John Doe</td>
                    <td>2023-05-15</td>
                    <td>2023-06-15</td>
                    <td><span class="status-borrowed">Borrowed</span></td>
                    <td>
                        <button class="btn-icon return-btn"><i class="fas fa-check"></i> Mark Returned</button>
                    </td>
                </tr>
                <tr>
                    <td>1002</td>
                    <td>Clean Code</td>
                    <td>Jane Smith</td>
                    <td>2023-05-10</td>
                    <td>2023-06-10</td>
                    <td><span class="status-overdue">Overdue</span></td>
                    <td>
                        <button class="btn-icon return-btn"><i class="fas fa-check"></i> Mark Returned</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </section>

        <!-- User Management Section -->
        <section id="users" class="admin-section">
            <div class="section-header">
                <h2><i class="fas fa-users"></i> Manage Users</h2>
                <div class="search-box">
                    <input type="text" id="userSearch" placeholder="Search users...">
                    <i class="fas fa-search"></i>
                </div>
            </div>

            <table class="users-table">
                <thead>
                <tr>
                    <th></th> <!-- Icon column -->
                    <th>Name</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Joined</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% if (userList != null) {
                    for (User listUser : userList) { %>
                <tr>
                    <td class="user-icon">
                        <% if (listUser.getImage() != null) {
                            String userImage = Base64.getEncoder().encodeToString(listUser.getImage());
                        %>
                        <img src="data:image/jpeg;base64,<%= userImage %>"
                             alt="User Profile"
                             class="profile-img-small">
                        <% } else { %>
                        <div class="profile-initial-small"><%= listUser.getFullName().charAt(0) %></div>
                        <% } %>
                    </td>
                    <td><%= listUser.getFullName() %></td>
                    <td><span class="role-badge <%= listUser.getRole().name().toLowerCase() %>">
                    <%= listUser.getRole() %>
                </span></td>
                    <td><span class="status-active">Active</span></td>
                    <td><%= new java.text.SimpleDateFormat("MMM d, yyyy").format(listUser.getCreated_at()) %></td>
                    <td class="actions">
                        <a href="<%= request.getContextPath() %>/AdminServlet?action=viewUser&userId=<%= listUser.getId() %>"
                           class="btn-icon view-btn">
                            <i class="fas fa-eye"></i> View
                        </a>
                        <% if (listUser.getRole() != User.Role.admin) { %>
                        <button class="btn-icon delete-btn" onclick="deleteUser(<%= listUser.getId() %>)">
                            <i class="fas fa-trash"></i> Delete
                        </button>
                        <% } %>
                    </td>
                </tr>
                <%   }
                } %>
                </tbody>
            </table>
        </section>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<!-- Add this script at the bottom of the page, before </body> -->
<script>
    function deleteUser(userId) {
        if (confirm('Are you sure you want to delete this user?')) {
            fetch('<%= request.getContextPath() %>/AdminServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=deleteUser&userId=' + userId
            })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    }
                    throw new Error('Network response was not ok');
                })
                .then(message => {
                    alert(message);
                    window.location.reload(); // Refresh the page to see changes
                })
                .catch(error => {
                    alert('Error deleting user: ' + error.message);
                });
        }
    }
</script>
</body>
</html>