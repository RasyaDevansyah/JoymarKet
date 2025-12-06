package controller;

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
            existingDelivery.setStatus("Assigned"); // Or a more appropriate status
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
}
