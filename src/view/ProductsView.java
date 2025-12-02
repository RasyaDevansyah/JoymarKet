package view;

import controller.ProductHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import controller.CartItemHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Main;
import model.Product;
import model.Session; // Import Session
import model.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProductsView extends BorderPane {

    private TableView<Product> productTable;
    private ProductHandler productHandler;
    private CartItemHandler cartItemHandler;

    public ProductsView() {
        productHandler = new ProductHandler();
        cartItemHandler = new CartItemHandler();

        Label title = new Label("Products");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        // Initialize TableView
        productTable = new TableView<>();
        initializeTableColumns();
        loadProductData();

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER); // Align to top center
        contentBox.setPadding(new Insets(20, 0, 0, 0)); // Add 20 padding at the top
        contentBox.getChildren().addAll(title, productTable);

        setCenter(contentBox);
    }

    private void initializeTableColumns() {
        TableColumn<Product, String> idCol = new TableColumn<>("Product ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        idCol.setPrefWidth(100);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(80);

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(120);

        TableColumn<Product, Void> addCol = new TableColumn<>("Action");
        addCol.setPrefWidth(100);
        addCol.setCellFactory(param -> new TableCell<Product, Void>() {
            private final Button addButton = new Button("Add to Cart");
            {
                addButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    User currentUser = Session.getInstance().getCurrentUser(); // Get current logged in user from
                                                                               // Session
                    if (currentUser != null) {
                        boolean success = cartItemHandler.addProductToCart(currentUser.getIdUser(),
                                product.getIdProduct(), 1);
                        if (success) {
                            showAlert(AlertType.INFORMATION, "Success", "Product added to cart!");
                            Main.getInstance().changePageTo("Cart");
                        } else {
                            showAlert(AlertType.ERROR, "Error", "Failed to add product to cart.");
                        }
                    } else {
                        showAlert(AlertType.WARNING, "Login Required", "Please log in to add products to cart.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                }
            }
        });

        productTable.getColumns().addAll(idCol, nameCol, priceCol, stockCol, categoryCol, addCol);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadProductData() {
        productTable.getItems().clear();
        productTable.getItems().addAll(productHandler.getAllProducts());
    }
}
