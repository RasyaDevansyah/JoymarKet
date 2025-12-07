package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Connect;

public class UserDAO {

    private Connect connect = Connect.getInstance();

    public boolean saveUser(String fullName, String password, String email, String phone, String address, String gender, String role) { 
        String sql = "INSERT INTO users (full_name, password, email, phone, address, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connect.preparedStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, gender);
            pstmt.setString(7, role);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String idUser = rs.getString("id_user");
                String fullName = rs.getString("full_name");
                String userEmail = rs.getString("email");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String gender = rs.getString("gender");
                String role = rs.getString("role");

                switch (role) {
                    case "ADMIN":
                        String adminSql = "SELECT emergency_contact FROM admins WHERE id_user = ?";
                        try (PreparedStatement adminPstmt = connect.preparedStatement(adminSql)) {
                            adminPstmt.setString(1, idUser);
                            ResultSet adminRs = adminPstmt.executeQuery();
                            if (adminRs.next()) {
                                String emergencyContact = adminRs.getString("emergency_contact");
                                return new Admin(idUser, fullName, userEmail, password, phone, address, gender, emergencyContact);
                            }
                        }
                        break;
                    case "CUSTOMER":
                        String customerSql = "SELECT balance FROM customers WHERE id_user = ?";
                        try (PreparedStatement customerPstmt = connect.preparedStatement(customerSql)) {
                            customerPstmt.setString(1, idUser);
                            ResultSet customerRs = customerPstmt.executeQuery();
                            if (customerRs.next()) {
                                double balance = customerRs.getDouble("balance");
                                return new Customer(idUser, fullName, userEmail, password, phone, address, gender, balance);
                            }
                        }
                        break;
                    case "COURIER":
                        String courierSql = "SELECT vehicle_type, vehicle_plate FROM couriers WHERE id_user = ?";
                        try (PreparedStatement courierPstmt = connect.preparedStatement(courierSql)) {
                            courierPstmt.setString(1, idUser);
                            ResultSet courierRs = courierPstmt.executeQuery();
                            if (courierRs.next()) {
                                String vehicleType = courierRs.getString("vehicle_type");
                                String vehiclePlate = courierRs.getString("vehicle_plate");
                                return new Courier(idUser, fullName, userEmail, password, phone, address, gender, vehicleType, vehiclePlate);
                            }
                        }
                        break;
                }
                // Fallback to generic User if subclass data not found or role is unknown
                return new User(idUser, fullName, userEmail, password, phone, address, gender, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(String idUser, String fullName, String email, String password, String phone,
            String address, String gender) {
        String sql = "UPDATE users SET full_name = ?, email = ?, password = ?, phone = ?, address = ?, gender = ? WHERE id_user = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, gender); 
            pstmt.setString(7, idUser);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(String idUser) {
        String sql = "SELECT * FROM users WHERE id_user = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, idUser);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userId = rs.getString("id_user");
                String fullName = rs.getString("full_name");
                String userEmail = rs.getString("email");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String gender = rs.getString("gender");
                String role = rs.getString("role");

                switch (role) {
                    case "ADMIN":
                        String adminSql = "SELECT emergency_contact FROM admins WHERE id_user = ?";
                        try (PreparedStatement adminPstmt = connect.preparedStatement(adminSql)) {
                            adminPstmt.setString(1, userId);
                            ResultSet adminRs = adminPstmt.executeQuery();
                            if (adminRs.next()) {
                                String emergencyContact = adminRs.getString("emergency_contact");
                                return new Admin(userId, fullName, userEmail, password, phone, address, gender, emergencyContact);
                            }
                        }
                        break;
                    case "CUSTOMER":
                        String customerSql = "SELECT balance FROM customers WHERE id_user = ?";
                        try (PreparedStatement customerPstmt = connect.preparedStatement(customerSql)) {
                            customerPstmt.setString(1, userId);
                            ResultSet customerRs = customerPstmt.executeQuery();
                            if (customerRs.next()) {
                                double balance = customerRs.getDouble("balance");
                                return new Customer(userId, fullName, userEmail, password, phone, address, gender, balance);
                            }
                        }
                        break;
                    case "COURIER":
                        String courierSql = "SELECT vehicle_type, vehicle_plate FROM couriers WHERE id_user = ?";
                        try (PreparedStatement courierPstmt = connect.preparedStatement(courierSql)) {
                            courierPstmt.setString(1, userId);
                            ResultSet courierRs = courierPstmt.executeQuery();
                            if (courierRs.next()) {
                                String vehicleType = courierRs.getString("vehicle_type");
                                String vehiclePlate = courierRs.getString("vehicle_plate");
                                return new Courier(userId, fullName, userEmail, password, phone, address, gender, vehicleType, vehiclePlate); 
                            }
                        }
                        break;
                }
                return new User(userId, fullName, userEmail, password, phone, address, gender, role); // Pass gender
            }
        }

         catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
