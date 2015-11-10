import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class RandomForestReducer extends Reducer<Text, Text, Text, Text> {

  // private IntWritable reducerResult = new IntWritable();
  public void reduce(Text key, Iterable<Text> values, Context context) throws IOException,
          InterruptedException {
    HashMap<String, Integer> hmap = new HashMap<String, Integer>();
    StringBuffer serializedForest = new StringBuffer();
    int i=0;
	Cluster cluster = Cluster.builder().addContactPoint("172.31.99.252").build();
	Session session = cluster.connect("FE");
    for (Text txt : values) {
    	String str = txt.toString();
    	serializedForest.append(str+"\n");
    	session.execute("INSERT INTO rfmodel (id,tree) VALUES ("+i+",'"+str+"')");
    	i++;
    }    
    context.write(key, new Text(serializedForest.toString()));
  }
}