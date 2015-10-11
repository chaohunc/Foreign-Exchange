package decisionTree;

import java.util.ArrayList;

public class Node {
	ArrayList<Tuple> tuples = new ArrayList<>();
	ArrayList<String> columnName;
	double condition;
	boolean isLeaf = false;
	double label;
	int attributeNum;
	Node left;
	Node right;
	double ig;
	
}

