package controller;

import main.Main;
import model.Payload;
import model.User;

public class UserHandler {

    public void EditProfile(String fullName, String email, String password, String phone, String address) {
        // Code to edit user profile
    }

    public void GetUser(String idUser) {
        // Code to get user information
    }

    public Payload RegisterCustomer(String fullName, String email, String password, String phone, String address) {

        // User newUser = new User(fullName, email, password, phone, address);

        // Main.getInstance().setCurrentUser(user);
        // Code to register a new customer
        return new Payload("User registered successfully", null, true);

    }

    public Payload LoginCustomer(String email, String password) {

        User dummyUser = new User("1", "John Doe", email, password, "1234567890", "123 Main St", "Customer");
        Main.getInstance().setCurrentUser(dummyUser);
        Main.getInstance().updateNavbarForUser(dummyUser.getUsername());
        return new Payload("User logged in successfully", null, true);
    }

}
