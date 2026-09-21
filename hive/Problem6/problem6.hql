CREATE DATABASE IF NOT EXISTS shopsphere;

USE shopsphere;

-- Orders table
CREATE EXTERNAL TABLE IF NOT EXISTS orders (
    orderId STRING,
    customerId STRING,
    category STRING,
    quantity INT,
    price DOUBLE,
    orderDate STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/shopsphere/input/orders';


-- Raw customers CSV table
CREATE EXTERNAL TABLE IF NOT EXISTS customers_csv (
    customerId STRING,
    city STRING,
    membershipTier STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE
LOCATION '/shopsphere/input/customers';


-- RCFILE customer table
CREATE TABLE IF NOT EXISTS customers (
    customerId STRING,
    city STRING,
    membershipTier STRING
)
STORED AS RCFILE;


INSERT INTO TABLE customers
SELECT
    customerId,
    city,
    membershipTier
FROM customers_csv;


-- Problem 3
SELECT
    c.membershipTier,
    AVG(o.quantity * o.price) AS average_order_value
FROM orders o
JOIN customers c
ON o.customerId = c.customerId
GROUP BY c.membershipTier;