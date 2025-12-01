package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class WelcomeView {

    private Scene scene;
    private BorderPane borderPane;
    private Label titleLabel;
    private Button loginButton, registerButton;
    private VBox buttonContainer;

    public WelcomeView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        borderPane = new BorderPane();
        titleLabel = new Label("Welcome to JoymarKet");
        titleLabel.setFont(new Font("Arial", 24));

        loginButton = new Button("Login");
        registerButton = new Button("Register");

        buttonContainer = new VBox(10); // Spacing between buttons
        buttonContainer.getChildren().addAll(loginButton, registerButton);
        buttonContainer.setAlignment(Pos.CENTER);
    }

    private void setupLayout() {
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        borderPane.setTop(titleLabel);
        borderPane.setCenter(buttonContainer);
        BorderPane.setMargin(titleLabel, new Insets(20));

        scene = new Scene(borderPane, 500, 500);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}
