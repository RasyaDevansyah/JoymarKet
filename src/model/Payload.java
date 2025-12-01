package model;

public class Payload {

    private String message;
    private Object data;
    public boolean success;

    public Payload(String message, Object data, boolean success) {
        this.message = message;
        this.data = data;
        this.success = success;
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

}
