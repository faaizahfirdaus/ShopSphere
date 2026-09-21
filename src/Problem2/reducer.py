#!/usr/bin/env python3
import sys

current_city = None
current_total = 0.0

for line in sys.stdin:
    line = line.strip()
    if not line:
        continue
    city, revenue = line.split("\t")
    revenue = float(revenue)
    if city == current_city:
        current_total += revenue
    else:
        if current_city is not None:
            print(f"{current_city}\t{current_total:.2f}")
        current_city = city
        current_total = revenue

# Don't forget the last city — the classic reducer bug.
if current_city is not None:
    print(f"{current_city}\t{current_total:.2f}")
