package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import util.Connect;
import view.CartView;
import view.LoginView;
import view.Navbar;
import view.OrderHistoryView;
import view.ProductsView;
import view.RegisterView;
import view.TopupView;

public class Main extends Application {

	private User currentUser;
	private BorderPane mainLayout;
	private Navbar navbar;

	private ProductsView productsView = new ProductsView();
	private TopupView topupView = new TopupView();
	private CartView cartView = new CartView();
	private OrderHistoryView orderHistoryView = new OrderHistoryView();
	private LoginView loginView = new LoginView();
	private RegisterView registerView = new RegisterView();

	private Connect connect = Connect.getInstance();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainLayout = new BorderPane();
		navbar = new Navbar();
		mainLayout.setTop(navbar);
		mainLayout.setCenter(productsView); // Default view

		primaryStage.setScene(new javafx.scene.Scene(mainLayout, 800, 600));
		primaryStage.setTitle("JoyMarket");
		primaryStage.show();

		// Navbar button actions
		navbar.getProductsButton().setOnAction(e -> changePageTo("Products"));
		navbar.getRegisterButton().setOnAction(e -> changePageTo("Register"));

		// Assuming a successful login
		loginView.getLoginButton().setOnAction(e -> {
			// Placeholder for actual login logic
			// If login is successful:
			currentUser = new User("TestUser", "test@example.com", "password123", "08123456789", "123 Test St"); // Dummy user
			navbar.setupLoggedInView(currentUser.getUsername());
			changePageTo("Products"); // Go to products after login

			// Update logged-in navbar button actions
			navbar.getProductsButton().setOnAction(event -> changePageTo("Products"));
			navbar.getTopupButton().setOnAction(event -> changePageTo("Topup"));
			navbar.getCartButton().setOnAction(event -> changePageTo("Cart"));
			navbar.getOrderHistoryButton().setOnAction(event -> changePageTo("OrderHistory"));
		
			// The usernameLabel in navbar has no action, it's just a label
		});

		// Placeholder for register button action (after successful registration, go to login or products)
		registerView.getRegisterButton().setOnAction(e -> {
			// Placeholder for actual registration logic
			changePageTo("Login"); // After registration, go to login
		});
	}

	private void changePageTo(String pageName) {
		switch (pageName) {
			case "Products":
				mainLayout.setCenter(productsView);
				break;
			case "Topup":
				mainLayout.setCenter(topupView);
				break;
			case "Cart":
				mainLayout.setCenter(cartView);
				break;
			case "OrderHistory":
				mainLayout.setCenter(orderHistoryView);
				break;
			case "Register":
				mainLayout.setCenter(registerView);
				break;
			case "Login":
				mainLayout.setCenter(loginView);
				break;
			default:
				mainLayout.setCenter(productsView); // Fallback to products view
				break;
		}
	}

}
