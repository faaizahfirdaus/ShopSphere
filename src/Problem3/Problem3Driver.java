package Problem3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Problem3Driver {

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {

            System.out.println(
                "Usage: Problem3Driver <orders> <customers> <output>"
            );

            System.exit(1);
        }

        Configuration conf = new Configuration();

        FileSystem fs = FileSystem.get(conf);

        Path ordersPath = new Path(args[0]);
        Path customersPath = new Path(args[1]);

        Path tempOutput =
                new Path("/shopsphere/p3_join_output");

        Path finalOutput =
                new Path(args[2]);

        if (fs.exists(tempOutput)) {
            fs.delete(tempOutput, true);
        }

        if (fs.exists(finalOutput)) {
            fs.delete(finalOutput, true);
        }

        // -------------------------------
        // JOB 1: Reduce-side Join
        // -------------------------------

        Job job1 = Job.getInstance(conf, "ShopSphere P3 Reduce Side Join");

        job1.setJarByClass(Problem3Driver.class);

        MultipleInputs.addInputPath(
                job1,
                ordersPath,
                TextInputFormat.class,
                Problem3Mapper.class
        );

        MultipleInputs.addInputPath(
                job1,
                customersPath,
                TextInputFormat.class,
                Problem3Mapper.class
        );

        job1.setReducerClass(Problem3Reducer.class);

        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(Text.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);

        FileOutputFormat.setOutputPath(job1, tempOutput);

        if (!job1.waitForCompletion(true)) {

            System.exit(1);
        }

        // -------------------------------
        // JOB 2: Average by Membership Tier
        // -------------------------------

        Job job2 =
                Job.getInstance(conf,
                        "ShopSphere P3 Average By Membership Tier");

        job2.setJarByClass(Problem3Driver.class);

        job2.setMapperClass(Problem3AggregationMapper.class);

        job2.setReducerClass(Problem3AggregationReducer.class);

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(DoubleWritable.class);

        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job2, tempOutput);

        FileOutputFormat.setOutputPath(job2, finalOutput);

        System.exit(job2.waitForCompletion(true) ? 0 : 1);
    }
}