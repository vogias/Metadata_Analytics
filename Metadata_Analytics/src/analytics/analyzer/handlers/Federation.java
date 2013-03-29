/**
 * 
 */
package analytics.analyzer.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections.MultiHashMap;

import analytics.constants.AnalyticsConstants;
import analytics.storage.Storage;

/**
 * @author vogias
 * 
 */
public class Federation {

	MultiHashMap elementFreq;
	MultiHashMap elementComp;
	MultiHashMap elementDim;
	MultiHashMap elementEntropy;
	Vector<Float> fileSize;
	int numberOfRepos;
	Vector<String> repoNames;
	Properties props;

	public Federation(int repoNum) throws FileNotFoundException, IOException {
		// TODO Auto-generated constructor stub
		elementFreq = new MultiHashMap();
		elementComp = new MultiHashMap();
		elementDim = new MultiHashMap();
		elementEntropy = new MultiHashMap();
		fileSize = new Vector<>();
		numberOfRepos = repoNum;
		repoNames = new Vector<>();
		props = new Properties();
		props.load(new FileInputStream("configure.properties"));
	}

	public void appendFreqElements(HashMap<String, Double> elements) {

		Set<String> keySet = elements.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String elName = iterator.next();
			Double value = elements.get(elName);

			elementFreq.put(elName, value);
		}

	}

	public void appendFileSize(float fileSize) {
		this.fileSize.addElement(fileSize);
	}

	/**
	 * @return the fileSize
	 */
	public Vector<Float> getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(Vector<Float> fileSize) {
		this.fileSize = fileSize;
	}

	public void appendCompletnessElements(HashMap<String, Double> elements) {

		Set<String> keySet = elements.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String elName = iterator.next();
			Double value = elements.get(elName);

			elementComp.put(elName, value);
		}

	}

	public void appendDimensionalityElements(HashMap<String, Double> elements) {

		Set<String> keySet = elements.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String elName = iterator.next();
			Double value = elements.get(elName);

			elementDim.put(elName, value);
		}

	}

	public void appendEntropyElements(HashMap<String, Double> elements) {

		Set<String> keySet = elements.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String elName = iterator.next();
			Double value = elements.get(elName);

			elementEntropy.put(elName, value);
		}

	}

	@SuppressWarnings("deprecation")
	public HashMap<String, Double> getElementsSFrequency()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		Set keySet = elementFreq.keySet();

		HashMap<String, Double> data = new HashMap<>();

		Iterator iterator = keySet.iterator();

		while (iterator.hasNext()) {
			String nextElement = (String) iterator.next();

			if (!data.containsKey(nextElement)) {
				ArrayList<Double> collection = (ArrayList<Double>) elementFreq
						.getCollection(nextElement);
				data.put(nextElement, getFreqSum(collection));
			}

		}
		Storage storageClass = getStorageClass();
		storageClass.storeElementData(data, "Sum Frequency", "Federation",
				"_Element_Analysis", "Element Name");

		return data;
	}

	public float getAverageFileSize() {
		Vector<Float> fileSize = getFileSize();
		float avg = 0;
		for (int i = 0; i < fileSize.size(); i++)
			avg += fileSize.elementAt(i);
		avg = avg / fileSize.size();
		return avg;
	}

	@SuppressWarnings("deprecation")
	public HashMap<String, Double> getElementsMCompletness()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		Set keySet = elementComp.keySet();

		HashMap<String, Double> data = new HashMap<>();

		Iterator iterator = keySet.iterator();

		while (iterator.hasNext()) {
			String nextElement = (String) iterator.next();

			if (!data.containsKey(nextElement)) {
				ArrayList<Double> collection = (ArrayList<Double>) elementComp
						.getCollection(nextElement);
				data.put(nextElement, getAvg(collection));
			}

		}
		Storage storageClass = getStorageClass();
		storageClass.storeElementData(data, "Average Completeness(%)",
				"Federation", "_Element_Analysis", "Element Name");

		return data;
	}

	@SuppressWarnings("deprecation")
	public HashMap<String, Double> getElementsMaxDimensionality()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		Set keySet = elementDim.keySet();

		HashMap<String, Double> data = new HashMap<>();

		Iterator iterator = keySet.iterator();

		while (iterator.hasNext()) {
			String nextElement = (String) iterator.next();

			if (!data.containsKey(nextElement)) {
				ArrayList<Double> collection = (ArrayList<Double>) elementDim
						.getCollection(nextElement);
				data.put(nextElement, getMaxDimensionality(collection));
			}

		}
		Storage storageClass = getStorageClass();
		storageClass.storeElementData(data, "Max Dimensionality", "Federation",
				"_Element_Analysis", "Element Name");

		return data;
	}

	@SuppressWarnings("deprecation")
	public HashMap<String, Double> getElementsMEntropy()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		Set keySet = elementEntropy.keySet();

		HashMap<String, Double> data = new HashMap<>();

		Iterator iterator = keySet.iterator();

		while (iterator.hasNext()) {
			String nextElement = (String) iterator.next();

			if (!data.containsKey(nextElement)) {
				ArrayList<Double> collection = (ArrayList<Double>) elementEntropy
						.getCollection(nextElement);
				data.put(nextElement, getAvg(collection));
			}

		}
		Storage storageClass = getStorageClass();
		storageClass.storeElementData(data, "Average Entropy", "Federation",
				"_Element_Analysis", "Element Name");

		return data;
	}

	public void getAttributesSumFreq() {

		Vector<String> repoNames = getRepoNames();

		HashMap<String, Integer> data = new HashMap<>();
		for (int i = 0; i < repoNames.size(); i++) {
			File repoFolder = new File("Analysis", repoNames.elementAt(i));
			File attAnalysisTxt = new File(repoFolder, repoNames.elementAt(i)
					+ "_Attribute_Analysis.txt");

			System.out.println(attAnalysisTxt.getAbsolutePath());
			
			//getAttributesFromFile(attAnalysisTxt);
		}
	}

	private HashMap<String, Integer> getAttributesFromFile(File attFile) {

		BufferedReader br = null;

		try {

			String sCurrentLine;
			String attName = "";

			HashMap<String, Integer> data = new HashMap<>();
			br = new BufferedReader(new FileReader(attFile));

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("Attribute value:{")) {
					String att = sCurrentLine.substring(
							sCurrentLine.indexOf("{") + 1,
							sCurrentLine.lastIndexOf("}"));
					att = att.replace("=", ":");
					String freq = sCurrentLine.substring(
							sCurrentLine.lastIndexOf(":") + 1,
							sCurrentLine.length());
					int frequency = Integer.parseInt(freq);

					data.put(attName + "_" + att, frequency);
				}
				if (sCurrentLine.contains("Attribute_Name")) {

					attName = sCurrentLine.substring(
							sCurrentLine.indexOf(":") + 1,
							sCurrentLine.indexOf(","));

				}

			}

			return data;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	private Storage getStorageClass() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		AnalyticsConstants constants = new AnalyticsConstants();
		String storageClass = props.getProperty(constants.storageClass);

		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

		Class myClass = myClassLoader.loadClass(storageClass);

		Object whatInstance = myClass.newInstance();
		Storage storage = (Storage) whatInstance;

		return storage;
	}

	private Double getMaxDimensionality(ArrayList<Double> data) {

		return Collections.max(data);
	}

	private Double getAvg(ArrayList<Double> data) {

		Double avg = 0.0;
		for (int i = 0; i < data.size(); i++) {

			avg += data.get(i);
		}
		avg = avg / data.size();
		return avg;
	}

	private Double getFreqSum(ArrayList<Double> data) {
		Double sum = 0.0;
		for (int i = 0; i < data.size(); i++) {

			sum += data.get(i);
		}
		return sum;
	}

	public void addRepoName(String repoName) {
		repoNames.addElement(repoName);
	}

	/**
	 * @return the numberOfRepos
	 */
	public int getNumberOfRepos() {
		return numberOfRepos;
	}

	/**
	 * @param numberOfRepos
	 *            the numberOfRepos to set
	 */
	public void setNumberOfRepos(int numberOfRepos) {
		this.numberOfRepos = numberOfRepos;
	}

	/**
	 * @return the repoNames
	 */
	public Vector<String> getRepoNames() {
		return repoNames;
	}

	/**
	 * @param repoNames
	 *            the repoNames to set
	 */
	public void setRepoNames(Vector<String> repoNames) {
		this.repoNames = repoNames;
	}

	public static void main(String args[]) throws Exception {
		Federation federation = new Federation(3);

		federation.getAttributesSumFreq();
	}
}
