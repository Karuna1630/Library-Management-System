<%--
  Created by IntelliJ IDEA.
  User: USER-PC
  Date: 4/17/2025
  Time: 10:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.librarymanagementsystem.model.User" %>
<html>
<head>
    <title>Admin Dashboard | UniShelf</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%@include file="../components/navbar.jsp" %>
<%User user = (User) session.getAttribute("user");%>
<div class="admin-container">
    <!-- Sidebar Navigation -->
    <aside class="admin-sidebar">
        <div class="admin-profile">
            <% if (session.getAttribute("base64Image") != null) { %>
            <img src="data:image/jpeg;base64,<%= session.getAttribute("base64Image") %>"
                 alt="Admin Profile" class="profile-img">
            <% } else { %>
            <div class="profile-initial"><%= user.getFullName().charAt(0) %></div>
            <% } %>
            <h3><%= user.getFullName() %></h3>
            <p>Administrator</p>
        </div>

        <nav class="admin-menu">
            <ul>
                <li class="active"><a href="#dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                <li><a href="#books"><i class="fas fa-book"></i> Manage Books</a></li>
                <li><a href="#borrowings"><i class="fas fa-exchange-alt"></i> Borrow Records</a></li>
                <li><a href="#users"><i class="fas fa-users"></i> Manage Users</a></li>
                <li><a href="#categories"><i class="fas fa-tags"></i> Book Categories</a></li>
            </ul>
        </nav>
    </aside>

    <!-- Main Content Area -->
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
                <!-- Book Add/Edit Form (Initially Hidden) -->
                <div class="book-form-container">
                    <form id="bookForm" class="book-form">
                        <input type="hidden" id="bookId">
                        <div class="form-group">
                            <label for="bookTitle">Title</label>
                            <input type="text" id="bookTitle" required>
                        </div>
                        <div class="form-group">
                            <label for="bookAuthor">Author</label>
                            <input type="text" id="bookAuthor" required>
                        </div>
                        <div class="form-group">
                            <label for="bookYear">Publication Year</label>
                            <input type="date" id="bookYear" required>
                        </div>
                        <div class="form-group">
                            <label for="bookCategory">Category</label>
                            <select id="bookCategory" required>
                                <option value="">Select Category</option>
                                <option value="1">Computer Science</option>
                                <option value="2">Engineering</option>
                                <option value="3">Mathematics</option>
                                <option value="4">Literature</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="bookStock">Stock Quantity</label>
                            <input type="number" id="bookStock" min="1" value="1" required>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Save Book</button>
                            <button type="button" class="btn btn-outline" id="cancelBookBtn">Cancel</button>
                        </div>
                    </form>
                </div>

                <!-- Books Table -->
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
                        <tr>
                            <td>101</td>
                            <td>Introduction to Algorithms</td>
                            <td>Thomas H. Cormen</td>
                            <td>2009</td>
                            <td>Computer Science</td>
                            <td>5</td>
                            <td>
                                <button class="btn-icon edit-btn"><i class="fas fa-edit"></i></button>
                                <button class="btn-icon delete-btn"><i class="fas fa-trash"></i></button>
                            </td>
                        </tr>
                        <tr>
                            <td>102</td>
                            <td>Clean Code</td>
                            <td>Robert C. Martin</td>
                            <td>2008</td>
                            <td>Programming</td>
                            <td>3</td>
                            <td>
                                <button class="btn-icon edit-btn"><i class="fas fa-edit"></i></button>
                                <button class="btn-icon delete-btn"><i class="fas fa-trash"></i></button>
                            </td>
                        </tr>
                        <!-- More rows would be dynamically populated -->
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
                <!-- More rows would be dynamically populated -->
                </tbody>
            </table>
        </section>
    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</body>
</html>