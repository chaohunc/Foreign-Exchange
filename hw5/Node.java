

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {
	ArrayList<Tuple> tuples = new ArrayList<Tuple>();
	ArrayList<String> columnName;
	double condition;
	boolean isLeaf = false;
	double label;
	int attributeNum;
	Node left;
	Node right;
	double ig;
	
}



