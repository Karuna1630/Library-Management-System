package com.example.librarymanagementsystem.filters;

import com.example.librarymanagementsystem.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Publicly accessible paths
        if (requestURI.startsWith(contextPath + "/assets/") ||
                requestURI.equals(contextPath + "/") ||
                requestURI.contains("/LoginServlet") ||
                requestURI.contains("/RegisterServlet") ||
                requestURI.contains("/login.jsp") ||
                requestURI.contains("/register.jsp") ||
                requestURI.contains("/AccessDeniedServlet")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(contextPath + "/LoginServlet");
            return;
        }

        // Get user from session
        User user = (User) session.getAttribute("user");
        String userRole = user.getRole().name().toLowerCase(); // "admin" or "user"

        // Admin pages protection - redirect to access denied if not admin
        if (requestURI.contains("/admin/") && !"admin".equals(userRole)) {
            httpResponse.sendRedirect(contextPath + "/AccessDeniedServlet");
            return;
        }

        // User pages protection - redirect to access denied if not user
        if (requestURI.contains("/user/") && !"user".equals(userRole)) {
            httpResponse.sendRedirect(contextPath + "/AccessDeniedServlet");
            return;
        }

        chain.doFilter(request, response);
    }
}















