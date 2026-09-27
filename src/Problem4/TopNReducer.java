package com.shopsphere.p4_topcustomers;

import java.io.IOException;
import java.util.PriorityQueue;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TopNReducer extends Reducer<Text, Text, Text, Text> {

    private static class Customer {
        String id;
        double spend;

        Customer(String id, double spend) {
            this.id = id;
            this.spend = spend;
        }
    }

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        PriorityQueue<Customer> top5 =
                new PriorityQueue<>((a, b) ->
                        Double.compare(a.spend, b.spend));

        for (Text value : values) {

            String[] parts = value.toString().split("\\t");

            if (parts.length != 2) {
                continue;
            }

            try {
                String customerId = parts[0];
                double spend = Double.parseDouble(parts[1]);

                top5.add(new Customer(customerId, spend));

                if (top5.size() > 5) {
                    top5.poll();
                }

            } catch (NumberFormatException e) {
                // Ignore invalid values
            }
        }

        while (!top5.isEmpty()) {
            Customer c = top5.poll();

            context.write(
                    new Text(c.id),
                    new Text(String.format("%.2f", c.spend))
            );
        }
    }
}
