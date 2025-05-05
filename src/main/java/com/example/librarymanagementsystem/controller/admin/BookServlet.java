package com.example.librarymanagementsystem.controller.admin;

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
        String action = request.getParameter("action");
        Connection connection = null;

        try {
            connection = DBConnectionUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);

            if("getAllBooks".equals(action)) {
                try {
                    List<Book> books = bookDAO.getAllBooks();
                    request.setAttribute("books", books);
                    request.getRequestDispatcher("/view/dashboard/admin_dashboard.jsp").forward(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "Error fetching books: " + e.getMessage());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching books");
                }
            }
            else if("getBook".equals(action)) {
                // Get individual book for editing
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                try {
                    int bookId = Integer.parseInt(request.getParameter("id"));
                    Book book = bookDAO.getBookById(bookId);

                    if (book != null) {
                        // Convert to JSON manually (or use a JSON library like Gson)
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
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\":\"Book not found\"}");
                    }
                } catch (NumberFormatException | SQLException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                }
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database connection error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error");
        } finally {
            // Always close the connection
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

        Connection connection = null;
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");

        try {
            connection = DBConnectionUtil.getConnection();
            if (connection == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Unable to establish database connection\"}");
                return;
            }

            BookDAO bookDAO = new BookDAO(connection);

            if ("addBook".equals(action)) {
                // Get form parameters
                String title = request.getParameter("title");
                String author = request.getParameter("author");
                String publicationYearStr = request.getParameter("publicationYear");
                String category = request.getParameter("category");
                int stock = Integer.parseInt(request.getParameter("stock"));

                // Convert the publication year to a proper SQL Date format
                // Handle the case where the input is just a year
                Date publicationDate;
                try {
                    if (publicationYearStr.length() == 4) {
                        // If it's just a year, append month and day
                        publicationDate = Date.valueOf(publicationYearStr + "-01-01");
                    } else {
                        // Otherwise try to parse as is
                        publicationDate = Date.valueOf(publicationYearStr);
                    }
                } catch (IllegalArgumentException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid date format. Use YYYY-MM-DD format.\"}");
                    return;
                }

                // Process image upload
                Part filePart = request.getPart("image");
                byte[] imageBytes = null;

                if (filePart != null && filePart.getSize() > 0) {
                    if (!filePart.getContentType().startsWith("image/")) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Only image files are allowed\"}");
                        return;
                    }
                    imageBytes = filePart.getInputStream().readAllBytes();
                }

                // Create and save book
                Book book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublicationYear(publicationDate);
                book.setCategory(category);
                book.setStock(stock);
                book.setImage(imageBytes);

                boolean success = bookDAO.addBook(book);

                if (success) {
                    out.print("{\"message\": \"Book added successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"Failed to add book\"}");
                }
            }
            else if ("updateBook".equals(action)) {
                // Get book ID and other parameters
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
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid date format\"}");
                    return;
                }

                // Get existing book
                Book book = bookDAO.getBookById(bookId);
                if (book == null) {
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

                // Process image if uploaded
                Part filePart = request.getPart("image");
                if (filePart != null && filePart.getSize() > 0) {
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
                    out.print("{\"message\": \"Book updated successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"Failed to update book\"}");
                }
            }
            else if ("deleteBook".equals(action)) {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                boolean success = bookDAO.deleteBook(bookId);

                if (success) {
                    out.print("{\"message\": \"Book deleted successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"Failed to delete book\"}");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Database error: " + escapeJson(e.getMessage()) + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + escapeJson(e.getMessage()) + "\"}");
        } finally {
            // Always close the connection
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to escape JSON strings
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}