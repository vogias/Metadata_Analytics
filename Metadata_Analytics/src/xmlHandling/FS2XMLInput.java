/**
 * 
 */
package xmlHandling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class FS2XMLInput extends XmlHandlerInput {

	@Override
	public void getInputData(Repository repo, String[] elements2Analyze)
			throws ParserConfigurationException {
		// TODO Auto-generated method stub
		Iterator<File> iterator = (Iterator<File>) repo.getXmls().iterator();
		// int j = 0;
		File xml = null;
		// try {

		while (iterator.hasNext()) {
			xml = iterator.next();

			XMLHandler xmlHandler = new XMLHandler(repo, elements2Analyze);

			InputStream inS = null;
			try {
				inS = new FileInputStream(xml);
				xmlHandler.parseDocument(inS);

			} catch (SAXException e) {
				// TODO Auto-generated catch block

				System.err.println("Bad formed xml file:" + xml.getPath());

				continue;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (inS != null) {
					try {
						inS.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		// } catch (IOException ex) {
		// ex.printStackTrace();
		//
		// }
	}
}
