package decisionTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class TreeConstructor {

	public Node buildTree(ArrayList<Tuple> trainingData) {
		// TODO Auto-generated method stub
		Node root = calculateFeatureWithHighestIG(trainingData);
		if (root.isLeaf)
			return root;
		ArrayList<Tuple> smaller = new ArrayList<Tuple>();
		ArrayList<Tuple> larger = new ArrayList<Tuple>();
		seperateByConditions(root, smaller, larger);
		root.left = buildTree(smaller);
		root.right = buildTree(larger);
		return root;
		
	}

	private void seperateByConditions(Node root, ArrayList<Tuple> smaller, ArrayList<Tuple> larger) {
		// TODO Auto-generated method stub
		
		int colIndex = root.attributeNum;
		double condition = root.condition;
		for (Tuple tuple:root.tuples)
		{
			if (tuple.columnValue.get(colIndex) > condition)
				larger.add(tuple);
			else
				smaller.add(tuple);
		}
	}

	private Node calculateFeatureWithHighestIG(ArrayList<Tuple> trainingData) {
		// TODO Auto-generated method stub
		Node node = new Node();
		ArrayList<Double> labels = new ArrayList<>();
		double conditionMax = 0;
		double igMax= Integer.MIN_VALUE;
		int igIndex = 0;
		for (Tuple tuple: trainingData)
			labels.add(tuple.labelValue);
		
		for (int i=0;i<trainingData.get(0).columnValue.size();i++)
		{
			ArrayList<Double> trainingDataPerCol = new ArrayList<>();
			for (int j=0;j<trainingData.size();j++)
			{
				trainingDataPerCol.add(trainingData.get(j).columnValue.get(i));
				//System.out.println(trainingData.get(j).columnValue.get(i));
			}
			CandidateFeature cf= maxIGwithThisFeature(trainingDataPerCol,labels);

			if (cf.ig>igMax)
			{
				igMax = cf.ig;
				igIndex = i;
				conditionMax = cf.condition;
			}
		}
		node.ig = igMax;
		node.condition=conditionMax;
		node.attributeNum=igIndex;
		
		//System.out.println("ig:"+igMax);
		//System.out.println("cond:"+conditionMax);
		//System.out.println("attrNum:"+igIndex);
		if (igMax<=0)
			node.isLeaf=true;
		return node;
	}

	private CandidateFeature maxIGwithThisFeature(ArrayList<Double> trainingDataPerCol,
			ArrayList<Double> labels) {
		ArrayList<PairElement> elementlist = new ArrayList<>();

		for (int i=0;i<labels.size();i++)
			elementlist.add(new PairElement(trainingDataPerCol.get(i),labels.get(i)));
		
		Collections.sort(elementlist, new Comparator<PairElement>(){
			public int compare(PairElement o1, PairElement o2) {
				if (o1.colVal>o2.colVal)
					return 1;
				else if (o1.colVal<o2.colVal)
					return -1;
				else 
					return 0;	
			}
		});

		int ptr = 0;
		HashMap<Double,Integer> hmap = new HashMap<Double,Integer>();

		for (PairElement pairElement:elementlist)
		{
			if (hmap.containsKey(pairElement.labelVal))
				hmap.put(pairElement.labelVal, hmap.get(pairElement.labelVal)+1);
			else
				hmap.put(pairElement.labelVal, 1);
		}
		
		double tempentropy = 0;
		for (Entry<Double,Integer> entry:hmap.entrySet())
		{
			//System.out.println("k"+entry.getKey()+" "+entry.getValue());
			double prob = ((double) entry.getValue())/elementlist.size();
			tempentropy += -1 * prob * Math.log(prob)/Math.log(2);
		}
		//System.out.println("tempentropy-"+tempentropy);
		double origianlEntropy = tempentropy; 
		
		HashMap<Double,Integer> hmapS = new HashMap<Double,Integer>();
		double cond = Integer.MAX_VALUE;
		double minEntropy = Integer.MAX_VALUE;
		while (ptr<labels.size()-1)
		{

			PairElement pairElement = elementlist.get(ptr);
			if (hmapS.containsKey(pairElement.labelVal))
				hmapS.put(pairElement.labelVal, hmap.get(pairElement.labelVal)+1);
			else
				hmapS.put(pairElement.labelVal, 1);
			
			if (hmap.get(pairElement.labelVal)==1)
				hmap.remove(pairElement.labelVal);
			else
				hmap.put(pairElement.labelVal, hmap.get(pairElement.labelVal)-1);				
			
			if (elementlist.get(ptr).colVal!=elementlist.get(ptr+1).colVal)
			{			
				double totalentropy = 0;
				tempentropy = 0;
				for (Entry<Double,Integer> entry:hmapS.entrySet())
				{
					double prob = ((double) entry.getValue())/(ptr+1);
					tempentropy+= -1 * prob * Math.log(prob)/Math.log(2);
					//System.out.println("prob:"+prob);
				}
				//System.out.println("tempentropy2:"+totalentropy);
				tempentropy *= ((double)(ptr+1))/elementlist.size();
				totalentropy += tempentropy;


				tempentropy=0;
				
				for (Entry<Double,Integer> entry:hmap.entrySet())
				{
					double prob = ((double) entry.getValue())/(labels.size()-ptr-1);
					tempentropy+= -1 * prob * Math.log(prob)/Math.log(2);
					//System.out.println("hmap prob:"+prob);
				}
				//System.out.println("hmap tempentropy:"+tempentropy);
				tempentropy *= ((double)(labels.size()-ptr-1))/elementlist.size();
				totalentropy += tempentropy;
				//System.out.println("totEntropy:"+totalentropy);		
				if (totalentropy<minEntropy)
				{
					cond = (elementlist.get(ptr).colVal+elementlist.get(ptr+1).colVal)/2;
					minEntropy = totalentropy;
				}
			}

			ptr++;
		}
		
		//System.out.println("cond:"+cond);			
		//System.out.println("ig:"+ (minEntropy-origianlEntropy));	
		return new CandidateFeature(minEntropy-origianlEntropy,cond);

		// TODO Auto-generated method stub
		
	}

	class PairElement
	{
		
		public PairElement(double colVal, double labelVal) {
			// TODO Auto-generated constructor stub
			this.colVal = colVal;
			this.labelVal = labelVal;
		}
		double colVal;
		double labelVal;
	}

	class CandidateFeature
	{
		public CandidateFeature(double ig, double cond) {
			// TODO Auto-generated constructor stub
			this.ig = ig;
			this.condition = cond;
		}
		double ig;
		double condition;
	}
}
