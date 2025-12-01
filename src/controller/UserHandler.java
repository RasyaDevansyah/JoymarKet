package controller;

import model.Payload;
import model.User;
import model.UserDAO;

public class UserHandler {

    UserDAO userDAO = new UserDAO();

    public void EditProfile(String fullName, String email, String password, String phone, String address) {
        // Code to edit user profile
    }

    public void GetUser(String idUser) {
        // Code to get user information
    }

    public Payload SaveDataUser(String fullName, String email, String password, String phone, String address) {
        // FullName validation
        if (fullName == null || fullName.trim().isEmpty()) {
            return new Payload("Full name cannot be empty", null, false);
        }

        // Email validation
        if (email == null || email.trim().isEmpty()) {
            return new Payload("Email cannot be empty", null, false);
        }
        if (!email.matches(
                "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            return new Payload("Invalid email format", null, false);
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

        boolean result = userDAO.saveUser(fullName, password, email, phone, address, "CUSTOMER");

        if (!result) {
            return new Payload("Failed to register user", null, false);
        } else {
            return new Payload("User registered successfully", null, true);
        }
    }

    public Payload LoginCustomer(String email, String password) {

        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            return new Payload("User not found", null, false);
        }
        if (!user.getPassword().equals(password)) {
            return new Payload("Incorrect password", null, false);
        }
        return new Payload("Login successful", user, true);
    }

}
