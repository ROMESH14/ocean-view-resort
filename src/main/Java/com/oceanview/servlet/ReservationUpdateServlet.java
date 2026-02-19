package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/updateReservation")
public class ReservationUpdateServlet extends HttpServlet {

    private ReservationDAO reservationDAO;

    @Override
    public void init() {
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));

        String guestName = req.getParameter("guestName");
        String address = req.getParameter("address");
        String contactNo = req.getParameter("contactNo");
        String roomType = req.getParameter("roomType");

        int guestCount = Integer.parseInt(req.getParameter("guestCount"));

        LocalDate checkIn = LocalDate.parse(req.getParameter("checkIn"));
        LocalDate checkOut = LocalDate.parse(req.getParameter("checkOut"));

        // ---------- VALIDATION ----------
        if (guestName == null || guestName.trim().isEmpty()) {
            req.setAttribute("error", "Guest Name is required.");
            req.setAttribute("reservation", reservationDAO.getReservationById(id));
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }

        if (contactNo == null || !contactNo.matches("\\d+")) {
            req.setAttribute("error", "Contact No must contain only numbers.");
            req.setAttribute("reservation", reservationDAO.getReservationById(id));
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }

        if (guestCount < 1) {
            req.setAttribute("error", "Guest count must be at least 1.");
            req.setAttribute("reservation", reservationDAO.getReservationById(id));
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            req.setAttribute("error", "Check-out date must be after Check-in date.");
            req.setAttribute("reservation", reservationDAO.getReservationById(id));
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }
        // ---------- END VALIDATION ----------

        Reservation r = new Reservation();
        r.setId(id);
        r.setGuestName(guestName);
        r.setAddress(address);
        r.setContactNo(contactNo);
        r.setRoomType(roomType);
        r.setGuestCount(guestCount);   // ✅ IMPORTANT (you missed this)
        r.setCheckIn(checkIn);
        r.setCheckOut(checkOut);

        reservationDAO.updateReservation(r);

        // Back to list with success message
        resp.sendRedirect(req.getContextPath() + "/reservations?success=updated");
    }

}
