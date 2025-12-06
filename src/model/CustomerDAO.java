package model;

import util.Connect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public boolean updateBalance(String customerId, double amount) {
        String sql = "UPDATE customers SET balance = ? WHERE id_user = ?";
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

    public Customer getCustomerById(String idUser) {
        String sql = "SELECT u.id_user, u.password, u.full_name, u.email, u.phone, u.address, c.balance FROM users u JOIN customers c ON u.id_user = c.id_user WHERE u.id_user = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, idUser);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userId = rs.getString("id_user");
                String password = rs.getString("password");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                double balance = rs.getDouble("balance");

                return new Customer(userId, fullName, email, password, phone, address, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getCustomerBalance(String idUser) {
        String sql = "SELECT balance FROM customers WHERE id_user = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, idUser);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
