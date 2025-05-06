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

@WebServlet(name = "BorrowServlet", value = "/BorrowServlet")
public class BorrowServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        User user = (User) session.getAttribute("user");
        String bookIdParam = request.getParameter("bookId");

        Connection connection = null;
        try {
            connection = DBConnectionUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);
            BorrowDAO borrowDAO = new BorrowDAO(connection);

            // Parse book ID
            int bookId = Integer.parseInt(bookIdParam);
            Book book = bookDAO.getBookById(bookId);

            if (book == null) {
                session.setAttribute("errorMessage", "Book not found");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            if (book.getStock() <= 0) {
                session.setAttribute("errorMessage", "This book is currently unavailable for borrowing");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Check if user already has an active borrow for this book
            if (borrowDAO.hasActiveBorrow(user.getId(), bookId)) {
                session.setAttribute("errorMessage", "You already have an active borrow for this book");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Create borrow record
            Borrow borrow = new Borrow();
            borrow.setUserId(user.getId());
            borrow.setBookId(bookId);
            borrow.setBorrowDate(new Date());
            borrow.setStatus("borrowed");

            // Set due_date to 14 days from now
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 14);
            borrow.setDueDate(new java.sql.Date(calendar.getTimeInMillis()));

            // Update book stock
            book.setStock(book.getStock() - 1);
            boolean bookUpdated = bookDAO.updateBook(book);
            boolean borrowCreated = borrowDAO.addBorrow(borrow);

            if (bookUpdated && borrowCreated) {
                session.setAttribute("successMessage", "Book borrowed successfully. Due date: " + borrow.getDueDate());
            } else {
                session.setAttribute("errorMessage", "Failed to borrow book");
            }
            response.sendRedirect(request.getContextPath() + "/BookListServlet");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid book ID format");
            response.sendRedirect(request.getContextPath() + "/BookListServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/BookListServlet");
        } finally {
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