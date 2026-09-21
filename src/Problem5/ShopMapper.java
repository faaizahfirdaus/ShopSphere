import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ShopMapper extends Mapper<Object, Text, Text, Text> {

    Text outKey = new Text();
    Text outValue = new Text();

    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        
        if (line.startsWith("customerId") || line.startsWith("orderId")) {
            return;
        }

        String[] f = line.split(",");

        
        if (f.length == 3) {

            outKey.set(f[0]);

            // C | city | membershipTier
            outValue.set("C|" + f[1] + "|" + f[2]);

        } 
        // Order record
        else {

            double amount =
                    Double.parseDouble(f[3]) *
                    Double.parseDouble(f[4]);

            // Customer ID is at index 1
            outKey.set(f[1]);

            // O | orderAmount
            outValue.set("O|" + amount);
        }

        context.write(outKey, outValue);
    }
}
