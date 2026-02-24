package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@WebServlet("/viewBill")
public class ReservationBillServlet extends HttpServlet {

    private ReservationDAO reservationDAO;

    @Override
    public void init() {
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) Safe read id
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect("reservations");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect("reservations");
            return;
        }

        // 2) Load reservation
        Reservation r = reservationDAO.getReservationById(id);
        if (r == null) {
            resp.sendRedirect("reservations");
            return;
        }

        long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
        if (nights <= 0) {
            req.setAttribute("error", "Invalid stay dates for this reservation.");
            resp.sendRedirect("reservations");
            return;
        }

        int guestCount = r.getGuestCount();
        String roomType = r.getRoomType();
        BigDecimal baseRatePerRoom = reservationDAO.getRatePerNight(roomType);
        if (baseRatePerRoom == null || baseRatePerRoom.compareTo(BigDecimal.ZERO) <= 0) {
            req.setAttribute("error", "Room rate not found for room type: " + roomType);
            req.getRequestDispatcher("/bill.jsp").forward(req, resp);
            return;
        }

        int maxGuestsPerRoom;
        int includedGuestsPerRoom;
        BigDecimal extraGuestFeePerNight;
        switch (roomType) {
            case "DELUXE":
                includedGuestsPerRoom = 2;
                maxGuestsPerRoom = 3;
                extraGuestFeePerNight = new BigDecimal("5000");
                break;

            case "SUITE":
                includedGuestsPerRoom = 2;
                maxGuestsPerRoom = 2;
                extraGuestFeePerNight = BigDecimal.ZERO;
                break;

            default:
                includedGuestsPerRoom = 2;
                maxGuestsPerRoom = 3;
                extraGuestFeePerNight = new BigDecimal("2000");
                break;
        }

        int roomsNeeded = (int) Math.ceil((double) guestCount / maxGuestsPerRoom);
        int totalIncludedGuests = roomsNeeded * includedGuestsPerRoom;
        int extraGuests = Math.max(0, guestCount - totalIncludedGuests);

        BigDecimal roomCost = baseRatePerRoom
                .multiply(BigDecimal.valueOf(roomsNeeded))
                .multiply(BigDecimal.valueOf(nights));

        BigDecimal extraCost = extraGuestFeePerNight
                .multiply(BigDecimal.valueOf(extraGuests))
                .multiply(BigDecimal.valueOf(nights));

        BigDecimal total = roomCost.add(extraCost);

        req.setAttribute("reservation", r);
        req.setAttribute("nights", nights);
        req.setAttribute("roomsNeeded", roomsNeeded);
        req.setAttribute("ratePerRoom", baseRatePerRoom);
        req.setAttribute("extraGuests", extraGuests);
        req.setAttribute("extraFee", extraGuestFeePerNight);
        req.setAttribute("roomCost", roomCost);
        req.setAttribute("extraCost", extraCost);
        req.setAttribute("total", total);
        req.setAttribute("roomNumber", r.getRoomNumber());

        req.getRequestDispatcher("/bill.jsp").forward(req, resp);
    }
}
