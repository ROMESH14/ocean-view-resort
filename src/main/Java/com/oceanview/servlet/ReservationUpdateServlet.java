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
        Reservation existing = reservationDAO.getReservationById(id);
        if (existing == null) {
            resp.sendRedirect(req.getContextPath() + "/reservations");
            return;
        }

        //validation
        if (guestName == null || guestName.trim().isEmpty()) {
            req.setAttribute("error", "Guest Name is required.");
            req.setAttribute("reservation", existing);
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }

        if (contactNo == null || !contactNo.matches("\\d+")) {
            req.setAttribute("error", "Contact No must contain only numbers.");
            req.setAttribute("reservation", existing);
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }

        if (guestCount < 1) {
            req.setAttribute("error", "Guest count must be at least 1.");
            req.setAttribute("reservation", existing);
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            req.setAttribute("error", "Check-out date must be after Check-in date.");
            req.setAttribute("reservation", existing);
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }
        int roomId = existing.getRoomId();
        String roomIdParam = req.getParameter("roomId");
        if (roomIdParam != null && !roomIdParam.trim().isEmpty()) {
            try {
                roomId = Integer.parseInt(roomIdParam);
            } catch (NumberFormatException ignored) {
                // keep existing roomId
            }
        }
        Reservation r = new Reservation();
        r.setId(id);
        r.setReservationNo(existing.getReservationNo());
        r.setGuestName(guestName);
        r.setAddress(address);
        r.setContactNo(contactNo);
        r.setRoomType(roomType);
        r.setRoomId(roomId);
        r.setGuestCount(guestCount);
        r.setCheckIn(checkIn);
        r.setCheckOut(checkOut);
        r.setTotalAmount(existing.getTotalAmount());
        boolean ok = reservationDAO.updateReservationIfAvailable(r);

        if (!ok) {
            req.setAttribute("error", "Room already booked for selected dates. Please choose another room or dates.");
            req.setAttribute("reservation", existing);
            req.getRequestDispatcher("/edit-reservation.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/reservations?success=updated");
    }
}
