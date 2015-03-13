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
package analytics.analyzer.handlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.slf4j.Logger;
import org.xml.sax.SAXException;

import xmlHandling.XmlHandlerInput;
import analytics.constants.AnalyticsConstants;
import analytics.measures.ElementCompleteness;
import analytics.measures.ElementFrequency;
import analytics.measures.RelativeEntropy;
import analytics.storage.Storage;

/**
 * @author vogias
 * 
 */
public class Repository {

	HashMap<String, Double> xmlElements;

	Vector<String> xmlElementsDistinct;

	HashMap<String, Integer> attributes;

	HashMap<String, Integer> elementDims;
	HashMap<String, Integer> elementCompletness;
	HashMap<String, Double> elementImportance;
	HashMap<String, Double> entropyData;
	Vector<String> elementEntropy;
	HashMap<String, HashMap<String, Integer>> vocabularies;
	float avgRepoInformativeness;

	String repoName;
	Properties props;
	String schema;
	Storage storage;

	File currentXmlFile;

	float fileSizeM;

	float requirements;
	HashMap<String, Double> completenessMap;
	XmlHandlerInput handlerInput;
	int numberOfFiles;
	long fileSize;

	public Repository(HashMap<String, Integer> attributes,
			HashMap<String, Double> xmlElements,
			Vector<String> xmlElementsDistinct,
			HashMap<String, Integer> elementDims,
			HashMap<String, Integer> elementCompletness,
			Vector<String> elementEntropy,
			HashMap<String, Double> elementImportance, Properties props)
			throws FileNotFoundException, IOException, SAXException,
			ParserConfigurationException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		// TODO Auto-generated constructor stub

		this.xmlElements = xmlElements;
		avgRepoInformativeness = 0;
		fileSizeM = 0;
		requirements = 0;
		entropyData = new HashMap<>();

		this.attributes = attributes;

		this.elementImportance = elementImportance;

		this.elementCompletness = elementCompletness;

		this.xmlElementsDistinct = xmlElementsDistinct;

		this.elementDims = elementDims;

		numberOfFiles = 0;
		fileSize = 0;
		this.elementEntropy = elementEntropy;

		this.props = props;

		vocabularies = new HashMap<>();
		handlerInput = (XmlHandlerInput) this.createXMLHandlerInputClass();
		this.storage = this.createStorageClass();

	}

	/**
	 * @return the handlerInput
	 */
	public XmlHandlerInput getHandlerInput() {
		return handlerInput;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void raiseFileSize(long fileSize) {
		this.fileSize += fileSize;
	}

	/**
	 * @return the numberOfFiles
	 */
	public int getNumberOfFiles() {
		return numberOfFiles;
	}

	/**
	 * @param numberOfFiles
	 *            the numberOfFiles to set
	 */
	public void raiseNumberOfFiles() {
		this.numberOfFiles += 1;
	}

	/**
	 * @return the currentXmlFile
	 */
	public File getCurrentXmlFile() {
		return currentXmlFile;
	}

	/**
	 * @param currentXmlFile
	 *            the currentXmlFile to set
	 */
	public void setCurrentXmlFile(File currentXmlFile) {
		this.currentXmlFile = currentXmlFile;
	}

	/**
	 * @return the vocabularies
	 */
	public HashMap<String, HashMap<String, Integer>> getVocabularies() {
		return vocabularies;
	}

	/**
	 * @return the avgRepoInformativeness
	 */
	public float getAvgRepoInformativeness() {
		return avgRepoInformativeness;
	}

	/**
	 * @param avgRepoInformativeness
	 *            the avgRepoInformativeness to set
	 */
	private void setAvgRepoInformativeness(float avgRepoInformativeness) {
		this.avgRepoInformativeness = avgRepoInformativeness;
	}

	/**
	 * @return the completenessMap
	 */
	public HashMap<String, Double> getCompletenessMap() {
		return completenessMap;
	}

	public void parseXML(String[] elements2Analyze, String[] elementsVocs,
			String[] attributes2analyze, SAXParser parser)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SAXException, ParserConfigurationException {

		handlerInput.getInputData(this, elements2Analyze, elementsVocs,
				attributes2analyze, parser);

	}

	/**
	 * @return the requirements
	 */
	public float getRequirements() {
		return requirements;
	}

	/**
	 * @param requirements
	 *            the requirements to set
	 */
	public void setRequirements(float requirements) {
		this.requirements = requirements;
	}

	/**
	 * @return the schema
	 */
	public String getSchema(boolean showInfo) {
		if (showInfo == true)
			System.out.println(getRepoName() + " schema namespace:" + schema);

		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	private Storage createStorageClass() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		String storageClass = props
				.getProperty(AnalyticsConstants.storageClass);

		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

		Class<?> myClass = myClassLoader.loadClass(storageClass);

		Object whatInstance = myClass.newInstance();
		Storage storage = (Storage) whatInstance;

		return storage;
	}

	private Object createXMLHandlerInputClass() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		String xmlInputClass = props
				.getProperty(AnalyticsConstants.xmlHandlerInput);

		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

		Class<?> myClass = myClassLoader.loadClass(xmlInputClass);

		Object whatInstance = myClass.newInstance();

		return whatInstance;
	}

	private Storage getStorageClass() {

		return this.storage;
	}

	/**
	 * @return the elementEntropy
	 */

	public Vector<String> getElementEntropy() {
		return elementEntropy;
	}

	public void addVoc(String element, String voc) {

		HashMap<String, Integer> data = new HashMap<>();

		voc = voc.trim();

		if (voc.equals(""))
			voc = "empty";

		if (!getVocabularies().containsKey(element)) {

			data.put(voc, 1);

			getVocabularies().put(element, data);
			// data.clear();
		} else {

			data = getVocabularies().get(element);

			if (!data.containsKey(voc)) {

				data.put(voc, 1);

			} else {

				data.put(voc, data.get(voc) + 1);

			}

			getVocabularies().put(element, data);

		}

	}

	public void addEvalue2File(String name, String value) {

		if (name.contains(":"))
			name = name.replace(":", "_");

		File dir = new File("buffer");
		if (!dir.exists())
			dir.mkdir();

		File f = new File("buffer/" + name + ".txt");

		if (f.exists()) {
			FileWriter fw = null;
			try {
				fw = new FileWriter(f, true);
				writeValue2File(fw, value);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} else {
			FileWriter fw = null;
			try {
				f.createNewFile();
				fw = new FileWriter(f);
				writeValue2File(fw, value);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	}

	private void writeValue2File(FileWriter fw, String value) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(fw);
			bw.write(value);
			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return the entropyData
	 */
	public HashMap<String, Double> getEntropyData() {
		return entropyData;
	}

	public HashMap<String, Double> computeElementEntropy() throws IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		System.out.println("Computing elements' entropy");

		Vector<String> elementsDistinct = getXmlElementsDistinct();

		StringBuffer element = new StringBuffer();

		for (int i = 0; i < elementsDistinct.size(); i++) {

			element.append(elementsDistinct.elementAt(i));

			HashMap<String, Integer> vocs = this.getVocabularies().get(
					element.toString());

			RelativeEntropy entropy = new RelativeEntropy();

			getEntropyData().put(element.toString(), entropy.compute(vocs));

			element.delete(0, element.length());
		}

		Storage storageClass = getStorageClass();

		storageClass.storeElementData(getEntropyData(), "Entropy",
				this.getRepoName(), "_Element_Analysis", "Element Name", false);

		System.out.println("Done.");
		return getEntropyData();

	}

	private void computeAVGRepoInformativeness() {

		int noe = getEntropyData().size();

		Set<String> keySet = getEntropyData().keySet();

		Iterator<String> iterator = keySet.iterator();

		float sum = 0;

		StringBuffer elName = new StringBuffer();
		while (iterator.hasNext()) {
			elName.append(iterator.next());

			sum += getEntropyData().get(elName.toString());

			elName.delete(0, elName.length());
		}

		this.setAvgRepoInformativeness(sum / noe);

	}

	public void computeElementValueFreq(String[] elementName, Logger logger)
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		int time = 0;
		for (int i = 0; i < elementName.length; i++) {

			System.out.println("Element:" + elementName[i]
					+ " vocabulary statistical analysis");

			HashMap<String, Integer> vocs = this.getVocabularies().get(
					elementName[i]);

			if (vocs != null) {

				if (elementName[i].contains(":"))
					elementName[i] = elementName[i].replace(":", "_");

				Storage storageClass = getStorageClass();

				storageClass.storeElementValueData(vocs, "Frequency",
						this.getRepoName(), "_" + elementName[i]
								+ "_ElementValue_Analysis", "Element Value",
						elementName[i], logger, time);

			}
		}

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

	public HashMap<String, Double> getElementDimensions()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		System.out.println("Computing elements' dimensionality");
		Set<String> keySet = elementDims.keySet();
		Iterator<String> iterator = keySet.iterator();
		HashMap<String, Double> data = new HashMap<>();
		StringBuffer key = new StringBuffer();
		while (iterator.hasNext()) {

			key.append(iterator.next());
			data.put(key.toString(), (double) elementDims.get(key.toString()));
			key.delete(0, key.length());
		}

		Storage storageClass = getStorageClass();

		storageClass.storeElementData(data, "Dimensions", this.getRepoName(),
				"_Element_Analysis", "Element Name", false);
		System.out.println("Done.");
		return data;
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

	public float getFileSizeDistribution() {

		float fz = getFileSize() / getNumberOfFiles();
		setFileSizeM(fz);

		System.out.println("File size mean:" + fz + " bytes");
		return fz;
	}

	/**
	 * @return the fileSizeM
	 */
	public float getFileSizeM() {
		return fileSizeM;
	}

	/**
	 * @param fileSizeM
	 *            the fileSizeM to set
	 */
	public void setFileSizeM(float fileSizeM) {
		this.fileSizeM = fileSizeM;
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

	public void addxmlElements(String elementName) {

		if (!xmlElements.containsKey(elementName)) {
			xmlElements.put(elementName, (double) 1);
		} else {
			xmlElements.put(elementName, xmlElements.get(elementName) + 1);
		}

		if (!xmlElementsDistinct.contains(elementName))
			xmlElementsDistinct.addElement(elementName);
	}

	public void showElements() {
		for (int i = 0; i < xmlElementsDistinct.size(); i++) {
			System.out.println("Element:" + xmlElementsDistinct.elementAt(i));
		}
	}

	public void addAttributes(String name) {

		if (!attributes.containsKey(name)) {
			attributes.put(name, 1);
		} else {

			attributes.put(name, attributes.get(name) + 1);
		}

	}

	/**
	 * @return the xmlElements
	 */
	public HashMap<String, Double> getXmlElements() {
		return xmlElements;
	}

	/**
	 * @param xmlElements
	 *            the xmlElements to set
	 */
	public void setXmlElements(HashMap<String, Double> xmlElements) {
		this.xmlElements = xmlElements;
	}

	public HashMap<String, Double> getElementImportance() {

		System.out.println("Computing elements' Importance...");
		Set<String> keySet = this.getXmlElements().keySet();
		Iterator<String> iterator = keySet.iterator();
		StringBuffer key = new StringBuffer();

		while (iterator.hasNext()) {

			key.append(iterator.next());
			Double frequency = this.getXmlElements().get(key.toString());
			Double completeness = this.getCompletenessMap().get(key.toString());
			elementImportance.put(key.toString(), frequency * completeness);
			key.delete(0, key.length());
		}

		Storage storageClass = getStorageClass();

		storageClass.storeElementData(elementImportance, "Importance",
				this.getRepoName(), "_Element_Analysis", "Element Name", false);

		System.out.println("Done.");
		return elementImportance;
	}

	public HashMap<String, Double> getElementFrequency()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		System.out.println("Computing elements' Frequency...");

		HashMap<String, Double> data = this.getXmlElements();

		Storage storageClass = getStorageClass();

		storageClass.storeElementData(data, "Frequency", this.getRepoName(),
				"_Element_Analysis", "Element Name", false);
		System.out.println("Done.");
		return data;
	}

	public void getAttributeFrequency(Logger logger) {

		System.out.println("Computing attributes' frequency.");

		if (attributes.size() > 0) {

			ElementFrequency atFrequency = new ElementFrequency(attributes);
			atFrequency.compute(attributes, getRepoName(), logger);

		}

		System.out.println("Done");
	}

	public HashMap<String, Double> getElementCompleteness()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		System.out.println("Computing elements' completeness...");
		ElementCompleteness completeness = new ElementCompleteness(
				getNumberOfFiles());

		completenessMap = completeness.compute(getElementCompletnessMatrix());

		Storage storageClass = getStorageClass();

		storageClass.storeElementData(completenessMap, "Completeness(%)",
				this.getRepoName(), "_Element_Analysis", "Element Name", false);
		System.out.println("Done.");
		return completenessMap;
	}

	public float getApproStorageRequirements() {

		requirements = getFileSize();

		System.out.println(this.getRepoName()
				+ " Approximate Storage Requirements:" + requirements
				+ " bytes.");
		return requirements;
	}

	public void storeRepoGeneralInfo(boolean fed) {
		Storage storageClass = getStorageClass();
		this.computeAVGRepoInformativeness();

		if (fed == false)
			storageClass.storeRepositoryData(repoName, getNumberOfFiles(),
					getFileSizeDistribution(), getApproStorageRequirements(),
					getAvgRepoInformativeness(), getSchema(true));
		else
			storageClass.storeRepositoryData(repoName, getNumberOfFiles(),
					getFileSizeM(), getApproStorageRequirements(),
					getAvgRepoInformativeness(), getSchema(true));
	}

	public String getGeneralDataFilePath() {
		return storage.getGeneralIngoFilePath();
	}

	public String getElementDataFiledPath() {
		return storage.getElementDataFilePath();
	}
}
