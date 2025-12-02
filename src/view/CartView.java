package view;

import java.util.List;

import controller.CartItemHandler;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import main.Main;
import model.CartItem;
import model.Session;
import model.User;

import java.util.Optional;

public class CartView extends BorderPane {

    private TableView<CartItem> cartTable;
    private CartItemHandler cartItemHandler;
    private Label totalLabel;
    private Button continueShoppingButton;
    private Button checkoutButton;

    public CartView() {
        cartItemHandler = new CartItemHandler();

        // Continue Shopping Button
        continueShoppingButton = new Button("Continue Shopping");
        continueShoppingButton.setOnAction(e -> {
            Main.getInstance().changePageTo("Products");
        });

        // Checkout Button
        checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> {
            // Placeholder for checkout logic
            System.out.println("Checkout button clicked!");
        });

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

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(continueShoppingButton, checkoutButton);

        contentBox.getChildren().addAll(title, cartTable, totalLabel, buttonBox);

        setCenter(contentBox);
        updateTotal(); // Initial total update
    }

    private void initializeTableColumns() {
        // --- COLUMN 1: Product Name ---
        TableColumn<CartItem, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setPrefWidth(200);
        productNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProduct().getName()));

        // --- COLUMN 2: Price ---
        TableColumn<CartItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setPrefWidth(100);
        priceCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(
                cellData.getValue().getProduct().getPrice()));
        priceCol.setCellFactory(column -> new TableCell<CartItem, Double>() {
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

        // --- COLUMN 3: Quantity (THE FIX IS HERE) ---
        TableColumn<CartItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setPrefWidth(100);
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        quantityCol.setCellFactory(column -> new TableCell<CartItem, Integer>() {
            private final Spinner<Integer> spinner = new Spinner<>(1, 99, 1);
            // Flag to prevent loop
            private boolean isUpdating = false;

            {
                spinner.setPrefWidth(80);
                spinner.setEditable(true);

                // Listener for spinner value changes
                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    // STOP if we are currently inside updateItem (programmatic change)
                    if (isUpdating) {
                        return;
                    }

                    if (newValue != null && !newValue.equals(oldValue)) {
                        int index = getIndex();
                        if (index >= 0 && index < getTableView().getItems().size()) {
                            CartItem item = getTableView().getItems().get(index);
                            item.setCount(newValue);

                            // Update DB
                            cartItemHandler.updateCartItem(item);

                            // Update UI Total safely
                            updateTotal();

                            // NOTE: Do NOT call getTableView().refresh() here.
                            // It is unnecessary because the Spinner already shows the new value.
                            // Calling refresh() here causes the loop.
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Integer quantity, boolean empty) {
                super.updateItem(quantity, empty);

                if (empty || quantity == null) {
                    setGraphic(null);
                } else {
                    // Set flag to TRUE before setting value
                    isUpdating = true;
                    spinner.getValueFactory().setValue(quantity);
                    // Set flag to FALSE after setting value
                    isUpdating = false;

                    setGraphic(spinner);
                }
            }
        });

        // --- COLUMN 4: Subtotal ---
        TableColumn<CartItem, Double> subtotalCol = new TableColumn<>("Subtotal");
        subtotalCol.setPrefWidth(120);
        subtotalCol.setCellValueFactory(cellData -> {
            CartItem item = cellData.getValue();
            double total = item.getProduct().getPrice() * item.getCount();
            return new SimpleObjectProperty<>(total);
        });
        subtotalCol.setCellFactory(column -> new TableCell<CartItem, Double>() {
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

        // --- COLUMN 5: Delete Button (CLEANED UP) ---
        TableColumn<CartItem, Void> deleteCol = new TableColumn<>("Action");
        deleteCol.setPrefWidth(120);

        // Ideally, add a dummy value factory for Void columns to ensure cells
        // initialize correctly
        deleteCol.setCellValueFactory(param -> new SimpleObjectProperty<>(null));

        deleteCol.setCellFactory(column -> new TableCell<CartItem, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setPrefWidth(80);
                deleteButton.setAlignment(Pos.CENTER);

                // Removed Platform.runLater wrapping - it's not needed for click handlers
                deleteButton.setOnAction(event -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle("Confirm Deletion");
                        alert.setHeaderText("Remove Item from Cart");
                        alert.setContentText("Are you sure you want to remove '" + item.getProduct().getName() + "'?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            cartItemHandler.removeCartItem(item);
                            loadCartData(); // This refreshes the table
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        cartTable.getColumns().addAll(productNameCol, priceCol, quantityCol, subtotalCol, deleteCol);
    }

    private void loadCartData() {
        cartTable.getItems().clear();
        User currentUser = Session.getInstance().getCurrentUser();
        if (currentUser != null) {
            List<CartItem> items = cartItemHandler.getCartItems();
            cartTable.getItems().addAll(items);
            updateTotal();
        } else {
            // Optionally, show a message that user needs to log in
            totalLabel.setText("Please log in to view your cart.");
        }
    }

    public void updateTotal() {
        double total = 0.0;
        for (CartItem item : cartTable.getItems()) {
            total += item.getProduct().getPrice() * item.getCount();
        }
        totalLabel.setText(String.format("Total: Rp %.2f", total));
    }
}
