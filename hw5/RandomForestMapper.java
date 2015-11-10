import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.io.
//public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class RandomForestMapper extends Mapper<LongWritable, Text, Text, Text> {
//  private Point testExample;
  private static Text label = new Text();
  private static DoubleWritable distance = new DoubleWritable();
  private ArrayList<Tuple> trainingData =
          new ArrayList<Tuple>();
  
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

	ArrayList<Tuple> trainingSplit = new ArrayList<Tuple> ();
	ArrayList<Tuple> testingSplit = new ArrayList<Tuple> ();
    Configuration conf = context.getConfiguration();

	splitDataSet(trainingData,trainingSplit, testingSplit,Double.parseDouble(conf.get("trainingDataSplitRatio")));	
	boolean[] featuresCouldUse = initialFeaturesUse(trainingSplit.get(0).columnValue.size());
	
	TreeConstructor treeConstructor = new TreeConstructor();
	Node node = treeConstructor.buildTree(trainingSplit, featuresCouldUse);
	ForestSerializer forestSerialzier = new ForestSerializer();
    label.set(forestSerialzier.serializeTree(node));
    context.write(new Text("1"), label);
  }
    
  /**
   * Return the cosine similarity of two examples.  Larger similarities 
   * mean the two examples are more similar
   */
  protected void setup(Context context)
          throws java.io.IOException, InterruptedException {
   
		Cluster cluster = Cluster.builder().addContactPoint("172.31.99.252").build();
		Session session = cluster.connect("FE");
		ResultSet results = session.execute("SELECT * FROM EURUSD");
	    //ArrayList<Tuple> tuples = new ArrayList<Tuple>();
	    for (Row row : results) {
			Tuple tuple = new Tuple();
	    	tuple.columnValue.add(Double.parseDouble(row.getString("timestamp")));
	    	for (int i=1;i<=14;i++)
	    		tuple.columnValue.add((double) row.getFloat("f"+i));
	    	tuple.labelValue = (double)row.getInt("usdeurcloseaskdirection");
	    	trainingData.add(tuple);
		}
	  System.out.println("done.");
  }
  
	private boolean[] initialFeaturesUse(int size) {
		// TODO Auto-generated method stub
		boolean[] featuresCouldUse = new boolean[size];
		double sqrtSize = Math.sqrt(size);
		Random generator = new Random(); 
		for (int i=0;i<= sqrtSize;i++)
		{
			int ran = generator.nextInt(size);
			while (featuresCouldUse[ran]==true)
				ran = generator.nextInt(size);
			featuresCouldUse[ran]=true;
		}
		return featuresCouldUse;
	}

	private void splitDataSet(ArrayList<Tuple> trainingData,
			ArrayList<Tuple> trainingSplit, ArrayList<Tuple> testingSplit, double trainingDataSplitRatio) {
		// TODO Auto-generated method stub

		Random generator = new Random(); 
		for (Tuple tuple: trainingData)
		{
			if (generator.nextDouble()<trainingDataSplitRatio)
				trainingSplit.add(tuple);
			else
				testingSplit.add(tuple);
		}
	}
}