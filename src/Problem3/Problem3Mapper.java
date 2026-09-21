package Problem3;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Problem3Mapper
        extends Mapper<Object, Text, Text, Text> {

    private Text customerId = new Text();
    private Text outputValue = new Text();

    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        // Skip header
        if (line.startsWith("orderId") || line.startsWith("customerId")) {
            return;
        }

        String[] fields = line.split(",");

        // Orders file
        if (fields.length == 6) {

            String id = fields[1];
            String quantity = fields[3];
            String price = fields[4];

            outputValue.set(
                "ORD|" + quantity + "|" + price
            );

            customerId.set(id);
            context.write(customerId, outputValue);
        }

        // Customers file
        else if (fields.length == 3) {

            String id = fields[0];
            String city = fields[1];
            String membershipTier = fields[2];

            outputValue.set(
                "CUST|" + city + "|" + membershipTier
            );

            customerId.set(id);
            context.write(customerId, outputValue);
        }
    }
}