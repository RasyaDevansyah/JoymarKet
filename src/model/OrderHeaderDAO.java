package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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

    public List<OrderHeader> getOrderHeadersByCustomerId(String customerId) {
        List<OrderHeader> orderHeaders = new java.util.ArrayList<>();
        String sql = "SELECT id_order, id_customer, id_promo, status, ordered_at, total_amount FROM order_headers WHERE id_customer = ? ORDER BY ordered_at DESC";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderHeader orderHeader = new OrderHeader(
                        rs.getString("id_customer"),
                        rs.getString("id_promo"),
                        rs.getString("status"),
                        rs.getDate("ordered_at"),
                        rs.getDouble("total_amount")
                    );
                    orderHeader.setIdOrder(rs.getInt("id_order"));
                    orderHeaders.add(orderHeader);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderHeaders;
    }

    public OrderHeader getOrderHeaderById(int orderId) {
        String sql = "SELECT id_order, id_customer, id_promo, status, ordered_at, total_amount FROM order_headers WHERE id_order = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    OrderHeader orderHeader = new OrderHeader(
                        rs.getString("id_customer"),
                        rs.getString("id_promo"),
                        rs.getString("status"),
                        rs.getDate("ordered_at"),
                        rs.getDouble("total_amount")
                    );
                    orderHeader.setIdOrder(rs.getInt("id_order"));
                    return orderHeader;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderHeader> getAllOrderHeaders() {
        List<OrderHeader> orderHeaders = new java.util.ArrayList<>();
        String sql = "SELECT id_order, id_customer, id_promo, status, ordered_at, total_amount FROM order_headers ORDER BY ordered_at DESC";
        try (PreparedStatement pstmt = connect.preparedStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                OrderHeader orderHeader = new OrderHeader(
                    rs.getString("id_customer"),
                    rs.getString("id_promo"),
                    rs.getString("status"),
                    rs.getDate("ordered_at"),
                    rs.getDouble("total_amount")
                );
                orderHeader.setIdOrder(rs.getInt("id_order"));
                orderHeaders.add(orderHeader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderHeaders;
    }
}
