import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ShopReducer
        extends Reducer<Text, Text, Text, Text> {

    Map<String, Integer> cityOrders = new HashMap<>();

    Map<String, Double> tierValue = new HashMap<>();

    Map<String, Integer> tierCount = new HashMap<>();

    int totalOrders = 0;

    public void reduce(Text key, Iterable<Text> values,
                        Context context)
            throws IOException, InterruptedException {

        String city = "";
        String tier = "";

        ArrayList<Double> orders = new ArrayList<>();

        // Separate customer information and order information
        for (Text v : values) {

            String[] p = v.toString().split("\\|");

            if (p[0].equals("C")) {

                city = p[1];
                tier = p[2];

            } else {

                orders.add(
                    Double.parseDouble(p[1])
                );
            }
        }

        // Process all orders of this customer
        for (double amount : orders) {

            // City-wise order count
            cityOrders.put(
                city,
                cityOrders.getOrDefault(city, 0) + 1
            );

            totalOrders++;

            // Membership tier total value
            tierValue.put(
                tier,
                tierValue.getOrDefault(tier, 0.0) + amount
            );

            // Membership tier order count
            tierCount.put(
                tier,
                tierCount.getOrDefault(tier, 0) + 1
            );
        }
    }

    protected void cleanup(Context context)
            throws IOException, InterruptedException {

        // -------------------------------
        // CITY-WISE ORDER PERCENTAGE
        // -------------------------------

        context.write(
            new Text("CITY-WISE ORDER PERCENTAGE"),
            new Text("")
        );

        for (String city : cityOrders.keySet()) {

            double percent =
                cityOrders.get(city) * 100.0
                / totalOrders;

            String result =
                cityOrders.get(city)
                + " orders, "
                + String.format("%.2f", percent)
                + "%";

            context.write(
                new Text(city),
                new Text(result)
            );
        }

        // -------------------------------
        // MEMBERSHIP TIER ANALYSIS
        // -------------------------------

        context.write(
            new Text("MEMBERSHIP TIER ANALYSIS"),
            new Text("")
        );

        for (String tier : tierValue.keySet()) {

            double average =
                tierValue.get(tier)
                / tierCount.get(tier);

            String result =
                "Average Order Value = "
                + String.format("%.2f", average);

            context.write(
                new Text(tier),
                new Text(result)
            );
        }
    }
}
