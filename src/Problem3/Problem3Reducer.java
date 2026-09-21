package Problem3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Problem3Reducer extends Reducer<Text, Text, Text, Text> {

    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        String membershipTier = null;
        List<Double> orderValues = new ArrayList<>();

        for (Text value : values) {

            String[] parts = value.toString().split("\\|");

            if (parts[0].equals("CUST")) {

                membershipTier = parts[2];

            } else if (parts[0].equals("ORD")) {

                int quantity = Integer.parseInt(parts[1]);
                double price = Double.parseDouble(parts[2]);

                double orderValue = quantity * price;

                orderValues.add(orderValue);
            }
        }

        if (membershipTier != null) {

            for (double orderValue : orderValues) {

                context.write(
                    new Text(membershipTier),
                    new Text(String.valueOf(orderValue))
                );
            }
        }
    }
}