package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/reservations")
public class ReservationListServlet extends HttpServlet {

    private ReservationDAO reservationDAO;

    @Override
    public void init() {
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String q = req.getParameter("q");

        List<Reservation> list;

        if (q != null && !q.trim().isEmpty()) {
            list = reservationDAO.searchByGuestName(q.trim());
            req.setAttribute("q", q);
        } else {
            list = reservationDAO.getAllReservations();
        }

        req.setAttribute("reservations", list);
        req.getRequestDispatcher("/reservations.jsp").forward(req, resp);
    }

}
