package Problem2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.util.HashMap;
import java.util.Map;

public class Problem2Mapper
        extends Mapper<Object, Text, Text, DoubleWritable> {

    private final Map<String, String> cityOf = new HashMap<>();

    private final Text city = new Text();
    private final DoubleWritable revenue = new DoubleWritable();

    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException {

        /*
         * customers.csv is distributed to the mapper's working directory
         * by the Driver using Hadoop's distributed cache.
         */
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("customers.csv"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty() || line.startsWith("customerId")) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length >= 2) {

                    String customerId = parts[0].trim();
                    String customerCity = parts[1].trim();

                    cityOf.put(customerId, customerCity);
                }
            }
        }
    }

    @Override
    public void map(
            Object key,
            Text value,
            Context context)
            throws IOException, InterruptedException {

        String line = value.toString().trim();

        // Skip empty lines and header
        if (line.isEmpty() || line.startsWith("orderId")) {
            return;
        }

        String[] fields = line.split(",");

        try {

            /*
             * Orders format:
             *
             * 0 = orderId
             * 1 = customerId
             * 2 = ...
             * 3 = quantity
             * 4 = price
             */

            String customerId = fields[1].trim();

            int quantity = Integer.parseInt(fields[3].trim());

            double price = Double.parseDouble(fields[4].trim());

            String customerCity = cityOf.get(customerId);

            // Unknown customer -> skip order
            if (customerCity == null) {
                return;
            }

            double orderRevenue = quantity * price;

            city.set(customerCity);
            revenue.set(orderRevenue);

            context.write(city, revenue);

        } catch (IndexOutOfBoundsException |
                 NumberFormatException e) {

            // Skip malformed rows
        }
    }
}
