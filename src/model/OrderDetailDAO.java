package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
