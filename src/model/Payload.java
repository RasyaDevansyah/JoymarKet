package model;

public class Payload {

    private String message;
    private Object data;
    public boolean success;
    private String promoId; // New field for promo ID

    public Payload(String message, Object data, boolean success) {
        this.message = message;
        this.data = data;
        this.success = success;
        this.promoId = null; // Initialize to null for existing constructor
    }

    public Payload(String message, Object data, boolean success, String promoId) {
        this.message = message;
        this.data = data;
        this.success = success;
        this.promoId = promoId;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPromoId() {
        return promoId;
    }

}
