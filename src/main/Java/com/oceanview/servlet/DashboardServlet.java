package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final ReservationDAO dao = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Prevent caching (so dashboard always refreshes counts)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        int totalReservations = dao.getTotalReservations();
        int bookingsToday = dao.getBookingsMadeToday();
        int todayCheckIns = dao.getTodayCheckIns();
        int todayCheckOuts = dao.getTodayCheckOuts();

        BigDecimal monthlyRevenue = dao.getMonthlyRevenue();
        if (monthlyRevenue == null) monthlyRevenue = BigDecimal.ZERO;

        request.setAttribute("totalReservations", totalReservations);
        request.setAttribute("bookingsToday", bookingsToday);
        request.setAttribute("todayCheckIns", todayCheckIns);
        request.setAttribute("todayCheckOuts", todayCheckOuts);
        request.setAttribute("monthlyRevenue", monthlyRevenue);

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
