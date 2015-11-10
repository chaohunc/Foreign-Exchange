import java.io.Serializable;
import java.util.ArrayList;

public class Tuple implements Serializable{
	ArrayList<Double> columnValue;
	double labelValue;
	Tuple()
	{
		this.columnValue = new ArrayList<Double>();		
	}
}


