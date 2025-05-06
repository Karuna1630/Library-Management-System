package com.example.librarymanagementsystem.controller.Admin;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "BookCoverServlet", value = "/BookCoverServlet")
public class BookCoverServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection connection = null;

        try {
            String bookIdParam = request.getParameter("id");
            if (bookIdParam == null || bookIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
                return;
            }

            int bookId = Integer.parseInt(bookIdParam);
            connection = DBConnectionUtil.getConnection();
            BookDAO bookDAO = new BookDAO(connection);
            Book book = bookDAO.getBookById(bookId);

            if (book == null || book.getImage() == null || book.getImage().length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book image not found");
                return;
            }

            // Use generic content type to support multiple image formats
            response.setContentType("image/*");

            try (OutputStream out = response.getOutputStream()) {
                out.write(book.getImage());
                out.flush();
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID format");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
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