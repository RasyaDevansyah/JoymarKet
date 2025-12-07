package view;

import controller.CourierHandler;
import controller.OrderHandler;
import controller.UserHandler;
import controller.DeliveryHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Main;
import model.Courier;
import model.OrderHeader;
import model.Payload;

import java.util.List;

public class AssignOrderView extends BorderPane {

    private int orderId;
    private OrderHandler orderHandler;
    private CourierHandler courierHandler;
    private DeliveryHandler deliveryHandler;
    private UserHandler userHandler;
    private Label orderIdLabel;
    private Label customerNameLabel;
    private Label totalAmountLabel;
    private ComboBox<Courier> courierComboBox;
    private Button assignButton;

    public AssignOrderView(int orderId) {
        this.orderId = orderId;
        this.orderHandler = new OrderHandler();
        this.courierHandler = new CourierHandler();
        this.deliveryHandler = new DeliveryHandler();
        this.userHandler = new UserHandler();

        initializeComponents();
        loadOrderDetails();
        loadCouriers();
    }

    private void initializeComponents() {
        Label title = new Label("Assign Courier to Order");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        orderIdLabel = new Label("Order ID: ");
        customerNameLabel = new Label("Customer Name: ");
        totalAmountLabel = new Label("Total Amount: ");
        courierComboBox = new ComboBox<>();
        courierComboBox.setPromptText("Select Courier");
        assignButton = new Button("Assign Courier");
        assignButton.setOnAction(e -> handleAssignCourier());

        GridPane formPane = new GridPane();
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setPadding(new Insets(20));
        formPane.addRow(0, new Label("Order ID:"), orderIdLabel);
        formPane.addRow(1, new Label("Customer Name:"), customerNameLabel);
        formPane.addRow(2, new Label("Total Amount:"), totalAmountLabel);
        formPane.addRow(3, new Label("Select Courier:"), courierComboBox);
        formPane.add(assignButton, 1, 4);

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 0));
        contentBox.getChildren().addAll(title, formPane);

        setCenter(contentBox);
    }

    private void loadOrderDetails() {
        Payload payload = orderHandler.getOrderHeaderById(orderId);
        if (payload.isSuccess() && payload.getData() instanceof OrderHeader) {
            OrderHeader order = (OrderHeader) payload.getData();

            orderIdLabel.setText(String.valueOf(order.getIdOrder()));

            String id = order.getIdCustomer().toString();
            Payload customerPayload = userHandler.GetUser(id);

            if (customerPayload.isSuccess() && customerPayload.getData() instanceof model.Customer) {
                model.Customer customer = (model.Customer) customerPayload.getData();
                customerNameLabel.setText(customer.getFullName());
            } else {
                customerNameLabel.setText("Unknown Customer");
            }

            totalAmountLabel.setText(String.format("Rp %.2f", order.getTotalAmount()));
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to load order details: " + payload.getMessage());
            Main.getInstance().changePageTo("ViewAllOrders");
        }
    }

    private void loadCouriers() {
        Payload payload = courierHandler.getAllCouriers();
        if (payload.isSuccess() && payload.getData() instanceof List) {
            @SuppressWarnings("unchecked")
            List<Courier> couriers = (List<Courier>) payload.getData();
            ObservableList<Courier> observableCouriers = FXCollections.observableArrayList(couriers);
            courierComboBox.setItems(observableCouriers);
            courierComboBox.setConverter(new javafx.util.StringConverter<Courier>() {
                @Override
                public String toString(Courier courier) {
                    return courier.getFullName();
                }

                @Override
                public Courier fromString(String string) {
                    return null; // Not needed for this use case
                }
            });
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to load couriers: " + payload.getMessage());
        }
    }

    private void handleAssignCourier() {
        Courier selectedCourier = courierComboBox.getSelectionModel().getSelectedItem();
        if (selectedCourier == null) {
            showAlert(AlertType.WARNING, "Warning", "Please select a courier.");
            return;
        }

        // Assign courier to order (implement this method in DeliveryHandler)
        Payload payload = deliveryHandler.assignOrderToCourier(orderId, selectedCourier.getIdUser());
        if (payload.isSuccess()) {
            showAlert(AlertType.INFORMATION, "Success", "Order assigned successfully!");
            Main.getInstance().changePageTo("ViewAllOrders");
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to assign order: " + payload.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
