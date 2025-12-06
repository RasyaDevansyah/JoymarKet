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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Main;
import model.OrderHeader;
import model.Payload;
import model.Session;
import model.User;

public class OrderHistoryView extends BorderPane {

    private TableView<OrderHeader> orderTable;
    private OrderHandler orderHandler;

    public OrderHistoryView() {
        orderHandler = new OrderHandler();

        Label title = new Label("Order History");
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

        // Removed Customer ID column as requested
        // TableColumn<OrderHeader, String> customerIdCol = new TableColumn<>("Customer ID");
        // customerIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdCustomer()));

        // Removed Promo ID column as requested
        // TableColumn<OrderHeader, String> promoIdCol = new TableColumn<>("Promo ID");
        // promoIdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdPromo()));

        TableColumn<OrderHeader, String> promoCodeCol = new TableColumn<>("Promo Code");
        promoCodeCol.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getPromoCode() != null ? cellData.getValue().getPromoCode() : "N/A"
        ));

        TableColumn<OrderHeader, String> promoHeadlineCol = new TableColumn<>("Promo Headline");
        promoHeadlineCol.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getPromoHeadline() != null ? cellData.getValue().getPromoHeadline() : "N/A"
        ));

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

        TableColumn<OrderHeader, Void> detailCol = new TableColumn<>("Details");
        detailCol.setCellFactory(param -> new TableCell<OrderHeader, Void>() {
            private final Button detailButton = new Button("View Details");

            {
                detailButton.setOnAction(event -> {
                    OrderHeader order = getTableView().getItems().get(getIndex());
                    if (order != null) {
                        // Redirect to OrderDetailView with order ID
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
                    setGraphic(detailButton);
                }
            }
        });

        orderTable.getColumns().addAll(idCol, promoCodeCol, promoHeadlineCol, statusCol, orderDateCol, totalAmountCol, detailCol);
    }

    private void loadOrderData() {
        orderTable.getItems().clear();
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null && currentUser instanceof model.Customer) {
            model.Customer customer = (model.Customer) currentUser;
            Payload payload = orderHandler.getOrderHeadersByCustomerId(customer.getIdUser());
            if (payload.isSuccess() && payload.getData() instanceof List) {
                @SuppressWarnings("unchecked")
                List<OrderHeader> orders = (List<OrderHeader>) payload.getData();
                orderTable.getItems().addAll(orders);
            } else {
                System.err.println("Error loading order data: " + payload.getMessage());
            }
        }
    }
}
