import csv
import random
from datetime import datetime, timedelta

random.seed(42)

cities = [
    "Hyderabad",
    "Mumbai",
    "Delhi",
    "Bangalore",
    "Chennai",
    "Pune",
    "Kolkata",
    "Ahmedabad",
    "Jaipur",
    "Lucknow"
]

membership_tiers = ["Silver", "Gold", "Platinum"]

categories = [
    "Electronics",
    "Clothing",
    "Books",
    "Home",
    "Beauty",
    "Sports"
]

# Create customers.csv
customers = []

with open("data/customers.csv", "w", newline="") as file:
    writer = csv.writer(file)

    writer.writerow([
        "customerId",
        "city",
        "membershipTier"
    ])

    for i in range(1, 101):
        customer_id = f"C{i:03d}"
        city = random.choice(cities)
        membership = random.choice(membership_tiers)

        customers.append(
            (customer_id, city, membership)
        )

        writer.writerow([
            customer_id,
            city,
            membership
        ])

# Create orders.csv
start_date = datetime(2026, 1, 1)

with open("data/orders.csv", "w", newline="") as file:
    writer = csv.writer(file)

    writer.writerow([
        "orderId",
        "customerId",
        "category",
        "quantity",
        "price",
        "orderDate"
    ])

    for i in range(1, 1001):
        order_id = f"O{i:04d}"

        customer_id = random.choice(customers)[0]

        category = random.choice(categories)

        quantity = random.randint(1, 5)

        price = random.choice([
            199, 299, 499, 799,
            999, 1499, 1999,
            2499, 4999, 7999,
            9999, 14999
        ])

        order_date = start_date + timedelta(
            days=random.randint(0, 270)
        )

        writer.writerow([
            order_id,
            customer_id,
            category,
            quantity,
            price,
            order_date.strftime("%Y-%m-%d")
        ])

print("Dataset created successfully!")
print("customers.csv -> 100 customers")
print("orders.csv -> 1000 orders")