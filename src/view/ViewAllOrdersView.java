package view;

import java.util.List;

import controller.OrderHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import model.Courier;
import model.CourierDAO;
import model.Customer;
import model.CustomerDAO;
import model.OrderHeader;
import model.Payload;

public class ViewAllOrdersView extends BorderPane {

    private TableView<OrderHeader> orderTable;
    private OrderHandler orderHandler;
    private CustomerDAO customerDAO;
    private CourierDAO courierDAO;

    public ViewAllOrdersView() {
        orderHandler = new OrderHandler();
        customerDAO = new CustomerDAO();
        courierDAO = new CourierDAO();

        Label title = new Label("All Orders");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        orderTable = new TableView<>();
        initializeTableColumns();
        loadOrderData();

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 0));
        contentBox.getChildren().addAll(title, orderTable);

        setCenter(contentBox);
    }

    private void initializeTableColumns() {
        TableColumn<OrderHeader, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdOrder()));

        TableColumn<OrderHeader, String> customerNameCol = new TableColumn<>("Customer Name");
        customerNameCol.setCellValueFactory(cellData -> {
            Customer customer = customerDAO.getCustomerById(cellData.getValue().getIdCustomer());
            return new SimpleStringProperty(customer != null ? customer.getFullName() : "None");
        });

        TableColumn<OrderHeader, String> courierNameCol = new TableColumn<>("Courier Name");
        courierNameCol.setCellValueFactory(cellData -> {
            String courierId = orderHandler.getCourierIdForOrder(cellData.getValue().getIdOrder());
            if (courierId != null) {
                Courier courier = courierDAO.getCourierById(courierId);
                return new SimpleStringProperty(courier != null ? courier.getFullName() : "None");
            }
            return new SimpleStringProperty("None");
        });

        TableColumn<OrderHeader, String> promoIdCol = new TableColumn<>("Promo ID");
        promoIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdPromo()));

        TableColumn<OrderHeader, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        TableColumn<OrderHeader, String> orderDateCol = new TableColumn<>("Order Date");
        orderDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderedAt().toString()));

        TableColumn<OrderHeader, Double> totalAmountCol = new TableColumn<>("Total Amount");
        totalAmountCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalAmount()));
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
            private final Button detailButton = new Button("View Details");
            private final Button assignButton = new Button("Assign Order");
            private final HBox pane = new HBox(5, detailButton, assignButton);

            {
                detailButton.setOnAction(event -> {
                    OrderHeader order = getTableView().getItems().get(getIndex());
                    if (order != null) {
                        Main.getInstance().changePageTo("OrderDetail", String.valueOf(order.getIdOrder()));
                    }
                });

                assignButton.setOnAction(event -> {
                    OrderHeader order = getTableView().getItems().get(getIndex());
                    if (order != null) {
                        Main.getInstance().changePageTo("AssignOrder", String.valueOf(order.getIdOrder()));
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

        orderTable.getColumns().addAll(idCol, customerNameCol, courierNameCol, promoIdCol, statusCol, orderDateCol, totalAmountCol, actionCol);
    }

    private void loadOrderData() {
        orderTable.getItems().clear();
        Payload payload = orderHandler.getAllOrderHeaders();
        if (payload.isSuccess() && payload.getData() instanceof List) {
            List<OrderHeader> orders = (List<OrderHeader>) payload.getData();
            orderTable.getItems().addAll(orders);
        } else {
            System.err.println("Error loading all order data: " + payload.getMessage());
        }
    }
}
