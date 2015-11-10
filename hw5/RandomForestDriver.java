import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class RandomForestDriver {
    public static void main(String[] args) throws Exception {
    
    // initialized
    Configuration conf = new Configuration();
    if (args.length<=2)
        conf.set("trainingDataSplitRatio", "0.6666");
    else
      conf.set("trainingDataSplitRatio", args[2]);
    
    Job job = Job.getInstance(conf, "RandomForest");
    job.addFileToClassPath(new Path("/user/hue/cassandra-driver-core-2.0.2.jar")); 
    job.addFileToClassPath(new Path("/user/hue/metrics-core-3.0.2.jar")); 
    job.setJarByClass(RandomForestDriver.class);
    job.setMapperClass(RandomForestMapper.class);
    job.setReducerClass(RandomForestReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
