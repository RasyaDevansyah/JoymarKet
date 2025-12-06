package controller;

import java.util.ArrayList;
import java.util.List;

import model.Courier;
import model.CourierDAO;
import model.Payload;

public class CourierHandler {

    private CourierDAO courierDAO = new CourierDAO();

    public Payload getAllCouriers() {
        List<Courier> couriers = courierDAO.getAllCouriers();
        if (couriers != null) {
            return new Payload("Couriers retrieved successfully.", couriers, true);
        }
        return new Payload("Failed to retrieve couriers.", new ArrayList<>(), false);
    }
}
