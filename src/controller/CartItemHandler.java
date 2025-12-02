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

    public boolean updateCartItem(CartItem cartItem) {
        if (!session.isLoggedIn() || session.getCurrentUser() == null || cartItem == null) {
            return false;
        }
        // Ensure the cart item belongs to the current user
        if (!cartItem.getIdCustomer().equals(session.getCurrentUser().getIdUser())) {
            return false;
        }
        // If quantity is 0 or less, remove the item
        if (cartItem.getCount() <= 0) {
            return cartItemDAO.deleteCartItem(cartItem.getIdCustomer(), cartItem.getProduct().getIdProduct());
        } else {
            return cartItemDAO.updateCartItemQuantity(cartItem.getIdCustomer(), cartItem.getProduct().getIdProduct(), cartItem.getCount());
        }
    }

    public boolean removeCartItem(CartItem cartItem) {
        if (!session.isLoggedIn() || session.getCurrentUser() == null || cartItem == null) {
            return false;
        }
        if (!cartItem.getIdCustomer().equals(session.getCurrentUser().getIdUser())) {
            return false;
        }
        return cartItemDAO.deleteCartItem(cartItem.getIdCustomer(), cartItem.getProduct().getIdProduct());
    }
}
