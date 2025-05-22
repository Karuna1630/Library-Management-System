package com.example.librarymanagementsystem.controller.pages;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.dao.BorrowDAO;
import com.example.librarymanagementsystem.dao.ReservationDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Borrow;
import com.example.librarymanagementsystem.model.Reservation;
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
import java.util.Set;

@WebServlet(name = "ReturnServlet", value = "/ReturnServlet")
public class ReturnServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Redirect to login page if user is not authenticated
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Get the authenticated user and request parameters
        User user = (User) session.getAttribute("user");
        String borrowIdParam = request.getParameter("borrowId");
        String bookIdParam = request.getParameter("bookId");

        Connection connection = null;
        try {
            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);
            BorrowDAO borrowDAO = new BorrowDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection);

            // Parse borrow and book IDs
            int borrowId = Integer.parseInt(borrowIdParam);
            int bookId = Integer.parseInt(bookIdParam);

            // Verify borrow record exists and belongs to the user
            Borrow borrow = borrowDAO.getBorrowById(borrowId);
            if (borrow == null || borrow.getUserId() != user.getId()) {
                session.setAttribute("errorMessage", "Invalid borrow record");
                response.sendRedirect(request.getContextPath() + "/UserBooksServlet");
                return;
            }

            // Fetch book details
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                session.setAttribute("errorMessage", "Book not found");
                response.sendRedirect(request.getContextPath() + "/UserBooksServlet");
                return;
            }

            // Update borrow record to mark as returned
            boolean borrowUpdated = borrowDAO.updateBorrowStatus(borrowId, "returned");

            // Remove book ID from borrowedBooks set in session
            Set<Integer> borrowedBooks = (Set<Integer>) session.getAttribute("borrowedBooks");
            if (borrowedBooks != null) {
                borrowedBooks.remove(bookId);
                session.setAttribute("borrowedBooks", borrowedBooks);
            }

            // Check for active reservations on the book
            Reservation earliestReservation = reservationDAO.getEarliestActiveReservation(bookId);
            boolean bookUpdated;
            if (earliestReservation != null) {
                // Auto-borrow the book for the user with the earliest reservation
                Borrow newBorrow = new Borrow();
                newBorrow.setUserId(earliestReservation.getUserId());
                newBorrow.setBookId(bookId);
                newBorrow.setBorrowDate(new Date());
                newBorrow.setStatus("borrowed");

                // Set due date to 14 days from now
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, 14);
                newBorrow.setDueDate(new java.sql.Date(calendar.getTimeInMillis()));

                // Create new borrow record and mark reservation as fulfilled
                boolean newBorrowCreated = borrowDAO.addBorrow(newBorrow);
                boolean reservationFulfilled = reservationDAO.markReservationAsFulfilled(earliestReservation.getReservationId());

                bookUpdated = newBorrowCreated && reservationFulfilled;
                if (!bookUpdated) {
                    // Set error message if auto-borrow fails
                    session.setAttribute("errorMessage", "Failed to auto-borrow for reserved user");
                    response.sendRedirect(request.getContextPath() + "/UserBooksServlet");
                    return;
                }
            } else {
                // Increment book stock if no active reservations exist
                book.setStock(book.getStock() + 1);
                bookUpdated = bookDAO.updateBook(book);
            }

            if (borrowUpdated && bookUpdated) {
                // Set success message if return and related updates succeed
                session.setAttribute("successMessage", "Book returned successfully");
            } else {
                // Set error message if any update fails
                session.setAttribute("errorMessage", "Failed to return book");
            }
            // Redirect to user books page
            response.sendRedirect(request.getContextPath() + "/UserBooksServlet");

        } catch (NumberFormatException e) {
            // Handle invalid ID format
            session.setAttribute("errorMessage", "Invalid borrow or book ID format");
            response.sendRedirect(request.getContextPath() + "/UserBooksServlet");
        } catch (SQLException e) {
            // Log and handle database errors
            e.printStackTrace();
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/UserBooksServlet");
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