package Problem3;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Problem3AggregationMapper
        extends Mapper<Object, Text, Text, DoubleWritable> {

    private Text membershipTier = new Text();
    private DoubleWritable orderValue = new DoubleWritable();

    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        String[] parts = line.split("\t");

        if (parts.length == 2) {

            membershipTier.set(parts[0]);
            orderValue.set(Double.parseDouble(parts[1]));

            context.write(membershipTier, orderValue);
        }
    }
}