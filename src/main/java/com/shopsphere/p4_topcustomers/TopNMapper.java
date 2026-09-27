package com.shopsphere.p4_topcustomers;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TopNMapper extends Mapper<Object, Text, Text, Text> {

    private Text keyOut = new Text("ALL");

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] fields = value.toString().trim().split("\\s+");

        if (fields.length >= 2) {
            context.write(keyOut, new Text(fields[0] + "\t" + fields[1]));
        }
    }
}
