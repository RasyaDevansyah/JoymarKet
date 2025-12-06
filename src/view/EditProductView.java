package view;

import controller.ProductHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Main;
import model.Product;
import model.Payload;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class EditProductView extends BorderPane {

    private ProductHandler productHandler;
    private Product product;
    private TextField nameField;
    private TextField priceField;
    private TextField stockField;
    private TextField categoryField;

    public EditProductView(String productId) {
        productHandler = new ProductHandler();
        loadProductData(productId);

        Label title = new Label("Edit Product");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        VBox form = createForm();

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 0));
        contentBox.getChildren().addAll(title, form);

        setCenter(contentBox);
    }

    private VBox createForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER);

        nameField = new TextField();
        priceField = new TextField();
        stockField = new TextField();
        categoryField = new TextField();

        if (product != null) {
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            stockField.setText(String.valueOf(product.getStock()));
            categoryField.setText(product.getCategory());
        }

        Button updateButton = new Button("Update Product");
        updateButton.setOnAction(e -> handleUpdate());

        form.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Price:"), priceField,
                new Label("Stock:"), stockField,
                new Label("Category:"), categoryField,
                updateButton
        );
        return form;
    }

    private void loadProductData(String productId) {
        Payload payload = productHandler.getProductById(productId);
        if (payload.isSuccess() && payload.getData() instanceof Product) {
            this.product = (Product) payload.getData();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to load product details.");
            Main.getInstance().changePageTo("Products");
        }
    }

    private void handleUpdate() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        String category = categoryField.getText();

        Payload payload = productHandler.updateProduct(product.getIdProduct(), name, price, stock, category);
        if (payload.isSuccess()) {
            showAlert(AlertType.INFORMATION, "Success", "Product updated successfully!");
            Main.getInstance().changePageTo("Products");
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to update product: " + payload.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
