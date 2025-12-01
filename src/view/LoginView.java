package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import controller.UserHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink; // Import Hyperlink
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import main.Main;
import model.Payload;
import model.User;
import javafx.application.Platform;

public class LoginView extends BorderPane {

    private UserHandler userController = new UserHandler();

    private Label titleLabel, errorLabel, successLabel; // Add successLabel
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Hyperlink registerLink; // Add Hyperlink
    private GridPane formGrid;

    private static String registrationSuccessMessage = null; // Static field to hold the success message

    public LoginView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        titleLabel = new Label("Login");
        titleLabel.setFont(new Font("Arial", 24));

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        successLabel = new Label(); // Initialize successLabel
        successLabel.setStyle("-fx-text-fill: green;"); // Style the success message in green

        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        loginButton = new Button("Login");
        loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            saveDataUser();
        });

        registerLink = new Hyperlink("Don't have an account? Register"); // Initialize Hyperlink
        registerLink.setOnAction(e -> {
            Main.getInstance().changePageTo("Register");
        });

        formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        // Display success message if available
        if (registrationSuccessMessage != null) {
            Platform.runLater(() -> {
                successLabel.setText(registrationSuccessMessage);
                registrationSuccessMessage = null; // Clear the message after displaying
            });
        }
    }

    private void saveDataUser() {

        String email = getEmail();
        String password = getPassword();
        Payload result = userController.LoginCustomer(email, password);

        if (result.isSuccess()) {
            System.out.println("Login successful: " + result.getMessage());
            Main.getInstance().setCurrentUser((User) result.getData());
            Main.getInstance().changePageTo("Products");


        } else {
            errorLabel.setText("Login failed: " + result.getMessage());
            successLabel.setText("");
            System.out.println("Login failed: " + result.getMessage());
        }
    }

    private void setupLayout() {
        formGrid.add(new Label("Email:"), 0, 0);
        formGrid.add(emailField, 1, 0);
        formGrid.add(new Label("Password:"), 0, 1);
        formGrid.add(passwordField, 1, 1);
        formGrid.add(successLabel, 0, 2, 2, 1); // Add success label
        GridPane.setHalignment(successLabel, HPos.CENTER);
        formGrid.add(errorLabel, 0, 3, 2, 1); // Add error label
        GridPane.setHalignment(errorLabel, HPos.CENTER);
        formGrid.add(loginButton, 0, 4, 2, 1); // Span 2 columns
        GridPane.setHalignment(loginButton, HPos.CENTER);
        formGrid.add(registerLink, 0, 5, 2, 1); // Add register link
        GridPane.setHalignment(registerLink, HPos.CENTER);

        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        setTop(titleLabel);
        setCenter(formGrid);
        BorderPane.setMargin(titleLabel, new Insets(20));
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    // Static method to set the success message
    public static void setSuccessMessage(String message) {
        registrationSuccessMessage = message;
    }
}
