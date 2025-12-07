package controller;

import java.util.List;
import java.util.ArrayList;

import model.Delivery;
import model.DeliveryDAO;
import model.Payload;

public class DeliveryHandler {

    private DeliveryDAO deliveryDAO = new DeliveryDAO();

    public Payload assignOrderToCourier(int orderId, String courierId) {
        // Check if a delivery already exists for this order
        Delivery existingDelivery = deliveryDAO.getDeliveryByOrderId(orderId);

        if (existingDelivery != null) {
            // Update existing delivery
            existingDelivery.setIdCourier(courierId);
            existingDelivery.setStatus("Assigned");
            if (deliveryDAO.updateDelivery(existingDelivery)) {
                return new Payload("Courier reassigned successfully.", null, true);
            }
            return new Payload("Failed to reassign courier.", null, false);
        } else {
            // Create new delivery
            Delivery newDelivery = new Delivery(String.valueOf(orderId), courierId, "Assigned");
            if (deliveryDAO.insertDelivery(newDelivery)) {
                return new Payload("Courier assigned successfully.", null, true);
            }
            return new Payload("Failed to assign courier.", null, false);
        }
    }

    public Payload getDeliveriesByCourierId(String courierId) {
        List<Delivery> deliveries = deliveryDAO.getDeliveriesByCourierId(courierId);
        if (deliveries != null) {
            return new Payload("Deliveries retrieved successfully.", deliveries, true);
        }
        return new Payload("Failed to retrieve deliveries.", new ArrayList<>(), false);
    }
}
