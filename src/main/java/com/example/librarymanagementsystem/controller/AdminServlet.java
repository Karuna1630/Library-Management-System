package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if("dashboard".equals(action)) {
            // Verify admin role
            HttpSession session = request.getSession(false);
            if(session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                if(user.getRole() == User.Role.admin) {
                    request.getRequestDispatcher("/view/dashboard/admin-dashboard.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
        }
    }
}