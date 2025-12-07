package controller;

import java.sql.Date;
import java.util.List;

import model.CartItem;
import model.CartItemDAO;
import model.Customer;
import model.CustomerDAO;
import model.Delivery;
import model.DeliveryDAO;
import model.OrderDetail;
import model.OrderDetailDAO;
import model.OrderHeader;
import model.OrderHeaderDAO;
import model.Payload;
import model.Product;
import model.ProductDAO;
import model.PromoDAO;
import model.User;

public class OrderHandler {

    OrderHeaderDAO orderHeaderDAO = new OrderHeaderDAO();
    OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    ProductDAO productDAO = new ProductDAO();
    DeliveryDAO deliveryDAO = new DeliveryDAO(); // Add DeliveryDAO
    PromoDAO promoDAO = new PromoDAO(); // Add PromoDAO

    public Payload processCheckout(double totalAmount, User user, String promoId) {
        Payload validationPayload = validateCheckout(totalAmount, user);
        if (!validationPayload.isSuccess()) {
            return validationPayload;
        }

        Customer customer = (Customer) user;
        CustomerDAO customerDAO = new CustomerDAO();
        CartItemDAO cartItemDAO = new CartItemDAO();

        // Stock validation
        List<CartItem> cartItems = cartItemDAO.getAllCartItems(customer.getIdUser());
        for (CartItem item : cartItems) {
            Product product = productDAO.getProductById(item.getProduct().getIdProduct());
            if (product.getStock() < item.getCount()) {
                return new Payload("Insufficient stock for " + product.getName(), null, false);
            }
        }

        // Create OrderHeader
        Date orderDate = new Date(System.currentTimeMillis());
        // Pass promoId to OrderHeader constructor
        OrderHeader orderHeader = new OrderHeader(customer.getIdUser(), promoId, "Pending", orderDate, totalAmount);
        int orderId = orderHeaderDAO.insert(orderHeader);

        if (orderId != -1) {
            // Create OrderDetails and update stock
            for (CartItem item : cartItems) {
                OrderDetail orderDetail = new OrderDetail(orderId, item.getProduct().getIdProduct(), item.getCount());
                orderDetailDAO.insert(orderDetail);
                productDAO.updateStock(item.getProduct().getIdProduct(), item.getCount());
            }

            // Mark promo as used if applied
            if (promoId != null) {
                promoDAO.markPromoAsUsed(customer.getIdUser(), promoId);
            }
        }

        // Clear cart
        cartItemDAO.clearCart(customer.getIdUser());

        // Reduce balance
        double newBalance = customer.getBalance() - totalAmount;
        customer.setBalance(newBalance);
        customerDAO.updateBalance(customer.getIdUser(), newBalance);

        return new Payload("Checkout successful.", null, true);
    }

    public Payload validateCheckout(double totalAmount, User user) {
        if (!(user instanceof Customer)) {
            return new Payload("Only customers can perform checkout.", null, false);
        }

        Customer customer = (Customer) user;
        // Retrieve the latest customer data to ensure balance is up-to-date
        CustomerDAO customerDAO = new CustomerDAO();
        Customer updatedCustomer = customerDAO.getCustomerById(customer.getIdUser());

        if (updatedCustomer == null) {
            return new Payload("Customer data not found.", null, false);
        }

        if (updatedCustomer.getBalance() < totalAmount) {
            return new Payload(
                    "Insufficient balance. Current balance: Rp " + String.format("%.2f", updatedCustomer.getBalance()),
                    null, false);
        }

        return new Payload("Balance is sufficient.", null, true);

    }

    public Payload getCustomerOrderHeaders(String customerId) {
        List<OrderHeader> orderHeaders = orderHeaderDAO.getCustomerOrderHeaders(customerId);
        if (orderHeaders != null) {
            return new Payload("Order headers retrieved successfully.", orderHeaders, true);
        }
        return new Payload("Failed to retrieve order headers.", null, false);
    }

    public Payload getCustomerOrderHeader(int orderId) {
        OrderHeader orderHeader = orderHeaderDAO.getCustomerOrderHeader(orderId);
        if (orderHeader != null) {
            return new Payload("Order header retrieved successfully.", orderHeader, true);
        }
        return new Payload("Failed to retrieve order header.", null, false);
    }

    public Payload getCustomerOrderDetails(int orderId) {
        List<OrderDetail> orderDetails = orderDetailDAO.getCustomerOrderDetails(orderId);
        if (orderDetails != null) {
            return new Payload("Order details retrieved successfully.", orderDetails, true);
        }
        return new Payload("Failed to retrieve order details.", null, false);
    }

    public Payload getAllOrderHeaders() {
        List<OrderHeader> orderHeaders = orderHeaderDAO.getAllOrderHeaders();
        if (orderHeaders != null) {
            return new Payload("All order headers retrieved successfully.", orderHeaders, true);
        }
        return new Payload("Failed to retrieve all order headers.", null, false);
    }

    public String getCourierIdForOrder(int orderId) {
        Delivery delivery = deliveryDAO.getDeliveryByOrderId(orderId);
        if (delivery != null) {
            return delivery.getIdCourier();
        }
        return null;
    }

    public Payload updateOrderStatus(int orderId, String newStatus) {
        if (orderHeaderDAO.updateOrderStatus(orderId, newStatus)) {
            return new Payload("Order status updated successfully.", null, true);
        }
        return new Payload("Failed to update order status.", null, false);
    }

    public String getNextOrderStatus(String currentStatus) {
        if (currentStatus.equals("Pending")) {
            return "In Progress";
        } else if (currentStatus.equals("In Progress")) {
            return "Delivered";
        }
        return currentStatus; // If already Delivered or unknown, return current status
    }
}
