package Problem2;

import java.io.IOException;
import java.util.Locale;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class Problem2Reducer
        extends Reducer<Text, DoubleWritable, Text, Text> {

    private final Text outputValue = new Text();

    @Override
    public void reduce(
            Text city,
            Iterable<DoubleWritable> revenues,
            Context context)
            throws IOException, InterruptedException {

        double totalRevenue = 0.0;

        for (DoubleWritable revenue : revenues) {

            totalRevenue += revenue.get();
        }

        /*
         * Match the Python reducer's:
         *
         * print(f"{current_city}\t{current_total:.2f}")
         */
        outputValue.set(
                String.format(Locale.US, "%.2f", totalRevenue)
        );

        context.write(city, outputValue);
    }
}
