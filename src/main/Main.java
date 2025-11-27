package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Registrant;
import util.Connect;

public class Main extends Application {

	private ObservableList<Registrant> registrants = FXCollections.observableArrayList();

	private BorderPane bp;
	private Scene scene;

	private GridPane grid;
	private Label nameLabel, ageLabel, addressLabel, ipkLabel;
	private TextField nameField, ageField, addressField, ipkField;
	private Button submitButton, updateButton, deleteButton;

	private Connect connect = Connect.getInstance();

	int tempId; // simpen id

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();

		primaryStage.setScene(scene);
		primaryStage.setTitle("JDBC Sesi 9");
		primaryStage.show();

	}

	private void initialize() {
		bp = new BorderPane();
		createForm();
		createTableView();

		scene = new Scene(bp, 500, 500);
	}

	private void createForm() {
		grid = new GridPane();
		nameLabel = new Label("Name:");
		ageLabel = new Label("Age:");
		addressLabel = new Label("Address:");
		ipkLabel = new Label("IPK:");

		nameField = new TextField();
		ageField = new TextField();
		addressField = new TextField();
		ipkField = new TextField();

		submitButton = new Button("Submit");
		updateButton = new Button("Update");
		deleteButton = new Button("Delete");

		// Param: item, col, row
		grid.add(nameLabel, 0, 0);
		grid.add(nameField, 1, 0);

		grid.add(ageLabel, 0, 1);
		grid.add(ageField, 1, 1);

		grid.add(addressLabel, 0, 2);
		grid.add(addressField, 1, 2);

		grid.add(ipkLabel, 0, 3);
		grid.add(ipkField, 1, 3);

		grid.add(submitButton, 0, 4);
		grid.add(updateButton, 1, 4);
		grid.add(deleteButton, 2, 4);

		submitButton.setOnAction(event -> {
			String name = nameField.getText();
			int age = Integer.parseInt(ageField.getText());
			String address = addressField.getText();
			double ipk = Double.parseDouble(ipkField.getText());

			insertData(name, age, address, ipk);

			registrants.clear();
			getData();
		});

		updateButton.setOnAction(event -> {
			String name = nameField.getText();
			int age = Integer.parseInt(ageField.getText());
			String address = addressField.getText();
			double ipk = Double.parseDouble(ipkField.getText());

			updateData(name, age, address, ipk);

			registrants.clear();
			getData();
		});

		deleteButton.setOnAction(event -> {
			if (tempId != 0) {
				deleteData();
			}

			registrants.clear();
			getData();

			// Buat UI rapi aja
			nameField.clear();
			ageField.clear();
			addressField.clear();
			ipkField.clear();

		});

		bp.setBottom(grid);
	}

	private void createTableView() {
		TableView<Registrant> registrantTable = new TableView<>();
		TableColumn<Registrant, Integer> idCol = new TableColumn<>("ID");
		TableColumn<Registrant, String> nameCol = new TableColumn<>("Name");
		TableColumn<Registrant, Integer> ageCol = new TableColumn<>("Age");
		TableColumn<Registrant, String> addressCol = new TableColumn<>("Address");
		TableColumn<Registrant, Double> ipkCol = new TableColumn<>("IPK");

		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
		addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
		ipkCol.setCellValueFactory(new PropertyValueFactory<>("ipk"));

		registrantTable.getColumns().addAll(idCol, nameCol, ageCol, addressCol, ipkCol);

		// Set data
		getData();
		registrantTable.setItems(registrants);

		// Set untuk interaksi tabel
		// .setOnmouseClicked
		registrantTable.setOnMouseClicked(event -> {
			// .getSelectionModel = kasih tau user lagi di row yang mana
			// .getSelectedItem = ambil object yang di select oleh user

			Registrant selectedRegistrant = registrantTable.getSelectionModel().getSelectedItem();

			if (selectedRegistrant != null) {
				nameField.setText(selectedRegistrant.getName());
				ageField.setText(String.valueOf(selectedRegistrant.getAge()));
				addressField.setText(selectedRegistrant.getAddress());
				ipkField.setText(String.valueOf(selectedRegistrant.getIpk()));

				tempId = selectedRegistrant.getId();
			}
		});

		bp.setCenter(registrantTable);
	}

	private void getData() {
		String query = "SELECT * FROM registrant";

		connect.rs = connect.execQuery(query);

		try {
			while (connect.rs.next()) {
				int id = connect.rs.getInt("ID");
				String name = connect.rs.getString("Name");
				int age = connect.rs.getInt("Age");
				String address = connect.rs.getString("Address");
				double ipk = connect.rs.getDouble("IPK");

				registrants.add(new Registrant(id, name, age, address, ipk));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void insertData(String name, int age, String address, double ipk) {
		String query = String.format(
				"INSERT INTO registrant " + "(Name, Age, Address, IPK) VALUES ('%s', %d, '%s', %f)", name, age, address,
				ipk);

		connect.execUpdate(query);
	}

	private void updateData(String name, int age, String address, double ipk) {
		// String query = String.format("UPDATE registrant SET Name = '%s', Age = %d,
		// Address = '%s', IPK = %f WHERE ID = %d", name, age, address, ipk, tempId);
		//
		// connect.execUpdate(query);

		String query = "UPDATE registrant SET Name = ?, Age = ?, Address = ?, IPK = ? WHERE ID = ?";

		PreparedStatement ps = connect.preparedStatement(query);

		// arg 1 = posisi
		// arg 2 = value
		try {
			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, address);
			ps.setDouble(4, ipk);
			ps.setInt(5, tempId);

			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteData() {
		// String query = String.format("DELETE FROM registrant WHERE ID = %d", tempId);
		// connect.execUpdate(query);

		// Pake PreparedStatement
		String query = "DELETE FROM registrant WHERE ID = ?";

		PreparedStatement ps = connect.preparedStatement(query);

		try {
			ps.setInt(1, tempId);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
