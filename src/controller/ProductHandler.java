package controller;

import java.util.List;
import model.Product;
import model.ProductDAO;
import model.Payload;
import java.util.ArrayList;

public class ProductHandler {

    private ProductDAO productDAO = new ProductDAO();

    public Payload getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        if (products != null) {
            return new Payload("Products retrieved successfully.", products, true);
        }
        return new Payload("Failed to retrieve products.", new ArrayList<Product>(), false);
    }

    public Payload getProductById(String productId) {
        Product product = productDAO.getProductById(productId);
        if (product != null) {
            return new Payload("Product retrieved successfully.", product, true);
        }
        return new Payload("Failed to retrieve product.", null, false);
    }

    public Payload updateProduct(String productId, String name, double price, int stock, String category) {
        // Validate product ID
        if (productId == null || productId.trim().isEmpty()) {
            return new Payload("Product ID cannot be empty", null, false);
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            return new Payload("Product name cannot be empty", null, false);
        }

        // Validate price
        if (price < 0) {
            return new Payload("Price cannot be negative", null, false);
        }

        // Validate stock
        if (stock < 0) {
            return new Payload("Stock cannot be negative", null, false);
        }

        // Validate category
        if (category == null || category.trim().isEmpty()) {
            return new Payload("Category cannot be empty", null, false);
        }

        Product product = new Product(productId, name, price, stock, category);

        if (productDAO.updateProduct(product)) {
            return new Payload("Product updated successfully.", null, true);
        }
        return new Payload("Failed to update product.", null, false);
    }
}
