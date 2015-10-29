package decisionTree;

import java.util.ArrayList;
import java.util.Random;

public class ForestConstructor {
	
	
	public ArrayList<Node> buildForest(ArrayList<Tuple> trainingData, int numOfTrees, double trainingDataSplitRatio) {
		ArrayList<Node> forests = new ArrayList<Node>();

		
		TreeConstructor treeConstructor = new TreeConstructor();
		
		for (int i=0;i<numOfTrees;i++)
		{
			ArrayList<Tuple> trainingSplit = new ArrayList<Tuple> ();
			ArrayList<Tuple> testingSplit = new ArrayList<Tuple> ();
			splitDataSet(trainingData,trainingSplit, testingSplit,trainingDataSplitRatio);	
			//System.out.println(trainingData.size()+" "+trainingSplit.size()+" "+testingSplit.size());
			boolean[] featuresCouldUse = initialFeaturesUse(trainingSplit.get(0).columnValue.size());
			Node node = treeConstructor.buildTree(trainingSplit, featuresCouldUse);
			forests.add(node);
		}
		 
		return forests;		
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
