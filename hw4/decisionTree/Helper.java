package decisionTree;

import java.util.ArrayList;
import java.util.Random;

public class Helper {
	public void splitDataSet(ArrayList<Tuple> trainingData,
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
