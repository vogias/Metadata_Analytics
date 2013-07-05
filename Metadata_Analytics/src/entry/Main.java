/**
 * 
 */
package entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Federation;
import analytics.analyzer.handlers.Repository;
import analytics.analyzer.handlers.XMLHandler;
import analytics.constants.AnalyticsConstants;
import analytics.input.Input;
import analytics.measures.FileSizeMean;

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
		AnalyticsConstants constants = new AnalyticsConstants();

		String repo2Analyze = props.getProperty(constants.analyzeRepositories);
		String federated = props.getProperty(constants.fedAnalysis);
		String temporal = props.getProperty(constants.temporalAnalysis);

		boolean fedFlag = false;
		boolean temporalFlag = false;

		try {
			if (!federated.equals(""))
				fedFlag = Boolean.parseBoolean(federated);

		} catch (NullPointerException ex) {
			fedFlag = false;
		}
		try {
			if (!temporal.equals(""))
				temporalFlag = Boolean.parseBoolean(temporal);

		} catch (NullPointerException ex) {
			temporalFlag = false;
		}

		try {
			if (repo2Analyze.equals(""))
				repo2Analyze = "*";
		} catch (NullPointerException ex) {
			repo2Analyze = "*";
		}
		String dataInputClass = props.getProperty(constants.inputClass);

		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

		Class myClass = myClassLoader.loadClass(dataInputClass);

		Object whatInstance = myClass.newInstance();
		Input in = (Input) whatInstance;

		try {

			Collection<File> dataProviders = (Collection<File>) in.getData(
					props.getProperty(constants.mdstorePath), repo2Analyze);

			if (dataProviders.isEmpty()) {

				System.out.println("Wrong data providers file names.");
				System.out.println("Exiting...");

				System.exit(-1);
			}

			List<File> dp = (List<File>) dataProviders;

			Federation federation = null;
			if (fedFlag) {
				System.out
						.println("Federated statistical analysis is activated...");
				federation = new Federation(dp.size(), temporalFlag);
			}

			for (int i = 0; i < dp.size(); i++) {

				String[] extensions = { "xml" };
				FileUtils utils = new FileUtils();
				Collection<File> xmls = utils.listFiles(dp.get(i), extensions,
						true);

				Repository repo = new Repository(xmls, temporalFlag);

				repo.setRepoName(dp.get(i).getName());
				repo.setRecordsNum(xmls.size());

				if (fedFlag) {
					federation.addRepoName(dp.get(i).getName());

					System.out
							.println("######################################");
					System.out.println("Analysing repository:"
							+ repo.getRepoName());
					System.out.println("Number of records:" + xmls.size());

					federation.appendFreqElements(repo.getElementFrequency());

					federation.appendCompletnessElements(repo
							.getElementCompleteness());

					federation.appendDimensionalityElements(repo
							.getElementDimensions());

					federation.appendEntropyElements(repo
							.computeElementEntropy());

					repo.computeElementValueFreq(props
							.getProperty(constants.elementValues));

					FileUtils.deleteDirectory(new File("buffer"));

					repo.getAttributeFrequency();

					federation.appendFileSize(repo.getFileSizeDistribution());
					federation.appendNoRecords(repo.getXmls().size());
					repo.storeRepoGeneralInfo();
					federation.appendSchemas(repo.getSchema());
					federation.appendRequirements(repo.getRequirements());

					System.out.println("Repository:" + repo.getRepoName()
							+ " analysis completed.");
					System.out
							.println("======================================");
				} else {
					System.out
							.println("######################################");
					System.out.println("Analysing repository:"
							+ repo.getRepoName());
					System.out.println("Number of records:"
							+ repo.getXmls().size());
					repo.getElementFrequency();
					repo.getElementCompleteness();
					repo.getElementDimensions();
					repo.computeElementEntropy();
					repo.computeElementValueFreq(props
							.getProperty(constants.elementValues));

					repo.storeRepoGeneralInfo();

					FileUtils.deleteDirectory(new File("buffer"));

					repo.getAttributeFrequency();
					System.out
							.println("======================================");
					System.out.println("Repository:" + repo.getRepoName()
							+ " analysis completed.");
					System.out
							.println("======================================");

				}

			}

			if (fedFlag) {
				federation.getElementsSFrequency();
				federation.getElementsMCompletness();
				federation.getElementsMaxDimensionality();
				federation.getElementsMEntropy();
				federation.getAttributesSumFreq();
				federation.getElementValueSumFreq(props
						.getProperty(constants.elementValues));
				System.out.println("Average file size:"
						+ federation.getAverageFileSize() + " Bytes");
				System.out.println("Sum number of records:"
						+ federation.getRecordsSum() + " records");
				System.out.println("Sum storage requirements:"
						+ federation.getRequirements() + " bytes");
				federation.storeGeneralInfo2CSV();

			}

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}
}
