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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;

import analytics.constants.AnalyticsConstants;

/**
 * @author vogias
 * 
 */
public class ElementFrequency extends Metric {

	Collection<?> dData;
	HashMap<String, Integer> attributes;

	public ElementFrequency(Collection<?> dData) {
		// TODO Auto-generated constructor stub
		this.dData = dData;
	}

	public ElementFrequency(HashMap<String, Integer> attributes) {
		// TODO Auto-generated constructor stub
		this.attributes = attributes;
	}

	@Override
	public HashMap<String, Double> compute(Collection<?> data) {

		HashMap<String, Double> freq = new HashMap<>();
		Iterator<?> iterator = dData.iterator();
		while (iterator.hasNext()) {
			String element = (String) iterator.next();
			freq.put(element, (double) Collections.frequency(data, element));
		}

		return freq;
	}

	@Override
	public void compute(Collection<?> data, String elementName) {

	}

	public void compute(HashMap<String, Integer> data, String provider,
			Logger logger) {

		Set<String> keySet = data.keySet();

		Iterator<String> iterator = keySet.iterator();

		Properties props = new Properties();
		try {
			props.load(new FileInputStream("configure.properties"));

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}

		String path = props.getProperty(AnalyticsConstants.resultsPath);
		File anls = new File(path + "Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		File dir = new File(anls, provider);
		if (!dir.exists())
			dir.mkdir();

		File attInfo = new File(dir, provider + "_Attribute_Analysis" + ".csv");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(attInfo));

			writer.write("Attribute Name,Element Used,Attribute Value,Frequency");
			writer.newLine();

			StringBuffer logstring = new StringBuffer();

			while (iterator.hasNext()) {
				String attName = (String) iterator.next();

				String[] info = attName.split("#");

				provider = provider.replace(" ", "");

				writer.write(info[0] + ",");
				logstring.append(provider);
				logstring.append(" " + info[0]);

				writer.write(info[1] + ",");
				logstring.append(" " + info[1]);

				writer.write(info[2] + ",");
				logstring.append(" " + info[2]);

				Integer integer = data.get(attName);

				writer.write(integer.toString());
				logstring.append(" " + integer.toString());

				writer.newLine();
				logger.info(logstring.toString());
				logstring.delete(0, logstring.capacity());

				// writeAtts2file(writer, attName, hashMap, logger, provider);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
