package view;

import java.util.List;
import java.util.ArrayList;

import controller.DeliveryHandler;
import controller.OrderHandler;
import controller.UserHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Main;
import model.Delivery;
import model.OrderHeader;
import model.Payload;
import model.Session;
import model.User;
import javafx.scene.control.Alert.AlertType;

public class DeliveriesView extends BorderPane {

    private TableView<OrderHeader> deliveryTable;
    private OrderHandler orderHandler;
    private DeliveryHandler deliveryHandler;
    private UserHandler userHandler;
    private User currentUser;

    public DeliveriesView() {
        orderHandler = new OrderHandler();
        deliveryHandler = new DeliveryHandler();
        userHandler = new UserHandler();
        currentUser = Session.getInstance().getCurrentUser();

        Label title = new Label("My Deliveries");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        deliveryTable = new TableView<>();
        initializeTableColumns();
        loadDeliveryData();

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 0));
        contentBox.getChildren().addAll(title, deliveryTable);

        setCenter(contentBox);
    }

    private void initializeTableColumns() {
        TableColumn<OrderHeader, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdOrder()));

        TableColumn<OrderHeader, String> customerIdCol = new TableColumn<>("Customer ID");
        customerIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdCustomer()));

        TableColumn<OrderHeader, String> customerNameCol = new TableColumn<>("Customer Name");
        customerNameCol.setCellValueFactory(cellData -> {
            Payload payload = userHandler.getUser(cellData.getValue().getIdCustomer());
            User user = (payload.isSuccess() && payload.getData() instanceof User) ? (User) payload.getData() : null;
            return new SimpleStringProperty(user != null ? user.getFullName() : "N/A");
        });

        TableColumn<OrderHeader, String> customerEmailCol = new TableColumn<>("Customer Email");
        customerEmailCol.setCellValueFactory(cellData -> {
            Payload payload = userHandler.getUser(cellData.getValue().getIdCustomer());
            User user = (payload.isSuccess() && payload.getData() instanceof User) ? (User) payload.getData() : null;
            return new SimpleStringProperty(user != null ? user.getEmail() : "N/A");
        });

        TableColumn<OrderHeader, String> customerPhoneCol = new TableColumn<>("Customer Phone");
        customerPhoneCol.setCellValueFactory(cellData -> {
            Payload payload = userHandler.getUser(cellData.getValue().getIdCustomer());
            User user = (payload.isSuccess() && payload.getData() instanceof User) ? (User) payload.getData() : null;
            return new SimpleStringProperty(user != null ? user.getPhone() : "N/A");
        });

        TableColumn<OrderHeader, String> customerAddressCol = new TableColumn<>("Customer Address");
        customerAddressCol.setCellValueFactory(cellData -> {
            Payload payload = userHandler.getUser(cellData.getValue().getIdCustomer());
            User user = (payload.isSuccess() && payload.getData() instanceof User) ? (User) payload.getData() : null;
            return new SimpleStringProperty(user != null ? user.getAddress() : "N/A");
        });

        TableColumn<OrderHeader, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        TableColumn<OrderHeader, String> orderDateCol = new TableColumn<>("Order Date");
        orderDateCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getOrderedAt().toString()));

        TableColumn<OrderHeader, Double> totalAmountCol = new TableColumn<>("Total Amount");
        totalAmountCol
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalAmount()));
        totalAmountCol.setCellFactory(column -> new TableCell<OrderHeader, Double>() {
            @Override
            protected void updateItem(Double totalAmount, boolean empty) {
                super.updateItem(totalAmount, empty);
                if (empty || totalAmount == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp %.2f", totalAmount));
                }
            }
        });

        TableColumn<OrderHeader, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<OrderHeader, Void>() {
            private final Button updateStatusButton = new Button("Update Status");
            private final Button viewDetailsButton = new Button("View Details");
            private final HBox pane = new HBox(5, updateStatusButton, viewDetailsButton);

            {
                updateStatusButton.setOnAction(event -> {
                    OrderHeader order = getTableView().getItems().get(getIndex());
                    if (order != null) {
                        String currentStatus = order.getStatus();
                        String newStatus = "Pending";
                        if (currentStatus.equals("Pending")) {
                            newStatus = "In Progress";
                        } else if (currentStatus.equals("In Progress")) {
                            newStatus = "Delivered";
                        } else if (currentStatus.equals("Delivered")) {
                            showAlert(AlertType.INFORMATION, "Info", "Order is already delivered.");
                            return;
                        }
                        Payload payload = orderHandler.updateOrderStatus(order.getIdOrder(), newStatus);
                        if (payload.isSuccess()) {
                            showAlert(AlertType.INFORMATION, "Success", "Order status updated to " + newStatus);
                            loadDeliveryData(); // Refresh table
                        } else {
                            showAlert(AlertType.ERROR, "Error", "Failed to update status: " + payload.getMessage());
                        }
                    }
                });

                viewDetailsButton.setOnAction(event -> {
                    OrderHeader order = getTableView().getItems().get(getIndex());
                    if (order != null) {
                        Main.getInstance().changePageTo("OrderDetail", String.valueOf(order.getIdOrder()));
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        deliveryTable.getColumns().addAll(idCol, customerIdCol, customerNameCol, customerEmailCol, customerPhoneCol,
                customerAddressCol, statusCol, orderDateCol, totalAmountCol, actionCol);
    }

    private void loadDeliveryData() {
        deliveryTable.getItems().clear();
        if (currentUser != null && currentUser.getRole().equals("COURIER")) {
            Payload payload = deliveryHandler.getDeliveriesByCourierId(currentUser.getIdUser());
            if (payload.isSuccess() && payload.getData() instanceof List) {
                List<Delivery> deliveries = (List<Delivery>) payload.getData();
                List<OrderHeader> orders = new ArrayList<>();
                for (Delivery delivery : deliveries) {
                    Payload orderPayload = orderHandler.getCustomerOrderHeader(Integer.parseInt(delivery.getIdOrder()));
                    if (orderPayload.isSuccess() && orderPayload.getData() instanceof OrderHeader) {
                        OrderHeader orderHeader = (OrderHeader) orderPayload.getData();
                        orders.add(orderHeader);
                    }
                }
                deliveryTable.getItems().addAll(orders);
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to load deliveries: " + payload.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "Access Denied", "Only couriers can view deliveries.");
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
