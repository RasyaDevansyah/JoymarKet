package model;

public class Admin extends User {
    private String emergencyContact;

    public Admin(String idUser, String fullName, String email, String password, String phone, String address,
            String gender, String emergencyContact) {
        super(idUser, fullName, email, password, phone, address, gender, "ADMIN");
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

}
