package com.oceanview.dao;
import com.oceanview.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
public class RoomDAO {

    public Integer getOneAvailableRoomId(String roomType, LocalDate checkIn, LocalDate checkOut) {

        String roomTypeDb = normalizeRoomType(roomType);
        String sql =
                "SELECT rm.id " + "FROM rooms rm " + "WHERE rm.status='ACTIVE' " + "  AND rm.room_type = ? " + "  AND NOT EXISTS ( " +
                        "    SELECT 1 FROM reservations r " + "    WHERE r.room_id = rm.id " + "      AND r.check_in < ? " + "      AND r.check_out > ? " + "  ) " +
                        "ORDER BY rm.id " + "LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, roomTypeDb);
            ps.setDate(2, Date.valueOf(checkOut));
            ps.setDate(3, Date.valueOf(checkIn));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
