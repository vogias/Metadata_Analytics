/**
 * 
 */
package analytics.analyzer.handlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
	int numberOfRepos;
	Vector<String> repoNames;
	Properties props;

	public Federation(int repoNum) throws FileNotFoundException, IOException {
		// TODO Auto-generated constructor stub
		elementFreq = new MultiHashMap();
		elementComp = new MultiHashMap();
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

	public void appendCompletnessElements(HashMap<String, Double> elements) {

		Set<String> keySet = elements.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String elName = iterator.next();
			Double value = elements.get(elName);

			elementFreq.put(elName, value);
		}

	}

	@SuppressWarnings("deprecation")
	public HashMap<String, Double> getElementsMFrequency()
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
		storageClass.storeElementData(data, "Frequency", "Federation",
				"_Element_Analysis", "Element Name");

		return data;
	}

	@SuppressWarnings("deprecation")
	public HashMap<String, Double> getElementsMCompletness()
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
				data.put(nextElement, getCompletnessAvg(collection));
			}

		}
		Storage storageClass = getStorageClass();
		storageClass.storeElementData(data, "Completeness(%)", "Federation",
				"_Element_Analysis", "Element Name");

		return data;
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

	private Double getCompletnessAvg(ArrayList<Double> data) {
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

}
