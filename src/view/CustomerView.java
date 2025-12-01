package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CustomerView {
    private BorderPane bp;
    private Scene scene;
    private GridPane registrationForm;
    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;
    private TextField addressField;
    private Button registerButton;
    private Button cancelButton;
    private Label titleLabel;

    public CustomerView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        bp = new BorderPane();
        registrationForm = new GridPane();
        registrationForm.setAlignment(Pos.CENTER);
        registrationForm.setHgap(10);
        registrationForm.setVgap(10);
        registrationForm.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));

        titleLabel = new Label("Customer Registration");
        nameField = new TextField();
        nameField.setPromptText("Enter full name");
        emailField = new TextField();
        emailField.setPromptText("Enter email");
        phoneField = new TextField();
        phoneField.setPromptText("Enter phone number");
        addressField = new TextField();
        addressField.setPromptText("Enter address");
        registerButton = new Button("Register");
        cancelButton = new Button("Cancel");
    }

    private void setupLayout() {
        registrationForm.add(new Label("Name:"), 0, 1);
        registrationForm.add(nameField, 1, 1);
        registrationForm.add(new Label("Email:"), 0, 2);
        registrationForm.add(emailField, 1, 2);
        registrationForm.add(new Label("Phone:"), 0, 3);
        registrationForm.add(phoneField, 1, 3);
        registrationForm.add(new Label("Address:"), 0, 4);
        registrationForm.add(addressField, 1, 4);
        registrationForm.add(registerButton, 0, 5);
        registrationForm.add(cancelButton, 1, 5);

        bp.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        bp.setCenter(registrationForm);
        scene = new Scene(bp, 500, 500);
    }

    public Scene getScene() {
        return scene;
    }
}
