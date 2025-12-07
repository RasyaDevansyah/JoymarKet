package view;

import controller.CourierHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Courier;
import model.Payload;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.List;

public class ViewAllCouriersView extends BorderPane {

    private TableView<Courier> courierTable;
    private CourierHandler courierHandler;

    public ViewAllCouriersView() {
        courierHandler = new CourierHandler();

        Label title = new Label("All Couriers");
        title.setFont(new Font("Arial", 24));
        setAlignment(title, Pos.TOP_CENTER);

        courierTable = new TableView<>();
        initializeTableColumns();
        loadCourierData();

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 0));
        contentBox.getChildren().addAll(title, courierTable);

        setCenter(contentBox);
    }

    private void initializeTableColumns() {
        TableColumn<Courier, String> idCol = new TableColumn<>("Courier ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        idCol.setPrefWidth(100);

        TableColumn<Courier, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName")); 
        nameCol.setPrefWidth(200);

        TableColumn<Courier, String> vehicleTypeCol = new TableColumn<>("Vehicle Type");
        vehicleTypeCol.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        vehicleTypeCol.setPrefWidth(150);

        TableColumn<Courier, String> vehiclePlateCol = new TableColumn<>("Vehicle Plate");
        vehiclePlateCol.setCellValueFactory(new PropertyValueFactory<>("vehiclePlate"));
        vehiclePlateCol.setPrefWidth(150);

        courierTable.getColumns().addAll(idCol, nameCol, vehicleTypeCol, vehiclePlateCol);
    }

    private void loadCourierData() {
        courierTable.getItems().clear();
        Payload payload = courierHandler.getAllCouriers();
        if (payload.isSuccess() && payload.getData() instanceof List) {
            List<Courier> couriers = (List<Courier>) payload.getData();
            courierTable.getItems().addAll(couriers);
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to load couriers: " + payload.getMessage());
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
