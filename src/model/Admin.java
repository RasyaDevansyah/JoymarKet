package model;

public class Admin extends User {
    private String emergencyContact;

    public Admin(String idUser, String fullName, String email, String password, String phone, String address,
            String role,
            String emergencyContact) {
        super(idUser, fullName, email, password, phone, address, role);
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

}
