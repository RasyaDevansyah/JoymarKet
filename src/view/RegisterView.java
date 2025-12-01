package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class RegisterView {

    private Scene scene;
    private BorderPane borderPane;
    private Label titleLabel;
    private TextField nameField, emailField, phoneField, addressField;
    private PasswordField passwordField;
    private Button registerButton, backButton;
    private GridPane formGrid;

    public RegisterView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        borderPane = new BorderPane();
        titleLabel = new Label("Register");
        titleLabel.setFont(new Font("Arial", 24));

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
        backButton = new Button("Back");

        formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
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
        formGrid.add(registerButton, 0, 5);
        formGrid.add(backButton, 1, 5);

        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        borderPane.setTop(titleLabel);
        borderPane.setCenter(formGrid);
        BorderPane.setMargin(titleLabel, new Insets(20));

        scene = new Scene(borderPane, 500, 600);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getBackButton() {
        return backButton;
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
