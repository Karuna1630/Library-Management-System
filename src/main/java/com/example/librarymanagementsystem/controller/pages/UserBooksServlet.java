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
import java.util.List;

@WebServlet(name = "UserBooksServlet", value = "/UserBooksServlet")
public class UserBooksServlet extends HttpServlet {
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
            if (connection == null) {
                throw new SQLException("Failed to establish database connection");
            }

            ReservationDAO reservationDAO = new ReservationDAO(connection);
            BorrowDAO borrowDAO = new BorrowDAO(connection);

            // Fetch active reservations
            List<Reservation> reservations = reservationDAO.getActiveReservationsByUser(user.getId());
            if (reservations == null) {
                session.setAttribute("errorMessage", "Failed to fetch reservations");
                response.sendRedirect(request.getContextPath() + "/view/auth/access-denied.jsp");
                return;
            }

            // Fetch active borrows
            List<Borrow> borrows = borrowDAO.getActiveBorrowsByUser(user.getId());
            if (borrows == null) {
                session.setAttribute("errorMessage", "Failed to fetch borrows");
                response.sendRedirect(request.getContextPath() + "/view/auth/access-denied.jsp");
                return;
            }

            request.setAttribute("reservations", reservations);
            request.setAttribute("borrows", borrows);
            request.getRequestDispatcher("/view/pages/user-books.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
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
}