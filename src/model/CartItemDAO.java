package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.Connect;

public class CartItemDAO {

    private Connect connect = Connect.getInstance();

    public boolean addCartItem(String idCustomer, String idProduct, int count) {
        String sql = "INSERT INTO cart_items (id_customer, id_product, count) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE count = count + VALUES(count)";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, idCustomer);
            pstmt.setString(2, idProduct);
            pstmt.setInt(3, count);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
