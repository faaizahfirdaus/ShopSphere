package Problem2;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Problem2Driver {

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {

            System.out.println(
                    "Usage: Problem2Driver <orders> <customers> <output>"
            );

            System.exit(1);
        }

        Configuration conf = new Configuration();

        FileSystem fs = FileSystem.get(conf);

        Path ordersPath = new Path(args[0]);
        Path customersPath = new Path(args[1]);
        Path outputPath = new Path(args[2]);

        /*
         * Remove old output directory if it already exists.
         */
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        /*
         * --------------------------------------------------
         * Hadoop MapReduce Job
         * --------------------------------------------------
         */

        Job job = Job.getInstance(
                conf,
                "ShopSphere Problem 2 Revenue By City"
        );

        job.setJarByClass(Problem2Driver.class);

        /*
         * Mapper
         */
        job.setMapperClass(Problem2Mapper.class);

        /*
         * Reducer
         */
        job.setReducerClass(Problem2Reducer.class);

        /*
         * Mapper output:
         *
         * city -> revenue
         */
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        /*
         * Final output:
         *
         * city -> total revenue
         */
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        /*
         * --------------------------------------------------
         * Distributed Cache
         * --------------------------------------------------
         *
         * Every mapper needs customers.csv locally.
         *
         * #customers.csv creates the local filename
         * expected by Problem2Mapper.
         */
        job.addCacheFile(
                new URI(
                        customersPath.toString() + "#customers.csv"
                )
        );

        /*
         * Input = orders.csv
         */
        FileInputFormat.addInputPath(
                job,
                ordersPath
        );

        /*
         * Output = final city totals
         */
        FileOutputFormat.setOutputPath(
                job,
                outputPath
        );

        /*
         * Run Hadoop job.
         */
        System.exit(
                job.waitForCompletion(true) ? 0 : 1
        );
    }
}
