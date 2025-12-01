package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.Connect;

public class UserDAO {

    private Connect connect = Connect.getInstance();

    public boolean saveUser(String fullName, String password, String email, String phone, String address, String role) {
        // added role to SQL
        String sql = "INSERT INTO users (full_name, password, email, phone, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, role); // Set the role (e.g., "CUSTOMER")
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, email);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("id_user"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
