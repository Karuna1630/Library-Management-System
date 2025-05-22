package com.example.librarymanagementsystem.controller.pages;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "IndexServlet", value = "/IndexServlet")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            // Establish database connection
            connection = DBConnectionUtil.getConnection();
            if (connection == null) {
                throw new SQLException("Failed to establish database connection");
            }

            // Initialize BookDAO with the database connection
            BookDAO bookDAO = new BookDAO(connection);

            // Fetch the latest 4 books to display on the homepage
            List<Book> latestBooks = bookDAO.getLatestBooks(4);

            // Set the list of latest books as a request attribute for the JSP
            request.setAttribute("latestBooks", latestBooks);

            // Forward to the index JSP page for rendering
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (SQLException e) {
            // Log and handle database errors
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
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
        // No POST handling implemented for this servlet
    }
}