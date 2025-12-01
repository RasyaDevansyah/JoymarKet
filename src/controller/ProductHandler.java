package controller;

import java.util.List;
import model.Product;
import model.ProductDAO;

public class ProductHandler {

    private ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
}
