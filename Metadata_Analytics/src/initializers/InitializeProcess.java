/**
 * 
 */
package initializers;

import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Federation;
import analytics.input.Input;

/**
 * @author vogias
 * 
 */
public abstract class InitializeProcess {

	public abstract boolean pathCheck(Input in,Properties props);
	public abstract List<?> getProvidersData(Input in,Properties props);

	public abstract void doAnalysis(Federation federation,
			List<?> dataProviders, boolean fedFlag,String[] elements2Analyze)
			throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SAXException, ParserConfigurationException;
}
