package decisionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainPipeLineRF {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	//	String fileName = "trainTiny";
		String fileName = "EURUSD-GBPUSD_out";
		boolean withColumnName = false;
		
		ArrayList<Tuple> trainingData = readByfileName(fileName,withColumnName);
		ForestConstructor forestConstructor = new ForestConstructor();
		ArrayList<Node> forest = forestConstructor.buildForest(trainingData, 1, 0.66);

		ArrayList<Tuple> testingData = readByfileName(fileName,withColumnName);
		for (Node root:forest)
		{
			PrintTree(root,0);
			System.out.println("===================");
		}
		ForestTester treeTester= new ForestTester();
		double accuracy = treeTester.test(forest,testingData);
		
		System.out.print("Testing Accuracy:" + accuracy);
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
	
	private static ArrayList<Tuple> readByfileName(String fileName, boolean withColumnName) throws IOException {
		// TODO Auto-generated method stub
	    BufferedReader reader = new BufferedReader(new FileReader(fileName));
	    String line;
	    ArrayList<Tuple> tuples = new ArrayList<>();
		while ((line = reader.readLine()) != null)
	    {
	    	String[] strs = line.split(" ");
	    	Tuple tuple = new Tuple();
	    	for (int i=0;i<strs.length;i++)
	    	{
	    		if (i!=strs.length-1)
	    			tuple.columnValue.add(Double.parseDouble(strs[i]));
	    		else
	    			tuple.labelValue = Double.parseDouble(strs[i]);
	    	}
	    	tuples.add(tuple);
	    }
		return tuples;
		
	}
}
