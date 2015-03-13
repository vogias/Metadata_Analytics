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

	// String tmpValue;

	StringBuffer tmpValue = new StringBuffer();
	Repository repositoryHandler;
	HashMap<String, Integer> dimensionalityMap;
	Vector<String> elements;
	AnalyticsConstants constants;
	String branche;
	// String elPath;
	Stack<String> xPaths;
	String[] elements2Analyze;
	String[] elementsVocs;
	boolean all, allattributes = false;
	StringBuffer buffer;
	String[] attributes2analyze;

	public XMLHandler(Repository repositoryHandler, String[] elements2Analyze,
			String[] elementVocs, String[] attributes2analyze) {
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
		// elPath = "";
		xPaths = new Stack<>();
		buffer = new StringBuffer();

		this.attributes2analyze = attributes2analyze;
		if (contains(attributes2analyze, "*"))
			allattributes = true;
		else
			allattributes = false;

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

		if (qName.contains(":")) {

			buffer.append(qName.substring(0, qName.indexOf(":") + 1));
			qName = qName.replace(buffer.toString(), "");
			buffer.delete(0, buffer.capacity());
		}

		tmpValue.setLength(0);

		branche += qName.toLowerCase();
		xPaths.push(branche);

		String elmt = "";

		for (int i = 0; i < attributes.getLength(); i++) {

			if (allattributes) {

				String name = attributes.getLocalName(i);

				String value = attributes.getValue(i);

				if (!name.contains("xsi:schemaLocation")
						&& !name.contains("xmlns")) {

					if (all == false) {
						if (contains(elements2Analyze, branche)) {

							if (value.equals(""))
								value = "empty";

							elmt = name + "#" + branche + "#" + value;

							repositoryHandler.addAttributes(elmt);
						}
					} else {

						if (value.equals(""))
							value = "empty";
						elmt = name + "#" + branche + "#" + value;
						repositoryHandler.addAttributes(elmt);
					}
				} else if (name.contains("xmlns")) {
					repositoryHandler.setSchema(value);
				}
			} else {
				String name = attributes.getLocalName(i);

				if (contains(this.attributes2analyze, name)) {

					String value = attributes.getValue(i);

					if (!name.contains("xsi:schemaLocation")
							&& !name.contains("xmlns")) {

						if (all == false) {
							if (contains(elements2Analyze, branche)) {

								if (value.equals(""))
									value = "empty";

								elmt = name + "#" + branche + "#" + value;

								repositoryHandler.addAttributes(elmt);
							}
						} else {

							if (value.equals(""))
								value = "empty";
							elmt = name + "#" + branche + "#" + value;
							repositoryHandler.addAttributes(elmt);
						}
					} else if (name.contains("xmlns")) {
						repositoryHandler.setSchema(value);
					}
				}
			}
		}

		branche += ".";

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		String elmt = "";
		qName = qName.toLowerCase();

		if (qName.contains(":")) {
			buffer.append(qName.substring(0, qName.indexOf(":") + 1));
			qName = qName.replace(buffer.toString(), "");
			buffer.delete(0, buffer.capacity());
		}

		if (branche.endsWith(qName + "" + ".")) {
			branche = branche.substring(0, branche.length() - qName.length()
					- 1);
			elmt = xPaths.elementAt(xPaths.size() - 1);
			xPaths.removeElementAt(xPaths.size() - 1);

		}

		// boolean controlFlag = false;

		if (all == false) {
			if (contains(elements2Analyze, elmt)) {
				doCalculations(elmt);
				// controlFlag = true;
			}
			// if (contains(elementsVocs, elmt) && controlFlag == false) {
			if (contains(elementsVocs, elmt)) {
				try {

					if (!tmpValue.toString().equals(""))
						repositoryHandler.addVoc(elmt, tmpValue.toString());
					else
						repositoryHandler.addVoc(elmt, "empty");

				} catch (NullPointerException e) {
					// TODO: handle exception

					repositoryHandler.addVoc(elmt, "empty");
				}
			}

		} else {

			doCalculations(elmt);

		}

		tmpValue.setLength(0);

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

			if (!tmpValue.toString().equals(""))
				repositoryHandler.addEntropyVoc(elmt, tmpValue.toString());
			else
				repositoryHandler.addEntropyVoc(elmt, "empty");
		} catch (NullPointerException e) {
			// TODO: handle exception

			repositoryHandler.addEntropyVoc(elmt, "empty");

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

		tmpValue.append(ch, start, length);

	}

	public void parseDocument(InputStream is, SAXParser parser)
			throws SAXException, IOException, ParserConfigurationException {
		// TODO Auto-generated method stub

		Reader reader = new InputStreamReader(is, "UTF-8");

		InputSource inputStream = new InputSource(reader);
		inputStream.setEncoding("UTF-8");

		parser.parse(inputStream, this);
	}
}
