package controller;

import model.CartItemDAO;
import model.User;

public class CartItemHandler {
    private CartItemDAO cartItemDAO = new CartItemDAO();

    public boolean addProductToCart(String userId, String productId, int quantity) {
        if (userId == null || productId == null || quantity <= 0) {
            return false;
        }
        return cartItemDAO.addCartItem(userId, productId, quantity);
    }
}
