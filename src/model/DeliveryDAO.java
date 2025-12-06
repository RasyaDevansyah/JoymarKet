package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Connect;

public class DeliveryDAO {

    private Connect connect = Connect.getInstance();

    public Delivery getDeliveryByOrderId(int orderId) {
        String sql = "SELECT id_order, id_courier, status FROM deliveries WHERE id_order = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Delivery(
                        String.valueOf(rs.getInt("id_order")),
                        String.valueOf(rs.getInt("id_courier")),
                        rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertDelivery(Delivery delivery) {
        String sql = "INSERT INTO deliveries (id_order, id_courier, status) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(delivery.getIdOrder()));
            pstmt.setInt(2, Integer.parseInt(delivery.getIdCourier()));
            pstmt.setString(3, delivery.getStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDelivery(Delivery delivery) {
        String sql = "UPDATE deliveries SET id_courier = ?, status = ? WHERE id_order = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(delivery.getIdCourier()));
            pstmt.setString(2, delivery.getStatus());
            pstmt.setInt(3, Integer.parseInt(delivery.getIdOrder()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
