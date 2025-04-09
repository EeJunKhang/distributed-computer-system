CREATE SCHEMA `food_ordering_database` ;

USE `food_ordering_database` ;

/*
-- Create --
*/

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    address VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    role ENUM('CUSTOMER', 'RESTAURANT', 'DELIVERY_PERSON', 'ADMIN') NOT NULL,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(50),
    image_url VARCHAR(255),
	stock_quantity INT NOT NULL CHECK (stock_quantity >= 0),
	last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELED') NOT NULL DEFAULT 'PENDING',
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Order_Items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price_per_unit DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE
);

CREATE TABLE Payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount_paid DECIMAL(10,2) NOT NULL,
    payment_method ENUM('Credit Card', 'Debit Card', 'PayPal', 'Cash on Delivery') NOT NULL,
    transaction_id VARCHAR(50) UNIQUE NOT NULL,
	payment_status ENUM('Pending', 'Completed', 'Failed', 'Refunded') NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

CREATE TABLE Logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    action TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

/*
-- Insert --
*/

INSERT INTO Users (first_name, last_name, username, password_hash, password_salt, email, address, contact_number, role)
VALUES
('John', 'Doe', 'johndoe', 'hashedpassword1', 'randomsalt1', 'john.doe@email.com', '123 Main St, City, Country', '555-1234', 'customer'),
('Jane', 'Smith', 'janesmith', 'hashedpassword2', 'randomsalt2', 'jane.smith@email.com', '456 Elm St, City, Country', '555-5678', 'admin');

INSERT INTO Orders (user_id, total_price, status, order_time)
VALUES
(1, 25.50, 'PENDING', CURRENT_TIMESTAMP),
(2, 49.99, 'DELIVERED', CURRENT_TIMESTAMP);

INSERT INTO Products (name, description, price, category, image_url, stock_quantity)
VALUES
('Bagel', 'Freshly baked sesame bagel', 2.50, 'Bakery', 'http://example.com/images/bagel.jpg', 100),
('Coffee', 'Hot brewed coffee with milk', 3.00, 'Beverages', 'http://example.com/images/coffee.jpg', 50);

INSERT INTO Order_Items (order_id, product_id, quantity, price_per_unit)
VALUES
(1, 1, 3, 2.50),  -- 3 bagels in order 1
(2, 2, 2, 3.00);  -- 2 coffees in order 2

INSERT INTO Payments (order_id, amount_paid, payment_method, transaction_id, payment_status)
VALUES
(1, 25.50, 'Credit Card', 'trans123abc', 'Completed'),
(2, 49.99, 'PayPal', 'trans456xyz', 'Completed');

INSERT INTO Logs (user_id, action)
VALUES
(1, 'Placed order #1'),
(2, 'Placed order #2');



-- Drop tables
SELECT CONCAT('DROP TABLE IF EXISTS `', table_name, '`;') -- Get the command and run
	FROM information_schema.tables
	WHERE table_schema = 'food_ordering_database';

SET FOREIGN_KEY_CHECKS = 0; -- Disable foreign key checks
DROP TABLE IF EXISTS `Users`;
DROP TABLE IF EXISTS `Products`;
DROP TABLE IF EXISTS `Orders`;
DROP TABLE IF EXISTS `Order_Items`;
DROP TABLE IF EXISTS `Payments`;
DROP TABLE IF EXISTS `Logs`;
SET FOREIGN_KEY_CHECKS = 1; -- Re-enable foreign key checks


DROP DATABASE food_ordering_database;
CREATE SCHEMA `food_ordering_database` ;
USE `dbvent` ;