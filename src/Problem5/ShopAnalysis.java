import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ShopAnalysis {

    public static void main(String[] args) throws Exception {

        // Create Hadoop configuration
        Configuration conf = new Configuration();

        // Create MapReduce job
        Job job = Job.getInstance(
                conf,
                "City and Membership Analysis"
        );

        // Set the main class
        job.setJarByClass(ShopAnalysis.class);

        // Set Mapper class
        job.setMapperClass(ShopMapper.class);

        // Set Reducer class
        job.setReducerClass(ShopReducer.class);

        // Set output key and value types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Set input path
        FileInputFormat.addInputPath(
                job,
                new Path(args[0])
        );

        // Set output path
        FileOutputFormat.setOutputPath(
                job,
                new Path(args[1])
        );

        // Start the MapReduce job
        System.exit(
                job.waitForCompletion(true) ? 0 : 1
        );
    }
}
