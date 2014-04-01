/**
 * 
 */
package initializers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Federation;
import analytics.analyzer.handlers.Repository;
import analytics.constants.AnalyticsConstants;
import analytics.input.Input;
import analytics.logging.ConfigureLogger;

/**
 * @author vogias
 * 
 */
public class FSInitializer extends InitializeProcess {

	@Override
	public boolean pathCheck(Input in, Properties props) {
		// TODO Auto-generated method stub

		String repo2Analyze = props
				.getProperty(AnalyticsConstants.analyzeRepositories);
		try {
			if (repo2Analyze.equals(""))
				repo2Analyze = "*";
		} catch (NullPointerException ex) {
			repo2Analyze = "*";
		}
		Collection<File> dataProviders = (Collection<File>) in
				.getData(props.getProperty(AnalyticsConstants.mdstorePath),
						repo2Analyze);

		if (dataProviders.isEmpty()) {

			// System.out.println("Wrong data providers file names.");
			// System.out.println("Exiting...");

			return false;
		} else
			return true;
	}

	@Override
	public void doAnalysis(Federation federation, List<?> dataProviders,
			boolean fedFlag, String[] elements2Analyze, String elmtVoc)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SAXException, ParserConfigurationException {
		// TODO Auto-generated method stub

		Vector<String> xmlElements = new Vector<>();
		Vector<String> xmlElementsDistinct = new Vector<>();
		MultiHashMap attributes = new MultiHashMap();
		MultiHashMap distinctAtts = new MultiHashMap();
		HashMap<String, Integer> elementDims = new HashMap<>();
		HashMap<String, Integer> elementCompletness = new HashMap<>();
		Vector<String> elementEntropy = new Vector<>();
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("configure.properties"));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		String[] elementVocs = elmtVoc.split(",");

		ConfigureLogger conf = new ConfigureLogger();
		Logger logger = conf.getLogger("vocAnalysis", "Analysis_Results"
				+ File.separator + "vocAnalysis.log");

		Logger loggerAtt = conf.getLogger("attributeAnalysis",
				"Analysis_Results" + File.separator + "attributeAnalysis.log");

		Logger loggerEl = conf.getLogger("elementAnalysis", "Analysis_Results"
				+ File.separator + "elementAnalysis.log");

		for (int i = 0; i < dataProviders.size(); i++) {

			String[] extensions = { "xml" };
			FileUtils utils = new FileUtils();
			Collection<File> xmls = utils.listFiles(
					(File) dataProviders.get(i), extensions, true);

			try {

				// Repository repo = new Repository(xmls, elements2Analyze);

				Repository repo = new Repository(xmls, elements2Analyze,
						attributes, distinctAtts, xmlElements,
						xmlElementsDistinct, elementDims, elementCompletness,
						elementEntropy, props, elementVocs);

				repo.setRepoName(((File) dataProviders.get(i)).getName());
				repo.setRecordsNum(xmls.size());

				if (fedFlag) {

					federation.addRepoName(((File) dataProviders.get(i))
							.getName());

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

					repo.computeElementValueFreq(elementVocs, logger);

					FileUtils.deleteDirectory(new File("buffer"));

					repo.getAttributeFrequency(loggerAtt);

					federation.appendFileSize(repo.getFileSizeDistribution());
					federation.appendNoRecords(repo.getXmls().size());
					repo.storeRepoGeneralInfo(true);
					federation.appendSchemas(repo.getSchema(false));
					federation.appendRequirements(repo.getRequirements());

					this.logElementAnalysis(loggerEl, repo.getRepoName());

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
					repo.computeElementValueFreq(elementVocs, logger);

					repo.storeRepoGeneralInfo(false);

					FileUtils.deleteDirectory(new File("buffer"));

					repo.getAttributeFrequency(loggerAtt);

					this.logElementAnalysis(loggerEl, repo.getRepoName());
					System.out
							.println("======================================");
					System.out.println("Repository:" + repo.getRepoName()
							+ " analysis completed.");
					System.out
							.println("======================================");

				}
			} catch (IOException ex) {

				ex.printStackTrace();
			}
			xmlElements.clear();
			xmlElementsDistinct.clear();
			attributes.clear();
			distinctAtts.clear();
			elementDims.clear();
			elementCompletness.clear();
			elementEntropy.clear();

		}

		if (fedFlag) {
			try {
				federation.getElementsSFrequency();
				federation.getElementsMCompletness();
				federation.getElementsMaxDimensionality();
				federation.getElementsMEntropy();
				federation.getAttributesSumFreq(loggerAtt);
				federation.getElementValueSumFreq(elmtVoc, logger);
				System.out.println("Average file size:"
						+ federation.getAverageFileSize() + " Bytes");
				System.out.println("Sum number of records:"
						+ federation.getRecordsSum() + " records");
				System.out.println("Sum storage requirements:"
						+ federation.getRequirements() + " bytes");
				federation.storeGeneralInfo2CSV();
				this.logElementAnalysis(loggerEl, "Federation");
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

	@Override
	public List<?> getProvidersData(Input in, Properties props) {
		// TODO Auto-generated method stub

		String repo2Analyze = props
				.getProperty(AnalyticsConstants.analyzeRepositories);
		try {
			if (repo2Analyze.equals(""))
				repo2Analyze = "*";
		} catch (NullPointerException ex) {
			repo2Analyze = "*";
		}
		Collection<File> dataProviders = (Collection<File>) in.getData(
				props.getProperty(AnalyticsConstants.mdstorePath),
				props.getProperty(AnalyticsConstants.analyzeRepositories));

		return (List<?>) dataProviders;
	}

	@Override
	public void logElementAnalysis(Logger logger, String providerName) {
		// TODO Auto-generated method stub

		File elAnalysisFile = new File("Analysis_Results" + File.separator
				+ providerName + File.separator + providerName
				+ "_Element_Analysis.csv");
		BufferedReader br = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(elAnalysisFile));

			int counter = 0;
			StringBuffer buffer = new StringBuffer();
			while ((sCurrentLine = br.readLine()) != null) {
				if (counter > 0) {
					buffer.append(providerName);
					buffer.append(" " + sCurrentLine.replace(",", " "));
					logger.info(buffer.toString());
					buffer.delete(0, buffer.capacity());
				}
				counter++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
