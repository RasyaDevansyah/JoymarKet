package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import main.Main;

public class Navbar extends HBox {

    private Button productsButton;
    private Button topupButton;
    private Button cartButton;
    private Button orderHistoryButton;
    private Button registerButton;
    private Label usernameLabel;

    public Navbar() {
        setupLoggedOutView();
    }

    // Helper method to keep code clean
    public void changePageTo(String pageName) {
        Main.getInstance().changePageTo(pageName);
    }

    public void setupLoggedOutView() {
        getChildren().clear();

        // 1. Initialize the buttons specifically for this view
        productsButton = new Button("Products");
        registerButton = new Button("Register");

        // 2. Set the actions immediately after creating them
        productsButton.setOnAction(e -> changePageTo("Products"));
        registerButton.setOnAction(e -> changePageTo("Register"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        setSpacing(10);
        getChildren().addAll(productsButton, spacer, registerButton);
    }

    public void setupLoggedInView(String username) {
        getChildren().clear();

        // 1. Initialize the buttons specifically for this view
        productsButton = new Button("Products");
        topupButton = new Button("Top-up");
        cartButton = new Button("Cart");
        orderHistoryButton = new Button("Order History");
        usernameLabel = new Label("Welcome, " + username);

        // 2. Set the actions immediately after creating them
        productsButton.setOnAction(e -> changePageTo("Products"));
        topupButton.setOnAction(e -> changePageTo("Topup"));
        cartButton.setOnAction(e -> changePageTo("Cart"));
        orderHistoryButton.setOnAction(e -> changePageTo("OrderHistory"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        setSpacing(10);
        getChildren().addAll(productsButton, spacer, topupButton, cartButton, orderHistoryButton, usernameLabel);
    }
}