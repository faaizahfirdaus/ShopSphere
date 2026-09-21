orders = LOAD '/shopsphere/input/orders.csv'
USING PigStorage(',')
AS (
    orderId:chararray,
    customerId:chararray,
    category:chararray,
    quantity:int,
    price:double,
    orderDate:chararray
);

customers = LOAD '/shopsphere/input/customers.csv'
USING PigStorage(',')
AS (
    customerId:chararray,
    city:chararray,
    membershipTier:chararray
);


-- Remove headers
orders_clean =
    FILTER orders BY orderId != 'orderId';

customers_clean =
    FILTER customers BY customerId != 'customerId';


-- Join orders and customers
joined =
    JOIN orders_clean BY customerId,
         customers_clean BY customerId;


-- Calculate order value
with_value =
    FOREACH joined GENERATE
        customers_clean::membershipTier AS membershipTier,
        (double)orders_clean::quantity *
        orders_clean::price AS orderValue;


-- Group by membership tier
grouped =
    GROUP with_value BY membershipTier;


-- Calculate average
result =
    FOREACH grouped GENERATE
        group AS membershipTier,
        AVG(with_value.orderValue) AS averageOrderValue;


DUMP result;