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
package analytics.filtering;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author vogias
 * 
 */
public class Filtering {
	DocumentBuilderFactory parseFactory;
	DocumentBuilder builder;

	XPathFactory filterFactory;
	XPath xpath;

	public Filtering() {
		// TODO Auto-generated constructor stub
		parseFactory = DocumentBuilderFactory.newInstance();
		parseFactory.setNamespaceAware(false);
		try {
			builder = parseFactory.newDocumentBuilder();

			filterFactory = XPathFactory.newInstance();
			xpath = filterFactory.newXPath();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Document parseDocument(File file) {

		Document parseDoc;
		try {

			parseDoc = builder.parse(file);

		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return parseDoc;
	}

	public boolean filterXML(File file, String expression) {
		Document doc = parseDocument(file);

		try {

			
			XPathExpression expr = xpath.compile(expression);

			Object result = expr.evaluate(doc);

			if (result != null) {
				if (result instanceof String) {

					String res = (String) result;
					try {
						double d = Double.parseDouble(res);
						System.out.println("Double value is:" + d);
						if (d > 0)
							return true;
						else
							return false;

					} catch (NumberFormatException ex) {

						if (res.equalsIgnoreCase("true"))
							return true;
						else if (res.equalsIgnoreCase("false"))
							return false;
						else {

							if (res.equals(""))
								return false;
							else
								return true;
						}
					}

				}

				else {

					return false;
					
				}
			}

			return false;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
}
