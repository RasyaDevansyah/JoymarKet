package controller;

import model.CustomerDAO; // Import CustomerDAO
import model.Customer; // Import Customer class
import model.Payload;
import model.Session;
import model.User;
import model.UserDAO;

public class UserHandler {

    UserDAO userDAO = new UserDAO();
    CustomerDAO customerDAO = new CustomerDAO(); // Initialize CustomerDAO
    private Session session = Session.getInstance();

    public Payload EditProfile(String fullName, String email, String password, String phone, String address,
            String gender) { // Added gender

        User currentUser = session.getCurrentUser();
        if (currentUser == null) {
            return new Payload("No user logged in to edit profile.", null, false);
        }

        Payload validationResult = validateUserData(fullName, email, password, phone, address, gender, true); // Pass
                                                                                                              // gender
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        boolean success = userDAO.updateUser(currentUser.getIdUser(), fullName, email, password, phone, address,
                gender); // Pass gender
        if (success) {
            // Update session user with new details
            currentUser.setFullName(fullName);
            currentUser.setEmail(email);
            currentUser.setPassword(password);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);
            currentUser.setGender(gender); // Set gender
            return new Payload("Profile updated successfully.", currentUser, true);
        } else {
            return new Payload("Failed to update profile.", null, false);
        }
    }

    public Payload GetUser(String idUser) {
        User user = userDAO.getUserById(idUser);
        if (user != null) {
            return new Payload("User retrieved successfully.", user, true);
        }
        return new Payload("User not found.", null, false);
    }

    public Payload SaveDataCustomer(String fullName, String email, String password, String phone, String address,
            String gender) { // Added gender

        Payload validationResult = validateUserData(fullName, email, password, phone, address, gender, false); // Pass
                                                                                                               // gender
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        boolean result = userDAO.saveUser(fullName, password, email, phone, address, gender, "CUSTOMER"); // Pass gender

        if (!result) {
            return new Payload("Failed to register user", null, false);
        }

        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return new Payload("Failed to retrieve registered user", null, false);
        }

        // Create customer entry
        boolean customerCreated = customerDAO.createCustomer(user.getIdUser());
        if (!customerCreated) {
            return new Payload("Failed to create customer profile", null, false);
        }

        return new Payload("User registered successfully", null, true);
    }

    public Payload Login(String email, String password) {

        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return new Payload("User not found", null, false);
        }
        if (!user.getPassword().equals(password)) {
            return new Payload("Incorrect password", null, false);
        }
        session.setCurrentUser(user); // Set the current user in the session
        return new Payload("Login successful", user, true);
    }

    public void LogoutCustomer() {
        session.clearSession();
    }

    public Payload TopUpBalance(String customerId, String amountText) {
        if (customerId == null || customerId.trim().isEmpty()) {
            return new Payload("Customer ID cannot be empty.", null, false);
        }

        // Validation: Must be filled.
        if (amountText == null || amountText.trim().isEmpty()) {
            return new Payload("Top-up amount must be filled.", null, false);
        }

        double amount;
        try {
            // Validation: Must be numeric.
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            return new Payload("Top-up amount must be numeric.", null, false);
        }

        // Validation: Minimum 10,000.
        if (amount < 10000) {
            return new Payload("Minimum top-up amount is Rp 10,000.", null, false);
        }

        boolean success = customerDAO.updateBalance(customerId, amount);
        if (success) {
            // Update the current user\'s balance in the session
            User currentUser = session.getCurrentUser();
            if (currentUser instanceof Customer) {
                Customer customer = (Customer) currentUser;
                customer.setBalance(customer.getBalance() + amount);
                session.setCurrentUser(customer);
            }
            return new Payload("Top-up successful.", null, true);
        } else {
            return new Payload("Failed to process top-up.", null, false);
        }
    }

    private Payload validateUserData(String fullName, String email, String password, String phone, String address,
            String gender, boolean isEdit) { // Added gender parameter
        // FullName validation
        if (fullName == null || fullName.trim().isEmpty()) {
            return new Payload("Full name cannot be empty", null, false);
        }

        // Gender validation
        if (gender == null || gender.trim().isEmpty()) {
            return new Payload("Gender must be selected", null, false);
        }
        if (!gender.equals("Male") && !gender.equals("Female")) {
            return new Payload("Invalid gender selected", null, false);
        }

        // Email validation
        if (email == null || email.trim().isEmpty()) {
            return new Payload("Email cannot be empty", null, false);
        }
        if (!email.matches(
                "^[\\w!#$%&\' *+/=?`{|}~^-]+(?:\\.[\\w!#$%&\' *+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            return new Payload("Invalid email format", null, false);
        }

        User userByEmail = userDAO.getUserByEmail(email);
        if (isEdit) {
            User currentUser = session.getCurrentUser();
            if (userByEmail != null && !userByEmail.getIdUser().equals(currentUser.getIdUser())) {
                return new Payload("Email is already registered by another user", null, false);
            }
        } else {
            if (userByEmail != null) {
                return new Payload("Email is already registered", null, false);
            }
        }

        // Password validation
        if (password == null || password.trim().isEmpty()) {
            return new Payload("Password cannot be empty", null, false);
        }
        if (password.length() < 6) { // Example: minimum 6 characters
            return new Payload("Password must be at least 6 characters long", null, false);
        }

        // Phone validation
        if (phone == null || phone.trim().isEmpty()) {
            return new Payload("Phone number cannot be empty", null, false);
        }
        if (!phone.matches("^\\d+$")) { // Only digits
            return new Payload("Phone number must contain only digits", null, false);
        }

        // Address validation
        if (address == null || address.trim().isEmpty()) {
            return new Payload("Address cannot be empty", null, false);
        }
        if (address.length() < 4) { // Example: minimum 10 characters
            return new Payload("Address must be at least 4 characters long", null, false);
        }

        return new Payload("Validation successful", null, true);
    }
}
