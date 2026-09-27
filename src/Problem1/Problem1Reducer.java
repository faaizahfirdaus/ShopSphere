import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Problem1Reducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        double totalRevenue = 0;
        int orderCount = 0;

        for (Text value : values) {

            String[] parts = value.toString().split(",");

            totalRevenue += Double.parseDouble(parts[0]);
            orderCount += Integer.parseInt(parts[1]);
        }

        context.write(key, new Text(totalRevenue + "\t" + orderCount));
    }
}
