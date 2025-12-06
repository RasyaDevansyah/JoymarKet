package view;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import main.Main;
import model.Session;
import model.User;

public class Navbar extends HBox {

    private Button productsButton;
    private Button topupButton;
    private Button cartButton;
    private Button orderHistoryButton;
    private Button registerButton;
    private Button usernameButton;

    // Admin specific buttons
    private Button addProductButton;
    private Button assignOrderButton;

    // Courier specific buttons
    private Button editDeliveryStatusButton;

    public Navbar() {
        this.setStyle("-fx-background-color: #d5d5d5ff; -fx-padding: 5 30 5 30;");
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

        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser == null) {
            setupLoggedOutView();
            return;
        }

        // Common button for all logged-in users
        usernameButton = new Button("Welcome, " + username);
        usernameButton.setOnAction(e -> changePageTo("Profile"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        setSpacing(10);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;"); // Red button for logout
        logoutButton.setOnAction(e -> Main.getInstance().handleLogout()); // Call handleLogout in Main

        // Add buttons based on user role
        switch (currentUser.getRole()) {
            case "CUSTOMER":
                productsButton = new Button("Products");
                topupButton = new Button("Top-up");
                cartButton = new Button("Cart");
                orderHistoryButton = new Button("Order History");

                productsButton.setOnAction(e -> changePageTo("Products"));
                topupButton.setOnAction(e -> changePageTo("Topup"));
                cartButton.setOnAction(e -> changePageTo("Cart"));
                orderHistoryButton.setOnAction(e -> changePageTo("OrderHistory"));

                getChildren().addAll(productsButton, spacer, topupButton, cartButton, orderHistoryButton,
                        usernameButton, logoutButton);
                break;
            case "ADMIN":
                productsButton = new Button("Products");
                addProductButton = new Button("Add Product");
                assignOrderButton = new Button("Assign Order");
                // Set actions for admin buttons (will need to implement these views later)
                productsButton.setOnAction(e -> changePageTo("Products"));
                addProductButton.setOnAction(e -> System.out.println("Add Product clicked")); // Placeholder
                assignOrderButton.setOnAction(e -> System.out.println("Assign Order clicked")); // Placeholder

                getChildren().addAll(productsButton, addProductButton, assignOrderButton, spacer, usernameButton,
                        logoutButton);
                break;
            case "COURIER":
                editDeliveryStatusButton = new Button("Edit Delivery Status");
                // Set action for courier button (will need to implement this view later)
                editDeliveryStatusButton.setOnAction(e -> System.out.println("Edit Delivery Status clicked")); // Placeholder

                getChildren().addAll(editDeliveryStatusButton, spacer, usernameButton, logoutButton);
                break;
            default:
                // Fallback for unknown roles, perhaps just the username button
                getChildren().addAll(spacer, usernameButton, logoutButton);
                break;
        }
    }
}
