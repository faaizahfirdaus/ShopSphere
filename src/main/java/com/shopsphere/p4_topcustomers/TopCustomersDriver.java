package com.shopsphere.p4_topcustomers;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TopCustomersDriver {

    public static void main(String[] args) throws Exception {

        String input = "/shopsphere/input/orders.csv";
        String spendOutput = "/shopsphere/output/p4_spend_totals";
        String top5Output = "/shopsphere/output/p4_top5";

        Configuration conf = new Configuration();

        // JOB 1: Calculate total spending for each customer
        Job job1 = Job.getInstance(conf, "ShopSphere Total Customer Spend");

        job1.setJarByClass(TopCustomersDriver.class);

        job1.setMapperClass(SpendMapper.class);
        job1.setReducerClass(SpendReducer.class);

        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(DoubleWritable.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job1, new Path(input));
        FileOutputFormat.setOutputPath(job1, new Path(spendOutput));

        if (!job1.waitForCompletion(true)) {
            System.exit(1);
        }

        // JOB 2: Find top 5 customers
        Job job2 = Job.getInstance(conf, "ShopSphere Top 5 Customers");

        job2.setJarByClass(TopCustomersDriver.class);

        job2.setMapperClass(TopNMapper.class);
        job2.setReducerClass(TopNReducer.class);

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(Text.class);

        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);

        job2.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job2, new Path(spendOutput));
        FileOutputFormat.setOutputPath(job2, new Path(top5Output));

        if (!job2.waitForCompletion(true)) {
            System.exit(1);
        }

        System.out.println("======================================");
        System.out.println("ShopSphere Top 5 Customers Completed");
        System.out.println("======================================");
    }
}
