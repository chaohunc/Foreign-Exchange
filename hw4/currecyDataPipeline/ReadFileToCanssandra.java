package currecyDataPipeline;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Row;

public class ReadFileToCanssandra {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// cluster;
		
		// Connect to the cluster and keyspace "demo"
			
		Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		Session session = cluster.connect("FE");
		
		
	    BufferedReader labelFileReader = new BufferedReader(new FileReader("EURUSD-GBPUSD_2_out"));
	    String line;
	    
	    while ((line = labelFileReader.readLine()) != null)
	    {
	    	String[] strs=line.split(" ");
	    	StringBuffer sql = new StringBuffer("INSERT INTO EURUSD (timestamp,");
	    	for (int i=1;i<strs.length-2;i++)
	    		sql.append("f"+i+",");
	    	sql.append("USDEURCloseBidDirection,");
	    	sql.append("USDEURCloseAskDirection) VALUES ('"+ strs[0] +"'");
	    	for  (int i=1;i<strs.length;i++)
	    		sql.append(", "+ strs[i]);
	    	sql.append(")");
	    	System.out.println(sql.toString());
	    	session.execute(sql.toString());
		}
	    
		ResultSet results = session.execute("SELECT * FROM EURUSD");
		for (Row row : results) {
			System.out.println(row.getString("timestamp"));
		}
	}
}
