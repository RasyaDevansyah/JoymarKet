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
}
