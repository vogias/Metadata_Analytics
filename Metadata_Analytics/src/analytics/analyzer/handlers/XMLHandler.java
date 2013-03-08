/**
 * 
 */
package analytics.analyzer.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author vogias
 * 
 */
public class XMLHandler extends DefaultHandler {

	String tmpValue;
	Repository repositoryHandler;
	HashMap<String, Integer> dimensionalityMap;
	Vector<String> elements;

	public XMLHandler(Repository repositoryHandler) {
		// TODO Auto-generated constructor stub
		this.repositoryHandler = repositoryHandler;
		elements = new Vector<>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		for (int i = 0; i < attributes.getLength(); i++) {

			String name = attributes.getLocalName(i);
			String value = attributes.getValue(i);
			if (!name.contains("xsi") && !name.contains("xmlns")) {
				// System.out.println("Name:" + attributes.getLocalName(i)
				// + " Value:" + attributes.getValue(i));
				repositoryHandler.addAttributes(attributes.getQName(i),
						attributes.getValue(i));
			}
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		repositoryHandler.addxmlElements(qName);

		if (!dimensionalityMap.containsKey(qName)) {
			dimensionalityMap.put(qName, 1);
		} else {

			dimensionalityMap.put(qName, dimensionalityMap.get(qName) + 1);
		}

		if (!elements.contains(qName)) {

			elements.addElement(qName);
			repositoryHandler.addCompletenessElement(qName);
		}

		//entropy calculation
		try {
			repositoryHandler.addEvalue2File(qName, tmpValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		SAXParser parser = spf.newSAXParser();

		parser.parse(is, this);

	}
}
