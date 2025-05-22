package com.example.librarymanagementsystem.controller.Admin;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "BookCoverServlet", value = "/BookCoverServlet")
public class BookCoverServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize database connection
        Connection connection = null;

        try {
            // Get book ID from request parameter
            String bookIdParam = request.getParameter("id");
            if (bookIdParam == null || bookIdParam.isEmpty()) {
                // Return error if book ID is missing or empty
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
                return;
            }

            // Parse book ID to integer
            int bookId = Integer.parseInt(bookIdParam);

            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);

            // Fetch book details by ID
            Book book = bookDAO.getBookById(bookId);

            // Check if book or its image is missing
            if (book == null || book.getImage() == null || book.getImage().length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book image not found");
                return;
            }

            // Set response content type to support various image formats
            response.setContentType("image/*");

            // Write book image to response output stream
            try (OutputStream out = response.getOutputStream()) {
                out.write(book.getImage());
                out.flush();
            }

        } catch (NumberFormatException e) {
            // Handle invalid book ID format
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID format");
        } catch (SQLException e) {
            // Log and handle database errors
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } finally {
            // Close database connection to prevent resource leaks
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