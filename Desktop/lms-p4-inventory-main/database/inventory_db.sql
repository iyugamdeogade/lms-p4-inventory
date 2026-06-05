CREATE DATABASE inventory_db;

USE inventory_db;


CREATE TABLE category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL
);

CREATE TABLE vendor (
    vendor_id INT PRIMARY KEY AUTO_INCREMENT,
    vendor_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    address VARCHAR(200)
);

CREATE TABLE product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,

    category_id INT,
    vendor_id INT,

    quantity INT DEFAULT 0,
    price DOUBLE,
    reorder_level INT,

    FOREIGN KEY (category_id)
    REFERENCES category(category_id),

    FOREIGN KEY (vendor_id)
    REFERENCES vendor(vendor_id)
);

CREATE TABLE purchase_order (
    po_id INT PRIMARY KEY AUTO_INCREMENT,

    vendor_id INT,
    order_date DATE,

    total_amount DOUBLE,
    status VARCHAR(50),

    FOREIGN KEY (vendor_id)
    REFERENCES vendor(vendor_id)
);

CREATE TABLE order_item (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,

    po_id INT,
    product_id INT,

    quantity INT,
    price DOUBLE,

    FOREIGN KEY (po_id)
    REFERENCES purchase_order(po_id),

    FOREIGN KEY (product_id)
    REFERENCES product(product_id)
);

CREATE TABLE stock_entry (
    entry_id INT PRIMARY KEY AUTO_INCREMENT,

    product_id INT,

    quantity_added INT,
    entry_date DATE,

    FOREIGN KEY (product_id)
    REFERENCES product(product_id)
);


INSERT INTO category(category_name)
VALUES
('Stationery'),


INSERT INTO vendor(vendor_name, phone, address)
VALUES
('Om sai ram Traders', '9876543210', 'Nagpur'),


INSERT INTO product(product_name, category_id, vendor_id, quantity, price, reorder_level)
VALUES
('Notebook', 1, 1, 5, 40, 10),
('Pen', 1, 1, 20, 10, 5),
('Hammer', 2, 2, 3, 250, 5);

SELECT *
FROM product
WHERE quantity < reorder_level;


USE inventory_db;

SELECT * FROM category;
SELECT * FROM vendor;
SELECT * FROM product;