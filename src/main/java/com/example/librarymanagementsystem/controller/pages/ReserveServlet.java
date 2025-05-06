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
import java.util.List;

@WebServlet(name = "ReserveServlet", value = "/ReserveServlet")
public class ReserveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        User user = (User) session.getAttribute("user");
        Connection connection = null;
        try {
            connection = DBConnectionUtil.getConnection();
            ReservationDAO reservationDAO = new ReservationDAO(connection);
            List<Reservation> reservations = reservationDAO.getActiveReservationsByUser(user.getId());

            request.setAttribute("reservations", reservations);
            request.getRequestDispatcher("/view/pages/user-books.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/view/auth/access-denied.jsp").forward(request, response);
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
        String action = request.getParameter("action");

        Connection connection = null;
        try {
            connection = DBConnectionUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection);

            if ("cancel".equals(action)) {
                // Cancel reservation
                String reservationIdParam = request.getParameter("reservationId");
                int reservationId = Integer.parseInt(reservationIdParam);
                Reservation reservation = reservationDAO.getReservationById(reservationId);

                if (reservation == null || reservation.getUserId() != user.getId()) {
                    session.setAttribute("errorMessage", "Invalid reservation");
                    response.sendRedirect(request.getContextPath() + "/BookListServlet");
                    return;
                }

                Book book = bookDAO.getBookById(reservation.getBookId());
                book.setStock(book.getStock() + 1);
                boolean bookUpdated = bookDAO.updateBook(book);
                boolean reservationCancelled = reservationDAO.cancelReservation(reservationId);

                if (bookUpdated && reservationCancelled) {
                    session.setAttribute("successMessage", "Reservation cancelled successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to cancel reservation");
                }
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Add reservation
            int bookId = Integer.parseInt(bookIdParam);
            Book book = bookDAO.getBookById(bookId);

            if (book == null) {
                session.setAttribute("errorMessage", "Book not found");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            if (book.getStock() > 0) {
                session.setAttribute("errorMessage", "This book is available for borrowing, not reservation");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Check if user already has an active reservation for this book
            if (reservationDAO.hasActiveReservation(user.getId(), bookId)) {
                session.setAttribute("errorMessage", "You already have an active reservation for this book");
                response.sendRedirect(request.getContextPath() + "/BookListServlet");
                return;
            }

            // Create reservation
            Reservation reservation = new Reservation();
            reservation.setUserId(user.getId());
            reservation.setBookId(bookId);
            reservation.setReservationDate(new Date());
            reservation.setStatus("active");

            // Set reserved_until to 7 days from now
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            reservation.setReservedUntil(new java.sql.Date(calendar.getTimeInMillis()));

            // Update book stock
            book.setStock(book.getStock() - 1);
            boolean bookUpdated = bookDAO.updateBook(book);
            boolean reservationCreated = reservationDAO.addReservation(reservation);

            if (bookUpdated && reservationCreated) {
                session.setAttribute("successMessage", "Book reserved successfully until " + reservation.getReservedUntil());
            } else {
                session.setAttribute("errorMessage", "Failed to reserve book");
            }
            response.sendRedirect(request.getContextPath() + "/BookListServlet");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid book or reservation ID format");
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