package view;

import controller.UserHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import main.Main;
import model.Payload;
import model.Session; // Import Session

public class RegisterView extends BorderPane {

    private Label titleLabel, errorLabel;
    private TextField nameField, emailField, phoneField, addressField;
    private PasswordField passwordField;
    private Button registerButton;
    private Hyperlink loginLink; // Add Hyperlink
    private GridPane formGrid;

    public RegisterView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        titleLabel = new Label("Register");
        titleLabel.setFont(new Font("Arial", 24));

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;"); // Style the error message in red

        nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter a password");
        phoneField = new TextField();
        phoneField.setPromptText("Enter your phone number");
        addressField = new TextField();
        addressField.setPromptText("Enter your address");

        registerButton = new Button("Register");

        registerButton.setOnAction(e -> {
            register();
        });

        loginLink = new Hyperlink("Already have an account? Login"); // Initialize Hyperlink
        loginLink.setOnAction(e -> {
            Main.getInstance().changePageTo("Login");
        });

        formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
    }

    private void register() {
        errorLabel.setText(""); // Clear previous errors

        UserHandler userController = new UserHandler();
        String name = getName();
        String email = getEmail();
        String password = getPassword();
        String phone = getPhone();
        String address = getAddress();

        Payload result = userController.SaveDataUser(name, email, password, phone, address);

        if (result.isSuccess()) {
            // User is already set in Session by UserHandler
            System.out.println("Registration successful: " + result.getMessage());
            // Set success message in LoginView before changing page
            LoginView.setSuccessMessage(result.getMessage());
            Main.getInstance().changePageTo("Login");
            errorLabel.setText("");
        } else {
            errorLabel.setText(result.getMessage()); // Display the error message
            System.out.println("Registration failed: " + result.getMessage());
        }

    }

    private void setupLayout() {
        formGrid.add(new Label("Name:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Email:"), 0, 1);
        formGrid.add(emailField, 1, 1);
        formGrid.add(new Label("Password:"), 0, 2);
        formGrid.add(passwordField, 1, 2);
        formGrid.add(new Label("Phone:"), 0, 3);
        formGrid.add(phoneField, 1, 3);
        formGrid.add(new Label("Address:"), 0, 4);
        formGrid.add(addressField, 1, 4);
        formGrid.add(errorLabel, 0, 5, 2, 1); // Add error label spanning 2 columns
        GridPane.setHalignment(errorLabel, HPos.CENTER);
        formGrid.add(registerButton, 0, 6, 2, 1); // Span 2 columns, move to next row
        GridPane.setHalignment(registerButton, HPos.CENTER);
        formGrid.add(loginLink, 0, 7, 2, 1); // Add login link below register button
        GridPane.setHalignment(loginLink, HPos.CENTER);

        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        setTop(titleLabel);
        setCenter(formGrid);
        BorderPane.setMargin(titleLabel, new Insets(20));
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public String getName() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getPhone() {
        return phoneField.getText();
    }

    public String getAddress() {
        return addressField.getText();
    }

}
