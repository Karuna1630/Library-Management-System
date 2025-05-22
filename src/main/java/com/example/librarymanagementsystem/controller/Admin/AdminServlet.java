package com.example.librarymanagementsystem.controller.Admin;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.dao.BorrowDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Borrow;
import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.services.UserService;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    // Initialize UserService to handle user-related operations
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the requested action from the query parameter
        String action = request.getParameter("action");

        // Handle admin dashboard request
        if ("dashboard".equals(action)) {
            // Check if a valid session exists and the user is an admin
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                if (user.getRole() == User.Role.admin) {
                    Connection connection = null;
                    try {
                        // Establish a new database connection
                        connection = DBConnectionUtil.getConnection();

                        // Fetch all registered users
                        List<User> users = userService.getAllUsers();
                        request.setAttribute("users", users);

                        // Retrieve all books from the database
                        BookDAO bookDAO = new BookDAO(connection);
                        List<Book> books;
                        try {
                            books = bookDAO.getAllBooks();
                            request.setAttribute("books", books);
                        } catch (SQLException e) {
                            // Log error and set an empty book list if fetching fails
                            e.printStackTrace();
                            request.setAttribute("books", new ArrayList<Book>());
                            request.setAttribute("errorMessage", "Error fetching books: " + e.getMessage());
                        }

                        // Retrieve all borrow records from the database
                        BorrowDAO borrowDAO = new BorrowDAO(connection);
                        List<Borrow> borrows;
                        try {
                            borrows = borrowDAO.getAllBorrows();
                            request.setAttribute("borrows", borrows);
                        } catch (SQLException e) {
                            // Log error and set an empty borrow list if fetching fails
                            e.printStackTrace();
                            request.setAttribute("borrows", new ArrayList<Borrow>());
                            request.setAttribute("errorMessage", "Error fetching borrow records: " + e.getMessage());
                        }

                        // Forward the request to the admin dashboard JSP page
                        request.getRequestDispatcher("/view/dashboard/admin_dashboard.jsp").forward(request, response);
                        return;
                    } catch (SQLException e) {
                        // Handle database connection errors
                        e.printStackTrace();
                        request.setAttribute("errorMessage", "Database connection error: " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error");
                        return;
                    } finally {
                        // Always close the database connection to prevent resource leaks
                        try {
                            if (connection != null && !connection.isClosed()) {
                                connection.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // Redirect to login page if user is not authenticated or not an admin
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
        }

        // Handle request to view a specific user's details
        else if ("viewUser".equals(action)) {
            // Get user ID from request parameter
            int userId = Integer.parseInt(request.getParameter("userId"));
            // Fetch user details by ID
            User viewedUser = userService.getUserById(userId);
            if (viewedUser != null) {
                // Set user details for display
                request.setAttribute("viewedUser", viewedUser);
            } else {
                // Redirect to dashboard if user not found
                response.sendRedirect(request.getContextPath() + "/AdminServlet?action=dashboard");
            }
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the requested action from the form data
        String action = request.getParameter("action");

        // Handle user deletion request
        if ("deleteUser".equals(action)) {
            // Get user ID to delete
            int userId = Integer.parseInt(request.getParameter("userId"));
            // Attempt to delete the user
            boolean success = userService.deleteUser(userId);

            if (success) {
                // Send success response
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("User deleted successfully");
            } else {
                // Send error response if deletion fails
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to delete user");
            }
        }
    }
}