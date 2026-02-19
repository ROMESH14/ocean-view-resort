package com.oceanview.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // ✅ simple hard-coded login (for assignment)
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {

            HttpSession session = req.getSession();
            session.setAttribute("user", username);

            // go to main page after login
            resp.sendRedirect(req.getContextPath() + "/dashboard");

        } else {
            // back to login with error
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=1");
        }
    }
}
