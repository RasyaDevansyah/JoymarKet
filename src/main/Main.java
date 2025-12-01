package main;

import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import util.Connect;
import view.LoginView;
import view.RegisterView;
import view.WelcomeView;

public class Main extends Application {

	private User currentUser;

	private WelcomeView welcomeView = new WelcomeView();
	private LoginView loginView = new LoginView();
	private RegisterView registerView = new RegisterView();

	private Connect connect = Connect.getInstance();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(welcomeView.getScene());
		primaryStage.setTitle("JoyMarket");
		primaryStage.show();

		// Navigation logic
		welcomeView.getLoginButton().setOnAction(e -> primaryStage.setScene(loginView.getScene()));
		welcomeView.getRegisterButton().setOnAction(e -> primaryStage.setScene(registerView.getScene()));

		loginView.getBackButton().setOnAction(e -> primaryStage.setScene(welcomeView.getScene()));
		registerView.getBackButton().setOnAction(e -> primaryStage.setScene(welcomeView.getScene()));
	}

}
