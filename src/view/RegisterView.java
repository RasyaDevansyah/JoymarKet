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
import javafx.scene.control.ComboBox;

public class RegisterView extends BorderPane {

    private Label titleLabel, errorLabel;
    private TextField nameField, emailField, phoneField, addressField;
    private PasswordField passwordField, confirmPasswordField;
    private ComboBox<String> genderComboBox;
    private Button registerButton;
    private Hyperlink loginLink;
    private GridPane formGrid;

    public RegisterView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        titleLabel = new Label("Register");
        titleLabel.setFont(new Font("Arial", 24));

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter a password");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        phoneField = new TextField();
        phoneField.setPromptText("Enter your phone number");
        addressField = new TextField();
        addressField.setPromptText("Enter your address");

        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female");
        genderComboBox.setPromptText("Select Gender");

        registerButton = new Button("Register");

        registerButton.setOnAction(e -> {
            register();
        });

        loginLink = new Hyperlink("Already have an account? Login");
        loginLink.setOnAction(e -> {
            Main.getInstance().changePageTo("Login");
        });

        formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
    }

    private void register() {
        errorLabel.setText("");

        UserHandler userController = new UserHandler();
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String gender = genderComboBox.getValue();

        Payload result = userController.SaveDataCustomer(name, email, password, confirmPassword, phone, address,
                gender);

        if (result.isSuccess()) {
            System.out.println("Registration successful: " + result.getMessage());

            LoginView.setSuccessMessage(result.getMessage());
            Main.getInstance().changePageTo("Login");
            errorLabel.setText("");
        } else {
            errorLabel.setText(result.getMessage());
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
        formGrid.add(new Label("Confirm Password:"), 0, 3);
        formGrid.add(confirmPasswordField, 1, 3);
        formGrid.add(new Label("Phone:"), 0, 4);
        formGrid.add(phoneField, 1, 4);
        formGrid.add(new Label("Address:"), 0, 5);
        formGrid.add(addressField, 1, 5);
        formGrid.add(new Label("Gender:"), 0, 6);
        formGrid.add(genderComboBox, 1, 6);
        formGrid.add(errorLabel, 0, 7, 2, 1);

        GridPane.setHalignment(errorLabel, HPos.CENTER);
        formGrid.add(registerButton, 0, 8, 2, 1);
        GridPane.setHalignment(registerButton, HPos.CENTER);
        formGrid.add(loginLink, 0, 9, 2, 1);
        GridPane.setHalignment(loginLink, HPos.CENTER);

        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        setTop(titleLabel);
        setCenter(formGrid);
        BorderPane.setMargin(titleLabel, new Insets(20));
    }

}
