package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import controller.UserHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import main.Main;
import model.Payload;

public class LoginView extends BorderPane {

    private UserHandler userController = new UserHandler();

    private Label titleLabel;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private GridPane formGrid;

    public LoginView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        titleLabel = new Label("Login");
        titleLabel.setFont(new Font("Arial", 24));

        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            saveDataUser();
        });

        formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
    }

    private void saveDataUser() {

        String email = getEmail();
        String password = getPassword();
        Payload result = userController.LoginCustomer(email, password);

        if (result.isSuccess()) {
            System.out.println("Login successful: " + result.getMessage());
            Main.getInstance().changePageTo("Products");

        } else {
            System.out.println("Login failed: " + result.getMessage());
        }
    }

    private void setupLayout() {
        formGrid.add(new Label("Email:"), 0, 0);
        formGrid.add(emailField, 1, 0);
        formGrid.add(new Label("Password:"), 0, 1);
        formGrid.add(passwordField, 1, 1);
        formGrid.add(loginButton, 0, 2, 2, 1); // Span 2 columns
        GridPane.setHalignment(loginButton, HPos.CENTER);

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
}
