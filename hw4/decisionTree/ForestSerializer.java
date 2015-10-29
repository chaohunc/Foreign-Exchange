package decisionTree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.StringTokenizer;

public class ForestSerializer {
	
	public ArrayList<Node> deSerializeForest(ArrayList<String> strs) throws ClassNotFoundException, IOException{
		ArrayList<Node> deSerializeForests = new ArrayList<Node>();
	    for (String nodeStrs:strs)
	    {
	    	deSerializeForests.add(deSerializeTree(nodeStrs)); 
	    }
	    return deSerializeForests;
	}
	
	public Node deSerializeTree(String strs) throws ClassNotFoundException, IOException{
	    if (strs == null || strs.length() == 0) return null;
	    StringTokenizer st = new StringTokenizer(strs, " ");
	    return deSerializeTree(st);
	}
	
	private Node deSerializeTree(StringTokenizer st) throws ClassNotFoundException, IOException{
	    if (!st.hasMoreTokens())
	        return null;
	    String val = st.nextToken();
	    if (val.equals("#"))
	        return null;
	    Node root = (Node) fromString(val);
	    root.left = deSerializeTree(st);
	    root.right = deSerializeTree(st);
	    return root;
	}
	
	private void serializeTree(Node x, StringBuilder sb) throws IOException{
	    if (x == null) {
	        sb.append("# ");
	    } else {
	    	x.columnName=null;
	    	x.tuples=null;
	    	Node left =x.left;
	    	Node right = x.right;
	    	x.left=null;
	    	x.right=null;
	        sb.append(toString(x) + " ");
	        serializeTree(left, sb);
	        serializeTree(right, sb);
	    }
	}
	
	public String serializeTree (Node node) throws IOException
	{
	    StringBuilder sb = new StringBuilder();
	    serializeTree(node, sb);
	    return sb.toString();
	}

	public ArrayList<String> serializeForest (ArrayList<Node> forest) throws IOException
	{
		ArrayList<String> forestStorer = new ArrayList<String>();
	    for (Node node:forest)
	    {
	    	String s = serializeTree(node);
	    	forestStorer.add(s); 
	    	System.out.println(s);
	    }
	    return forestStorer;
	}
	
    public static void main( String [] args )  throws IOException,
    ClassNotFoundException {
	String string = toString( new Node() );
	System.out.println(" Encoded serialized version " );
	System.out.println( string );
	Node some = ( Node ) fromString( string );
	System.out.println( "\n\nReconstituted object");
	System.out.println( some );
	
	
	}
	
	/** Read the object from Base64 string. */
	private static Object fromString( String s ) throws IOException ,
	     ClassNotFoundException {
	byte [] data = Base64.getDecoder().decode( s );
	ObjectInputStream ois = new ObjectInputStream( 
	new ByteArrayInputStream(  data ) );
	Object o  = ois.readObject();
	ois.close();
	return o;
	}
	
	/** Write the object to a Base64 string. */
	private static String toString( Serializable o ) throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream( baos );
	oos.writeObject( o );
	oos.close();
	return new String( Base64.getEncoder().encode( baos.toByteArray() ) );
	}
}
