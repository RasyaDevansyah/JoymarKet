package model;

public class User {
        private String idUser;
        private String fullName;
        private String email;
        private String password;
        private String phone;
        private String address;
        private String role;

        public User(String idUser, String fullName, String email, String password, String phone, String address, String role) {
            this.idUser = idUser;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.role = role;
        }

        // New constructor for login (without idUser and role, assuming these are set later or default)
        public User(String fullName, String email, String password, String phone, String address) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            // idUser and role can be set to null or default values
            this.idUser = null;
            this.role = "Customer"; // Default role for new users
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
