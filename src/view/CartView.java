package view;

import java.util.List;

import controller.CartItemHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.CartItem;
import model.Session;
import model.User;

public class CartView extends BorderPane {

    private TableView<CartItem> cartTable;
    private CartItemHandler cartItemHandler;
    private Label totalLabel;

    public CartView() {
        cartItemHandler = new CartItemHandler();

        Label title = new Label("Shopping Cart");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        totalLabel = new Label("Total: Rp 0.00");
        totalLabel.setFont(new Font("Arial", 18));

        cartTable = new TableView<>();
        initializeTableColumns();

        loadCartData();

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 0));
        contentBox.getChildren().addAll(title, cartTable, totalLabel);

        setCenter(contentBox);
    }

    private void initializeTableColumns() {
        // --- COLUMN 1: Product Name ---
        TableColumn<CartItem, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setPrefWidth(200);

        // FIX: Use a lambda to get the Name String directly from the Product object
        productNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getProduct().getName()));

        // --- COLUMN 2: Price ---
        TableColumn<CartItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setPrefWidth(100);

        // FIX: Use a lambda to get the Price Double directly
        priceCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(
                cellData.getValue().getProduct().getPrice()));

        // Format the price as Currency
        priceCol.setCellFactory(column -> new javafx.scene.control.TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp %.2f", price));
                }
            }
        });

        // --- COLUMN 3: Quantity ---
        TableColumn<CartItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setPrefWidth(80);
        // "count" is an integer, so this matches TableColumn<..., Integer>
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        // --- COLUMN 4: Subtotal ---
        TableColumn<CartItem, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setPrefWidth(120);

        // FIX: Calculate subtotal here to ensure the Column gets a Double
        subtotalCol.setCellValueFactory(cellData -> {
            CartItem item = cellData.getValue();
            double total = item.getProduct().getPrice() * item.getCount();
            return new javafx.beans.property.SimpleObjectProperty<>(total);
        });

        // Format the subtotal as Currency
        subtotalCol.setCellFactory(column -> new javafx.scene.control.TableCell<CartItem, Double>() {
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

        cartTable.getColumns().addAll(productNameCol, priceCol, quantityCol, subtotalCol);
    }

    private void loadCartData() {
        cartTable.getItems().clear();
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            List<CartItem> items = cartItemHandler.getCartItems();
            cartTable.getItems().addAll(items);
            updateTotal(items);
        } else {
            // Optionally, show a message that user needs to log in
            totalLabel.setText("Please log in to view your cart.");
        }
    }

    private void updateTotal(List<CartItem> items) {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getCount();
        }
        totalLabel.setText(String.format("Total: Rp %.2f", total));
    }
}
