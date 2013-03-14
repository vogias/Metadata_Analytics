/**
 * 
 */
package analytics.measures;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiHashMap;
import org.apache.lucene.util.CollectionUtil;

/**
 * @author vogias
 * 
 */
public class ElementFrequency extends Metric {

	Collection<?> dData;
	MultiHashMap atts;

	public ElementFrequency(Collection<?> dData) {
		// TODO Auto-generated constructor stub
		this.dData = dData;
	}

	public ElementFrequency(MultiHashMap atts) {
		// TODO Auto-generated constructor stub
		this.atts = atts;
	}

	@Override
	public HashMap<String, Double> compute(Collection<?> data) {

		HashMap<String, Double> freq = new HashMap<>();
		Iterator<?> iterator = dData.iterator();
		while (iterator.hasNext()) {
			String element = (String) iterator.next();
			freq.put(element, (double) Collections.frequency(data, element));
			System.out.println("Element:" + element + ", Frequency:"
					+ Collections.frequency(data, element));
		}

		return freq;
	}

	@Override
	public void compute(Collection<?> data, String elementName) {

		System.out.println("Element:" + elementName + ", Frequency:"
				+ Collections.frequency(data, elementName));

	}

	@SuppressWarnings("deprecation")
	public void compute(MultiHashMap data) {

		Set keySet = data.keySet();

		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String attName = (String) iterator.next();

			Collection attValues = data.getCollection(attName);

			System.out.println("----------------------------------");
			System.out.println("Attribute:" + attName + ", Frequency:"
					+ attValues.size());
			System.out.println("----------------------------------");
			Collection distinctAttsValues = atts.getCollection(attName);
			computeDominantAttValue(attValues, distinctAttsValues);
		}
	}

	private void computeDominantAttValue(Collection attValues,
			Collection distinctAV) {

		Iterator iterator = distinctAV.iterator();

		while (iterator.hasNext()) {

			String key = (String) iterator.next();

			System.out.println("Attribute value:" + key + ", Frequency:"
					+ Collections.frequency(attValues, key));

		}

	}
}
