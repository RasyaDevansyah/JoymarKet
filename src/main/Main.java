package main;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Session; // Import Session
import view.CartView;
import view.LoginView;
import view.Navbar;
import view.OrderHistoryView;
import view.ProductsView;
import view.RegisterView;
import view.TopupView;

public class Main extends Application {

	private static Main instance;
	private BorderPane mainLayout;
	private Navbar navbar;
	private Session session = Session.getInstance(); // Initialize Session

	private ProductsView productsView;
	private TopupView topupView;
	private CartView cartView;
	private OrderHistoryView orderHistoryView;
	private LoginView loginView;
	private RegisterView registerView;

	public static void main(String[] args) {
		launch(args);
	}

	public static Main getInstance() {
		return instance;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		// Initialize views
		navbar = new Navbar();
		productsView = new ProductsView();
		topupView = new TopupView();
		cartView = new CartView();
		orderHistoryView = new OrderHistoryView();
		loginView = new LoginView();
		registerView = new RegisterView();

		mainLayout = new BorderPane();
		mainLayout.setCenter(productsView); // Default view
		mainLayout.setTop(navbar);
		primaryStage.setScene(new javafx.scene.Scene(mainLayout, 800, 600));
		primaryStage.setTitle("JoyMarket");
		primaryStage.show();

	}

	public void changePageTo(String pageName) {
		refreshNavbar();
		switch (pageName) {
			case "Products":
				productsView = new ProductsView(); // Refresh products view
				mainLayout.setCenter(productsView);
				break;
			case "Topup":
				mainLayout.setCenter(topupView);
				break;
			case "Cart":
				cartView = new CartView(); // Refresh cart view
				mainLayout.setCenter(cartView);
				break;
			case "OrderHistory":
				mainLayout.setCenter(orderHistoryView);
				break;
			case "Register":
				registerView = new RegisterView(); // Refresh register view
				mainLayout.setCenter(registerView);
				break;
			case "Login":
				loginView = new LoginView(); // Refresh login view to show latest success message
				mainLayout.setCenter(loginView);
				break;
			default:
				mainLayout.setCenter(productsView); // Fallback to products view
				break;
		}
	}

	public void updateNavbarForUser(String username) {
		navbar.setupLoggedInView(username);
	}

	public void updateNavbarForGuest() {
		navbar.setupLoggedOutView();
	}

	public void refreshNavbar() {
		if (session.isLoggedIn()) {
			updateNavbarForUser(Session.getInstance().getCurrentUser().getUsername());
		} else {
			updateNavbarForGuest();
		}
	}

}
