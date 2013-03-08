/**
 * 
 */
package analytics.analyzer.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.io.FileUtils;

import analytics.measures.ElementCompleteness;
import analytics.measures.ElementFrequency;
import analytics.measures.FileSizeMean;
import analytics.measures.RelativeEntropy;

/**
 * @author vogias
 * 
 */
public class Repository {

	Vector<String> xmlElements;
	Vector<String> xmlElementsDistinct;
	MultiHashMap attributes, distinctAtts;
	HashMap<String, Integer> elementDims;
	HashMap<String, Integer> elementCompletness;
	Vector<String> elementEntropy;
	int recordsNum;
	String repoName;

	public Repository() {

		// TODO Auto-generated constructor stub
		xmlElements = new Vector<>();
		attributes = new MultiHashMap();
		distinctAtts = new MultiHashMap();
		elementCompletness = new HashMap<>();
		xmlElementsDistinct = new Vector<>();
		elementDims = new HashMap<>();
		recordsNum = 0;
		elementEntropy = new Vector<>();

	}

	/**
	 * @return the elementEntropy
	 */
	public Vector<String> getElementEntropy() {
		return elementEntropy;
	}

	public void addEvalue2File(String name, String value) throws IOException {

		if (name.contains(":"))
			name = name.replace(":", "_");

		File dir = new File("buffer");
		if (!dir.exists())
			dir.mkdir();

		File f = new File("buffer/" + name + ".txt");

		if (f.exists()) {
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(value);
			bw.newLine();
			bw.close();
		} else {
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(value);
			bw.newLine();
			bw.close();
		}
	}

	private Vector<String> getVectorFromFile(String filename)
			throws IOException {

		if (filename.contains(":"))
			filename = filename.replace(":", "_");

		File f = new File("buffer/" + filename + ".txt");

		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = br.readLine();
		Vector<String> data = new Vector<>();

		while (line != null) {
			data.add(line);
			line = br.readLine();
		}

		br.close();

		return data;
	}

	public void computeElementEntropy() throws IOException {

		Vector<String> elementsDistinct = getXmlElementsDistinct();

		for (int i = 0; i < elementsDistinct.size(); i++) {
			String element = elementsDistinct.elementAt(i);
			System.out.println("Element:" + element);
			Vector<String> vectorFromFile = getVectorFromFile(element);
			RelativeEntropy entropy = new RelativeEntropy();
			entropy.compute(vectorFromFile);
		}

		deleteAllFiles();
	}

	private void deleteAllFiles() throws IOException {
		FileUtils.deleteDirectory(new File("buffer"));
	}

	/**
	 * @return the recordsNum
	 */
	public int getRecordsNum() {
		return recordsNum;
	}

	/**
	 * @param recordsNum
	 *            the recordsNum to set
	 */
	public void setRecordsNum(int recordsNum) {
		this.recordsNum = recordsNum;
	}

	public void addCompletenessElement(String key) {

		if (!elementCompletness.containsKey(key))
			elementCompletness.put(key, 1);
		else {

			Integer inValue = elementCompletness.get(key);
			elementCompletness.put(key, inValue + 1);
		}
	}

	public HashMap<String, Integer> getElementCompletnessMatrix() {
		return elementCompletness;
	}

	public void addDimensionsElement(String key, int value) {

		if (!elementDims.containsKey(key))
			elementDims.put(key, value);
		else {
			Integer inValue = elementDims.get(key);
			if (inValue < value)
				elementDims.put(key, value);
		}
	}

	public void getElementDimensions() {

		Set<String> keySet = elementDims.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			System.out.println("Element:" + key + ", Dimensions:"
					+ elementDims.get(key));
		}
	}

	/**
	 * @return the elementDims
	 */
	public HashMap<String, Integer> getElementDims() {
		return elementDims;
	}

	/**
	 * @param elementDims
	 *            the elementDims to set
	 */
	public void setElementDims(HashMap<String, Integer> elementDims) {
		this.elementDims = elementDims;
	}

	/**
	 * @return the distinctAtts
	 */
	public MultiHashMap getDistinctAtts() {
		return distinctAtts;
	}

	/**
	 * @param distinctAtts
	 *            the distinctAtts to set
	 */
	public void setDistinctAtts(MultiHashMap distinctAtts) {
		this.distinctAtts = distinctAtts;
	}

	public void getFileSizeDistribution(Collection<File> xmls) {
		FileSizeMean fileSizeMean = new FileSizeMean();
		fileSizeMean.compute(xmls);
	}

	/**
	 * @return the xmlElementsDistinct
	 */
	public Vector<String> getXmlElementsDistinct() {
		return xmlElementsDistinct;
	}

	/**
	 * @param xmlElementsDistinct
	 *            the xmlElementsDistinct to set
	 */
	public void setXmlElementsDistinct(Vector<String> xmlElementsDistinct) {
		this.xmlElementsDistinct = xmlElementsDistinct;
	}

	/**
	 * @return the repoName
	 */
	public String getRepoName() {
		return repoName;
	}

	/**
	 * @param repoName
	 *            the repoName to set
	 */
	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	/**
	 * @return the attributes
	 */
	public MultiHashMap getAttributes() {
		return attributes;
	}

	public void showAttributes() {

		System.out.println(distinctAtts.toString());
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(MultiHashMap attributes) {
		this.attributes = attributes;
	}

	public void addxmlElements(String elementName) {

		xmlElements.addElement(elementName);
		if (!xmlElementsDistinct.contains(elementName))
			xmlElementsDistinct.addElement(elementName);
	}

	public void showElements() {
		for (int i = 0; i < xmlElementsDistinct.size(); i++) {
			System.out.println("Element:" + xmlElementsDistinct.elementAt(i));
		}
	}

	public void addAttributes(String name, String value) {

		// if (!attributes.containsValue(value))
		attributes.put(name, value);
		if (!distinctAtts.containsValue(value))
			distinctAtts.put(name, value);
	}

	/**
	 * @return the xmlElements
	 */
	public Vector<String> getXmlElements() {
		return xmlElements;
	}

	/**
	 * @param xmlElements
	 *            the xmlElements to set
	 */
	public void setXmlElements(Vector<String> xmlElements) {
		this.xmlElements = xmlElements;
	}

	public void getElementFrequency() {

		ElementFrequency elFrequency = new ElementFrequency(
				getXmlElementsDistinct());
		elFrequency.compute(xmlElements);

	}

	public void getAttributeFrequency() {

		ElementFrequency atFrequency = new ElementFrequency(getDistinctAtts());
		atFrequency.compute(attributes);
	}

	public void getElementCompleteness() {
		ElementCompleteness completeness = new ElementCompleteness(
				getRecordsNum());
		completeness.compute(getElementCompletnessMatrix());
	}
}
