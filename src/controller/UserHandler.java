package controller;

import model.CustomerDAO;
import model.Customer;
import model.Payload;
import model.Session;
import model.User;
import model.UserDAO;

public class UserHandler {

    UserDAO userDAO = new UserDAO();
    CustomerDAO customerDAO = new CustomerDAO();
    private Session session = Session.getInstance();

    public Payload EditProfile(String fullName, String email, String newPassword, String confirmPassword, String phone,
            String address,
            String gender) {

        User currentUser = session.getCurrentUser();
        if (currentUser == null) {
            return new Payload("No user logged in to edit profile.", null, false);
        }

        String passwordToUse = currentUser.getPassword();

        // Handle password update if provided
        if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            Payload passwordPayload = updatePassword(newPassword, confirmPassword);
            if (!passwordPayload.isSuccess()) {
                return passwordPayload;
            }
            passwordToUse = newPassword;
        }

        Payload validationResult = validateUserData(fullName, email, passwordToUse, phone, address, gender, true);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        boolean success = userDAO.updateUser(currentUser.getIdUser(), fullName, email, passwordToUse, phone, address,
                gender);
        if (success) {
            currentUser.setFullName(fullName);
            currentUser.setEmail(email);
            currentUser.setPassword(passwordToUse);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);
            currentUser.setGender(gender);
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

    public Payload SaveDataCustomer(String fullName, String email, String password, String confirmPassword,
            String phone, String address,
            String gender) {

        // Validate password confirmation
        Payload passwordCheckPayload = validatePasswordMatch(password, confirmPassword);
        if (!passwordCheckPayload.isSuccess()) {
            return passwordCheckPayload;
        }

        Payload validationResult = validateUserData(fullName, email, password, phone, address, gender, false);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        boolean result = userDAO.saveUser(fullName, password, email, phone, address, gender, "CUSTOMER");

        if (!result) {
            return new Payload("Failed to register user", null, false);
        }

        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return new Payload("Failed to retrieve registered user", null, false);
        }

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
            return new Payload("Gender must be chosen", null, false);
        }
        if (!gender.equals("Male") && !gender.equals("Female")) {
            return new Payload("Invalid gender selected", null, false);
        }

        // Email validation - must end with @gmail.com
        if (email == null || email.trim().isEmpty()) {
            return new Payload("Email must be filled", null, false);
        }
        if (!email.endsWith("@gmail.com")) {
            return new Payload("Email must end with @gmail.com", null, false);
        }

        User userByEmail = userDAO.getUserByEmail(email);
        if (isEdit) {
            User currentUser = session.getCurrentUser();
            if (userByEmail != null && !userByEmail.getIdUser().equals(currentUser.getIdUser())) {
                return new Payload("Email must be unique", null, false);
            }
        } else {
            if (userByEmail != null) {
                return new Payload("Email must be unique", null, false);
            }
        }

        // Password validation - at least 6 characters
        if (password == null || password.trim().isEmpty()) {
            return new Payload("Password must be filled", null, false);
        }
        if (password.length() < 6) {
            return new Payload("Password must be at least 6 characters", null, false);
        }

        // Phone validation - numeric and 10-13 digits
        if (phone == null || phone.trim().isEmpty()) {
            return new Payload("Phone must be filled", null, false);
        }

        // Check if phone contains only digits
        boolean isNumeric = true;
        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) {
                isNumeric = false;
                break;
            }
        }

        if (!isNumeric) {
            return new Payload("Phone must be numeric", null, false);
        }

        if (phone.length() < 10 || phone.length() > 13) {
            return new Payload("Phone must be 10–13 digits", null, false);
        }

        // Address validation
        if (address == null || address.trim().isEmpty()) {
            return new Payload("Address must be filled", null, false);
        }

        return new Payload("Validation successful", null, true);
    }

    private Payload validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return new Payload("Passwords do not match", null, false);
        }
        return new Payload("Passwords match", null, true);
    }

    private Payload updatePassword(String newPassword, String confirmPassword) {
        // If both are empty, skip password update
        if (newPassword.isEmpty() && confirmPassword.isEmpty()) {
            return new Payload("No password change", null, true);
        }

        // If one is filled but not both, return error
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return new Payload("Both password fields must be filled.", null, false);
        }

        // Validate passwords match
        Payload matchPayload = validatePasswordMatch(newPassword, confirmPassword);
        if (!matchPayload.isSuccess()) {
            return matchPayload;
        }

        return new Payload("Password validation successful", null, true);
    }
}
