/**
 * 
 */
package analytics.storage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author vogias
 * 
 */
public class store2csv extends Storage {

	String providerName;

	public store2csv(String providerName) {
		// TODO Auto-generated constructor stub
		this.providerName = providerName;
	}

	@Override
	public void storeData(HashMap<String, Double> data, String metricName) {
		// TODO Auto-generated method stub
		String sFileName = providerName + "_StatisticalAnalysis" + ".csv";
		try {
			FileWriter writer = new FileWriter(sFileName);
			writer.append("Element Name");
			writer.append(',');
			writer.append(metricName);
			writer.append('\n');

			Set<String> keySet = data.keySet();
			Iterator<String> iterator = keySet.iterator();

			while (iterator.hasNext()) {
				String key = iterator.next();
				writer.append(key);
				writer.append(',');
				Double value = data.get(key);
				writer.append(String.valueOf(value));
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
