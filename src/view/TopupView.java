package view;

import controller.UserHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Main;
import model.Customer;
import model.Payload;
import model.Session;
import model.User;

public class TopupView extends BorderPane {

    private UserHandler userHandler = new UserHandler();
    private Label titleLabel;
    private Label currentBalanceLabel;
    private TextField amountField;
    private Button topupButton;

    public TopupView() {
        initializeComponents();
        setupLayout();
        loadBalanceData();
    }

    private void initializeComponents() {
        titleLabel = new Label("Top-up Balance");
        titleLabel.setFont(new Font("Arial", 24));

        currentBalanceLabel = new Label("Current Balance: Rp 0.00");
        currentBalanceLabel.setFont(new Font("Arial", 18));

        amountField = new TextField();
        amountField.setPromptText("Enter top-up amount");

        topupButton = new Button("Top Up");
        topupButton.setOnAction(e -> handleTopUp());
    }

    private void setupLayout() {
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(new Label("Amount:"), 0, 0);
        formGrid.add(amountField, 1, 0);

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.getChildren().addAll(titleLabel, currentBalanceLabel, formGrid, topupButton);

        setCenter(contentBox);
    }

    private void loadBalanceData() {
        User currentUser = Session.getInstance().getCurrentUser();

        if (currentUser != null) {
            System.out.println("Current User: " + currentUser.getRole());
            if (currentUser.getRole().equals("CUSTOMER")) {
                Customer customer = (Customer) currentUser;
                currentBalanceLabel.setText(String.format("Current Balance: Rp %.2f", customer.getBalance()));
            }

            else {
                currentBalanceLabel.setText("Current Balance: N/A (Login as Customer)");
                amountField.setDisable(true);
                topupButton.setDisable(true);
            }
        }
    }

    private void handleTopUp() {
        User currentUser = Session.getInstance().getCurrentUser();
        if (!(currentUser instanceof Customer)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Only customers can top-up balance.");
            return;
        }

        String amountText = amountField.getText();

        // Call TopUpBalance with amountText (string)
        Payload result = userHandler.topUpBalance(currentUser.getIdUser(), amountText);

        if (result.isSuccess()) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Top-up successful!");
            loadBalanceData();
            Main.getInstance().refreshNavbar();
            amountField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", result.getMessage());
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
