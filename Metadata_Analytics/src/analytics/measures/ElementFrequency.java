/**
 * 
 */
package analytics.measures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
			
			// System.out.println("Element:" + element + ", Frequency:"
			// + Collections.frequency(data, element));
		}

		return freq;
	}

	@Override
	public void compute(Collection<?> data, String elementName) {

		// System.out.println("Element:" + elementName + ", Frequency:"
		// + Collections.frequency(data, elementName));

	}

	@SuppressWarnings("deprecation")
	public void compute(MultiHashMap data, String provider) {

		Set keySet = data.keySet();

		Iterator iterator = keySet.iterator();

		File anls = new File("Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		File dir = new File(anls, provider);
		if (!dir.exists())
			dir.mkdir();

		File attInfo = new File(dir, provider + "_Attribute_Analysis" + ".txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(attInfo));

			while (iterator.hasNext()) {
				String attName = (String) iterator.next();

				Collection attValues = data.getCollection(attName);

				// System.out.println("----------------------------------");
				writer.write("----------------------------------");
				writer.newLine();

				// System.out.println("Attribute:" + attName + ", Frequency:"
				// + attValues.size());
				writer.write("Attribute:" + attName + ", Frequency:"
						+ attValues.size());
				writer.newLine();
				writer.write("----------------------------------");
				writer.newLine();
				// System.out.println("----------------------------------");

				Collection distinctAttsValues = atts.getCollection(attName);

				computeDominantAttValue(attValues, distinctAttsValues, writer);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void computeDominantAttValue(Collection attValues,
			Collection distinctAV, BufferedWriter writer) throws IOException {

		Iterator iterator = distinctAV.iterator();

		while (iterator.hasNext()) {

			HashMap<String, String> key = (HashMap<String, String>) iterator
					.next();

			int frequency = Collections.frequency(attValues, key);
			writer.write("\tAttribute value:" + key + ", Frequency:"
					+ frequency);
			writer.newLine();
			// System.out.println("\tAttribute value:" + key + ", Frequency:"
			// + frequency);

		}

	}
}
