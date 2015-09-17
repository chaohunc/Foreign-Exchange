package currecyDataPipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

public class Preprocesser {
	public String readLabelFile(String filename, String scope)
	{
		 try
		  {
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    BufferedWriter writer = new BufferedWriter(new FileWriter(filename.substring(0,6)+"_out"));
		    String line;
		    int lineNum = 0;
		    double closedBidPrice = 0;
		    double closedAskPrice = 0;
		    double maxAskPrice = 0;
		    double minAskPrice = Integer.MAX_VALUE;
		    double maxBidPrice = 0;
		    double minBidPrice = Integer.MAX_VALUE;
	    	int lastTickYears = 0;
	    	int lastTickMonths = 0;
	    	int lastTickDays = 0;
		    int lastTickHours = 0;
		    int lastTickMinutes = 0;
		    int lastTickSeconds = 0;
		    String lastStringDate = null;
		    double lastBidClosedPrice = 0;
		    double lastAskClosedPrice = 0;
		    while ((line = reader.readLine()) != null)
		    {
		    	int thisTickYears = Integer.parseInt(line.substring(8, 12));
		    	int thisTickMonths = Integer.parseInt(line.substring(12, 14));
		    	int thisTickDays = Integer.parseInt(line.substring(14, 16));
		    	int thisTickHours = Integer.parseInt(line.substring(17, 19));
		    	int thisTickMinutes = Integer.parseInt(line.substring(20, 22));
		    	int thisTickSeconds = Integer.parseInt(line.substring(23, 25));
		    	
		    	if (lineNum!=0&& thisTickMinutes!=lastTickMinutes)
		    	{
			 
		    		StringBuffer strbuf = new StringBuffer();
		    		strbuf.append(lastStringDate);//+lastTickYears+" "+lastTickMonths+" "+lastTickDays+" "+lastTickHours+" "+lastTickMinutes+" "+lastTickSeconds);
		    		Calendar date = Calendar.getInstance();
		    		
		    		date.set(lastTickYears,lastTickMonths,lastTickDays,lastTickHours,lastTickMinutes,0);
		    		date.set(Calendar.MILLISECOND, 0);
		    		long timestamps= date.getTimeInMillis();
		    		strbuf.append(timestamps+" ");	
		    		if(lastTickHours<10)
			    		strbuf.append("0");		    			
			    	strbuf.append(lastTickHours);		    			
			    	strbuf.append(":");		    			
		    		if(lastTickMinutes<10)
			    		strbuf.append("0");		    			
			    	strbuf.append(lastTickMinutes);		    			
			    	strbuf.append(":00");		    			
			    	strbuf.append(" ");		    						    	
			    	strbuf.append(maxBidPrice);		    			
			    	strbuf.append(" ");		    			
			    	strbuf.append(minBidPrice);		    			
			    	strbuf.append(" ");		    			
			    	strbuf.append(closedBidPrice);		    			
			    	strbuf.append(" ");		    			
			    	strbuf.append(maxAskPrice);		    			
			    	strbuf.append(" ");		    			
			    	strbuf.append(minAskPrice);		    			
			    	strbuf.append(" ");		    			
			    	strbuf.append(closedAskPrice);		    			
			    	strbuf.append(" ");	
			    	if (closedBidPrice>lastBidClosedPrice)
				    	strbuf.append("1 ");			
			    	else
				    	strbuf.append("0 ");			

			    	if (closedAskPrice>lastAskClosedPrice)
				    	strbuf.append("1 ");			
			    	else
				    	strbuf.append("0 ");			
			    		
			    	strbuf.append("\n");			
		    		//System.out.print(strbuf.toString());
			    	writer.write(strbuf.toString());
				    lastAskClosedPrice = closedAskPrice;
				    lastBidClosedPrice = closedBidPrice;			    	
				    closedBidPrice = 0;
				    closedAskPrice = 0;
				    maxAskPrice = 0;
				    minAskPrice = Integer.MAX_VALUE;
				    maxBidPrice = 0;
				    minBidPrice = Integer.MAX_VALUE;

		    	}
		    		
		    	String[] strs = line.split(",");
		    	double bid = Double.parseDouble(strs[2]);
		    	double ask = Double.parseDouble(strs[3]);
		    	
		    	maxAskPrice = Math.max(maxAskPrice, ask);
		    	maxBidPrice = Math.max(maxBidPrice, bid);

		    	minAskPrice = Math.min(minAskPrice, ask);
		    	minBidPrice = Math.min(minBidPrice, bid);

		    	closedBidPrice = bid;
		    	closedAskPrice = ask;
		    	lastStringDate =  new String(line.substring(0,17));

		    	lastTickYears = thisTickYears;	
		    	lastTickMonths = thisTickMonths;	
		    	lastTickDays = thisTickDays;	
		    	lastTickHours = thisTickHours;	
		    	lastTickMinutes = thisTickMinutes;	
		    	lastTickSeconds = thisTickSeconds;	
		    	lineNum++;
		    	
		    }
		    reader.close();
		    writer.flush();
		    writer.close();
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filename);
		    e.printStackTrace();
		  }
		return filename.substring(0,6)+"_out";
	}

	private long getTimeStamps(int lastTickYears, int lastTickMonths,
			int lastTickDays, int lastTickHours, int lastTickMinutes,
			int lastTickSeconds) {
		long base = 1;
		long timeStamps = 0;
		timeStamps += lastTickSeconds;
		base*=60;
		timeStamps += lastTickMinutes*base;
		base*=60;
		timeStamps += lastTickHours*base;
		base*=24;
		timeStamps += lastTickDays*base;
		base*=24;
		
		
		return 0;
	}

	public void combineFeatures(String labelFileName, List<String> featuresFileName, String outputFileName) throws IOException {
		// TODO Auto-generated method stub
		Queue<Pair> resultQueue = new LinkedList<>();
		Queue<Pair> labelQueue = new LinkedList<>();
		ArrayList<Row> rows = new ArrayList<>();

	    BufferedReader labelFileReader = new BufferedReader(new FileReader(labelFileName));
	    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName+"_out"));
	    
	    String line;
	    while ((line = labelFileReader.readLine()) != null)
	    {
	    	String[] strs = line.split(" ");	
	    	Pair labelPair = new Pair (Long.parseLong(strs[1]),line.substring(40));
	    	labelQueue.add(labelPair);
	    }
	    
	    long timeWindows=60000;
		Queue<Pair> featureQueue = new LinkedList<>();
	    
	    
	    for (String featureFileName: featuresFileName)
		{
	    	
		    BufferedReader featureFileReader = new BufferedReader(new FileReader(featureFileName));
		    while ((line = featureFileReader.readLine()) != null)
		    {
		    	String[] strs = line.split(" ");	    	
		    	Pair featurePair = new Pair (Long.parseLong(strs[1]),line.substring(40));
		    	featureQueue.add(featurePair);
		    }
		    
		    while (labelQueue.size()!=0)
		    {
		    	Pair labelPair = labelQueue.peek();
		    	Pair featurePair=null;
		    	if (featureQueue.size()!=0)
		    		featurePair = featureQueue.peek();
		    	if (featurePair !=null && labelPair.timestamp == featurePair.timestamp - timeWindows)
		    	{
		    		resultQueue.add(new Pair(labelPair.timestamp,new String(featurePair.content+" "+labelPair.content)));		    		
		    		labelQueue.poll();
		    		featureQueue.poll();
		    	}
		    	else if (featurePair==null || labelPair.timestamp < featurePair.timestamp - timeWindows)
		    	{
//		    		resultQueue.add(new Pair(labelPair.timestamp,new String(featurePair.content+" "+labelPair.content)));
		    		resultQueue.add(new Pair(labelPair.timestamp,new String("-1 -1 -1 -1 -1 -1 -1 -1 "+labelPair.content)));
		    		labelQueue.poll();
		    	}
		    	else
		    	{
		    		featureQueue.poll();
		    	}
		    }
		}
	    while (resultQueue.size()!=0)
	    {
	    	Pair resultPair = resultQueue.poll();
	    	writer.write(resultPair.timestamp+" "+resultPair.content+"\n");
	    }
	    writer.flush();
	    writer.close();
	}
}
