package com.example.librarymanagementsystem.controller.pages;

import com.example.librarymanagementsystem.dao.BorrowDAO;
import com.example.librarymanagementsystem.dao.ReservationDAO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet(name = "UserBooksServlet", value = "/UserBooksServlet")
public class UserBooksServlet extends HttpServlet {
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
            if (connection == null) {
                throw new SQLException("Failed to establish database connection");
            }

            // Initialize DAOs for reservation and borrow operations
            ReservationDAO reservationDAO = new ReservationDAO(connection);
            BorrowDAO borrowDAO = new BorrowDAO(connection);

            // Fetch active reservations for the user
            List<Reservation> reservations = reservationDAO.getActiveReservationsByUser(user.getId());
            if (reservations == null) {
                // Handle case where reservations fetch fails
                session.setAttribute("errorMessage", "Failed to fetch reservations");
                response.sendRedirect(request.getContextPath() + "/view/auth/access-denied.jsp");
                return;
            }

            // Update reservedBooks set in session
            Set<Integer> reservedBooks = new HashSet<>();
            for (Reservation reservation : reservations) {
                reservedBooks.add(reservation.getBookId());
                // Log reservation details for debugging
                System.out.println("User " + user.getId() + " - Reservation ID: " + reservation.getReservationId() +
                        ", Book ID: " + reservation.getBookId() + ", Status: " + reservation.getStatus());
            }
            session.setAttribute("reservedBooks", reservedBooks);
            System.out.println("User " + user.getId() + " - Reserved Books Set: " + reservedBooks);

            // Fetch active borrows for the user
            List<Borrow> borrows = borrowDAO.getActiveBorrowsByUser(user.getId());
            if (borrows == null) {
                // Handle case where borrows fetch fails
                session.setAttribute("errorMessage", "Failed to fetch borrows");
                response.sendRedirect(request.getContextPath() + "/view/auth/access-denied.jsp");
                return;
            }

            // Update borrowedBooks set in session
            Set<Integer> borrowedBooks = new HashSet<>();
            for (Borrow borrow : borrows) {
                borrowedBooks.add(borrow.getBookId());
                // Log borrow details for debugging
                System.out.println("User " + user.getId() + " - Borrow ID: " + borrow.getBorrowId() +
                        ", Book ID: " + borrow.getBookId() + ", Status: " + borrow.getStatus());
            }
            session.setAttribute("borrowedBooks", borrowedBooks);
            System.out.println("User " + user.getId() + " - Borrowed Books Set: " + borrowedBooks);

            // Set reservations and borrows as request attributes for the JSP
            request.setAttribute("reservations", reservations);
            request.setAttribute("borrows", borrows);

            // Forward to the user-books page to display reservations and borrows
            request.getRequestDispatcher("/view/pages/user-books.jsp").forward(request, response);

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
}