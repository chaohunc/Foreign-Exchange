package decisionTree;

import java.util.ArrayList;

public class TreeTester {

	public double test(Node root, ArrayList<Tuple> testingData) {
		// TODO Auto-generated method stub
		
		double totAcc = 0;
		for (Tuple tuple: testingData)
		{
			double classifiedLabel= classify(root,tuple.columnValue);
			if (Math.abs(tuple.labelValue-classifiedLabel)<0.0001)
				totAcc++;
		}
		return totAcc/testingData.size();
	}

	private double classify(Node root, ArrayList<Double> columnValue) {
		// TODO Auto-generated method stub
		if (columnValue.get(root.attributeNum)>root.condition)
			return 1;
		else 
			return -1;
	}

}
