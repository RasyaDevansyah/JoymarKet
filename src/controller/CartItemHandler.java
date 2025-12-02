package controller;

import model.CartItem;
import model.CartItemDAO;
import model.Session;

import java.util.List;

public class CartItemHandler {
    private CartItemDAO cartItemDAO = new CartItemDAO();
    private Session session = Session.getInstance();
    
    public boolean addProductToCart(String userId, String productId, int quantity) {
        if (userId == null || productId == null || quantity <= 0) {
            return false;
        }
        return cartItemDAO.addCartItem(userId, productId, quantity);
    }

    public List<CartItem> getCartItems() {
        if (!session.isLoggedIn() || session.getCurrentUser() == null) {
            return new java.util.ArrayList<>();
        }
        return cartItemDAO.getCartItemsByCustomerId(session.getCurrentUser().getIdUser());
    }
}
