-- phpMyAdmin SQL Dump
-- Updated for Auto Increment and Clean Re-import

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `joymarket`
--
DROP DATABASE IF EXISTS `joymarket`;
CREATE DATABASE IF NOT EXISTS `joymarket` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `joymarket`;

-- 1. Parent Table: User
-- Changed id_user to INT AUTO_INCREMENT
CREATE TABLE users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
        full_name VARCHAR(100) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        phone VARCHAR(20),
        address TEXT,
        gender ENUM("Male", "Female") NOT NULL,
        role ENUM("ADMIN", "CUSTOMER", "COURIER") NOT NULL
);

-- 2. Subclass Table: Admin
-- id_user is INT to match users, but not auto_increment (it shares the ID from users)
CREATE TABLE admins (
    id_user INT PRIMARY KEY,
    emergency_contact VARCHAR(100),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);

-- 3. Subclass Table: Customer
-- id_user is INT to match users
CREATE TABLE customers (
    id_user INT PRIMARY KEY,
    balance DECIMAL(10, 2) DEFAULT 0.0,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);

-- 4. Subclass Table: Courier
-- id_user is INT to match users
CREATE TABLE couriers (
    id_user INT PRIMARY KEY,
    vehicle_type VARCHAR(50),
    vehicle_plate VARCHAR(20),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);

-- 5. Product Table
-- Changed id_product to INT AUTO_INCREMENT
CREATE TABLE products (
    id_product INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    category VARCHAR(50)
);

-- 6. Promo Table
-- Changed id_promo to INT AUTO_INCREMENT
CREATE TABLE promos (
    id_promo INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    headline VARCHAR(255),
    discount_percentage DECIMAL(5, 2)
);

-- 7. Order Header Table
-- Changed id_order to INT AUTO_INCREMENT
-- Updated FKs to INT
CREATE TABLE order_headers (
    id_order INT AUTO_INCREMENT PRIMARY KEY,
    id_customer INT NOT NULL,
    id_promo INT,
    status VARCHAR(50),
    ordered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12, 2),
    FOREIGN KEY (id_customer) REFERENCES customers(id_user),
    FOREIGN KEY (id_promo) REFERENCES promos(id_promo)
);

-- 8. Order Detail Table (Composite PK)
-- Updated columns to INT
CREATE TABLE order_details (
    id_order INT,
    id_product INT,
    qty INT NOT NULL,
    PRIMARY KEY (id_order, id_product),
    FOREIGN KEY (id_order) REFERENCES order_headers(id_order),
    FOREIGN KEY (id_product) REFERENCES products(id_product)
);

-- 9. Delivery Table
-- id_order is INT (matches order_headers PK)
-- id_courier is INT (matches couriers PK)
CREATE TABLE deliveries (
    id_order INT PRIMARY KEY, -- One delivery per order
    id_courier INT NOT NULL,
    status VARCHAR(50),
    FOREIGN KEY (id_order) REFERENCES order_headers(id_order),
    FOREIGN KEY (id_courier) REFERENCES couriers(id_user)
);

-- 10. Cart Item Table (Composite PK)
-- Updated columns to INT
CREATE TABLE cart_items (
    id_customer INT,
    id_product INT,
    count INT NOT NULL,
    PRIMARY KEY (id_customer, id_product),
    FOREIGN KEY (id_customer) REFERENCES customers(id_user),
    FOREIGN KEY (id_product) REFERENCES products(id_product)
);

--
-- Sample Data
--

-- Admin User (1)
INSERT INTO users (full_name,  email, password, phone, address, gender, role) VALUES
("Admin User", "admin@joymarket.com", "12345678", "1234567890", "Admin Office, Joymarket Tower", "Male", "ADMIN");
INSERT INTO admins (id_user, emergency_contact) VALUES
((SELECT id_user FROM users WHERE email = "admin@joymarket.com"), "Admin Emergency Contact");

-- Customer User (1)
INSERT INTO users (full_name, email, password, phone, address, gender, role) VALUES
("Customer User", "customer@joymarket.com", "12345678", "0812345678", "Customer Home, Joymarket City", "Female", "CUSTOMER");
INSERT INTO customers (id_user, balance) VALUES
((SELECT id_user FROM users WHERE email = "customer@joymarket.com"), 1000.00);

-- Courier Users (3)
INSERT INTO users (full_name, email, password, phone, address, gender,role) VALUES
("Courier User 1", "courier1@joymarket.com", "12345678", "0821000001", "Courier Depot 1, Joymarket City", "Male" , "COURIER");
INSERT INTO couriers (id_user, vehicle_type, vehicle_plate) VALUES
((SELECT id_user FROM users WHERE email = "courier1@joymarket.com"), "Motorcycle", "B 1001 ABC");

INSERT INTO users (full_name, email, password, phone, address, gender, role) VALUES
("Courier User 2", "courier2@joymarket.com", "12345678", "0821000002", "Courier Depot 2, Joymarket City", "Male", "COURIER");
INSERT INTO couriers (id_user, vehicle_type, vehicle_plate) VALUES
((SELECT id_user FROM users WHERE email = "courier2@joymarket.com"), "Car", "B 2002 DEF");

INSERT INTO users (full_name, email, password, phone, address, gender, role) VALUES
("Courier User 3", "courier3@joymarket.com", "12345678", "0821000003", "Courier Depot 3, Joymarket City", "Male", "COURIER");
INSERT INTO couriers (id_user, vehicle_type, vehicle_plate) VALUES
((SELECT id_user FROM users WHERE email = "courier3@joymarket.com"), "Van", "B 3003 GHI");

-- Products (10 products with 20 stocks each) - unchanged from original
INSERT INTO products (name, price, stock, category) VALUES
("Premium Coffee Beans", 15900, 20, "Beverages"),
("Organic Green Tea", 12500, 20, "Beverages"),
("Artisan Bread", 5750, 20, "Bakery"),
("Fresh Milk (1L)", 18000, 20, "Dairy"),
("Chicken Breast (1kg)", 40000, 20, "Meat"),
("Assorted Vegetables Pack", 8990, 20, "Produce"),
("Local Honey (500g)", 9500, 20, "Pantry"),
("Olive Oil (750ml)", 18250, 20, "Pantry"),
("Dark Chocolate Bar", 7000, 20, "Snacks"),
("Sparkling Water (6-pack)", 4800, 20, "Beverages");

-- Promos (3)
INSERT INTO promos (code, headline, discount_percentage) VALUES
("SAVE10", "Get 10% off your first order!", 10.00);
INSERT INTO promos (code, headline, discount_percentage) VALUES
("FREEDELIVERY", "Free delivery on orders over $50!", 100.00);
INSERT INTO promos (code, headline, discount_percentage) VALUES
("HALFOFF", "50% off selected items!", 50.00);

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
