package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Navbar extends HBox {

    private Button productsButton;
    private Button topupButton;
    private Button cartButton;
    private Button orderHistoryButton;
    private Button registerButton;
    private Label usernameLabel;

    public Navbar() {
        // Initial state is logged out
        setupLoggedOutView();
    }

    private void setupLoggedOutView() {
        getChildren().clear();

        productsButton = new Button("Products");
        registerButton = new Button("Register");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        setSpacing(10);
        getChildren().addAll(productsButton, spacer, registerButton);
    }

    public void setupLoggedInView(String username) {
        getChildren().clear();

        productsButton = new Button("Products");
        topupButton = new Button("Top-up");
        cartButton = new Button("Cart");
        orderHistoryButton = new Button("Order History");
        usernameLabel = new Label("Welcome, " + username);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        setSpacing(10);
        getChildren().addAll(productsButton, spacer, topupButton, cartButton, orderHistoryButton, usernameLabel);
    }

    // Getters for all buttons
    public Button getProductsButton() {
        return productsButton;
    }

    public Button getTopupButton() {
        return topupButton;
    }

    public Button getCartButton() {
        return cartButton;
    }

    public Button getOrderHistoryButton() {
        return orderHistoryButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}
