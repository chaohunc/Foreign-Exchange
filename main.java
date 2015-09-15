package currecyDataPipeline;

import java.util.ArrayList;
import java.util.List;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Preprocesser p = new Preprocesser();
		String label = p.readLabelFile("EURUSD-2015-01.csv", "");
		String feature1 = p.readLabelFile("GBPUSD-2015-01.csv", "");
		List<String> features = new ArrayList<>();
		features.add(feature1);
		//p.combineFeatures(label,features);

	}

}
