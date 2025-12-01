package view;

import controller.ProductHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Product;

public class ProductsView extends BorderPane {

    private TableView<Product> productTable;
    private ProductHandler productHandler;

    public ProductsView() {
        productHandler = new ProductHandler();

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

        productTable.getColumns().addAll(idCol, nameCol, priceCol, stockCol, categoryCol);
    }

    private void loadProductData() {
        productTable.getItems().clear();
        productTable.getItems().addAll(productHandler.getAllProducts());
    }
}
