package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import util.Connect;

public class OrderDetailDAO {

    private Connect connect = Connect.getInstance();

    public boolean insert(OrderDetail orderDetail) {
        String sql = "INSERT INTO order_details (id_order, id_product, qty) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, orderDetail.getIdOrder());
            pstmt.setString(2, orderDetail.getIdProduct());
            pstmt.setInt(3, orderDetail.getQty());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new java.util.ArrayList<>();
        String sql = "SELECT id_order, id_product, qty FROM order_details WHERE id_order = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderDetail orderDetail = new OrderDetail(
                        rs.getInt("id_order"),
                        rs.getString("id_product"),
                        rs.getInt("qty")
                    );
                    orderDetails.add(orderDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }
}
