/*******************************************************************************
 * Copyright (c) 2014 Kostas Vogias.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Kostas Vogias - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package analytics.measures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author vogias
 * 
 */
public class RelativeEntropy extends Metric {

	@Override
	public HashMap<String, Double> compute(Collection<?> data) {
		// TODO Auto-generated method stub
		/*
		 * Map cardinalityMap = CollectionUtils.getCardinalityMap(data);
		 * 
		 * Set keySet = cardinalityMap.keySet(); int size = data.size();
		 * Iterator iterator = keySet.iterator();
		 * 
		 * double result = 0.0;
		 * 
		 * while (iterator.hasNext()) { String key = (String) iterator.next();
		 * 
		 * int value = (int) cardinalityMap.get(key);
		 * 
		 * double frequency = (double) value / size;
		 * 
		 * result -= frequency * (Math.log(frequency) / Math.log(2)); }
		 * 
		 * System.out.println("Relative Entropy:" + result +
		 * "(number of observations:" + size + ")");
		 */
		return null;

	}

	// Vector<String> data
	public double compute(HashMap<String, Integer> data) {
		// TODO Auto-generated method stub
		// Map cardinalityMap = CollectionUtils.getCardinalityMap(data);

		// Set keySet = cardinalityMap.keySet();

		Set<String> keySet = data.keySet();

		Collection<Integer> values = data.values();

		Iterator<Integer> iterator2 = values.iterator();
		int size = 0;

		while (iterator2.hasNext()) {

			size += iterator2.next();
		}

		Iterator<String> iterator = keySet.iterator();

		double result = 0.0;

		while (iterator.hasNext()) {
			String key = iterator.next();

			// int value = (int) cardinalityMap.get(key);

			Integer value = data.get(key);

			double frequency = (double) value / size;

			result -= frequency * (Math.log(frequency) / Math.log(2));

		}

		// System.out.println("Relative Entropy:" + result
		// + "(number of observations:" + size + ")");
		return result;

	}

	@Override
	public void compute(Collection<?> data, String elementName) {
		// TODO Auto-generated method stub

	}

}
