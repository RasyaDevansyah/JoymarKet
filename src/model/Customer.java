package model;

public class Customer extends User {
    private double balance;

    public Customer(String idUser, String fullName, String email, String password, String phone, String address) {
        super(idUser, fullName, email, password, phone, address, "CUSTOMER");
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}