package model;

import util.Connect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDAO {

    private Connect connect = Connect.getInstance();

    public boolean createCustomer(String userId) {
        String sql = "INSERT INTO customers (id_user, balance) VALUES (?, 0.0)";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        return false;
        }
    }

    public boolean updateCustomerBalance(String customerId, double amount) {
        String sql = "UPDATE customers SET balance = balance + ? WHERE id_user = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, customerId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
