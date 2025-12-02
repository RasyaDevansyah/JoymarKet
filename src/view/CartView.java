package view;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import controller.CartItemHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import main.Main;
import model.CartItem;
import model.Payload;
import model.Product;
import model.Session;
import model.User;

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

        // --- COLUMN 3: Quantity ---
        TableColumn<CartItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setPrefWidth(100);
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        quantityCol.setCellFactory(column -> new TableCell<CartItem, Integer>() {
            private final Spinner<Integer> spinner = new Spinner<>();
            private boolean isUpdating = false;

            {
                spinner.setPrefWidth(80);
                spinner.setEditable(true);

                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    if (isUpdating) {
                        return;
                    }

                    if (newValue != null && !newValue.equals(oldValue)) {
                        int index = getIndex();
                        if (index >= 0 && index < getTableView().getItems().size()) {
                            CartItem item = getTableView().getItems().get(index);
                            item.setCount(newValue);

                            Payload payload = cartItemHandler.updateCartItem(item);
                            if (!payload.isSuccess()) {
                                System.err.println("Error updating cart item: " + payload.getMessage());
                            }
                            
                            getTableView().refresh(); // Refresh table to update subtotal
                            updateTotal();
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
                    isUpdating = true;
                    CartItem item = getTableView().getItems().get(getIndex());
                    Product product = new model.ProductDAO().getProductById(item.getProduct().getIdProduct());
                    if (product != null) {
                        spinner.setValueFactory(new Spinner<Integer>(1, product.getStock(), quantity).getValueFactory());
                    } else {
                        spinner.setValueFactory(new Spinner<Integer>(1, 99, quantity).getValueFactory());
                    }
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

        // --- COLUMN 5: Delete Button ---
        TableColumn<CartItem, Void> deleteCol = new TableColumn<>("Action");
        deleteCol.setPrefWidth(120);
        deleteCol.setCellValueFactory(param -> new SimpleObjectProperty<>(null));

        deleteCol.setCellFactory(column -> new TableCell<CartItem, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(deleteButton); // Wrap button in HBox

            {
                deleteButton.setPrefWidth(80);
                deleteButton.setAlignment(Pos.CENTER);
                deleteButton.setPickOnBounds(true);

                pane.setAlignment(Pos.CENTER);

                pane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getTarget() == deleteButton) {
                        deleteButton.fire();
                        event.consume();
                    }
                });

                deleteButton.setOnAction(event -> {
                    System.out.println("Delete button action fired for item at index: " + getIndex());
                    CartItem item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle("Confirm Deletion");
                        alert.setHeaderText("Remove Item from Cart");
                        alert.setContentText("Are you sure you want to remove '" + item.getProduct().getName() + "'?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            Payload payload = cartItemHandler.removeCartItem(item);
                            if (!payload.isSuccess()) {
                                System.err.println("Error removing cart item: " + payload.getMessage());
                            }
                            Platform.runLater(() -> {
                                loadCartData();
                            });
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
                    setGraphic(pane);
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
            Payload payload = cartItemHandler.getCartItems();
            if (payload.isSuccess() && payload.getData() instanceof List) {
                @SuppressWarnings("unchecked")
                List<CartItem> items = (List<CartItem>) payload.getData();
                cartTable.getItems().addAll(items);
                updateTotal();
            } else {
                System.err.println("Error loading cart data: " + payload.getMessage());
                totalLabel.setText("Failed to load cart. " + payload.getMessage());
            }
        } else {
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
