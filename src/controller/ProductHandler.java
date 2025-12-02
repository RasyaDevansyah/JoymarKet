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
}
