package model;

public class User {
        private String idUser;
        private String fullName;
        private String email;
        private String password;
        private String phone;
        private String address;
        private String gender; // New field
        private String role;

        public User(String idUser, String fullName, String email, String password, String phone, String address, String gender, String role) {
            this.idUser = idUser;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.gender = gender; // Set gender
            this.role = role;
        }


        public User(String fullName, String email, String password, String phone, String address, String gender) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.gender = gender;
            this.idUser = null;
            this.role = "CUSTOMER"; 
        }

        public String getIdUser() {
            return idUser;
        }

        public String getUsername() {
            return fullName;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        // New getter and setter for gender
        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getRole() {
            return role;
        }

        public void setIdUser(String idUser) {
            this.idUser = idUser;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
