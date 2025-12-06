package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<CartItem> getCartItemsByCustomerId(String customerId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT ci.id_customer, ci.id_product, ci.count, p.name as product_name, p.price, p.category " +
                "FROM cart_items ci JOIN products p ON ci.id_product = p.id_product " +
                "WHERE ci.id_customer = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, customerId);
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getString("id_product"),
                            rs.getString("product_name"),
                            rs.getDouble("price"),
                            0,
                            rs.getString("category"));
                    cartItems.add(new CartItem(
                            rs.getString("id_customer"),
                            product,
                            rs.getInt("count")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public boolean updateCartItemQuantity(String idCustomer, String idProduct, int count) {
        String sql = "UPDATE cart_items SET count = ? WHERE id_customer = ? AND id_product = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, count);
            pstmt.setString(2, idCustomer);
            pstmt.setString(3, idProduct);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCartItem(String idCustomer, String idProduct) {
        String sql = "DELETE FROM cart_items WHERE id_customer = ? AND id_product = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, idCustomer);
            pstmt.setString(2, idProduct);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CartItem> getAllCartItems(String customerId) {
        return getCartItemsByCustomerId(customerId);
    }

    public boolean clearCart(String idCustomer) {
        String sql = "DELETE FROM cart_items WHERE id_customer = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, idCustomer);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
