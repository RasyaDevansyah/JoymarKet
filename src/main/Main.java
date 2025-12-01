package main;

import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import util.Connect;
import view.CustomerView;

public class Main extends Application {

	private User currentUser;

	private CustomerView customerView = new CustomerView();

	private Connect connect = Connect.getInstance();

	int tempId; // simpen id

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(customerView.getScene());
		primaryStage.setTitle("JoyMarket");
		primaryStage.show();

	}

}
