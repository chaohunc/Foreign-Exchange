package decisionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class MainPipeLineRFCassandra {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
	//	String fileName = "trainTiny";
		String fileName = "EURUSD-GBPUSD_out";
		boolean withColumnName = false;
		int treeNum = 100;
		ArrayList<Tuple> trainingData = readByCanssandra("EURUSD",withColumnName);
		System.out.println(trainingData.size());
		ForestConstructor forestConstructor = new ForestConstructor();
		ArrayList<Node> forest = forestConstructor.buildForest(trainingData, treeNum, 0.66);

		ArrayList<Tuple> testingData = readByCanssandra("EURUSD",withColumnName);
		for (Node root:forest)
		{
			PrintTree(root,0);
			System.out.println("===================");
		}


		ForestSerializer forestSerializer= new ForestSerializer();
		serializeRFtoCanssandra(forestSerializer.serializeForest(forest));		
		ArrayList<String> strs= deserializeRFfromCanssandra("rfmodel");
		ArrayList<Node> forestAfterSerialzation = forestSerializer.deSerializeForest(strs);
		
		ForestTester treeTester= new ForestTester();
		double accuracy = treeTester.test(forestAfterSerialzation,testingData);
		System.out.print("Testing Accuracy:" + accuracy);
		
		Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		Session session = cluster.connect("FE");
		session.execute("INSERT INTO rfmodelAcc (treeNum,Accuracy) VALUES ("+treeNum+","+accuracy+")");
	}

	
	
	private static ArrayList<String> deserializeRFfromCanssandra(String tableName) {
		// TODO Auto-generated method stub
		Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		Session session = cluster.connect("FE");
		ResultSet results = session.execute("SELECT * FROM "+tableName);
	    ArrayList<String> trees = new ArrayList<>();
	    for (Row row : results) {
	    	trees.add(row.getString("tree"));
		}
	    return trees;
	}



	private static void PrintTree(Node root, int level) {
		// TODO Auto-generated method stub
		if (root!=null)
		{
			for (int i=0;i<level;i++)
				System.out.print("|");
			System.out.println("Attribute "+root.attributeNum+">"+root.condition);
			level++;
			if (root.left!=null)
				PrintTree(root.left,level);
			if (root.right!=null)
				PrintTree(root.right,level);
		
			level--;
		}
	}
	
	private static ArrayList<Tuple> readByCanssandra(String tableName, boolean withColumnName) throws IOException {
		// TODO Auto-generated method stub
		Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		Session session = cluster.connect("FE");
		ResultSet results = session.execute("SELECT * FROM "+tableName);
	    ArrayList<Tuple> tuples = new ArrayList<>();
	    for (Row row : results) {
			Tuple tuple = new Tuple();
	    	tuple.columnValue.add(Double.parseDouble(row.getString("timestamp")));
	    	for (int i=1;i<=14;i++)
	    		tuple.columnValue.add((double) row.getFloat("f"+i));
	    	tuple.labelValue = (double)row.getInt("usdeurcloseaskdirection");
	    	tuples.add(tuple);
		}

		return tuples;
	}
	
	private static void serializeRFtoCanssandra(ArrayList<String> forest)
	{
		Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		Session session = cluster.connect("FE");
		for (int i=0;i<forest.size();i++)
		{
			session.execute("INSERT INTO rfmodel (id,tree) VALUES ("+i+",'"+forest.get(i)+"')");
		}
	}
	
	
}
