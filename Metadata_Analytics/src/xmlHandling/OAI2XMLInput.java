/**
 * 
 */
package xmlHandling;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Repository;
import analytics.analyzer.handlers.XMLHandler;

/**
 * @author vogias
 * 
 */
public class OAI2XMLInput extends XmlHandlerInput {

	@Override
	public void getInputData(Repository repo) throws SAXException,
			ParserConfigurationException {
		Iterator<String> iterator = (Iterator<String>) repo.getXmls()
				.iterator();
		int j = 0;
		try {
			while (iterator.hasNext()) {
				String xml = iterator.next();
				XMLHandler xmlHandler = new XMLHandler(repo);

				InputStream inS = new ByteArrayInputStream(xml.getBytes());

				xmlHandler.parseDocument(inS);
				j++;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
