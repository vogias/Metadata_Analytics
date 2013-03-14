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

	private void createHeaders(FileWriter fileWriter, String metricName) {

		try {
			fileWriter.append("Element Name");
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
	public void storeElementData(HashMap<String, Double> data, String metricName,
			String dataProvider, boolean append) {
		// TODO Auto-generated method stub

		if (append) {
			String sFileName = dataProvider + "_Element_Analysis" + ".csv";
			File file = new File(sFileName);

			try {
				if (!file.exists()) {
					FileWriter writer = new FileWriter(sFileName);
					createHeaders(writer, metricName);

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

					BufferedReader reader = new BufferedReader(new FileReader(
							file));

					File temp = new File("temp.csv");

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

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
