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
		StringBuffer key = new StringBuffer();

		while (iterator.hasNext()) {

			key.append(iterator.next());
			Integer value = data.get(key.toString());

			float percentage = ((float) value / records) * 100;

			output.put(key.toString(), (double) percentage);
			key.delete(0, key.length());
		}

		return output;

	}

	@Override
	public void compute(Collection<?> data, String elementName) {

	}

}
