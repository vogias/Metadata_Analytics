/**
 * 
 */
package initializers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.IdentifiersList;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;

import analytics.analyzer.handlers.Federation;
import analytics.analyzer.handlers.Repository;
import analytics.constants.AnalyticsConstants;
import analytics.input.Input;
import analytics.input.OAITargetInput;

/**
 * @author vogias
 * 
 */
public class OAIInitializer extends InitializeProcess {

	Properties props;

	@Override
	public boolean pathCheck(Input in, Properties props) {
		// TODO Auto-generated method stub
		String repositoriesList = props
				.getProperty(AnalyticsConstants.oaiRepositoriesList);
		if (!repositoriesList.equals("")) {
			// String[] strings = repositoriesList.split(",");

			return true;
		} else {

			return false;
		}

	}

	@Override
	public List<?> getProvidersData(Input in, Properties props) {
		// TODO Auto-generated method stub
		try {
			this.props = props;
			String repositoriesList = props
					.getProperty(AnalyticsConstants.oaiRepositoriesList);

			String[] repos;
			if (repositoriesList.contains(","))
				repos = repositoriesList.split(",");
			else
				repos = new String[] { repositoriesList };

			List<String> dataProviders = new ArrayList<>();

			for (int i = 0; i < repos.length; i++) {
				dataProviders.add(repos[i]);

			}

			return (List<?>) dataProviders;
		} catch (NullPointerException ex) {
			System.out
					.println("The 'analytics.repositories.list' is not present in the properties file.");
			return null;
		}

	}

	public List<String> getMetadataFormats() {

		try {
			String metadataFormats = props
					.getProperty(AnalyticsConstants.metadataFormats);

			String[] formats;
			if (metadataFormats.contains(","))
				formats = metadataFormats.split(",");
			else
				formats = new String[] { metadataFormats };

			List<String> mFormats = new ArrayList<>();

			for (int i = 0; i < formats.length; i++) {
				mFormats.add(formats[i]);

			}

			return (List<String>) mFormats;
		} catch (NullPointerException ex) {
			System.err.println("The metadata format field is not present...");
			return null;
		}
	}

	@Override
	public void doAnalysis(Federation federation, final List<?> dataProviders,
			boolean fedFlag, String elementNames)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SAXException, ParserConfigurationException {
		List<String> metadataFormats = getMetadataFormats();

		if (metadataFormats == null)
			System.exit(-1);

		boolean flag = false;

		if (dataProviders.size() == metadataFormats.size())
			flag = true;

		for (int i = 0; i < dataProviders.size(); i++) {

			OAITargetInput input = new OAITargetInput();

			Collection<String> xmls;
			if (flag) {
				xmls = (Collection<String>) input.getData(
						(String) dataProviders.get(i), metadataFormats.get(i));
			} else {
				xmls = (Collection<String>) input.getData(
						(String) dataProviders.get(i), metadataFormats.get(0));

			}
			if (xmls == null || xmls.size() == 0) {
				System.out.println("Exiting...");
				System.exit(-1);
			}
			try {
				Repository repo = new Repository(xmls);

				repo.setRepoName(input.getRepoName());
				repo.setRecordsNum(xmls.size());

				if (fedFlag) {

					federation.addRepoName(input.getRepoName());

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

					repo.computeElementValueFreq(elementNames);

					FileUtils.deleteDirectory(new File("buffer"));

					repo.getAttributeFrequency();

					// federation.appendFileSize(repo.getFileSizeDistribution());
					federation.appendNoRecords(repo.getXmls().size());
					// repo.storeRepoGeneralInfo();
					federation.appendSchemas(repo.getSchema());
					// federation.appendRequirements(repo.getRequirements());

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
					repo.computeElementValueFreq(elementNames);

					// repo.storeRepoGeneralInfo();

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
				federation.getElementValueSumFreq(elementNames);
				System.out.println("Average file size:"
						+ federation.getAverageFileSize() + " Bytes");
				System.out.println("Sum number of records:"
						+ federation.getRecordsSum() + " records");
				System.out.println("Sum storage requirements:"
						+ federation.getRequirements() + " bytes");
				// federation.storeGeneralInfo2CSV();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}
}
