package view;

import java.util.List;
import java.util.Optional;

import controller.CartItemHandler;
import controller.OrderHandler;
import controller.PromoHandler;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Main;
import model.CartItem;
import model.Customer;
import model.CustomerDAO;
import model.Payload;
import model.Product;
import model.Promo;
import model.Session;
import model.User;

public class CartView extends BorderPane {

    private TableView<CartItem> cartTable;
    private CartItemHandler cartItemHandler;
    private OrderHandler orderHandler;
    private PromoHandler promoHandler; // New PromoHandler
    private CustomerDAO customerDAO; // New CustomerDAO
    private Label totalLabel;
    private Label balanceLabel; // New balance label
    private TextField promoCodeField; // New promo code input
    private Button applyPromoButton; // New apply promo button
    private Label promoInfoLabel; // New label to display promo info
    private Button continueShoppingButton;
    private Button checkoutButton;

    private String appliedPromoId = null; // To store applied promo ID
    private double discountedTotal = 0.0; // To store discounted total

    public CartView() {
        cartItemHandler = new CartItemHandler();
        orderHandler = new OrderHandler();
        promoHandler = new PromoHandler(); // Initialize PromoHandler
        customerDAO = new CustomerDAO(); // Initialize CustomerDAO

        // Continue Shopping Button
        continueShoppingButton = new Button("Continue Shopping");
        continueShoppingButton.setOnAction(e -> {
            Main.getInstance().changePageTo("Products");
        });

        // Checkout Button
        checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> {
            User currentUser = Session.getInstance().getCurrentUser();
            if (currentUser == null) {
                showAlert(AlertType.ERROR, "Error", "Not Logged In", "Please log in to proceed with checkout.");
                return;
            }

            double total = calculateTotal();
            if (total <= 0) {
                showAlert(AlertType.WARNING, "Warning", "Empty Cart",
                        "Your cart is empty. Please add items before checking out.");
                return;
            }

            // Pass appliedPromoId to processCheckout
            Payload checkoutPayload = orderHandler.processCheckout(total, currentUser, appliedPromoId);

            if (checkoutPayload.isSuccess()) {
                showAlert(AlertType.INFORMATION, "Success", "Checkout Successful", checkoutPayload.getMessage());
                Main.getInstance().changePageTo("OrderHistory"); // Redirect to OrderHistoryView
            } else {
                showAlert(AlertType.ERROR, "Error", "Checkout Failed", checkoutPayload.getMessage());
            }
        });

        Label title = new Label("Shopping Cart");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        balanceLabel = new Label("Balance: Rp 0.00"); // Initialize balance label
        balanceLabel.setFont(new Font("Arial", 18));

        totalLabel = new Label("Total: Rp 0.00");
        totalLabel.setFont(new Font("Arial", 18));

        promoCodeField = new TextField();
        promoCodeField.setPromptText("Enter promo code");
        promoCodeField.setPrefWidth(150);
        applyPromoButton = new Button("Apply Promo");
        applyPromoButton.setOnAction(e -> handleApplyPromo());

        promoInfoLabel = new Label(""); // To display promo status
        promoInfoLabel.setFont(new Font("Arial", 12));

        cartTable = new TableView<>();
        initializeTableColumns();

        cartTable.setRowFactory(tv -> new TableRow<CartItem>() {
            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    Product product = new model.ProductDAO().getProductById(item.getProduct().getIdProduct());
                    if (product != null && product.getStock() <= 0) {
                        setStyle("-fx-background-color: #ffcdd2;"); // Light red for out of stock
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        loadCartData();

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 0));

        HBox promoBox = new HBox(10, promoCodeField, applyPromoButton, promoInfoLabel);
        promoBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(continueShoppingButton, checkoutButton);

        contentBox.getChildren().addAll(title, balanceLabel, cartTable, promoBox, totalLabel, buttonBox);

        setCenter(contentBox);
        updateTotalLabel(); // Initial total update
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

                            isUpdating = true;
                            Payload payload = cartItemHandler.updateCartItem(item);
                            if (!payload.isSuccess()) {
                                System.err.println("Error updating cart item: " + payload.getMessage());
                            }
                            updateTotalLabel();
                            isUpdating = false;
                        }
                    }
                    Platform.runLater(() -> updateTotalLabel()); // Update total immediately
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
                    if (product != null && product.getStock() > 0) { // Check for stock > 0
                        spinner.setValueFactory(
                                new Spinner<Integer>(1, product.getStock(), quantity).getValueFactory());
                    } else {
                        spinner.setValueFactory(new Spinner<Integer>(0, 0, 0).getValueFactory()); // Set to 0 if out of stock
                        spinner.setDisable(true); // Disable spinner if out of stock
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
        deleteCol.setPrefWidth(200);
        deleteCol.setCellValueFactory(param -> new SimpleObjectProperty<>(null));

        deleteCol.setCellFactory(column -> new TableCell<CartItem, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Label outOfStockLabel = new Label("Out of Stock");
            private final HBox pane = new HBox();

            {
                deleteButton.setPrefWidth(80);
                deleteButton.setAlignment(Pos.CENTER);
                deleteButton.setPickOnBounds(true);
                outOfStockLabel.setStyle("-fx-text-fill: red;");

                pane.setAlignment(Pos.CENTER);
                pane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getTarget() == deleteButton) {
                        deleteButton.fire();
                        event.consume();
                    }
                    updateTotalLabel(); // Update total after delete action
                });

                deleteButton.setOnAction(event -> {
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
                            Platform.runLater(() -> loadCartData());
                            Platform.runLater(() -> updateTotalLabel());
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
                    CartItem cartItem = getTableView().getItems().get(getIndex());
                    Product product = new model.ProductDAO().getProductById(cartItem.getProduct().getIdProduct());
                    if (product != null && product.getStock() <= 0) {
                        pane.getChildren().setAll(outOfStockLabel, deleteButton);
                    } else {
                        pane.getChildren().setAll(deleteButton);
                    }
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
            // Display customer balance
            if (currentUser instanceof Customer) {
                Customer customer = (Customer) currentUser;
                Customer updatedCustomer = customerDAO.getCustomerById(customer.getIdUser());
                if (updatedCustomer != null) {
                    balanceLabel.setText(String.format("Balance: Rp %.2f", updatedCustomer.getBalance()));
                }
            }

            Payload payload = cartItemHandler.getCartItems();
            if (payload.isSuccess() && payload.getData() instanceof List) {
                @SuppressWarnings("unchecked")
                List<CartItem> items = (List<CartItem>) payload.getData();
                cartTable.getItems().addAll(items);
                updateTotalLabel();
            } else {
                System.err.println("Error loading cart data: " + payload.getMessage());
                totalLabel.setText("Failed to load cart. " + payload.getMessage());
            }
        } else {
            balanceLabel.setText("Balance: Login to view"); // Default text for guest
            totalLabel.setText("Please log in to view your cart.");
        }
    }

    private double calculateTotal() {
        double total = 0.0;
        for (CartItem item : cartTable.getItems()) {
            Product product = new model.ProductDAO().getProductById(item.getProduct().getIdProduct());
            if (product != null && product.getStock() > 0) {
                total += item.getProduct().getPrice() * item.getCount();
            }
        }
        return discountedTotal > 0 ? discountedTotal : total; // Apply discount if available
    }

    public void updateTotalLabel() {
        double total = calculateTotal();
        totalLabel.setText(String.format("Total: Rp %.2f", total));
    }

    private void handleApplyPromo() {
        String promoCode = promoCodeField.getText();
        Payload payload = promoHandler.applyPromo(promoCode, calculateTotal());
        if (payload.isSuccess()) {
            discountedTotal = (double) payload.getData();
            appliedPromoId = payload.getPromoId();
            promoInfoLabel.setText("Promo applied! Discounted Total: Rp " + String.format("%.2f", discountedTotal));
            updateTotalLabel();
        } else {
            discountedTotal = 0.0; // Reset discounted total
            appliedPromoId = null; // Clear applied promo ID
            promoInfoLabel.setText("Error: " + payload.getMessage());
            updateTotalLabel();
        }
    }

    private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
