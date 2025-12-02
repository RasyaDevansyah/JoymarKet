package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import controller.UserHandler;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Payload;
import model.Session;
import model.User;

public class ProfileView extends BorderPane {

    private UserHandler userHandler = new UserHandler();
    private TextField nameField, emailField, phoneField, addressField;
    private PasswordField passwordField;
    private Button saveButton;
    private Label titleLabel;

    public ProfileView() {
        initializeComponents();
        setupLayout();
        loadProfileData();
    }

    private void initializeComponents() {
        titleLabel = new Label("My Profile");
        titleLabel.setFont(new Font("Arial", 24));

        nameField = new TextField();
        emailField = new TextField();
        phoneField = new TextField();
        addressField = new TextField();
        passwordField = new PasswordField();

        saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> handleSaveChanges());
    }

    private void setupLayout() {
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(new Label("Full Name:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Email:"), 0, 1);
        formGrid.add(emailField, 1, 1);
        formGrid.add(new Label("Phone:"), 0, 2);
        formGrid.add(phoneField, 1, 2);
        formGrid.add(new Label("Address:"), 0, 3);
        formGrid.add(addressField, 1, 3);
        formGrid.add(new Label("Password:"), 0, 4);
        formGrid.add(passwordField, 1, 4);
        formGrid.add(saveButton, 1, 5);

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.getChildren().addAll(titleLabel, formGrid);
        
        setCenter(contentBox);
    }

    private void loadProfileData() {
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            nameField.setText(currentUser.getFullName());
            emailField.setText(currentUser.getEmail());
            phoneField.setText(currentUser.getPhone());
            addressField.setText(currentUser.getAddress());
        }
    }

    private void handleSaveChanges() {
        String fullName = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText(); // Keep current password if empty
        String phone = phoneField.getText();
        String address = addressField.getText();

        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Use current password if the field is empty
            if (password == null || password.trim().isEmpty()) {
                password = currentUser.getPassword();
            }

            Payload result = userHandler.EditProfile(fullName, email, password, phone, address);
            if (result.isSuccess()) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
                // Refresh the profile view to reflect the changes
                loadProfileData(); 
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", result.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
