package com.oceanview.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String contextPath = req.getContextPath();

        // ✅ Root access: /ocean-view-resort/  -> go to login first
        boolean isRoot = path.equals(contextPath + "/") || path.equals(contextPath);

        // Allow login pages/servlets + static files
        boolean isLoginPage = path.equals(contextPath + "/login.jsp");
        boolean isLoginServlet = path.equals(contextPath + "/login");

        boolean isPublicResource =
                path.startsWith(contextPath + "/css/") ||
                        path.startsWith(contextPath + "/js/") ||
                        path.startsWith(contextPath + "/images/");

        // ✅ If user opens root "/", send to login first
        if (isRoot) {
            resp.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // ✅ Allow login + static resources without session
        if (isLoginPage || isLoginServlet || isPublicResource) {
            chain.doFilter(request, response);
            return;
        }

        // Check session
        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (!loggedIn) {
            resp.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // Continue
        chain.doFilter(request, response);
    }
}
