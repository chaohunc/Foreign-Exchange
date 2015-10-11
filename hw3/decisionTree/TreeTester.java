package decisionTree;

import java.util.ArrayList;

public class TreeTester {

	public double test(Node root, ArrayList<Tuple> testingData) {
		// TODO Auto-generated method stub
		
		double totAcc = 0;
		for (Tuple tuple: testingData)
		{
			double classifiedLabel= classify(root,tuple.columnValue);
			//System.out.println(classifiedLabel + " "+tuple.labelValue);
			if (Math.abs(tuple.labelValue-classifiedLabel)<0.0001)
				totAcc++;
		}
		//System.out.println(totAcc);
		//System.out.println(testingData.size());
		return totAcc/testingData.size();
	}

	private double classify(Node root, ArrayList<Double> columnValue) {
		// TODO Auto-generated method stub
		if (root.isLeaf)
			return root.label;
		if (columnValue.get(root.attributeNum)>root.condition)
			return classify(root.right, columnValue);
		else 
			return classify(root.left, columnValue);
	}

}
