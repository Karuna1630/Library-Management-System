package com.example.librarymanagementsystem.controller.pages;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.dao.BorrowDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Borrow;
import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "BorrowServlet", value = "/BorrowServlet")
public class BorrowServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Redirect to login page if user is not authenticated
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Get the authenticated user from session
        User user = (User) session.getAttribute("user");
        String bookIdParam = request.getParameter("bookId");

        Connection connection = null;
        try {
            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            if (connection == null) {
                throw new SQLException("Failed to establish database connection");
            }

            // Initialize DAOs for book and borrow operations
            BookDAO bookDAO = new BookDAO(connection);
            BorrowDAO borrowDAO = new BorrowDAO(connection);

            // Parse book ID from request parameter
            int bookId = Integer.parseInt(bookIdParam);
            Book book = bookDAO.getBookById(bookId);

            // Check if book exists
            if (book == null) {
                session.setAttribute("errorMessage", "Book not found");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Check if book is available for borrowing
            if (book.getStock() <= 0) {
                session.setAttribute("errorMessage", "This book is not available for borrowing");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Create a new borrow record
            Borrow borrow = new Borrow();
            borrow.setUserId(user.getId());
            borrow.setBookId(bookId);
            borrow.setBorrowDate(new Date());
            borrow.setStatus("borrowed");

            // Set due date to 14 days from the current date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 14);
            borrow.setDueDate(new java.sql.Date(calendar.getTimeInMillis()));

            // Update book stock and create borrow record
            book.setStock(book.getStock() - 1);
            boolean bookUpdated = bookDAO.updateBook(book);
            boolean borrowCreated = borrowDAO.addBorrow(borrow);

            if (bookUpdated && borrowCreated) {
                // Track borrowed book in session
                Set<Integer> borrowedBooks = (Set<Integer>) session.getAttribute("borrowedBooks");
                if (borrowedBooks == null) {
                    borrowedBooks = new HashSet<>();
                    session.setAttribute("borrowedBooks", borrowedBooks);
                }
                borrowedBooks.add(bookId);
                // Set success message
                session.setAttribute("successMessage", "Book borrowed successfully");
            } else {
                // Set error message if borrowing fails
                session.setAttribute("errorMessage", "Failed to borrow book");
            }
            // Redirect to book list page
            response.sendRedirect(request.getContextPath() + "/BookListServlet");

        } catch (NumberFormatException e) {
            // Handle invalid book ID format
            session.setAttribute("errorMessage", "Invalid book ID format");
            response.sendRedirect(request.getContextPath() + "/BookListServlet");
        } catch (SQLException e) {
            // Log and handle database errors
            e.printStackTrace();
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/BookListServlet");
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