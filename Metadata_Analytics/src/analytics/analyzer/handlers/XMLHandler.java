/**
 * 
 */
package analytics.analyzer.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import analytics.constants.AnalyticsConstants;

/**
 * @author vogias
 * 
 */
public class XMLHandler extends DefaultHandler {

	String tmpValue;
	Repository repositoryHandler;
	HashMap<String, Integer> dimensionalityMap;
	Vector<String> elements;
	AnalyticsConstants constants;
	String branche;
	String elPath;
	Stack<String> xPaths;
	String[] elements2Analyze;
	String[] elementsVocs;
	boolean all = false;

	public XMLHandler(Repository repositoryHandler, String[] elements2Analyze,
			String[] elementVocs) {
		// TODO Auto-generated constructor stub
		this.repositoryHandler = repositoryHandler;
		this.elements2Analyze = elements2Analyze;

		if (contains(elements2Analyze, "*"))
			all = true;
		else
			all = false;

		this.elementsVocs = elementVocs;

		elements = new Vector<>();
		constants = new AnalyticsConstants();
		branche = "";
		elPath = "";
		xPaths = new Stack<>();
	}

	private boolean contains(String[] els, String input) {

		boolean flag = false;
		for (int i = 0; i < els.length; i++) {

			if (input.equals(els[i])) {
				flag = true;
				break;
			}

		}

		return flag;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		branche += qName.toLowerCase();
		xPaths.push(branche);
		HashMap<String, String> elmt = new HashMap<>();

		for (int i = 0; i < attributes.getLength(); i++) {

			String name = attributes.getLocalName(i);
			String value = attributes.getValue(i);
			if (!name.contains("xsi:schemaLocation") && !name.contains("xmlns")) {
				// System.out.println("Name:" + attributes.getLocalName(i)
				// + " Value:" + attributes.getValue(i));

				if (all == false) {
					if (contains(elements2Analyze, branche)) {

						// HashMap<String, String> elmt = new HashMap<>();
						elmt.clear();

						elmt.put(branche, value);

						repositoryHandler.addAttributes(attributes.getQName(i),
								elmt);
					}
				} else {

					// HashMap<String, String> elmt = new HashMap<>();
					elmt.clear();
					elmt.put(branche, value);

					repositoryHandler.addAttributes(attributes.getQName(i),
							elmt);
				}
			} else if (name.contains("xmlns")) {
				repositoryHandler.setSchema(value);
			}
		}

		branche += ".";

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		String elmt = "";
		qName = qName.toLowerCase();

		if (branche.endsWith(qName + "" + ".")) {
			branche = branche.substring(0, branche.length() - qName.length()
					- 1);
			// branche = branche.toLowerCase();
			elmt = xPaths.elementAt(xPaths.size() - 1);
			xPaths.removeElementAt(xPaths.size() - 1);
			// System.out.println("--------End element-----");
			// System.out.println(elmt);
		}

		boolean controlFlag = false;

		if (all == false) {
			if (contains(elements2Analyze, elmt)) {
				doCalculations(elmt);
				controlFlag = true;
			}
			if (contains(elementsVocs, elmt) && controlFlag == false) {
				try {

					repositoryHandler.addEvalue2File(elmt, tmpValue);

				} catch (NullPointerException e) {
					// TODO: handle exception

					repositoryHandler.addEvalue2File(elmt, "");

				}
			}

		} else {

			doCalculations(elmt);

		}

	}

	private void doCalculations(String elmt) {

		repositoryHandler.addxmlElements(elmt);

		if (!dimensionalityMap.containsKey(elmt)) {
			dimensionalityMap.put(elmt, 1);
		} else {

			dimensionalityMap.put(elmt, dimensionalityMap.get(elmt) + 1);
		}

		if (!elements.contains(elmt)) {

			elements.addElement(elmt);
			repositoryHandler.addCompletenessElement(elmt);
		}

		// entropy calculation
		try {

			repositoryHandler.addEvalue2File(elmt, tmpValue);

		} catch (NullPointerException e) {
			// TODO: handle exception

			repositoryHandler.addEvalue2File(elmt, "");

		}

	}

	public void startDocument() {

		dimensionalityMap = new HashMap<>();

	}

	public void endDocument() {

		Set<String> keySet = dimensionalityMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Integer value = dimensionalityMap.get(key);

			repositoryHandler.addDimensionsElement(key, value);
		}

	}

	public void characters(char ch[], int start, int length)
			throws SAXException {

		tmpValue = new String(ch, start, length);

	}

	public void parseDocument(InputStream is) throws SAXException, IOException,
			ParserConfigurationException {
		// TODO Auto-generated method stub
		SAXParserFactory spf = SAXParserFactory.newInstance();

		Reader reader = new InputStreamReader(is, "UTF-8");

		InputSource inputStream = new InputSource(reader);
		inputStream.setEncoding("UTF-8");

		SAXParser parser = spf.newSAXParser();

		parser.parse(inputStream, this);

	}
}
