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
package analytics.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import analytics.constants.AnalyticsConstants;
import analytics.logging.ConfigureLogger;

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
			String headerColumn, Boolean fed) {
		// TODO Auto-generated method stub

		String sFileName = dataProvider + analysisType + ".csv";

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

		File anls = new File(props.getProperty(AnalyticsConstants.resultsPath)
				+ "Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		File dir = new File(anls, dataProvider);
		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		this.setElementDataFilePath(file.getAbsolutePath());
		FileWriter writer = null;
		BufferedWriter bw = null;

		BufferedReader reader = null;
		try {

			if (file.exists() && isAppendData() == false) {

				if (fed == false)
					file.delete();
				setAppend(true);
			} else if (!file.exists() && isAppendData() == false)
				setAppend(true);

			if (!file.exists() && isAppendData() == true) {
				writer = new FileWriter(file);
				bw = new BufferedWriter(writer);
				createHeaders(bw, metricName, headerColumn);

				Set<String> keySet = data.keySet();
				Iterator<String> iterator = keySet.iterator();

				StringBuffer key = new StringBuffer();
				while (iterator.hasNext()) {
					key.append(iterator.next());
					bw.append(key.toString());
					bw.append(',');
					Double value = data.get(key.toString());
					bw.append(String.valueOf(value));
					bw.newLine();
					key.delete(0, key.length());
				}

				bw.close();
				writer.close();
			} else if (file.exists() && isAppendData() == true) {

				reader = new BufferedReader(new FileReader(file));

				File temp = new File(dir, "temp.csv");

				writer = new FileWriter(temp);
				bw = new BufferedWriter(writer);

				String line;
				int counter = 0;
				StringBuffer key = new StringBuffer();
				while ((line = reader.readLine()) != null) {

					String[] split = line.split(",");

					key.append(split[0]);

					if (counter == 0) {
						line = line + "," + metricName;
						bw.append(line);
						bw.newLine();

					} else {

						Double value = data.get(key.toString());
						
						line = line + "," + value;
						bw.append(line);
						bw.newLine();
					}

					counter += 1;
					key.delete(0, key.length());

				}
				bw.close();
				writer.close();

				FileUtils.copyFile(temp, file);
				temp.delete();
				reader.close();

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (reader != null)
					reader.close();
				if (writer != null)
					writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void storeElementValueData(HashMap<String, Integer> data,
			String metricName, String dataProvider, String analysisType,
			String headerColumn, String element, Logger logger, int time) {
		// TODO Auto-generated method stub

		String sFileName = dataProvider + analysisType + ".csv";

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
		;
		File anls = new File(props.getProperty(AnalyticsConstants.resultsPath)
				+ "Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		else {
			
		}

		File dir = new File(anls, dataProvider);
		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);
		FileWriter writer;
		BufferedWriter bw = null;
		BufferedReader reader = null;
		try {

			if (file.exists() && time == 0)
				file.delete();

			writer = new FileWriter(file);
			bw = new BufferedWriter(writer);
			createHeaders(bw, metricName, headerColumn);

			Set<String> keySet = data.keySet();
			Iterator<String> iterator = keySet.iterator();
			StringBuffer logString = new StringBuffer();

			StringBuffer key = new StringBuffer();

			while (iterator.hasNext()) {
				key.append(iterator.next());

				Integer value = data.get(key.toString());

				if (key.toString().contains(","))
					key.replace(0, key.length(),
							key.toString().replace(",", "/"));
				
				bw.append(key);
				logString.append(dataProvider);
				logString.append(" " + element);
				logString.append(" " + key.toString().replace(" ", "_"));
				bw.append(',');
				bw.append(String.valueOf(value));
				logString.append(" " + String.valueOf(value));
				bw.newLine();

				logger.info(logString.toString());
				logString.delete(0, logString.capacity());
				key.delete(0, key.length());
			}
			bw.close();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (reader != null)
					reader.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void storeRepositoryData(String repoName, int noRecords,
			float avgFSize, float storageReq, float informativeness,
			String schema) {
		// TODO Auto-generated method stub

		ConfigureLogger conf = new ConfigureLogger();

		String sFileName = repoName + "_GeneralInfo" + ".csv";

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
		;
		File anls = new File(props.getProperty(AnalyticsConstants.resultsPath)
				+ "Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		File dir = new File(anls, repoName);
		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		Logger logger = conf.getLogger("generalInfo", anls + File.separator
				+ "repoGeneralInfo.log");

		StringBuffer logString = new StringBuffer();

		this.setGeneralDataFilePath(file.getAbsolutePath());
		FileWriter writer;
		BufferedWriter bw = null;
		try {
			writer = new FileWriter(file);
			bw = new BufferedWriter(writer);
			// create header
			bw.append("Repository Name");
			bw.append(",");
			bw.append("Number of records");
			bw.append(",");
			bw.append("Average file size(bytes)");
			bw.append(",");
			bw.append("Approximate Storage requirements(bytes)");
			bw.append(",");
			bw.append("Repository Informativeness(bits)");
			bw.append(",");
			bw.append("Metadata schema namespace");
			bw.newLine();

			// insert data
			bw.append(repoName);
			logString.append(repoName);
			bw.append(",");
			bw.append(String.valueOf(noRecords));
			logString.append(" " + String.valueOf(noRecords));
			bw.append(",");
			bw.append(String.valueOf(avgFSize));
			logString.append(" " + String.valueOf(avgFSize));
			bw.append(",");
			bw.append(String.valueOf(storageReq));
			logString.append(" " + String.valueOf(storageReq));

			bw.append(",");
			bw.append(String.valueOf(informativeness));
			logString.append(" " + String.valueOf(informativeness));
			bw.append(",");
			bw.append(schema);
			logString.append(" " + schema);
			bw.close();

			logger.info(logString.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
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
			float avgFSize, float storageReq, float informativeness,
			String schema) throws IOException {

		// TODO Auto-generated method stub
		String sFileName = "Federation" + "_GeneralInfo" + ".csv";
		ConfigureLogger conf = new ConfigureLogger();

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
		;
		File anls = new File(props.getProperty(AnalyticsConstants.resultsPath)
				+ "Analysis_Results");

		if (!anls.exists())
			anls.mkdir();

		File dir = new File(anls, "Federation");
		if (!dir.exists())
			dir.mkdir();

		File file = new File(dir, sFileName);

		if (!file.exists())
			file.createNewFile();

		Logger logger = conf.getLogger("generalInfo", anls + File.separator
				+ "repoGeneralInfo.log");

		StringBuffer logString = new StringBuffer();

		FileWriter writer;
		BufferedWriter bw = null;
		try {
			writer = new FileWriter(file, true);
			bw = new BufferedWriter(writer);

			if (!isAppendData()) {

				setAppend(true);
				// create header
				bw.append("Repository Name");

				bw.append(",");
				bw.append("Number of records");
				bw.append(",");
				bw.append("Average file size(bytes)");
				bw.append(",");
				bw.append("Approximate Storage requirements(bytes)");
				bw.append(",");
				bw.append("AVG informativeness(bits)");
				bw.append(",");
				bw.append("Metadata schema namespace");
				bw.newLine();
				bw.append(repoName);
				logString.append(repoName);
				bw.append(",");
				bw.append(String.valueOf(noRecords));
				logString.append(" " + String.valueOf(noRecords));
				bw.append(",");
				bw.append(String.valueOf(avgFSize));
				logString.append(" " + String.valueOf(avgFSize));
				bw.append(",");
				bw.append(String.valueOf(storageReq));
				logString.append(" " + String.valueOf(storageReq));
				bw.append(",");
				bw.append(String.valueOf(informativeness));
				logString.append(" " + String.valueOf(informativeness));
				bw.append(",");
				bw.append(schema);
				logString.append(" " + String.valueOf(schema));
				bw.newLine();
				bw.close();

			} else {
				// insert data
				bw.append(repoName);
				logString.append(repoName);
				bw.append(",");
				bw.append(String.valueOf(noRecords));
				logString.append(" " + String.valueOf(noRecords));
				bw.append(",");
				bw.append(String.valueOf(avgFSize));
				logString.append(" " + String.valueOf(avgFSize));
				bw.append(",");
				bw.append(String.valueOf(storageReq));
				logString.append(" " + String.valueOf(storageReq));
				bw.append(",");
				bw.append(String.valueOf(informativeness));
				logString.append(" " + String.valueOf(informativeness));
				bw.append(",");
				bw.append(schema);
				logString.append(" " + String.valueOf(schema));
				bw.newLine();
				bw.close();
			}
			logger.info(logString.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public void setAppend(Boolean append) {
		// TODO Auto-generated method stub
		this.appendData = append;
	}

}
