package currecyDataPipeline;

import java.util.List;

public class Row {
	long timeStamps;
	double askHigh;
	double askLow;
	double askClosed;
	double bidHigh;
	double bidLow;
	double bidClosed;
	Row (double bidHigh,double bidLow, double bidClosed, double askHigh, double askLow,double askClosed)
	{
		this.bidHigh = bidHigh;
		this.bidLow = bidLow;
		this.bidClosed = bidClosed;
		this.askHigh = askHigh;
		this.askLow = askLow;
		this.askClosed = askClosed;
	}
}
