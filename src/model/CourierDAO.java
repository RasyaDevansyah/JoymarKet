package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Connect;

public class CourierDAO {

    private Connect connect = Connect.getInstance();

    public List<Courier> getAllCouriers() {
        List<Courier> couriers = new ArrayList<>();
        String sql = "SELECT u.id_user, u.full_name, u.email, u.password, u.phone, u.address, u.gender, c.vehicle_type, c.vehicle_plate " + // Added u.gender
                     "FROM users u JOIN couriers c ON u.id_user = c.id_user " +
                     "WHERE u.role = 'COURIER'";
        try (PreparedStatement pstmt = connect.preparedStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                couriers.add(new Courier(
                    String.valueOf(rs.getInt("id_user")),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("gender"), // Pass gender
                    rs.getString("vehicle_type"),
                    rs.getString("vehicle_plate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return couriers;
    }

    public Courier getCourierById(String courierId) {
        String sql = "SELECT u.id_user, u.full_name, u.email, u.password, u.phone, u.address, u.gender, c.vehicle_type, c.vehicle_plate " + // Added u.gender
                     "FROM users u JOIN couriers c ON u.id_user = c.id_user " +
                     "WHERE u.id_user = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, courierId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Courier(
                        String.valueOf(rs.getInt("id_user")),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("gender"), // Pass gender
                        rs.getString("vehicle_type"),
                        rs.getString("vehicle_plate")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
