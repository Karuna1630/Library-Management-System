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

@WebServlet(name = "BookListServlet", value = "/BookListServlet")
public class BookListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection connection = DBConnectionUtil.getConnection()) {
            BookDAO bookDAO = new BookDAO(connection);
            List<Book> books = bookDAO.getAllBooks();
            request.setAttribute("books", books);
            request.getRequestDispatcher("/view/pages/books.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}