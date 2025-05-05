package com.example.librarymanagementsystem.controller.admin;

import com.example.librarymanagementsystem.dao.BookDAO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.services.UserService;
import com.example.librarymanagementsystem.utils.DBConnectionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // In AdminServlet.java, update the dashboard action
        if("dashboard".equals(action)) {
            // Verify admin role
            HttpSession session = request.getSession(false);
            if(session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                if(user.getRole() == User.Role.admin) {
                    try {
                        // Get a fresh database connection instead of from servlet context
                        Connection connection = DBConnectionUtil.getConnection();

                        // Get all users
                        List<User> users = userService.getAllUsers();
                        request.setAttribute("users", users);

                        // Get all books using BookDAO
                        BookDAO bookDAO = new BookDAO(connection);
                        try {
                            List<Book> books = bookDAO.getAllBooks();
                            request.setAttribute("books", books);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // Handle error appropriately - maybe set an empty list
                            request.setAttribute("books", new ArrayList<Book>());
                            request.setAttribute("errorMessage", "Error fetching books: " + e.getMessage());
                        } finally {
                            // Close the connection when done
                            try {
                                if (connection != null && !connection.isClosed()) {
                                    connection.close();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        request.getRequestDispatcher("/view/dashboard/admin_dashboard.jsp").forward(request, response);
                        return;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        request.setAttribute("errorMessage", "Database connection error: " + e.getMessage());
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error");
                        return;
                    }
                }
            }
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
        }

        else if("viewUser".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            User viewedUser = userService.getUserById(userId);
            if (viewedUser != null) {
                request.setAttribute("viewedUser", viewedUser);
            } else {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?action=dashboard");
            }
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if("deleteUser".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean success = userService.deleteUser(userId);

            if(success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("User deleted successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Failed to delete user");
            }
        }
    }
}