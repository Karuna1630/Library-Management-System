package com.example.librarymanagementsystem.filters;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

// Servlet filter to enforce authentication and authorization for web requests
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    // Constant for the "remember me" cookie name
    private static final String REMEMBER_ME_COOKIE = "rememberMe";

    // Filters incoming requests to enforce authentication and role-based access control
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Cast request and response to HTTP-specific types
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Allow access to public paths without authentication
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

        // Check if user is authenticated via session
        if (session == null || session.getAttribute("user") == null) {
            // Check for "remember me" cookie to auto-login
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (REMEMBER_ME_COOKIE.equals(cookie.getName())) {
                        String email = cookie.getValue();
                        // Validate remember-me token and retrieve user
                        User rememberedUser = AuthService.validateRememberMeToken(email);
                        if (rememberedUser != null) {
                            // Create new session and store user
                            HttpSession newSession = httpRequest.getSession(true);
                            newSession.setAttribute("user", rememberedUser);
                            chain.doFilter(request, response);
                            return;
                        }
                    }
                }
            }
            // Redirect to login page if not authenticated
            httpResponse.sendRedirect(contextPath + "/LoginServlet");
            return;
        }

        // Get user from session and check role
        User user = (User) session.getAttribute("user");
        String userRole = user.getRole().name().toLowerCase();

        // Restrict access to admin pages
        if (requestURI.contains("/admin/") && !"admin".equals(userRole)) {
            httpResponse.sendRedirect(contextPath + "/AccessDeniedServlet");
            return;
        }

        // Restrict access to user pages
        if (requestURI.contains("/user/") && !"user".equals(userRole)) {
            httpResponse.sendRedirect(contextPath + "/AccessDeniedServlet");
            return;
        }

        // Allow authenticated and authorized request to proceed
        chain.doFilter(request, response);
    }
}