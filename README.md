# JoyMarket - E-Commerce Application

JoyMarket is a comprehensive e-commerce platform built with Java and JavaFX that facilitates online shopping with role-based access control for customers, admins, and couriers.

## Table of Contents

1. [System Overview](#system-overview)
2. [User Roles](#user-roles)
3. [Features Documentation](#features-documentation)
4. [Installation & Setup](#installation--setup)

---

## System Overview

JoyMarket is a multi-role e-commerce system that allows:
- **Customers** to browse products, manage their cart, and place orders
- **Admins** to manage product inventory and assign deliveries to couriers
- **Couriers** to track and update delivery statuses

---

## User Roles

### 1. Customer
- Browse and purchase products
- Manage shopping cart
- Top up account balance
- View order history
- Edit profile
- Apply promo codes

### 2. Admin
- Manage product inventory (edit stock)
- View all customers and orders
- View all courier information
- Assign orders to couriers for delivery

### 3. Courier
- View assigned deliveries
- Update delivery status (Pending → In Progress → Delivered)
- View customer information for each delivery

---

## Features Documentation

### CUSTOMER FEATURES

#### 1. Register Account

**Purpose:** Create a new customer account

**Steps:**
1. Click on "Register" from the login page
2. Fill in the following information:
   - **Full Name:** Cannot be empty
   - **Email:** Must end with @gmail.com and be unique
   - **Password:** Minimum 6 characters
   - **Confirm Password:** Must match the password field
   - **Phone:** Must be numeric and 10-13 digits
   - **Address:** Must be at least 4 characters
   - **Gender:** Select Male or Female

3. Click "Register"
4. Upon successful registration, you will be redirected to the login page
5. Log in with your email and password

**Validation Rules:**
- All fields are mandatory
- Email must be unique in the system
- Password and Confirm Password must match

---

#### 2. Viewing Products

**Purpose:** Browse available products in the store

**Steps:**
1. Log in to your customer account
2. Navigate to the "Products" section
3. View all available products in a table format

**What You'll See:**
- Product ID
- Product Name
- Price (in Rupiah)
- Stock Availability
- Category
- Action Button (Add to Cart)

**Note:** Products with 0 stock are automatically filtered out for customers

---

#### 3. Add Product to Cart

**Purpose:** Add desired products to your shopping cart

**Steps:**
1. From the Products page, locate the product you want to purchase
2. Click the "Add to Cart" button in the Action column
3. A success message will appear
4. You will be automatically redirected to the Cart page

**Requirements:**
- You must be logged in as a customer
- The product must have available stock

---

#### 4. Update Cart Item

**Purpose:** Change the quantity of items in your cart

**Steps:**
1. Navigate to the Cart page
2. In the "Quantity" column, use the spinner control to adjust the quantity
3. The quantity can be increased or decreased based on available stock
4. The "Subtotal" and "Total" will automatically update

**Constraints:**
- Quantity cannot exceed available stock
- Minimum quantity is 1
- Out-of-stock items will have a disabled spinner

---

#### 5. Remove Cart Item

**Purpose:** Delete items from your shopping cart

**Steps:**
1. Navigate to the Cart page
2. Locate the item you want to remove
3. Click the "Delete" button in the Action column
4. A confirmation dialog will appear asking you to confirm the deletion
5. Click "OK" to confirm removal
6. The item will be removed from your cart

**Note:** The total amount will automatically recalculate after removal

---

#### 6. Top Up Balance

**Purpose:** Add funds to your account to purchase products

**Steps:**
1. Navigate to the Top Up page (or Balance page)
2. Enter the amount you want to add
3. Click "Top Up"

**Validation Rules:**
- Amount must be filled and numeric
- Minimum top-up amount is Rp 10,000
- The new balance will immediately reflect in your account

**After Top Up:**
- Your updated balance will be displayed on the Cart page
- You can proceed to checkout with your new balance

---

#### 7. Checkout and Place Order

**Purpose:** Complete your purchase and place an order

**Steps:**
1. Navigate to your Cart page
2. Review all items and the total amount
3. **(Optional)** Apply a promo code:
   - Enter the promo code in the "Enter promo code" field
   - Click "Apply Promo"
   - The discount will be displayed and the total will update
4. Click "Checkout"
5. Your order will be processed if:
   - You have sufficient balance
   - All products have available stock

**What Happens During Checkout:**
- Your balance is deducted by the order total
- Order details are saved to the database
- Your cart is automatically cleared
- If a promo code was applied, it's marked as used
- You'll receive a success message and be redirected to Order History

**Checkout Validations:**
- Your balance must be >= total amount
- All items must have sufficient stock
- Cart cannot be empty

---

#### 8. View Order History

**Purpose:** See all your past orders and their statuses

**Steps:**
1. Navigate to the "Order History" page
2. View a table with all your orders

**Information Displayed:**
- Order ID
- Order Date
- Total Amount (with promo discount if applied)
- Promo Code (if used)
- Order Status (Pending, In Progress, Delivered)

**Order Statuses:**
- **Pending:** Order received, awaiting courier assignment
- **In Progress:** Courier has picked up the order and is delivering
- **Delivered:** Order has been successfully delivered

---

#### 9. Edit Product Stock (as Admin)

**Purpose:** Update product inventory and manage stock levels

**Steps:**
1. Navigate to the "Products" page (Admin view)
2. Locate the product you want to edit
3. Click the "Edit" button in the Action column
4. On the Edit Product page, modify:
   - Product Name
   - Price
   - Stock
   - Category

5. Click "Save" to update the product

**Validation Rules:**
- Product Name: Cannot be empty
- Price: Cannot be negative
- Stock: Cannot be negative
- Category: Cannot be empty

**Note:** Stock changes will immediately affect customer's ability to purchase

---

#### 10. View All Couriers (as Admin)

**Purpose:** See all registered couriers in the system

**Steps:**
1. Navigate to the "Couriers" page (Admin section)
2. View a table with all courier information

**Information Displayed:**
- Courier ID
- Full Name
- Email
- Phone
- Address
- Status (if available)

---

#### 11. View All Orders (as Admin)

**Purpose:** Monitor all customer orders in the system

**Steps:**
1. Navigate to the "All Orders" page (Admin section)
2. View a table with all orders

**Information Displayed:**
- Order ID
- Customer ID
- Customer Name
- Order Date
- Total Amount
- Current Status (Pending, In Progress, Delivered)
- Promo Code (if applied)

---

#### 12. Assign Order to Courier (as Admin)

**Purpose:** Assign pending orders to couriers for delivery

**Steps:**
1. Navigate to the "All Orders" page
2. Identify orders with "Pending" status
3. Click the "Assign Courier" or "Assign" button
4. Select a courier from the available list
5. Click "Confirm"
6. The order will be assigned to the selected courier

**What Happens After Assignment:**
- A delivery record is created
- The courier can now see the order in their Deliveries list
- The order status remains "Pending" until the courier updates it

---

#### 13. View Assigned Deliveries (as Courier)

**Purpose:** See all orders assigned to you for delivery

**Steps:**
1. Log in as a courier
2. Navigate to the "My Deliveries" page
3. View a table with all assigned orders

**Information Displayed:**
- Order ID
- Customer ID
- Customer Name
- Customer Email
- Customer Phone
- Customer Address
- Order Date
- Total Amount
- Current Status (Pending, In Progress, Delivered)

---

#### 14. Edit Delivery Status (as Courier)

**Purpose:** Update the status of orders you're delivering

**Steps:**
1. Navigate to the "My Deliveries" page
2. Locate the order you want to update
3. Click "Update Status" button
4. The status will progress as follows:
   - Pending → In Progress
   - In Progress → Delivered
   - Delivered → (Cannot change further)

5. A confirmation message will appear
6. The delivery list will refresh with the updated status

**Status Flow:**
- **Pending:** Initial state when assigned to courier
- **In Progress:** You've picked up the order and are on the way
- **Delivered:** Order has been successfully delivered to the customer

---

#### 15. Edit Profile

**Purpose:** Update your personal information (Available for all user roles)

**Steps:**
1. Navigate to the "My Profile" page
2. Update your personal information:
   - Full Name
   - Email
   - Phone
   - Address
   - Gender

3. **(Optional)** Update your password:
   - Enter your new password
   - Confirm it in the "Confirm Password" field
   - Both fields must match if you want to update

4. Click "Save Changes"

**Profile Validation Rules:**
- Full Name: Cannot be empty
- Email: Must end with @gmail.com and be unique
- Phone: Must be numeric and 10-13 digits
- Address: Cannot be empty
- Gender: Must be selected
- Password: If updating, minimum 6 characters and must match confirmation

---

## Installation & Setup

### Requirements
- Java 11 or higher
- JavaFX SDK
- MySQL or compatible database
- Eclipse IDE (recommended)

### Setup Steps

1. **Clone/Import Project:**
   - Import the JoymarKet project into Eclipse

2. **Database Setup:**
   - Create a MySQL database named `joymarket`
   - Execute the SQL schema scripts provided

3. **Configure Database Connection:**
   - Update database credentials in the DAO classes if needed

4. **Run Application:**
   - Locate `Main.java`
   - Right-click and select "Run As" → "Java Application"
   - The login screen will appear

### Default Login Credentials (if applicable)
- Check with your system administrator for test credentials

---

## Error Handling

The application provides user-friendly error messages for:
- Invalid input data
- Insufficient balance during checkout
- Out-of-stock products
- Duplicate email registration
- Invalid password formats
- Database connection errors

All error messages are displayed in alert dialogs for easy visibility.

---

## Security Features

- Password validation (minimum 6 characters)
- Email uniqueness verification
- Session management (automatic logout capability)
- Role-based access control
- Input validation on all forms

---

## Support

For issues or questions regarding JoyMarket, please contact the system administrator.

---

**Last Updated:** 2024
**Version:** 1.0