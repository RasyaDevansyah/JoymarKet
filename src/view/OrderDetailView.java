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
import model.OrderDetail;
import model.OrderHeader;
import model.Payload;
import model.Product;
import model.ProductDAO;
import model.Session;
import model.User;

public class OrderDetailView extends BorderPane {

    private int orderId;
    private OrderHandler orderHandler;
    private ProductDAO productDAO;
    private Label orderIdLabel;
    private Label customerIdLabel;
    private Label promoIdLabel;
    private Label statusLabel;
    private Label orderedAtLabel;
    private Label totalAmountLabel;
    private TableView<OrderDetail> detailTable;
    private Button backButton;

    public OrderDetailView(int orderId) {
        this.orderId = orderId;
        this.orderHandler = new OrderHandler();
        this.productDAO = new ProductDAO();

        initializeComponents();
        loadOrderDetails();
    }

    private void initializeComponents() {
        Label title = new Label("Order Details");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        orderIdLabel = new Label("Order ID: ");
        customerIdLabel = new Label("Customer ID: ");
        promoIdLabel = new Label("Promo ID: ");
        statusLabel = new Label("Status: ");
        orderedAtLabel = new Label("Ordered At: ");
        totalAmountLabel = new Label("Total Amount: ");

        detailTable = new TableView<>();
        initializeTableColumns();

        backButton = new Button("Back"); // Changed button text to be more generic
        backButton.setOnAction(e -> {
            User currentUser = Session.getInstance().getCurrentUser();
            if (currentUser != null) {
                switch (currentUser.getRole()) {
                    case "CUSTOMER":
                        Main.getInstance().changePageTo("OrderHistory");
                        break;
                    case "ADMIN":
                        Main.getInstance().changePageTo("ViewAllOrders");
                        break;
                    case "COURIER":
                        Main.getInstance().changePageTo("Deliveries");
                        break;
                    default:
                        Main.getInstance().changePageTo("Products"); // Fallback
                        break;
                }
            } else {
                Main.getInstance().changePageTo("Products"); // Fallback if no user is logged in
            }
        });

        VBox infoBox = new VBox(5);
        infoBox.setPadding(new Insets(10));
        infoBox.getChildren().addAll(orderIdLabel, customerIdLabel, promoIdLabel, statusLabel, orderedAtLabel,
                totalAmountLabel);

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 0));
        contentBox.getChildren().addAll(title, infoBox, detailTable, backButton);

        setCenter(contentBox);
    }

    private void initializeTableColumns() {
        TableColumn<OrderDetail, String> productCol = new TableColumn<>("Product Name");
        productCol.setCellValueFactory(cellData -> {
            Product product = productDAO.getProductById(cellData.getValue().getIdProduct());
            return new SimpleStringProperty(product != null ? product.getName() : "N/A");
        });

        TableColumn<OrderDetail, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQty()));

        TableColumn<OrderDetail, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> {
            Product product = productDAO.getProductById(cellData.getValue().getIdProduct());
            return new SimpleObjectProperty<>(product != null ? product.getPrice() : 0.0);
        });
        priceCol.setCellFactory(column -> new TableCell<OrderDetail, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                }
                else {
                    setText(String.format("Rp %.2f", price));
                }
            }
        });

        TableColumn<OrderDetail, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setCellValueFactory(cellData -> {
            Product product = productDAO.getProductById(cellData.getValue().getIdProduct());
            double subtotal = (product != null ? product.getPrice() : 0.0) * cellData.getValue().getQty();
            return new SimpleObjectProperty<>(subtotal);
        });
        subtotalCol.setCellFactory(column -> new TableCell<OrderDetail, Double>() {
            @Override
            protected void updateItem(Double subtotal, boolean empty) {
                super.updateItem(subtotal, empty);
                if (empty || subtotal == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp %.2f", subtotal));
                }
            }
        });

        detailTable.getColumns().addAll(productCol, qtyCol, priceCol, subtotalCol);
    }

    private void loadOrderDetails() {
        Payload headerPayload = orderHandler.getOrderHeaderById(orderId);
        if (headerPayload.isSuccess() && headerPayload.getData() instanceof OrderHeader) {
            OrderHeader orderHeader = (OrderHeader) headerPayload.getData();
            orderIdLabel.setText("Order ID: " + orderHeader.getIdOrder());
            customerIdLabel.setText("Customer ID: " + orderHeader.getIdCustomer());
            promoIdLabel.setText("Promo ID: " + (orderHeader.getIdPromo() != null ? orderHeader.getIdPromo() : "N/A"));
            statusLabel.setText("Status: " + orderHeader.getStatus());
            orderedAtLabel.setText("Ordered At: " + orderHeader.getOrderedAt().toString());
            totalAmountLabel.setText(String.format("Total Amount: Rp %.2f", orderHeader.getTotalAmount()));

            Payload detailsPayload = orderHandler.getOrderDetailsByOrderId(orderId);
            if (detailsPayload.isSuccess() && detailsPayload.getData() instanceof List) {
                List<OrderDetail> details = (List<OrderDetail>) detailsPayload.getData();
                detailTable.getItems().addAll(details);
            } else {
                System.err.println("Error loading order details: " + detailsPayload.getMessage());
            }
        } else {
            System.err.println("Error loading order header: " + headerPayload.getMessage());
        }
    }
}
