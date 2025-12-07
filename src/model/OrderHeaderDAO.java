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

    public List<OrderHeader> getCustomerOrderHeaders(String customerId) {
        List<OrderHeader> orderHeaders = new java.util.ArrayList<>();
        String sql = "SELECT oh.id_order, oh.id_customer, oh.id_promo, p.code AS promo_code, p.headline AS promo_headline, oh.status, oh.ordered_at, oh.total_amount "
                +
                "FROM order_headers oh LEFT JOIN promos p ON oh.id_promo = p.id_promo WHERE oh.id_customer = ? ORDER BY oh.ordered_at DESC";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderHeader orderHeader = new OrderHeader(
                            rs.getInt("id_order"),
                            rs.getString("id_customer"),
                            rs.getString("id_promo"),
                            rs.getString("promo_code"),
                            rs.getString("promo_headline"),
                            rs.getString("status"),
                            rs.getDate("ordered_at"),
                            rs.getDouble("total_amount"));
                    orderHeaders.add(orderHeader);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderHeaders;
    }

    public OrderHeader getCustomerOrderHeader(int orderId) {
        String sql = "SELECT oh.id_order, oh.id_customer, oh.id_promo, p.code AS promo_code, p.headline AS promo_headline, oh.status, oh.ordered_at, oh.total_amount "
                +
                "FROM order_headers oh LEFT JOIN promos p ON oh.id_promo = p.id_promo WHERE oh.id_order = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    OrderHeader orderHeader = new OrderHeader(
                            rs.getInt("id_order"),
                            rs.getString("id_customer"),
                            rs.getString("id_promo"),
                            rs.getString("promo_code"),
                            rs.getString("promo_headline"),
                            rs.getString("status"),
                            rs.getDate("ordered_at"),
                            rs.getDouble("total_amount"));
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
        String sql = "SELECT oh.id_order, oh.id_customer, oh.id_promo, p.code AS promo_code, p.headline AS promo_headline, oh.status, oh.ordered_at, oh.total_amount "
                +
                "FROM order_headers oh LEFT JOIN promos p ON oh.id_promo = p.id_promo ORDER BY oh.ordered_at DESC";
        try (PreparedStatement pstmt = connect.preparedStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                OrderHeader orderHeader = new OrderHeader(
                        rs.getInt("id_order"),
                        rs.getString("id_customer"),
                        rs.getString("id_promo"),
                        rs.getString("promo_code"),
                        rs.getString("promo_headline"),
                        rs.getString("status"),
                        rs.getDate("ordered_at"),
                        rs.getDouble("total_amount"));
                orderHeaders.add(orderHeader);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return orderHeaders;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE order_headers SET status = ? WHERE id_order = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        }

        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
