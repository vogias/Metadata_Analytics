/**
 * 
 */
package analytics.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;

/**
 * @author vogias
 * 
 */
public class store2csv extends Storage {

	private void createHeaders(FileWriter fileWriter, String metricName,
			String element) {

		try {
			fileWriter.append(element);
			fileWriter.append(',');
			fileWriter.append(metricName);
			fileWriter.append('\n');
			fileWriter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void storeElementData(HashMap<String, Double> data,
			String metricName, String dataProvider, String analysisType,
			String headerColumn) {
		// TODO Auto-generated method stub

		String sFileName = dataProvider + analysisType + ".csv";

		File dir = new File(dataProvider);

		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		try {
			if (!file.exists()) {
				FileWriter writer = new FileWriter(file);
				createHeaders(writer, metricName, headerColumn);

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
			} else {

				BufferedReader reader = new BufferedReader(new FileReader(file));

				File temp = new File(dir, "temp.csv");

				FileWriter writer = new FileWriter(temp);

				String line;
				int counter = 0;

				Set<String> keySet = data.keySet();
				Iterator<String> iterator = keySet.iterator();

				while (iterator.hasNext()) {
					line = reader.readLine();

					if (counter == 0) {
						line = line + "," + metricName;
						writer.append(line);
						writer.append('\n');

					} else {
						String key = iterator.next();
						Double value = data.get(key);
						line = line + "," + value;
						writer.append(line);
						writer.append('\n');
					}

					counter += 1;

				}
				writer.flush();
				writer.close();

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
			String headerColumn) {
		// TODO Auto-generated method stub

		String sFileName = dataProvider + analysisType + ".csv";

		File dir = new File(dataProvider);

		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		try {
			if (!file.exists()) {
				FileWriter writer = new FileWriter(file);
				createHeaders(writer, metricName, headerColumn);

				Set<String> keySet = data.keySet();
				Iterator<String> iterator = keySet.iterator();

				while (iterator.hasNext()) {
					String key = iterator.next();
					writer.append(key);
					writer.append(',');
					Integer value = data.get(key);
					writer.append(String.valueOf(value));
					writer.append('\n');
				}
				writer.flush();
				writer.close();
			} else {

				BufferedReader reader = new BufferedReader(new FileReader(file));

				File temp = new File(dir, "temp.csv");

				FileWriter writer = new FileWriter(temp);

				String line;
				int counter = 0;

				Set<String> keySet = data.keySet();
				Iterator<String> iterator = keySet.iterator();

				while (iterator.hasNext()) {
					line = reader.readLine();

					if (counter == 0) {
						line = line + "," + metricName;
						writer.append(line);
						writer.append('\n');

					} else {
						String key = iterator.next();
						Integer value = data.get(key);
						line = line + "," + value;
						writer.append(line);
						writer.append('\n');
					}

					counter += 1;

				}
				writer.flush();
				writer.close();

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
