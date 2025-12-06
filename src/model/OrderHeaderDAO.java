package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.Connect;

public class OrderHeaderDAO {

    private Connect connect = Connect.getInstance();

    public int insert(OrderHeader orderHeader) {
        String sql = "INSERT INTO order_headers (id_customer, id_promo, status, ordered_at, total_amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connect.preparedStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, orderHeader.getIdCustomer());
            pstmt.setString(2, orderHeader.getIdPromo());
            pstmt.setString(3, orderHeader.getStatus());
            pstmt.setDate(4, orderHeader.getOrderedAt());
            pstmt.setDouble(5, orderHeader.getTotalAmount());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
