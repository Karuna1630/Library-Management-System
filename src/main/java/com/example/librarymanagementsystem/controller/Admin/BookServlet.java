package com.example.librarymanagementsystem.controller.Admin;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "BookServlet", value = "/BookServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5,  // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class BookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the requested action from query parameter
        String action = request.getParameter("action");
        Connection connection = null;

        try {
            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);

            if ("getAllBooks".equals(action)) {
                // Fetch all books and forward to admin dashboard
                try {
                    List<Book> books = bookDAO.getAllBooks();
                    request.setAttribute("books", books);
                    request.getRequestDispatcher("/view/dashboard/admin_dashboard.jsp").forward(request, response);
                } catch (SQLException e) {
                    // Log and handle error if fetching books fails
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "Error fetching books: " + e.getMessage());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching books");
                }
            } else if ("getBook".equals(action)) {
                // Fetch individual book details for editing
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                try {
                    // Parse book ID from request
                    int bookId = Integer.parseInt(request.getParameter("id"));
                    Book book = bookDAO.getBookById(bookId);

                    if (book != null) {
                        // Construct JSON response manually (consider using a JSON library like Gson for robustness)
                        String publicationYear = book.getPublicationYear() != null ?
                                book.getPublicationYear().toString() : "";

                        String json = "{"
                                + "\"bookId\":" + book.getBookId() + ","
                                + "\"title\":\"" + escapeJson(book.getTitle()) + "\","
                                + "\"author\":\"" + escapeJson(book.getAuthor()) + "\","
                                + "\"publicationYear\":\"" + escapeJson(publicationYear) + "\","
                                + "\"category\":\"" + escapeJson(book.getCategory()) + "\","
                                + "\"stock\":" + book.getStock() + ","
                                + "\"imageUrl\":\"" + (book.getImage() != null ?
                                request.getContextPath() + "/BookCoverServlet?id=" + book.getBookId() : "")
                                + "\""
                                + "}";

                        out.print(json);
                    } else {
                        // Return error if book not found
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Book not found\"}");
                    }
                } catch (NumberFormatException | SQLException e) {
                    // Handle invalid ID format or database errors
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                }
                return;
            }
        } catch (SQLException e) {
            // Handle database connection errors
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database connection error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error");
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response type to JSON
        Connection connection = null;
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        try {
            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            if (connection == null) {
                // Return error if connection fails
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Unable to establish database connection\"}");
                return;
            }

            BookDAO bookDAO = new BookDAO(connection);

            if ("addBook".equals(action)) {
                // Handle book addition
                // Extract form parameters
                String title = request.getParameter("title");
                String author = request.getParameter("author");
                String publicationYearStr = request.getParameter("publicationYear");
                String category = request.getParameter("category");
                int stock = Integer.parseInt(request.getParameter("stock"));

                // Convert publication year to SQL Date
                Date publicationDate;
                try {
                    if (publicationYearStr.length() == 4) {
                        // If only year is provided, default to January 1st
                        publicationDate = Date.valueOf(publicationYearStr + "-01-01");
                    } else {
                        // Otherwise parse full date
                        publicationDate = Date.valueOf(publicationYearStr);
                    }
                } catch (IllegalArgumentException e) {
                    // Handle invalid date format
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid date format. Use YYYY-MM-DD format.\"}");
                    return;
                }

                // Handle image upload
                Part filePart = request.getPart("image");
                byte[] imageBytes = null;

                if (filePart != null && filePart.getSize() > 0) {
                    // Validate that uploaded file is an image
                    if (!filePart.getContentType().startsWith("image/")) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Only image files are allowed\"}");
                        return;
                    }
                    imageBytes = filePart.getInputStream().readAllBytes();
                }

                // Create new book object and set properties
                Book book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublicationYear(publicationDate);
                book.setCategory(category);
                book.setStock(stock);
                book.setImage(imageBytes);

                // Save book to database
                boolean success = bookDAO.addBook(book);

                if (success) {
                    // Return success message
                    out.print("{\"message\": \"Book added successfully\"}");
                } else {
                    // Return error if addition fails
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"Failed to add book\"}");
                }
            } else if ("updateBook".equals(action)) {
                // Handle book update
                // Extract book ID and form parameters
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                String title = request.getParameter("title");
                String author = request.getParameter("author");
                String publicationYearStr = request.getParameter("publicationYear");
                String category = request.getParameter("category");
                int stock = Integer.parseInt(request.getParameter("stock"));

                // Convert publication year to SQL Date
                Date publicationDate;
                try {
                    if (publicationYearStr.length() == 4) {
                        publicationDate = Date.valueOf(publicationYearStr + "-01-01");
                    } else {
                        publicationDate = Date.valueOf(publicationYearStr);
                    }
                } catch (IllegalArgumentException e) {
                    // Handle invalid date format
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid date format\"}");
                    return;
                }

                // Fetch existing book
                Book book = bookDAO.getBookById(bookId);
                if (book == null) {
                    // Return error if book not found
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Book not found\"}");
                    return;
                }

                // Update book properties
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublicationYear(publicationDate);
                book.setCategory(category);
                book.setStock(stock);

                // Handle image update if provided
                Part filePart = request.getPart("image");
                if (filePart != null && filePart.getSize() > 0) {
                    // Validate image file
                    if (!filePart.getContentType().startsWith("image/")) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Only image files are allowed\"}");
                        return;
                    }
                    book.setImage(filePart.getInputStream().readAllBytes());
                }

                // Update book in database
                boolean success = bookDAO.updateBook(book);
                if (success) {
                    // Return success message
                    out.print("{\"message\": \"Book updated successfully\"}");
                } else {
                    // Return error if update fails
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"Failed to update book\"}");
                }
            } else if ("deleteBook".equals(action)) {
                // Handle book deletion
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                boolean success = bookDAO.deleteBook(bookId);

                if (success) {
                    // Return success message
                    out.print("{\"message\": \"Book deleted successfully\"}");
                } else {
                    // Return error if deletion fails
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"Failed to delete book\"}");
                }
            }
        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Database error: " + escapeJson(e.getMessage()) + "\"}");
        } catch (Exception e) {
            // Handle other unexpected errors
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        } finally {
            // Always close the database connection
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to escape special characters in JSON strings
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}