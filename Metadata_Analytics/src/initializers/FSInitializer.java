/**
 * 
 */
package initializers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Federation;
import analytics.analyzer.handlers.Repository;
import analytics.constants.AnalyticsConstants;
import analytics.input.Input;

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
			boolean fedFlag, String elements) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SAXException,
			ParserConfigurationException {
		// TODO Auto-generated method stub

		for (int i = 0; i < dataProviders.size(); i++) {

			String[] extensions = { "xml" };
			FileUtils utils = new FileUtils();
			Collection<File> xmls = utils.listFiles(
					(File) dataProviders.get(i), extensions, true);

			try {
				
				Repository repo = new Repository(xmls);

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

					repo.computeElementValueFreq(elements);

					FileUtils.deleteDirectory(new File("buffer"));

					repo.getAttributeFrequency();

					federation.appendFileSize(repo.getFileSizeDistribution());
					federation.appendNoRecords(repo.getXmls().size());
					repo.storeRepoGeneralInfo(true);
					federation.appendSchemas(repo.getSchema(false));
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
					repo.computeElementValueFreq(elements);

					repo.storeRepoGeneralInfo(false);

					FileUtils.deleteDirectory(new File("buffer"));

					repo.getAttributeFrequency();
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

		}

		if (fedFlag) {
			try {
				federation.getElementsSFrequency();
				federation.getElementsMCompletness();
				federation.getElementsMaxDimensionality();
				federation.getElementsMEntropy();
				federation.getAttributesSumFreq();
				federation.getElementValueSumFreq(elements);
				System.out.println("Average file size:"
						+ federation.getAverageFileSize() + " Bytes");
				System.out.println("Sum number of records:"
						+ federation.getRecordsSum() + " records");
				System.out.println("Sum storage requirements:"
						+ federation.getRequirements() + " bytes");
				federation.storeGeneralInfo2CSV();
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
}
