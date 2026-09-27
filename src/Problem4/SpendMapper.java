package com.shopsphere.p4_topcustomers;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SpendMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

    private Text customerId = new Text();
    private DoubleWritable spend = new DoubleWritable();

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString().trim();

        if (line.isEmpty() || line.startsWith("orderId")) {
            return;
        }

        String[] fields = line.split(",");

        if (fields.length != 6) {
            return;
        }

        try {
            String customer = fields[1].trim();
            double quantity = Double.parseDouble(fields[3].trim());
            double price = Double.parseDouble(fields[4].trim());

            customerId.set(customer);
            spend.set(quantity * price);

            context.write(customerId, spend);

        } catch (NumberFormatException e) {
            // Ignore invalid records
        }
    }
}
