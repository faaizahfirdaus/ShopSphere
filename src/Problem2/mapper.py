#!/usr/bin/env python3
import sys

# Stage 1: load the small customers file into memory ONCE.
# In Hadoop, -files ships customers.csv into the mapper's working directory,
# so we open it by its plain name.
city_of = {}
with open("customers.csv") as f:
    for line in f:
        line = line.strip()
        if not line or line.startswith("customerId"):   # skip header
            continue
        parts = line.split(",")
        if len(parts) >= 2:
            city_of[parts[0].strip()] = parts[1].strip()

# Stage 2: for each order, look up its city and emit (city, revenue).
for line in sys.stdin:
    line = line.strip()
    if not line or line.startswith("orderId"):           # skip header
        continue
    f = line.split(",")
    try:
        customer_id = f[1].strip()
        quantity = int(f[3])
        price = float(f[4])
    except (IndexError, ValueError):
        continue                                          # skip malformed rows
    city = city_of.get(customer_id)
    if city is None:
        continue              # order for an unknown customer — skip it
    revenue = quantity * price
    print(f"{city}\t{revenue}")
