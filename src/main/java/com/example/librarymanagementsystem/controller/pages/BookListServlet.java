package com.example.librarymanagementsystem.controller.pages;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "BookListServlet", value = "/BookListServlet")
public class BookListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the current session, if it exists
        HttpSession session = request.getSession(false);

        // Retrieve query parameters for filtering books
        String searchQuery = request.getParameter("search");
        String category = request.getParameter("category");
        String availability = request.getParameter("availability");

        // Validate availability parameter to prevent invalid inputs
        if (availability != null && !isValidAvailability(availability)) {
            session.setAttribute("errorMessage", "Invalid availability filter");
            request.getRequestDispatcher("/view/pages/books.jsp").forward(request, response);
            return;
        }

        Connection connection = null;
        try {
            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            if (connection == null) {
                throw new SQLException("Failed to establish database connection");
            }

            // Initialize BookDAO with the database connection
            BookDAO bookDAO = new BookDAO(connection);
            List<Book> books;

            // Check if any filters are applied
            if (searchQuery != null || category != null || availability != null) {
                // Fetch filtered books based on search, category, or availability
                books = bookDAO.filterBooks(searchQuery, category, availability);
                if (books.isEmpty()) {
                    // Set error message if no books match the criteria
                    session.setAttribute("errorMessage", "No books found matching your criteria");
                } else {
                    // Set success message if books are found
                    session.setAttribute("successMessage", "Books filtered successfully");
                }
            } else {
                // Fetch all books if no filters are applied
                books = bookDAO.getAllBooks();
                if (books.isEmpty()) {
                    // Set error message if no books are available
                    session.setAttribute("errorMessage", "No books available in the library");
                } else {
                    // Set success message for successful book retrieval
                    session.setAttribute("successMessage", "Books loaded successfully");
                }
            }

            // Set attributes for the JSP page
            request.setAttribute("books", books);
            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("category", category);
            request.setAttribute("availability", availability);

            // Forward to the books JSP page for rendering
            request.getRequestDispatcher("/view/pages/books.jsp").forward(request, response);

        } catch (SQLException e) {
            // Log and handle database errors
            e.printStackTrace();
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
            // Forward to access-denied page on database failure
            request.getRequestDispatcher("/view/auth/access-denied.jsp").forward(request, response);
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

    // Helper method to validate availability parameter
    private boolean isValidAvailability(String availability) {
        return availability == null ||
                availability.equals("All Availability") ||
                availability.equals("Available Now") ||
                availability.equals("Currently Reserved");
    }
}