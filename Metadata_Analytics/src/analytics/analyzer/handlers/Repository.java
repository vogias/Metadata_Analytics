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
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiHashMap;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import xmlHandling.XmlHandlerInput;
import analytics.constants.AnalyticsConstants;
import analytics.logging.ConfigureLogger;
import analytics.measures.ElementCompleteness;
import analytics.measures.ElementFrequency;
import analytics.measures.FileSizeMean;
import analytics.measures.RelativeEntropy;
import analytics.storage.Storage;

/**
 * @author vogias
 * 
 */
public class Repository {

	// Vector<String> xmlElements;

	HashMap<String, Double> xmlElements;
	Vector<String> xmlElementsDistinct;
	MultiHashMap attributes, distinctAtts;
	HashMap<String, Integer> elementDims;
	HashMap<String, Integer> elementCompletness;
	Vector<String> elementEntropy;
	int recordsNum;
	String repoName;
	Properties props;
	String schema;
	Storage storage;
	Collection<?> xmls;
	float fileSizeM;
	float requirements;

	// public Repository(Collection<?> xmls, String[] elements2Analyze,
	// MultiHashMap attributes, MultiHashMap distinctAtts,
	// Vector<String> xmlElements, Vector<String> xmlElementsDistinct,
	// HashMap<String, Integer> elementDims,
	// HashMap<String, Integer> elementCompletness,
	// Vector<String> elementEntropy, Properties props)

	public Repository(
			Collection<?> xmls,
			MultiHashMap attributes,
			MultiHashMap distinctAtts,
			HashMap<String, Double> xmlElements,// Vector<String> xmlElements
			Vector<String> xmlElementsDistinct,
			HashMap<String, Integer> elementDims,
			HashMap<String, Integer> elementCompletness,
			Vector<String> elementEntropy, Properties props)
			throws FileNotFoundException, IOException, SAXException,
			ParserConfigurationException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		// TODO Auto-generated constructor stub

		// xmlElements = new Vector<>();
		this.xmlElements = xmlElements;

		fileSizeM = 0;
		requirements = 0;
		this.xmls = xmls;
		// attributes = new MultiHashMap();
		this.attributes = attributes;
		// distinctAtts = new MultiHashMap();
		this.distinctAtts = distinctAtts;

		// elementCompletness = new HashMap<>();
		this.elementCompletness = elementCompletness;

		// xmlElementsDistinct = new Vector<>();
		this.xmlElementsDistinct = xmlElementsDistinct;

		// elementDims = new HashMap<>();
		this.elementDims = elementDims;

		recordsNum = 0;
		// elementEntropy = new Vector<>();
		this.elementEntropy = elementEntropy;

		// props = new Properties();
		this.props = props;
		// props.load(new FileInputStream("configure.properties"));
		//
		// XmlHandlerInput handlerInput = (XmlHandlerInput) this
		// .createXMLHandlerInputClass();
		//
		// handlerInput.getInputData(this, elements2Analyze, elementsVocs);
		this.storage = this.createStorageClass();

	}

	public void parseXMLs(String[] elements2Analyze, String[] elementsVocs)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SAXException, ParserConfigurationException {
		XmlHandlerInput handlerInput = (XmlHandlerInput) this
				.createXMLHandlerInputClass();

		handlerInput.getInputData(this, elements2Analyze, elementsVocs);

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

	/**
	 * @return the xmls
	 */
	public Collection<?> getXmls() {
		return xmls;
	}

	/**
	 * @param xmls
	 *            the xmls to set
	 */
	public void setXmls(Collection<File> xmls) {
		this.xmls = xmls;
	}

	private Storage createStorageClass() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		String storageClass = props
				.getProperty(AnalyticsConstants.storageClass);

		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

		Class myClass = myClassLoader.loadClass(storageClass);

		Object whatInstance = myClass.newInstance();
		Storage storage = (Storage) whatInstance;

		return storage;
	}

	private Object createXMLHandlerInputClass() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		String xmlInputClass = props
				.getProperty(AnalyticsConstants.xmlHandlerInput);

		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

		Class myClass = myClassLoader.loadClass(xmlInputClass);

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

	private Vector<String> getVectorFromFile(String filename) {

		if (filename.contains(":"))
			filename = filename.replace(":", "_");

		File f = new File("buffer/" + filename + ".txt");

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			Vector<String> data = new Vector<>();

			while (line != null) {
				data.add(line);
				line = br.readLine();
			}

			br.close();

			return data;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			Vector<String> data = new Vector<>();
			data.addElement("Element not found");
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block

			Vector<String> data = new Vector<>();
			data.addElement("Element not found");
			return data;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public HashMap<String, Double> computeElementEntropy() throws IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		System.out.println("Computing elements' entropy");

		Vector<String> elementsDistinct = getXmlElementsDistinct();

		HashMap<String, Double> data = new HashMap<>();
		for (int i = 0; i < elementsDistinct.size(); i++) {
			String element = elementsDistinct.elementAt(i);
			// System.out.println("Element:" + element);

			Vector<String> vectorFromFile = getVectorFromFile(element);
			RelativeEntropy entropy = new RelativeEntropy();
			data.put(element, entropy.compute(vectorFromFile));
		}

		Storage storageClass = getStorageClass();

		storageClass.storeElementData(data, "Entropy", this.getRepoName(),
				"_Element_Analysis", "Element Name", false);

		System.out.println("Done.");
		return data;

	}

	public void computeElementValueFreq(String[] elementName, Logger logger)
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		// String[] strings = elementName.split(",");

		for (int i = 0; i < elementName.length; i++) {
			// HashMap<String, Double> data = new HashMap<>();

			System.out.println("Element:" + elementName[i]
					+ " vocabulary statistical analysis");

			Vector<String> vectorFromFile = getVectorFromFile(elementName[i]);

			if (!vectorFromFile.contains("Element not found")) {

				Map cardinalityMap = CollectionUtils
						.getCardinalityMap(vectorFromFile);

				if (elementName[i].contains(":"))
					elementName[i] = elementName[i].replace(":", "_");

				Storage storageClass = getStorageClass();

				storageClass.storeElementValueData(
						(HashMap<String, Integer>) cardinalityMap, "Frequency",
						this.getRepoName(), "_" + elementName[i]
								+ "_ElementValue_Analysis", "Element Value",
						elementName[i], logger);

			}
		}

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

		/*
		 * if (key.equals("chor_dc:dc.dcterms:accessRights")) {
		 * System.out.println("Key:" + key + " value:" +
		 * elementCompletness.get(key)); }
		 */

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
			// String key = iterator.next();
			key.append(iterator.next());
			// System.out.println("Element:" + key + ", Dimensions:"
			// + elementDims.get(key));
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

	public float getFileSizeDistribution() {
		FileSizeMean fileSizeMean = new FileSizeMean();
		fileSizeMean.compute(xmls);
		fileSizeM = fileSizeMean.getFileSizeM();

		System.out.println("File size mean:" + fileSizeM + " bytes");
		return fileSizeM;
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

		// xmlElements.addElement(elementName);

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

	public void addAttributes(String name, HashMap<String, String> value) {

		// if (!attributes.containsValue(value))

		attributes.put(name, value);

		if (!distinctAtts.containsValue(value))
			distinctAtts.put(name, value);

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

	public HashMap<String, Double> getElementFrequency()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		System.out.println("Computing elements' Frequency...");
		ElementFrequency elFrequency = new ElementFrequency(
				getXmlElementsDistinct());
		// System.out.println(getXmlElementsDistinct());
		// HashMap<String, Double> data = elFrequency.compute(xmlElements);

		HashMap<String, Double> data = this.getXmlElements();

		Storage storageClass = getStorageClass();

		storageClass.storeElementData(data, "Frequency", this.getRepoName(),
				"_Element_Analysis", "Element Name", false);
		System.out.println("Done.");
		return data;
	}

	public void getAttributeFrequency(Logger logger) {

		System.out.println("Computing attributes' frequency.");
		MultiHashMap atts = getDistinctAtts();

		if (atts.size() > 0) {

			ElementFrequency atFrequency = new ElementFrequency(atts);
			atFrequency.compute(attributes, getRepoName(), logger);
		}
		System.out.println("Done");
	}

	public HashMap<String, Double> getElementCompleteness()
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		System.out.println("Computing elements' completeness...");
		ElementCompleteness completeness = new ElementCompleteness(
				getRecordsNum());

		HashMap<String, Double> map = completeness
				.compute(getElementCompletnessMatrix());

		Storage storageClass = getStorageClass();

		storageClass.storeElementData(map, "Completeness(%)",
				this.getRepoName(), "_Element_Analysis", "Element Name", false);
		System.out.println("Done.");
		return map;
	}

	public float getApproStorageRequirements() {

		requirements = xmls.size() * getFileSizeM();

		System.out.println(this.getRepoName()
				+ " Approximate Storage Requirements:" + requirements
				+ " bytes.");
		return requirements;
	}

	public void storeRepoGeneralInfo(boolean fed) {
		Storage storageClass = getStorageClass();

		if (fed == false)
			storageClass.storeRepositoryData(repoName, xmls.size(),
					getFileSizeDistribution(), getApproStorageRequirements(),
					getSchema(true));
		else
			storageClass.storeRepositoryData(repoName, xmls.size(),
					getFileSizeM(), getApproStorageRequirements(),
					getSchema(true));
	}

	public String getGeneralDataFilePath() {
		return storage.getGeneralIngoFilePath();
	}

	public String getElementDataFiledPath() {
		return storage.getElementDataFilePath();
	}
}
