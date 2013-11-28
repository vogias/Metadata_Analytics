/**
 * 
 */
package xmlHandling;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Repository;

/**
 * @author vogias
 * 
 */
public abstract class XmlHandlerInput {

	public abstract void getInputData(Repository repo,String[] elements2Analyze) throws SAXException,
			ParserConfigurationException;
}
