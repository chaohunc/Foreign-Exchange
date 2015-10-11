package decisionTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainPipeLine {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	//	String fileName = "trainTiny";
	//	String fileName = "EURUSD-GBPUSD_out";
		String fileName = "EURUSD-GBPUSD_2_out";
		boolean withColumnName = false;
		
		ArrayList<Tuple> trainingData = readByfileName(fileName,withColumnName);
		TreeConstructor treeConstructor= new TreeConstructor();
		boolean[] featuresCouldUse = new boolean[trainingData.get(0).columnValue.size()];
		for (int i=0;i<featuresCouldUse.length;i++)
			featuresCouldUse[i] = true;
		//System.out.println(featuresCouldUse.length);
		featuresCouldUse[15] = false;
		ArrayList<Tuple> trainingSplit = new ArrayList<Tuple>();
		ArrayList<Tuple> testingSplit = new ArrayList<Tuple>();
		Helper helper= new Helper(); 
		helper.splitDataSet(trainingData,trainingSplit,testingSplit,0.8);
		//System.out.println(trainingSplit.size()+" "+testingSplit.size());
		Node root = treeConstructor.buildTree(trainingSplit, featuresCouldUse);
		PrintTree(root,0);
		//ArrayList<Tuple> testingData = readByfileName(fileName,withColumnName);
		TreeTester treeTester= new TreeTester();
		double accuracy = treeTester.test(root,testingSplit);
		System.out.print("Testing Accuracy:" + accuracy);
	}


	
	private static void PrintTree(Node root, int level) {
		// TODO Auto-generated method stub
		if (root.isLeaf==true)
			System.out.println("t" + root.label);
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
