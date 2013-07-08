/**
 * 
 */
package entry;

import initializers.InitializeProcess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Federation;
import analytics.constants.AnalyticsConstants;
import analytics.input.Input;

/**
 * @author vogias
 * 
 */
public class Main {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, SAXException, ParserConfigurationException {

		// TODO Auto-generated method stub
		Properties props = new Properties();
		props.load(new FileInputStream("configure.properties"));

		String federated = props.getProperty(AnalyticsConstants.fedAnalysis);

		boolean fedFlag = false;

		try {
			if (!federated.equals(""))
				fedFlag = Boolean.parseBoolean(federated);

		} catch (NullPointerException ex) {
			fedFlag = false;
		}

		String dataInputClass = props
				.getProperty(AnalyticsConstants.inputClass);
		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
		Class myClass = myClassLoader.loadClass(dataInputClass);
		Object whatInstance = myClass.newInstance();
		Input in = (Input) whatInstance;

		String initilizerClass = props
				.getProperty(AnalyticsConstants.initilizer);
		myClassLoader = ClassLoader.getSystemClassLoader();
		myClass = myClassLoader.loadClass(initilizerClass);
		whatInstance = myClass.newInstance();

		InitializeProcess initProcess = (InitializeProcess) whatInstance;

		try {

			if (!initProcess.pathCheck(in, props)) {

				System.out.println("Wrong data providers file names.");
				System.out.println("Exiting...");

				System.exit(-1);
			}

			List<?> dp = initProcess.getProvidersData(in, props);

			Federation federation = null;
			if (fedFlag) {
				System.out
						.println("Federated statistical analysis is activated...");
				federation = new Federation(dp.size());// temporalFlag
			}

			initProcess.doAnalysis(federation, dp, fedFlag,
					AnalyticsConstants.elementValues);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}
}
