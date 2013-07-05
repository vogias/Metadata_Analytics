/**
 * 
 */
package analytics.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

/**
 * @author vogias
 * 
 */
public class store2csv extends Storage {

	String elementDataFilePath;
	String specificElementDataFilePath;
	String generalDataFilePath;
	String attributeDataFilePath;
	boolean appendData = false;

	/**
	 * @return the appendData
	 */
	public boolean isAppendData() {
		return appendData;
	}

	/**
	 * @param appendData
	 *            the appendData to set
	 */
	public void setAppendData(boolean appendData) {
		this.appendData = appendData;
	}

	/**
	 * @return the generalDataFilePath
	 */
	public String getGeneralDataFilePath() {
		return generalDataFilePath;
	}

	/**
	 * @param generalDataFilePath
	 *            the generalDataFilePath to set
	 */
	public void setGeneralDataFilePath(String generalDataFilePath) {
		this.generalDataFilePath = generalDataFilePath;
	}

	/**
	 * @param elementDataFilePath
	 *            the elementDataFilePath to set
	 */
	public void setElementDataFilePath(String elementDataFilePath) {
		this.elementDataFilePath = elementDataFilePath;
	}

	/**
	 * @param specificElementDataFilePath
	 *            the specificElementDataFilePath to set
	 */
	public void setSpecificElementDataFilePath(
			String specificElementDataFilePath) {
		this.specificElementDataFilePath = specificElementDataFilePath;
	}

	/**
	 * @param attributeDataFilePath
	 *            the attributeDataFilePath to set
	 */
	public void setAttributeDataFilePath(String attributeDataFilePath) {
		this.attributeDataFilePath = attributeDataFilePath;
	}

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

		this.setElementDataFilePath(file.getAbsolutePath());
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

	@Override
	public void storeRepositoryData(String repoName, int noRecords,
			float avgFSize, float storageReq, String schema) {
		// TODO Auto-generated method stub

		String sFileName = repoName + "_GeneralInfo" + ".csv";

		File anls = new File("Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		File dir = new File(anls, repoName);
		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		this.setGeneralDataFilePath(file.getAbsolutePath());
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			// create header
			bw.append("Repository Name");
			bw.append(",");
			bw.append("Number of records");
			bw.append(",");
			bw.append("Average file size(bytes)");
			bw.append(",");
			bw.append("Approximate Storage requirements(bytes)");
			bw.append(",");
			bw.append("Metadata schema namespace");
			bw.newLine();

			// insert data
			bw.append(repoName);
			bw.append(",");
			bw.append(String.valueOf(noRecords));
			bw.append(",");
			bw.append(String.valueOf(avgFSize));
			bw.append(",");
			bw.append(String.valueOf(storageReq));
			bw.append(",");
			bw.append(schema);
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String getElementDataFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSpecificElementDataFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGeneralIngoFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeDataFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendRepositoryData(String repoName, int noRecords,
			float avgFSize, float storageReq, String schema) {

		// TODO Auto-generated method stub
		String sFileName = "Federation" + "_GeneralInfo" + ".csv";

		File anls = new File("Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		File dir = new File(anls, repoName);
		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		FileWriter writer;
		try {
			writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);

			if (!isAppendData()) {

				setAppendData(true);
				// create header
				bw.append("Repository Name");
				bw.append(",");
				bw.append("Number of records");
				bw.append(",");
				bw.append("Average file size(bytes)");
				bw.append(",");
				bw.append("Approximate Storage requirements(bytes)");
				bw.append(",");
				bw.append("Metadata schema namespace");
				bw.newLine();
				bw.append(repoName);
				bw.append(",");
				bw.append(String.valueOf(noRecords));
				bw.append(",");
				bw.append(String.valueOf(avgFSize));
				bw.append(",");
				bw.append(String.valueOf(storageReq));
				bw.append(",");
				bw.append(schema);
				bw.close();
			} else {
				// insert data
				bw.append(repoName);
				bw.append(",");
				bw.append(String.valueOf(noRecords));
				bw.append(",");
				bw.append(String.valueOf(avgFSize));
				bw.append(",");
				bw.append(String.valueOf(storageReq));
				bw.append(",");
				bw.append(schema);
				bw.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
