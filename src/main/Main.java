package main;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Session; 
import view.CartView;
import view.LoginView;
import view.Navbar;
import view.OrderDetailView;
import view.OrderHistoryView;
import view.ProductsView;
import view.AssignOrderView;
import view.DeliveriesView;
import view.EditProductView;
import view.ProfileView;
import view.RegisterView;
import view.TopupView;
import view.ViewAllCouriersView;
import view.ViewAllOrdersView;

public class Main extends Application {

	private static Main instance;
	private BorderPane mainLayout;
	private Navbar navbar;
	private Session session = Session.getInstance(); // Initialize Session

	private ProductsView productsView;
	private TopupView topupView;
	private CartView cartView;
	private OrderHistoryView orderHistoryView;
	private OrderDetailView orderDetailView;
	private LoginView loginView;
	private RegisterView registerView;
	private ProfileView profileView; 

	// Admin/Courier specific views
	private AssignOrderView assignOrderView;
	private DeliveriesView deliveriesView;
	private EditProductView editProductView;
	private ViewAllCouriersView viewAllCouriersView;
	private ViewAllOrdersView viewAllOrdersView;

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
		profileView = new ProfileView();

		// Initialize Admin/Courier specific views
		viewAllCouriersView = new ViewAllCouriersView();
		viewAllOrdersView = new ViewAllOrdersView();

		mainLayout = new BorderPane();
		mainLayout.setCenter(productsView); // Default view
		mainLayout.setTop(navbar);
		primaryStage.setScene(new javafx.scene.Scene(mainLayout, 800, 600));
		primaryStage.setTitle("JoyMarket");
		primaryStage.show();

	}

	// Method to change pages/views
	public void changePageTo(String pageName, String... params) {
		refreshNavbar();
		switch (pageName) {
			case "Products":
				productsView = new ProductsView(); 
				mainLayout.setCenter(productsView);
				break;
			case "Topup":
				topupView = new TopupView(); 
				mainLayout.setCenter(topupView);
				break;
			case "Cart":
				cartView = new CartView(); 
				mainLayout.setCenter(cartView);
				break;
			case "OrderHistory":
				orderHistoryView = new OrderHistoryView();
				mainLayout.setCenter(orderHistoryView);
				break;
			case "OrderDetail":
				if (params.length > 0) {
					int orderId = Integer.parseInt(params[0]);
					orderDetailView = new OrderDetailView(orderId);
					mainLayout.setCenter(orderDetailView);
				} else {
					System.err.println("Order ID not provided for OrderDetailView.");
					mainLayout.setCenter(orderHistoryView);
				}
				break;
			case "Register":
				registerView = new RegisterView(); 
				mainLayout.setCenter(registerView);
				break;
			case "Login":
				loginView = new LoginView();
				mainLayout.setCenter(loginView);
				break;
			case "Profile":
				profileView = new ProfileView();
				mainLayout.setCenter(profileView);
				break;
			case "AssignOrder":
				if (params.length > 0) {
					int orderId = Integer.parseInt(params[0]);
					assignOrderView = new AssignOrderView(orderId);
					mainLayout.setCenter(assignOrderView);
				} else {
					System.err.println("Order ID not provided for AssignOrderView.");
					mainLayout.setCenter(viewAllOrdersView);
				}
				break;
			case "Deliveries":
				deliveriesView = new DeliveriesView();
				mainLayout.setCenter(deliveriesView);
				break;
			case "EditProduct":
				if (params.length > 0) {
					String productId = params[0];
					editProductView = new EditProductView(productId);
					mainLayout.setCenter(editProductView);
				} else {
					System.err.println("Product ID not provided for EditProductView.");
					mainLayout.setCenter(productsView);
				}
				break;
			case "ViewAllCouriers":
				viewAllCouriersView = new ViewAllCouriersView();
				mainLayout.setCenter(viewAllCouriersView);
				break;
			case "ViewAllOrders":
				viewAllOrdersView = new ViewAllOrdersView();
				mainLayout.setCenter(viewAllOrdersView);
				break;
			default:
				mainLayout.setCenter(productsView);
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

	public void handleLogout() {
		session.clearSession();
		changePageTo("Login");
		refreshNavbar(); // Refresh navbar to show logged-out view
	}

}
