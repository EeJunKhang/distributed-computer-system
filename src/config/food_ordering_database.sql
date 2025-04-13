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
-- Insert --users
*/
-- Inserting Users
INSERT INTO Users (user_id, first_name, last_name, username, password_hash, password_salt, email, address, contact_number, role)
VALUES
(-1, 'asd', 'asd', 'asd', 'wXCqjyAV0PfhvFXYtqB+JCU+otdv2khuTxUvUlbMABU=', 'B1+UNw+BdM+AjTflj7i1NA==', 'asd@email.com', 'Cyber , City, Country', '000-000', 'admin');

INSERT INTO Users (first_name, last_name, username, password_hash, password_salt, email, address, contact_number, role)
VALUES
('Andy', 'Andrew', 'andrew', 'wXCqjyAV0PfhvFXYtqB+JCU+otdv2khuTxUvUlbMABU=', 'B1+UNw+BdM+AjTflj7i1NA==', 'andy.andrew@email.com', '123 Oak St, City, Country', '555-1111', 'customer'),
('Leyley', 'Ashley', 'ashley', 'wXCqjyAV0PfhvFXYtqB+JCU+otdv2khuTxUvUlbMABU=', 'B1+UNw+BdM+AjTflj7i1NA==', 'leyley.ashley@email.com', '123 Oak St, City, Country', '555-1111', 'customer'),
('Barbara', 'Baker', 'barbara', 'wXCqjyAV0PfhvFXYtqB+JCU+otdv2khuTxUvUlbMABU=', 'B1+UNw+BdM+AjTflj7i1NA==', 'barbara.baker@email.com', '456 Birch St, City, Country', '555-2222', 'customer'),
('Clara', 'Carter', 'clara', 'wXCqjyAV0PfhvFXYtqB+JCU+otdv2khuTxUvUlbMABU=', 'B1+UNw+BdM+AjTflj7i1NA==', 'clara.carter@email.com', '789 Cedar St, City, Country', '555-3333', 'customer'),
('David', 'Davis', 'david', 'wXCqjyAV0PfhvFXYtqB+JCU+otdv2khuTxUvUlbMABU=', 'B1+UNw+BdM+AjTflj7i1NA==', 'david.davis@email.com', '101 Pine St, City, Country', '555-4444', 'customer');

INSERT INTO Products (name, description, price, category, image_url, stock_quantity)
VALUES
('Apple', 'Fresh and juicy red apple', 1.50, 'Fruits', 'src/resources/images/temp.png', 200),
('Banana', 'Ripe and sweet yellow banana', 0.75, 'Fruits', 'src/resources/images/temp.png', 150),
('Carrot', 'Crunchy and nutritious carrot', 1.20, 'Vegetables', 'src/resources/images/temp.png', 180),
('Date', 'Sweet and chewy dates', 2.50, 'Fruits', 'src/resources/images/temp.png', 80),
('Eggplant', 'Fresh purple eggplant', 1.75, 'Vegetables', 'src/resources/images/temp.png', 100);

INSERT INTO Orders (user_id, total_price, status, order_time)
VALUES
(1, 30.00, 'PENDING', CURRENT_TIMESTAMP),
(2, 35.50, 'CONFIRMED', CURRENT_TIMESTAMP),
(3, 12.50, 'PREPARING', CURRENT_TIMESTAMP),
(4, 45.00, 'OUT_FOR_DELIVERY', CURRENT_TIMESTAMP),
(5, 47.50, 'DELIVERED', CURRENT_TIMESTAMP),  -- Order 5: DELIVERED
(5, 50.00, 'CANCELED', CURRENT_TIMESTAMP);   -- Order 6: CANCELED

-- Inserting Order Items (make sure each order has corresponding items)
INSERT INTO Order_Items (order_id, product_id, quantity, price_per_unit)
VALUES
(1, 1, 10, 1.50),  -- 10 apples in order 1
(2, 2, 15, 0.75),  -- 15 bananas in order 2
(3, 3, 5, 1.20),   -- 5 carrots in order 3
(4, 4, 6, 2.50),   -- 6 dates in order 4
(5, 4, 7, 2.50),   -- 6 dates in order 5 (DELIVERED)
(6, 4, 8, 2.50);   -- 6 dates in order 6 (CANCELED)

-- Inserting Payments (one for each order)
INSERT INTO Payments (order_id, amount_paid, payment_method, transaction_id, payment_status)
VALUES
(1, 30.00, 'Credit Card', 'trans123abc', 'Completed'),
(2, 35.50, 'PayPal', 'trans456xyz', 'Completed'),
(3, 12.50, 'Credit Card', 'trans789xyz', 'Failed'),
(4, 45.00, 'PayPal', 'trans987abc', 'Pending'),
(5, 45.00, 'PayPal', 'trans987abac', 'Completed'),  -- Payment for DELIVERED order
(6, 45.00, 'Credit Card', 'trans987xyz', 'Completed'); -- Payment for CANCELED order

-- Inserting Logs (user actions for both orders of user_id = 5)
INSERT INTO Logs (user_id, action)
VALUES
(1, 'Placed order #1'),
(2, 'Placed order #2'),
(3, 'Placed order #3'),
(4, 'Placed order #4'),
(5, 'Placed order #5'),  -- Action for DELIVERED order
(5, 'Order #5 marked as DELIVERED'),  -- Status change to DELIVERED
(5, 'Placed order #6'),  -- Action for CANCELED order
(5, 'Order #6 marked as CANCELED'); -- Status change to CANCELED




-- Drop tables
SELECT CONCAT('DROP TABLE IF EXISTS `', table_name, '`;') 
	FROM information_schema.tables
	WHERE table_schema = 'food_ordering_database';

USE `food_ordering_database` ;
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