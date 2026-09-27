package com.shopsphere.p4_topcustomers;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SpendReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    @Override
    public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
            throws IOException, InterruptedException {

        double total = 0.0;

        for (DoubleWritable value : values) {
            total += value.get();
        }

        context.write(key, new DoubleWritable(total));
    }
}
