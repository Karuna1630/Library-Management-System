package com.example.librarymanagementsystem.filters;

import com.example.librarymanagementsystem.model.User;
import com.example.librarymanagementsystem.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    private static final String REMEMBER_ME_COOKIE = "rememberMe";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        assert session != null;
        // Publicly accessible paths
        if (requestURI.startsWith(contextPath + "/assets/") ||
                requestURI.equals(contextPath + "/") ||
                requestURI.contains("/LoginServlet") ||
                requestURI.contains("/RegisterServlet") ||
                requestURI.contains("/login.jsp") ||
                requestURI.contains("/register.jsp") ||
                requestURI.contains("/AccessDeniedServlet")||
                requestURI.contains("/index.jsp")||
                requestURI.contains("/IndexServlet")) {
            chain.doFilter(request, response);
            System.out.println("These are publlic pages");
            return;
        }

        // Check if user is logged in via session
        if (session == null || session.getAttribute("user") == null) {
            // Check for remember me cookie
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (REMEMBER_ME_COOKIE.equals(cookie.getName())) {
                        String email = cookie.getValue();
                        User rememberedUser = AuthService.validateRememberMeToken(email);
                        if (rememberedUser != null) {
                            // Create new session
                            HttpSession newSession = httpRequest.getSession(true);
                            newSession.setAttribute("user", rememberedUser);
                            chain.doFilter(request, response);
                            return;
                        }
                    }
                }
            }
            httpResponse.sendRedirect(contextPath + "/LoginServlet");
            return;
        }

        // Get user from session
        User user = (User) session.getAttribute("user");
        String userRole = user.getRole().name().toLowerCase();

        // Admin pages protection
        if (requestURI.contains("/admin/") && !"admin".equals(userRole)) {
            httpResponse.sendRedirect(contextPath + "/AccessDeniedServlet");
            return;
        }

        // User pages protection
        if (requestURI.contains("/user/") && !"user".equals(userRole)) {
            httpResponse.sendRedirect(contextPath + "/AccessDeniedServlet");
            return;
        }

        chain.doFilter(request, response);
    }
}