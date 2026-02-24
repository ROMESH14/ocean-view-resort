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

        boolean isRoot = path.equals(contextPath + "/") || path.equals(contextPath);
        boolean isLoginPage = path.equals(contextPath + "/login.jsp");
        boolean isLoginServlet = path.equals(contextPath + "/login");
        boolean isPublicResource =
                path.startsWith(contextPath + "/css/") ||
                        path.startsWith(contextPath + "/js/") ||
                        path.startsWith(contextPath + "/images/") ||
                        path.startsWith(contextPath + "/assets/") ||
                        path.endsWith(".css") ||
                        path.endsWith(".js")  ||
                        path.endsWith(".png") ||
                        path.endsWith(".jpg") ||
                        path.endsWith(".jpeg")||
                        path.endsWith(".gif") ||
                        path.endsWith(".svg") ||
                        path.endsWith(".ico");

        if (isRoot) {
            resp.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        if (isLoginPage || isLoginServlet || isPublicResource) {
            chain.doFilter(request, response);
            return;
        }

        //login session
        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (!loggedIn) {
            resp.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}