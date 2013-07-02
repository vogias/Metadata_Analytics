/**
 * 
 */
package analytics.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;

/**
 * @author vogias
 * 
 */
public class store2csv extends Storage {

	private void createHeaders(BufferedWriter writer, String metricName,
			String element) {

		try {
			writer.append(element);
			writer.append(',');
			writer.append(metricName);
			writer.newLine();
			// writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void storeElementData(HashMap<String, Double> data,
			String metricName, String dataProvider, String analysisType,
			String headerColumn, boolean temporal) {
		// TODO Auto-generated method stub

		String sFileName = dataProvider + analysisType + ".csv";

		File anls = new File("Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		File dir = new File(anls, dataProvider);
		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		try {
			if (!file.exists()) {
				FileWriter writer = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(writer);
				createHeaders(bw, metricName, headerColumn);

				Set<String> keySet = data.keySet();
				Iterator<String> iterator = keySet.iterator();

				while (iterator.hasNext()) {
					String key = iterator.next();
					// System.out.println(key);
					bw.append(key);
					bw.append(',');
					Double value = data.get(key);
					bw.append(String.valueOf(value));
					bw.newLine();
				}
				bw.close();
			} else {

				BufferedReader reader = new BufferedReader(new FileReader(file));

				File temp = new File(dir, "temp.csv");

				FileWriter writer = new FileWriter(temp);
				BufferedWriter bw = new BufferedWriter(writer);

				String line;
				int counter = 0;

				// Set<String> keySet = data.keySet();
				// Iterator<String> iterator = keySet.iterator();

				while ((line = reader.readLine()) != null) {// iterator.hasNext()

					String[] split = line.split(",");
					// System.out.println(line);

					String key = split[0];
					if (counter == 0) {
						line = line + "," + metricName;
						bw.append(line);
						bw.newLine();

					} else {

						Double value = data.get(key);
						// System.out.println("Appending key:" + key + " value:"
						// + value);
						line = line + "," + value;
						// /System.out.println("Appending line:" + line);
						bw.append(line);
						bw.newLine();
					}

					counter += 1;

				}
				bw.close();

				FileUtils.copyFile(temp, file);
				temp.delete();
				reader.close();

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void storeElementValueData(HashMap<String, Integer> data,
			String metricName, String dataProvider, String analysisType,
			String headerColumn, boolean temporal) {
		// TODO Auto-generated method stub

		String sFileName = dataProvider + analysisType + ".csv";

		File anls = new File("Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		else {
			// if (temporal == false) {
			// FileUtils.deleteQuietly(anls);
			// anls.mkdir();
			// }
		}

		File dir = new File(anls, dataProvider);
		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		try {
			if (!file.exists()) {
				FileWriter writer = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(writer);
				createHeaders(bw, metricName, headerColumn);

				Set<String> keySet = data.keySet();
				Iterator<String> iterator = keySet.iterator();

				while (iterator.hasNext()) {
					String key = iterator.next();
					Integer value = data.get(key);

					if (key.contains(","))
						key = key.replace(",", "/");

					bw.append(key);
					bw.append(',');
					bw.append(String.valueOf(value));
					bw.newLine();
				}
				bw.close();
			} else {

				BufferedReader reader = new BufferedReader(new FileReader(file));

				File temp = new File(dir, "temp.csv");

				FileWriter writer = new FileWriter(temp);
				BufferedWriter bw = new BufferedWriter(writer);

				String line;
				int counter = 0;

				// Set<String> keySet = data.keySet();
				// Iterator<String> iterator = keySet.iterator();

				while ((line = reader.readLine()) != null) {
					String[] split = line.split(",");
					// System.out.println(line);

					if (counter == 0) {
						line = line + "," + metricName;
						bw.append(line);
						bw.newLine();

					} else {
						// String key = iterator.next();
						String key = split[0];
						Integer value = data.get(key);

						if (key.contains(","))
							key = key.replace(",", "/");

						line = line + "," + value;
						bw.append(line);
						bw.newLine();
					}

					counter += 1;

				}
				bw.close();
				FileUtils.copyFile(temp, file);
				temp.delete();
				reader.close();

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
