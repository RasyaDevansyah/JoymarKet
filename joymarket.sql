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
        role ENUM('ADMIN', 'CUSTOMER', 'COURIER') NOT NULL
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

-- Admin User
INSERT INTO users (full_name, email, password, phone, address, role) VALUES
('Admin User', 'admin@joymarket.com', 'admin', '1234567890', 'Admin Office, Joymarket Tower', 'ADMIN');
INSERT INTO admins (id_user, emergency_contact) VALUES
((SELECT id_user FROM users WHERE email = 'admin@joymarket.com'), 'Admin Emergency Contact');

-- Courier User
INSERT INTO users (full_name, email, password, phone, address, role) VALUES
('Courier User', 'courier@joymarket.com', 'courier', '0987654321', 'Courier Depot, Joymarket City', 'COURIER');
INSERT INTO couriers (id_user, vehicle_type, vehicle_plate) VALUES
((SELECT id_user FROM users WHERE email = 'courier@joymarket.com'), 'Motorcycle', 'B 1234 XYZ');

-- Products (10 products with 20 stocks each)
INSERT INTO products (name, price, stock, category) VALUES
('Premium Coffee Beans', 15.99, 20, 'Beverages'),
('Organic Green Tea', 12.50, 20, 'Beverages'),
('Artisan Bread', 5.75, 20, 'Bakery'),
('Fresh Milk (1L)', 3.20, 20, 'Dairy'),
('Chicken Breast (1kg)', 10.00, 20, 'Meat'),
('Assorted Vegetables Pack', 8.99, 20, 'Produce'),
('Local Honey (500g)', 9.50, 20, 'Pantry'),
('Olive Oil (750ml)', 18.25, 20, 'Pantry'),
('Dark Chocolate Bar', 4.00, 20, 'Snacks'),
('Sparkling Water (6-pack)', 7.80, 20, 'Beverages');

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
