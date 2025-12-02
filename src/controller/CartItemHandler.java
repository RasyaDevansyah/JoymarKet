package controller;

import model.CartItem;
import model.CartItemDAO;
import model.Session;

import java.util.List;
import model.Payload; // Import Payload
import java.util.ArrayList; // Import ArrayList

public class CartItemHandler {
    private CartItemDAO cartItemDAO = new CartItemDAO();
    private Session session = Session.getInstance();
    
    public Payload addProductToCart(String userId, String productId, int quantity) {
        if (userId == null || productId == null || quantity <= 0) {
            return new Payload("Invalid input for adding product to cart.", null, false);
        }
        if (cartItemDAO.addCartItem(userId, productId, quantity)) {
            return new Payload("Product added to cart successfully.", null, true);
        }
        return new Payload("Failed to add product to cart.", null, false);
    }

    public Payload getCartItems() {
        if (!session.isLoggedIn() || session.getCurrentUser() == null) {
            return new Payload("User not logged in.", new ArrayList<CartItem>(), false);
        }
        List<CartItem> items = cartItemDAO.getCartItemsByCustomerId(session.getCurrentUser().getIdUser());
        if (items != null) {
            return new Payload("Cart items retrieved successfully.", items, true);
        }
        return new Payload("Failed to retrieve cart items.", new ArrayList<CartItem>(), false);
    }

    public Payload updateCartItem(CartItem cartItem) {
        if (!session.isLoggedIn() || session.getCurrentUser() == null || cartItem == null) {
            return new Payload("User not logged in or invalid cart item.", null, false);
        }
        // Ensure the cart item belongs to the current user
        if (!cartItem.getIdCustomer().equals(session.getCurrentUser().getIdUser())) {
            return new Payload("Cart item does not belong to the current user.", null, false);
        }
        // If quantity is 0 or less, remove the item
        if (cartItem.getCount() <= 0) {
            if (cartItemDAO.deleteCartItem(cartItem.getIdCustomer(), cartItem.getProduct().getIdProduct())) {
                return new Payload("Cart item removed successfully (quantity was 0 or less).", null, true);
            }
            return new Payload("Failed to remove cart item.", null, false);
        } else {
            if (cartItemDAO.updateCartItemQuantity(cartItem.getIdCustomer(), cartItem.getProduct().getIdProduct(), cartItem.getCount())) {
                return new Payload("Cart item quantity updated successfully.", null, true);
            }
            return new Payload("Failed to update cart item quantity.", null, false);
        }
    }

    public Payload removeCartItem(CartItem cartItem) {
        if (!session.isLoggedIn() || session.getCurrentUser() == null || cartItem == null) {
            return new Payload("User not logged in or invalid cart item.", null, false);
        }
        if (!cartItem.getIdCustomer().equals(session.getCurrentUser().getIdUser())) {
            return new Payload("Cart item does not belong to the current user.", null, false);
        }
        if (cartItemDAO.deleteCartItem(cartItem.getIdCustomer(), cartItem.getProduct().getIdProduct())) {
            return new Payload("Cart item removed successfully.", null, true);
        }
        return new Payload("Failed to remove cart item.", null, false);
    }
}
