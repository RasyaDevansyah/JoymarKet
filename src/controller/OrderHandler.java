package controller;

import model.Customer;
import model.CustomerDAO;
import model.Payload;
import model.User;

public class OrderHandler {

    public Payload validateCheckout(double totalAmount, User user) {
        if (!(user instanceof Customer)) {
            return new Payload("Only customers can perform checkout.", null, false);
        }

        Customer customer = (Customer) user;
        // Retrieve the latest customer data to ensure balance is up-to-date
        CustomerDAO customerDAO = new CustomerDAO();
        Customer updatedCustomer = customerDAO.getCustomerById(customer.getIdUser());

        if (updatedCustomer == null) {
            return new Payload("Customer data not found.", null, false);
        }

        if (updatedCustomer.getBalance() < totalAmount) {
            return new Payload("Insufficient balance. Current balance: Rp " + String.format("%.2f", updatedCustomer.getBalance()), null, false);
        }

        
        
        return new Payload("Balance is sufficient.", null, true);


    }
}
