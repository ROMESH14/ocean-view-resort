package com.oceanview.test;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InsertTest {
    public static void main(String[] args) {

        Reservation r = new Reservation();

        r.setReservationNo("R2001");
        r.setGuestName("Romesh");
        r.setAddress("Galle");
        r.setContactNo("0771234567");

        // IMPORTANT: DB uses STANDARD / DELUXE / SUITE
        r.setRoomType("DELUXE");

        // MUST EXIST in rooms table (check phpMyAdmin)
        r.setRoomId(5);   // example: room id 5

        r.setCheckIn(LocalDate.of(2026, 2, 20));
        r.setCheckOut(LocalDate.of(2026, 2, 23));

        r.setGuestCount(2);
        r.setTotalAmount(new BigDecimal("45000"));

        ReservationDAO dao = new ReservationDAO();

        // ✅ USE SAFE METHOD
        if (dao.addReservationIfAvailable(r)) {
            System.out.println("✅ Insert success!");
        } else {
            System.out.println("❌ Insert failed or room already booked!");
        }
    }
}
