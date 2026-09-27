import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Problem1Mapper extends Mapper<LongWritable, Text, Text, Text> {

    private Text category = new Text();
    private Text revenueCount = new Text();

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        // Skip header
        if (line.startsWith("orderId")) {
            return;
        }

        String[] fields = line.split(",");

        if (fields.length == 6) {

            String cat = fields[2];
            int quantity = Integer.parseInt(fields[3]);
            double price = Double.parseDouble(fields[4]);

            double revenue = quantity * price;

            category.set(cat);
            revenueCount.set(revenue + ",1");

            context.write(category, revenueCount);
        }
    }
}
