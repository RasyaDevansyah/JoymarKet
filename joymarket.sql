-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 22, 2025 at 02:23 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

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
CREATE DATABASE IF NOT EXISTS `joymarket` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `joymarket`;

-- 1. Parent Table: User
CREATE TABLE users (
    id_user VARCHAR(50) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    role VARCHAR(20) NOT NULL -- 'ADMIN', 'CUSTOMER', 'COURIER'
);

-- 2. Subclass Table: Admin
CREATE TABLE admins (
    id_user VARCHAR(50) PRIMARY KEY,
    emergency_contact VARCHAR(100),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);

-- 3. Subclass Table: Customer
CREATE TABLE customers (
    id_user VARCHAR(50) PRIMARY KEY,
    balance DECIMAL(10, 2) DEFAULT 0.0,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);

-- 4. Subclass Table: Courier
CREATE TABLE couriers (
    id_user VARCHAR(50) PRIMARY KEY,
    vehicle_type VARCHAR(50),
    vehicle_plate VARCHAR(20),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);

-- 5. Product Table
CREATE TABLE products (
    id_product VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    category VARCHAR(50)
);

-- 6. Promo Table
CREATE TABLE promos (
    id_promo VARCHAR(50) PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    headline VARCHAR(255),
    discount_percentage DECIMAL(5, 2)
);

-- 7. Order Header Table
CREATE TABLE order_headers (
    id_order VARCHAR(50) PRIMARY KEY,
    id_customer VARCHAR(50) NOT NULL,
    id_promo VARCHAR(50),
    status VARCHAR(50),
    ordered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12, 2),
    FOREIGN KEY (id_customer) REFERENCES customers(id_user),
    FOREIGN KEY (id_promo) REFERENCES promos(id_promo)
);

-- 8. Order Detail Table (Composite PK)
CREATE TABLE order_details (
    id_order VARCHAR(50),
    id_product VARCHAR(50),
    qty INT NOT NULL,
    PRIMARY KEY (id_order, id_product),
    FOREIGN KEY (id_order) REFERENCES order_headers(id_order),
    FOREIGN KEY (id_product) REFERENCES products(id_product)
);

-- 9. Delivery Table
CREATE TABLE deliveries (
    id_order VARCHAR(50) PRIMARY KEY, -- Assuming one delivery per order
    id_courier VARCHAR(50) NOT NULL,
    status VARCHAR(50),
    FOREIGN KEY (id_order) REFERENCES order_headers(id_order),
    FOREIGN KEY (id_courier) REFERENCES couriers(id_user)
);

-- 10. Cart Item Table (Composite PK)
CREATE TABLE cart_items (
    id_customer VARCHAR(50),
    id_product VARCHAR(50),
    count INT NOT NULL,
    PRIMARY KEY (id_customer, id_product),
    FOREIGN KEY (id_customer) REFERENCES customers(id_user),
    FOREIGN KEY (id_product) REFERENCES products(id_product)
);