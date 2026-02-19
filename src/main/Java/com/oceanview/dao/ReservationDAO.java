package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.test.DBConnection; // change to com.oceanview.util.DBConnection if needed

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // =========================================================
    // ✅ SAFE INSERT (Prevents double booking)
    // =========================================================
    public boolean addReservationIfAvailable(Reservation r) {

        String lockRoomSql = "SELECT id FROM rooms WHERE id = ? FOR UPDATE";

        String overlapSql =
                "SELECT 1 FROM reservations " +
                        "WHERE room_id = ? " +
                        "  AND check_in < ? " +   // new_check_out
                        "  AND check_out > ? " +  // new_check_in
                        "LIMIT 1";

        String insertSql = "INSERT INTO reservations " +
                "(reservation_no, guest_name, address, contact_no, room_type, room_id, check_in, check_out, guest_count, total_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 1) Lock chosen room row (race-condition protection)
                try (PreparedStatement lockPs = con.prepareStatement(lockRoomSql)) {
                    lockPs.setInt(1, r.getRoomId());
                    lockPs.executeQuery();
                }

                // 2) Check overlap
                boolean conflict;
                try (PreparedStatement ovPs = con.prepareStatement(overlapSql)) {
                    ovPs.setInt(1, r.getRoomId());
                    ovPs.setDate(2, Date.valueOf(r.getCheckOut())); // new_check_out
                    ovPs.setDate(3, Date.valueOf(r.getCheckIn()));  // new_check_in
                    try (ResultSet rs = ovPs.executeQuery()) {
                        conflict = rs.next();
                    }
                }

                if (conflict) {
                    con.rollback();
                    return false;
                }

                // 3) Insert reservation
                try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                    ps.setString(1, r.getReservationNo());
                    ps.setString(2, r.getGuestName());
                    ps.setString(3, r.getAddress());
                    ps.setString(4, r.getContactNo());
                    ps.setString(5, normalizeRoomType(r.getRoomType()));
                    ps.setInt(6, r.getRoomId());
                    ps.setDate(7, Date.valueOf(r.getCheckIn()));
                    ps.setDate(8, Date.valueOf(r.getCheckOut()));
                    ps.setInt(9, r.getGuestCount());
                    ps.setBigDecimal(10, safeAmount(r.getTotalAmount()));
                    ps.executeUpdate();
                }

                con.commit();
                return true;

            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================
    // ✅ SAFE UPDATE (Prevents double booking when editing)
    // =========================================================
    public boolean updateReservationIfAvailable(Reservation r) {

        String lockRoomSql = "SELECT id FROM rooms WHERE id = ? FOR UPDATE";

        String overlapSql =
                "SELECT 1 FROM reservations " +
                        "WHERE room_id = ? " +
                        "  AND id <> ? " +
                        "  AND check_in < ? " +
                        "  AND check_out > ? " +
                        "LIMIT 1";

        String updateSql = "UPDATE reservations SET " +
                "guest_name=?, address=?, contact_no=?, room_type=?, room_id=?, check_in=?, check_out=?, guest_count=?, total_amount=? " +
                "WHERE id=?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 1) lock room row
                try (PreparedStatement lockPs = con.prepareStatement(lockRoomSql)) {
                    lockPs.setInt(1, r.getRoomId());
                    lockPs.executeQuery();
                }

                // 2) check overlap excluding itself
                boolean conflict;
                try (PreparedStatement ovPs = con.prepareStatement(overlapSql)) {
                    ovPs.setInt(1, r.getRoomId());
                    ovPs.setInt(2, r.getId());
                    ovPs.setDate(3, Date.valueOf(r.getCheckOut()));
                    ovPs.setDate(4, Date.valueOf(r.getCheckIn()));
                    try (ResultSet rs = ovPs.executeQuery()) {
                        conflict = rs.next();
                    }
                }

                if (conflict) {
                    con.rollback();
                    return false;
                }

                // 3) update
                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setString(1, r.getGuestName());
                    ps.setString(2, r.getAddress());
                    ps.setString(3, r.getContactNo());
                    ps.setString(4, normalizeRoomType(r.getRoomType()));
                    ps.setInt(5, r.getRoomId());
                    ps.setDate(6, Date.valueOf(r.getCheckIn()));
                    ps.setDate(7, Date.valueOf(r.getCheckOut()));
                    ps.setInt(8, r.getGuestCount());
                    ps.setBigDecimal(9, safeAmount(r.getTotalAmount()));
                    ps.setInt(10, r.getId());

                    int updated = ps.executeUpdate();
                    con.commit();
                    return updated > 0;
                }

            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
                return false;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================
    // NORMAL INSERT (NOT SAFE) - includes room_id
    // =========================================================
    public boolean addReservation(Reservation r) {

        String sql = "INSERT INTO reservations " +
                "(reservation_no, guest_name, address, contact_no, room_type, room_id, check_in, check_out, guest_count, total_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getReservationNo());
            ps.setString(2, r.getGuestName());
            ps.setString(3, r.getAddress());
            ps.setString(4, r.getContactNo());
            ps.setString(5, normalizeRoomType(r.getRoomType()));
            ps.setInt(6, r.getRoomId());
            ps.setDate(7, Date.valueOf(r.getCheckIn()));
            ps.setDate(8, Date.valueOf(r.getCheckOut()));
            ps.setInt(9, r.getGuestCount());
            ps.setBigDecimal(10, safeAmount(r.getTotalAmount()));

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // GET ALL RESERVATIONS (WITH ROOM NUMBER)
    // =========================
    public List<Reservation> getAllReservations() {

        List<Reservation> list = new ArrayList<>();

        String sql =
                "SELECT r.id, r.reservation_no, r.guest_name, r.address, r.contact_no, " +
                        "r.room_type, r.room_id, rm.room_number, " +
                        "r.check_in, r.check_out, r.guest_count, r.total_amount " +
                        "FROM reservations r " +
                        "LEFT JOIN rooms rm ON rm.id = r.room_id " +
                        "ORDER BY r.id DESC";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Reservation r = new Reservation();

                r.setId(rs.getInt("id"));
                r.setReservationNo(rs.getString("reservation_no"));
                r.setGuestName(rs.getString("guest_name"));
                r.setAddress(rs.getString("address"));
                r.setContactNo(rs.getString("contact_no"));
                r.setRoomType(rs.getString("room_type"));
                r.setRoomId(rs.getInt("room_id"));

                // ✅ NEW
                r.setRoomNumber(rs.getString("room_number"));

                r.setCheckIn(rs.getDate("check_in").toLocalDate());
                r.setCheckOut(rs.getDate("check_out").toLocalDate());
                r.setGuestCount(rs.getInt("guest_count"));
                r.setTotalAmount(rs.getBigDecimal("total_amount"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // DELETE RESERVATION
    // =========================
    public boolean deleteReservation(int id) {

        String sql = "DELETE FROM reservations WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // GET RESERVATION BY ID (WITH ROOM NUMBER)
    // =========================
    public Reservation getReservationById(int id) {

        String sql =
                "SELECT r.id, r.reservation_no, r.guest_name, r.address, r.contact_no, " +
                        "r.room_type, r.room_id, rm.room_number, " +
                        "r.check_in, r.check_out, r.guest_count, r.total_amount " +
                        "FROM reservations r " +
                        "LEFT JOIN rooms rm ON rm.id = r.room_id " +
                        "WHERE r.id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reservation r = new Reservation();

                    r.setId(rs.getInt("id"));
                    r.setReservationNo(rs.getString("reservation_no"));
                    r.setGuestName(rs.getString("guest_name"));
                    r.setAddress(rs.getString("address"));
                    r.setContactNo(rs.getString("contact_no"));
                    r.setRoomType(rs.getString("room_type"));
                    r.setRoomId(rs.getInt("room_id"));

                    // ✅ NEW
                    r.setRoomNumber(rs.getString("room_number"));

                    r.setCheckIn(rs.getDate("check_in").toLocalDate());
                    r.setCheckOut(rs.getDate("check_out").toLocalDate());
                    r.setGuestCount(rs.getInt("guest_count"));
                    r.setTotalAmount(rs.getBigDecimal("total_amount"));

                    return r;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // UPDATE RESERVATION (NOT SAFE)
    // =========================
    public boolean updateReservation(Reservation r) {

        String sql = "UPDATE reservations SET " +
                "guest_name=?, address=?, contact_no=?, room_type=?, room_id=?, check_in=?, check_out=?, guest_count=?, total_amount=? " +
                "WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getGuestName());
            ps.setString(2, r.getAddress());
            ps.setString(3, r.getContactNo());
            ps.setString(4, normalizeRoomType(r.getRoomType()));
            ps.setInt(5, r.getRoomId());
            ps.setDate(6, Date.valueOf(r.getCheckIn()));
            ps.setDate(7, Date.valueOf(r.getCheckOut()));
            ps.setInt(8, r.getGuestCount());
            ps.setBigDecimal(9, safeAmount(r.getTotalAmount()));
            ps.setInt(10, r.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // SEARCH BY GUEST NAME (WITH ROOM NUMBER)
    // =========================
    public List<Reservation> searchByGuestName(String keyword) {

        List<Reservation> list = new ArrayList<>();

        String sql =
                "SELECT r.id, r.reservation_no, r.guest_name, r.address, r.contact_no, " +
                        "r.room_type, r.room_id, rm.room_number, " +
                        "r.check_in, r.check_out, r.guest_count, r.total_amount " +
                        "FROM reservations r " +
                        "LEFT JOIN rooms rm ON rm.id = r.room_id " +
                        "WHERE r.guest_name LIKE ? " +
                        "ORDER BY r.id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reservation r = new Reservation();

                    r.setId(rs.getInt("id"));
                    r.setReservationNo(rs.getString("reservation_no"));
                    r.setGuestName(rs.getString("guest_name"));
                    r.setAddress(rs.getString("address"));
                    r.setContactNo(rs.getString("contact_no"));
                    r.setRoomType(rs.getString("room_type"));
                    r.setRoomId(rs.getInt("room_id"));

                    // ✅ NEW
                    r.setRoomNumber(rs.getString("room_number"));

                    r.setCheckIn(rs.getDate("check_in").toLocalDate());
                    r.setCheckOut(rs.getDate("check_out").toLocalDate());
                    r.setGuestCount(rs.getInt("guest_count"));
                    r.setTotalAmount(rs.getBigDecimal("total_amount"));

                    list.add(r);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // DASHBOARD METHODS
    // =========================
    public int getTotalReservations() {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayCheckIns() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE check_in = CURDATE()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayCheckOuts() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE check_out = CURDATE()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBookingsMadeToday() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE DATE(created_at) = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BigDecimal getMonthlyRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) " +
                "FROM reservations " +
                "WHERE YEAR(created_at) = YEAR(CURDATE()) " +
                "AND MONTH(created_at) = MONTH(CURDATE())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    // =========================================================
    // Helpers
    // =========================================================
    private BigDecimal safeAmount(BigDecimal amount) {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    private String normalizeRoomType(String roomType) {
        if (roomType == null) return "STANDARD";
        String rt = roomType.trim();

        if (rt.equalsIgnoreCase("Standard")) return "STANDARD";
        if (rt.equalsIgnoreCase("Deluxe")) return "DELUXE";
        if (rt.equalsIgnoreCase("Suite")) return "SUITE";

        if (rt.equalsIgnoreCase("STANDARD")) return "STANDARD";
        if (rt.equalsIgnoreCase("DELUXE")) return "DELUXE";
        if (rt.equalsIgnoreCase("SUITE")) return "SUITE";

        return "STANDARD";
    }
}
