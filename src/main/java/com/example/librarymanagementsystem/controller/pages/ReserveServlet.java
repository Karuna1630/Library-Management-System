package com.example.librarymanagementsystem.controller.pages;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.dao.ReservationDAO;
import com.example.librarymanagementsystem.model.Book;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "ReserveServlet", value = "/ReserveServlet")
public class ReserveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Redirect to login page if user is not authenticated
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Get the authenticated user from session
        User user = (User) session.getAttribute("user");
        Connection connection = null;
        try {
            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            ReservationDAO reservationDAO = new ReservationDAO(connection);

            // Fetch active reservations for the user
            List<Reservation> reservations = reservationDAO.getActiveReservationsByUser(user.getId());

            // Set reservations as request attribute for display
            request.setAttribute("reservations", reservations);

            // Forward to the user-books page to show reservations
            request.getRequestDispatcher("/view/pages/user-books.jsp").forward(request, response);

        } catch (SQLException e) {
            // Log and handle database errors
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
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
        String bookIdParam = request.getParameter("bookId");
        String action = request.getParameter("action");

        Connection connection = null;
        try {
            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection);

            if ("cancel".equals(action)) {
                // Handle reservation cancellation
                String reservationIdParam = request.getParameter("reservationId");
                int reservationId = Integer.parseInt(reservationIdParam);
                Reservation reservation = reservationDAO.getReservationById(reservationId);

                // Validate reservation and user ownership
                if (reservation == null || reservation.getUserId() != user.getId()) {
                    session.setAttribute("errorMessage", "Invalid reservation");
                    response.sendRedirect(request.getContextPath() + "/BookListServlet");
                    return;
                }

                // Increase book stock and cancel reservation
                Book book = bookDAO.getBookById(reservation.getBookId());
                book.setStock(book.getStock() + 1);
                boolean bookUpdated = bookDAO.updateBook(book);
                boolean reservationCancelled = reservationDAO.cancelReservation(reservationId);

                if (bookUpdated && reservationCancelled) {
                    // Remove book ID from reservedBooks set in session
                    Set<Integer> reservedBooks = (Set<Integer>) session.getAttribute("reservedBooks");
                    if (reservedBooks != null) {
                        reservedBooks.remove(reservation.getBookId());
                        session.setAttribute("reservedBooks", reservedBooks);
                    }
                    // Set success message
                    session.setAttribute("successMessage", "Reservation cancelled successfully");
                } else {
                    // Set error message if cancellation fails
                    session.setAttribute("errorMessage", "Failed to cancel reservation");
                }
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Handle new reservation
            int bookId = Integer.parseInt(bookIdParam);
            Book book = bookDAO.getBookById(bookId);

            // Check if book exists
            if (book == null) {
                session.setAttribute("errorMessage", "Book not found");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Check if book is available for borrowing (not reservation)
            if (book.getStock() > 0) {
                session.setAttribute("errorMessage", "This book is available for borrowing, not reservation");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Check for existing active reservation
            if (reservationDAO.hasActiveReservation(user.getId(), bookId)) {
                session.setAttribute("errorMessage", "You already have an active reservation for this book");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Create a new reservation record
            Reservation reservation = new Reservation();
            reservation.setUserId(user.getId());
            reservation.setBookId(bookId);
            reservation.setReservationDate(new Date());
            reservation.setStatus("active");

            // Set reservation expiry to 7 days from now
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            reservation.setReservedUntil(new java.sql.Date(calendar.getTimeInMillis()));

            // Save reservation (no stock change needed since book is already out of stock)
            boolean reservationCreated = reservationDAO.addReservation(reservation);

            if (reservationCreated) {
                // Add book ID to reservedBooks set in session
                Set<Integer> reservedBooks = (Set<Integer>) session.getAttribute("reservedBooks");
                if (reservedBooks == null) {
                    reservedBooks = new HashSet<>();
                    session.setAttribute("reservedBooks", reservedBooks);
                }
                reservedBooks.add(bookId);
                // Set success message with reservation expiry
                session.setAttribute("successMessage", "Book reserved successfully until " + reservation.getReservedUntil());
            } else {
                // Set error message if reservation fails
                session.setAttribute("errorMessage", "Failed to reserve book");
            }
            response.sendRedirect(request.getContextPath() + "/BookListServlet");

        } catch (NumberFormatException e) {
            // Handle invalid ID format
            session.setAttribute("errorMessage", "Invalid book or reservation ID format");
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