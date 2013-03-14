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
	public HashMap<String,Double>  compute(Collection<?> data) {
		return null;

	}

	public void compute(HashMap<String, Integer> data) {

		HashMap<String, Integer> dat = (HashMap<String, Integer>) data;

		Set<String> keySet = dat.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Integer value = dat.get(key);

			float percentage = ((float) value / records) * 100;

			System.out.println("Element:" + key + ", Completeness:"
					+ percentage + "%");
		}

	}

	@Override
	public void compute(Collection<?> data, String elementName) {

	}

}
