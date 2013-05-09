/**
 * 
 */
package analytics.measures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author vogias
 * 
 */
public class ElementCompleteness extends Metric {

	int records;

	public ElementCompleteness(int repoRecords) {
		// TODO Auto-generated constructor stub
		records = repoRecords;
	}

	@Override
	public HashMap<String, Double> compute(Collection<?> data) {
		return null;

	}

	public HashMap<String, Double> compute(HashMap<String, Integer> data) {

		Set<String> keySet = data.keySet();
		Iterator<String> iterator = keySet.iterator();
		HashMap<String, Double> output = new HashMap<>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Integer value = data.get(key);

			float percentage = ((float) value / records) * 100;

			/*
			 * if (key.equals("chor_dc:dc.dcterms:accessRights")) {
			 * System.out.println("Value:" + value + " number of records:" +
			 * records + " percentage:" + percentage); }
			 */

			// System.out.println("Element:" + key + ", Completeness:"
			// + percentage + "%");
			output.put(key, (double) percentage);
		}

		return output;

	}

	@Override
	public void compute(Collection<?> data, String elementName) {

	}

}
