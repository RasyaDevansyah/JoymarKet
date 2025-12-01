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

public class LoginView {

    private Scene scene;
    private BorderPane borderPane;
    private Label titleLabel;
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton, backButton;
    private GridPane formGrid;

    public LoginView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        borderPane = new BorderPane();
        titleLabel = new Label("Login");
        titleLabel.setFont(new Font("Arial", 24));

        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        loginButton = new Button("Login");
        backButton = new Button("Back");

        formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
    }

    private void setupLayout() {
        formGrid.add(new Label("Email:"), 0, 0);
        formGrid.add(emailField, 1, 0);
        formGrid.add(new Label("Password:"), 0, 1);
        formGrid.add(passwordField, 1, 1);
        formGrid.add(loginButton, 0, 2);
        formGrid.add(backButton, 1, 2);

        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        borderPane.setTop(titleLabel);
        borderPane.setCenter(formGrid);
        BorderPane.setMargin(titleLabel, new Insets(20));

        scene = new Scene(borderPane, 500, 500);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}
