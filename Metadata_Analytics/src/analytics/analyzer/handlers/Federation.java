/**
 * 
 */
package analytics.analyzer.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	HashMap<String, HashMap<String, Integer>> vocs;
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

	public void getAttributesSumFreq() throws IOException {

		Vector<String> repoNames = getRepoNames();

		HashMap<String, Integer> data = new HashMap<>();
		for (int i = 0; i < repoNames.size(); i++) {
			File repoFolder = new File("Analysis_Results",
					repoNames.elementAt(i));
			File attAnalysisCsv = new File(repoFolder, repoNames.elementAt(i)
					+ "_Attribute_Analysis.csv");

			if (attAnalysisCsv.exists()) {
				HashMap<String, Integer> attributesFromFile = getAttributesFromFile(attAnalysisCsv);

				Set<String> keySet = attributesFromFile.keySet();

				Iterator<String> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					String next = iterator.next();
					if (!data.containsKey(next)) {
						data.put(next, attributesFromFile.get(next));
					} else {
						data.put(next,
								data.get(next) + attributesFromFile.get(next));
					}
				}
			}
		}
		// System.out.println(data);
		saveAttFreqSums2File(data);

	}

	private void saveAttFreqSums2File(HashMap<String, Integer> data)
			throws IOException {
		File an = new File("Analysis_Results");

		if (!an.exists())
			an.mkdir();

		File fedDir = new File("Analysis_Results", "Federation");

		if (!fedDir.exists())
			fedDir.mkdir();

		File attInfo = new File(fedDir, "Federation" + "_Attribute_Analysis"
				+ ".csv");
		BufferedWriter writer = new BufferedWriter(new FileWriter(attInfo));

		Set<String> keySet = data.keySet();
		Iterator<String> iterator = keySet.iterator();

		writer.append("Attribute Name");
		writer.append(",");
		writer.append("Element Used");
		writer.append(",");
		writer.append("Attribute Value");
		writer.append(",");
		writer.append("Frequency");
		writer.newLine();

		while (iterator.hasNext()) {
			String next = iterator.next();
			String[] attributes = next.split(",");
			String attName = attributes[0];
			String element = attributes[1];
			String attValue = attributes[2];

			writer.append(attName);
			writer.append(",");
			writer.append(element);
			writer.append(",");
			writer.append(attValue);
			writer.append(",");

			Integer freqValue = data.get(next);
			writer.append(String.valueOf(freqValue));
			writer.newLine();

		}
		writer.close();
	}

	private HashMap<String, Integer> getAttributesFromFile(File attFile) {

		BufferedReader br = null;

		try {

			String sCurrentLine;

			HashMap<String, Integer> data = new HashMap<>();
			br = new BufferedReader(new FileReader(attFile));

			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine
						.contains("Attribute Name,Element Used,Attribute Value,Frequency")) {

					String[] attributes = sCurrentLine.split(",");

					String attName = attributes[0];
					String element = attributes[1];
					String value = attributes[2];
					String freq = attributes[3];
					int frequency = Integer.parseInt(freq);

					data.put(attName + "," + element + "," + value, frequency);
				}
				/*
				 * if (sCurrentLine.contains("Attribute_Name")) {
				 * 
				 * attName = sCurrentLine.substring( sCurrentLine.indexOf(":") +
				 * 1, sCurrentLine.indexOf(","));
				 * 
				 * }
				 */

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

	public void getElementValueSumFreq(String elementsAnalyzed)
			throws IOException {

		String[] elements = elementsAnalyzed.split(",");
		Vector<String> repos = getRepoNames();

		File ar = new File("Analysis_Results");

		vocs = new HashMap<>();
		for (int i = 0; i < repos.size(); i++) {
			String repo = repos.elementAt(i);
			String elementName = "";
			for (int j = 0; j < elements.length; j++) {
				elementName = elements[j];
				elementName = repo + "_" + elementName
						+ "_ElementValue_Analysis.csv";
				File repoFolder = new File(ar, repo);
				File csvFile = new File(repoFolder, elementName);

				if (csvFile.exists()) {

					appendVocabularies(elements[j],
							getVocabularyFromFile(csvFile));
				}

			}
		}
		// System.out.println(vocs);
		saveVocsToCSV();

	}

	private void saveVocsToCSV() throws IOException {

		File an = new File("Analysis_Results");
		File federationFolder = new File(an, "Federation");

		Set<String> keySet = vocs.keySet();
		Iterator<String> iterator = keySet.iterator();

		while (iterator.hasNext()) {
			String key = iterator.next();
			HashMap<String, Integer> map = vocs.get(key);
			File f = new File(federationFolder, "Federation_" + key
					+ "_ElementValue_Analysis.csv");

			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.append("Element Value,Frequency");
			writer.newLine();

			Set<String> keySet2 = map.keySet();
			Iterator<String> iterator2 = keySet2.iterator();

			while (iterator2.hasNext()) {
				String key2 = iterator2.next();
				Integer freq = map.get(key2);
				writer.append(key2 + "," + String.valueOf(freq));
				writer.newLine();
			}

			writer.close();

		}

	}

	private HashMap<String, Integer> mapMerge(HashMap<String, Integer> map1,
			HashMap<String, Integer> map2) {

		Set<String> keySet = map2.keySet();
		Iterator<String> iterator = keySet.iterator();

		while (iterator.hasNext()) {
			String key = iterator.next();
			Integer freq = map2.get(key);

			if (map1.containsKey(key)) {
				map1.put(key, map1.get(key) + freq);
			} else {
				map1.put(key, freq);
			}
		}

		return map1;
	}

	private void appendVocabularies(String element,
			HashMap<String, Integer> data) {

		if (vocs.containsKey(element)) {

			HashMap<String, Integer> map2 = vocs.get(element);

			vocs.put(element, mapMerge(data, map2));

		} else {
			vocs.put(element, data);
		}

	}

	private HashMap<String, Integer> getVocabularyFromFile(File f) {
		BufferedReader br = null;

		try {

			String sCurrentLine;

			HashMap<String, Integer> data = new HashMap<>();
			br = new BufferedReader(new FileReader(f));

			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.contains("Element Value,Frequency")) {

					String[] attributes = sCurrentLine.split(",");

					String voc = attributes[0];
					String freq = attributes[1];
					int frequency = Integer.parseInt(freq);

					data.put(voc, frequency);
				}

			}

			// System.out.println(data);
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
		avg = avg / this.numberOfRepos;
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
		Federation federation = new Federation(2);
		federation.addRepoName("TRAGLOR");
		federation.addRepoName("TRANGLOR_COPY");

		federation.getAttributesSumFreq();
	}
}
