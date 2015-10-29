package decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ForestTester {
	public double test(ArrayList<Node> forest, ArrayList<Tuple> testingData) {
		// TODO Auto-generated method stub
		
		double totAcc = 0;
		for (Tuple tuple: testingData)
		{
			HashMap<Double,Integer> majority = new HashMap<>();
			for (Node root:forest)
			{
				double classifiedLabel= classify(root,tuple.columnValue);
				if (majority.containsKey(classifiedLabel))
					majority.put(classifiedLabel,majority.get(classifiedLabel)+1);
				else
					majority.put(classifiedLabel, 1);
			}
			
			double maxLabel = 0;
			int maxLabelVal = 0;
			
			for (Entry<Double,Integer> entry:majority.entrySet())
			{
			//	System.out.println(entry.getKey()+ " "+entry.getValue());
				if (entry.getValue()>maxLabelVal)
				{
					maxLabelVal = entry.getValue();
					maxLabel = entry.getKey();
				}
				
			}

			if (Math.abs(tuple.labelValue-maxLabel)<0.0001)
				totAcc++;
		}
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
