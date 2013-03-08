/**
 * 
 */
package analytics.measures;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author vogias
 * 
 */
public class RelativeEntropy extends Metric {

	@Override
	public void compute(Collection<?> data) {
		// TODO Auto-generated method stub
		Map cardinalityMap = CollectionUtils.getCardinalityMap(data);

		Set keySet = cardinalityMap.keySet();
		int size = data.size();
		Iterator iterator = keySet.iterator();

		double result = 0.0;

		while (iterator.hasNext()) {
			String key = (String) iterator.next();

			int value = (int) cardinalityMap.get(key);

			double frequency = (double) value / size;

			result -= frequency * (Math.log(frequency) / Math.log(2));
		}

		System.out.println("Relative Entropy:" + result
				+ "(number of observations:" + size + ")");

	}

	@Override
	public void compute(Collection<?> data, String elementName) {
		// TODO Auto-generated method stub

	}

}
