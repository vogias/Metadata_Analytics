/*******************************************************************************
 * Copyright (c) 2014 Kostas Vogias.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Kostas Vogias - initial API and implementation
 ******************************************************************************/
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
import java.util.ArrayList;
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
import analytics.input.OAITargetInput;
import analytics.logging.ConfigureLogger;

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
			boolean fedFlag, String[] elements2Analyze, String elmtVoc)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SAXException, ParserConfigurationException {
		List<String> metadataFormats = getMetadataFormats();

		if (metadataFormats == null)
			System.exit(-1);

		boolean flag = false;

		if (dataProviders.size() == metadataFormats.size())
			flag = true;

		Vector<String> xmlElements = new Vector<>();
		Vector<String> xmlElementsDistinct = new Vector<>();
		MultiHashMap attributes = new MultiHashMap();
		MultiHashMap distinctAtts = new MultiHashMap();
		HashMap<String, Integer> elementDims = new HashMap<>();
		HashMap<String, Integer> elementCompletness = new HashMap<>();
		Vector<String> elementEntropy = new Vector<>();

		String resultsPath = props.getProperty(AnalyticsConstants.resultsPath);

		String[] elementVocs = elmtVoc.split(",");
		ConfigureLogger conf = new ConfigureLogger();
		Logger logger = conf.getLogger("vocAnalysis", resultsPath
				+ "Analysis_Results" + File.separator + "vocAnalysis.log");
		Logger loggerAtt = conf
				.getLogger("attributeAnalysis", resultsPath
						+ "Analysis_Results" + File.separator
						+ "attributeAnalysis.log");

		Logger loggerEl = conf.getLogger("elementAnalysis", resultsPath
				+ "Analysis_Results" + File.separator + "elementAnalysis.log");

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
				System.err.println("Exiting...");
				System.err.println("No XMLs found on target");
				System.exit(-1);
			}
			try {
				// Repository repo = new Repository(xmls,elements2Analyze);

				Repository repo = new Repository(xmls, attributes,
						distinctAtts, xmlElements, xmlElementsDistinct,
						elementDims, elementCompletness, elementEntropy, props);
				repo.setRepoName(input.getRepoName());
				repo.setRecordsNum(xmls.size());

				if (fedFlag) {

					federation.addRepoName(input.getRepoName());

					System.out
							.println("######################################");
					System.out.println("Analysing repository:"
							+ repo.getRepoName());
					System.out.println("Number of records:" + xmls.size());
					repo.parseXMLs(elements2Analyze, elementVocs);

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
					this.logElementAnalysis(loggerEl, repo.getRepoName(),
							resultsPath);

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
					repo.parseXMLs(elements2Analyze, elementVocs);
					repo.getElementFrequency();
					repo.getElementCompleteness();
					repo.getElementDimensions();
					repo.computeElementEntropy();
					repo.computeElementValueFreq(elementVocs, logger);

					// repo.storeRepoGeneralInfo();

					FileUtils.deleteDirectory(new File("buffer"));

					repo.getAttributeFrequency(loggerAtt);

					this.logElementAnalysis(loggerEl, repo.getRepoName(),
							resultsPath);
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
				this.logElementAnalysis(loggerEl, "Federation", resultsPath);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	@Override
	public void logElementAnalysis(Logger logger, String providerName,
			String resultsPath) {
		// TODO Auto-generated method stub
		File elAnalysisFile = new File(resultsPath + "Analysis_Results"
				+ File.separator + providerName + File.separator + providerName
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
