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

        int id = Integer.parseInt(req.getParameter("id"));

        Reservation r = reservationDAO.getReservationById(id);

        if (r == null) {
            resp.sendRedirect("reservations");
            return;
        }

        long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());

        int guestCount = r.getGuestCount();
        String roomType = r.getRoomType();

        int maxGuestsPerRoom;
        int includedGuestsPerRoom;
        BigDecimal baseRatePerRoom;
        BigDecimal extraGuestFeePerNight;

        switch (roomType) {

            case "Deluxe":
                baseRatePerRoom = new BigDecimal("15000");
                includedGuestsPerRoom = 2;
                maxGuestsPerRoom = 3;
                extraGuestFeePerNight = new BigDecimal("5000");
                break;

            case "Suite":
                baseRatePerRoom = new BigDecimal("25000");
                includedGuestsPerRoom = 2;
                maxGuestsPerRoom = 2;
                extraGuestFeePerNight = BigDecimal.ZERO;
                break;

            default:
                baseRatePerRoom = new BigDecimal("10000");
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

        req.getRequestDispatcher("/bill.jsp").forward(req, resp);
    }
}
