package com.oceanview.servlet;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {

    private ReservationDAO reservationDAO;
    private RoomDAO roomDAO;

    @Override
    public void init() {
        reservationDAO = new ReservationDAO();
        roomDAO = new RoomDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Auto-generate Reservation No
        String reservationNo = "RES-" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        String guestName = req.getParameter("guestName");
        String address = req.getParameter("address");
        String contactNo = req.getParameter("contactNo");

        // UI values: Standard / Deluxe / Suite
        String roomTypeUi = req.getParameter("roomType");

        // --------- VALIDATION (basic) ----------
        if (guestName == null || guestName.trim().isEmpty()) {
            req.setAttribute("error", "Guest Name is required.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        if (contactNo == null || !contactNo.matches("\\d+")) {
            req.setAttribute("error", "Contact No must contain only numbers.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        if (roomTypeUi == null || roomTypeUi.trim().isEmpty()) {
            req.setAttribute("error", "Room Type is required.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        int guestCount;
        try {
            guestCount = Integer.parseInt(req.getParameter("guestCount"));
        } catch (Exception e) {
            req.setAttribute("error", "Guest count is invalid.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        if (guestCount < 1) {
            req.setAttribute("error", "Guest count must be at least 1.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        LocalDate checkIn;
        LocalDate checkOut;
        try {
            checkIn = LocalDate.parse(req.getParameter("checkIn"));
            checkOut = LocalDate.parse(req.getParameter("checkOut"));
        } catch (Exception e) {
            req.setAttribute("error", "Invalid dates selected.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            req.setAttribute("error", "Check-out date must be after Check-in date.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            req.setAttribute("error", "Invalid dates selected.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        // ---------- Map UI room type -> DB room type ----------
        // DB values: STANDARD / DELUXE / SUITE
        String roomTypeDb;
        switch (roomTypeUi) {
            case "Deluxe":
                roomTypeDb = "DELUXE";
                break;
            case "Suite":
                roomTypeDb = "SUITE";
                break;
            default:
                roomTypeDb = "STANDARD";
                break;
        }

        // ---------- Guest rules + extra fee rules (NOT base price) ----------
        int maxGuestsPerRoom;
        int includedGuestsPerRoom;
        BigDecimal extraGuestFeePerNight;

        switch (roomTypeDb) {
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

            default: // STANDARD
                includedGuestsPerRoom = 2;
                maxGuestsPerRoom = 3;
                extraGuestFeePerNight = new BigDecimal("2000");
                break;
        }

        // Your system supports 1 room per reservation right now
        int roomsNeeded = 1;

        if (guestCount > maxGuestsPerRoom) {
            req.setAttribute("error", "Too many guests for one " + roomTypeUi + " room. Book rooms separately.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        int extraGuests = Math.max(0, guestCount - includedGuestsPerRoom);

        // ✅ IMPORTANT FIX: base rate comes from DB (room_rates table)
        BigDecimal baseRatePerRoom = reservationDAO.getRatePerNight(roomTypeDb);

        if (baseRatePerRoom == null || baseRatePerRoom.compareTo(BigDecimal.ZERO) <= 0) {
            req.setAttribute("error", "Room rate is not configured in database for " + roomTypeDb + ".");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        BigDecimal roomCost = baseRatePerRoom
                .multiply(BigDecimal.valueOf(roomsNeeded))
                .multiply(BigDecimal.valueOf(nights));

        BigDecimal extraCost = extraGuestFeePerNight
                .multiply(BigDecimal.valueOf(extraGuests))
                .multiply(BigDecimal.valueOf(nights));

        BigDecimal total = roomCost.add(extraCost);

        // ---------- Find an available room_id ----------
        Integer roomId = roomDAO.getOneAvailableRoomId(roomTypeDb, checkIn, checkOut);
        if (roomId == null) {
            req.setAttribute("error", "No rooms available for selected dates. Please choose different dates.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        // ---------- Build reservation ----------
        Reservation r = new Reservation();
        r.setReservationNo(reservationNo);
        r.setGuestName(guestName);
        r.setAddress(address);
        r.setContactNo(contactNo);
        r.setRoomType(roomTypeDb);
        r.setRoomId(roomId);
        r.setCheckIn(checkIn);
        r.setCheckOut(checkOut);
        r.setGuestCount(guestCount);
        r.setTotalAmount(total); // ✅ now DB total matches bill total

        // ✅ SAFE INSERT (prevents double booking)
        boolean success = reservationDAO.addReservationIfAvailable(r);
        if (!success) {
            req.setAttribute("error", "Room already booked for selected dates. Please try again.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        // ---------- Forward result ----------
        req.setAttribute("success", true);
        req.setAttribute("reservation", r);

        req.setAttribute("nights", nights);
        req.setAttribute("roomsNeeded", roomsNeeded);
        req.setAttribute("ratePerRoom", baseRatePerRoom);
        req.setAttribute("extraGuests", extraGuests);
        req.setAttribute("extraFee", extraGuestFeePerNight);
        req.setAttribute("roomCost", roomCost);
        req.setAttribute("extraCost", extraCost);
        req.setAttribute("total", total);

        req.getRequestDispatcher("/reservation-result.jsp").forward(req, resp);
    }
}
