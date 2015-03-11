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
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Federation;
import analytics.analyzer.handlers.Repository;
import analytics.constants.AnalyticsConstants;
import analytics.filtering.Filtering;
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
		HashMap<String, Double> xmlElements = new HashMap<>();
		Vector<String> xmlElementsDistinct = new Vector<>();
		HashMap<String, Integer> attributes = new HashMap<>();
		HashMap<String, Integer> elementDims = new HashMap<>();
		HashMap<String, Integer> elementCompletness = new HashMap<>();
		Vector<String> elementEntropy = new Vector<>();
		HashMap<String, Double> elementImportance = new HashMap<>();

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

			FileWalker fileWalker;

			try {

				Repository repo = new Repository(attributes, xmlElements,
						xmlElementsDistinct, elementDims, elementCompletness,
						elementEntropy, elementImportance, props);
				File dataProv = (File) dataProviders.get(i);
				repo.setRepoName(dataProv.getName());

				String filterXMLs = props
						.getProperty(AnalyticsConstants.filteringEnabled);

				if (filterXMLs.equalsIgnoreCase("true")) {
					Filtering filtering = new Filtering();
					String expression = props
							.getProperty(AnalyticsConstants.xpathExpression);
					System.out.println("Filtering is enabled.");

					fileWalker = new FileWalker("xml", true, expression,
							filtering, repo, elements2Analyze, elementVocs);
				} else
					fileWalker = new FileWalker("xml", false, null, null, repo,
							elements2Analyze, elementVocs);

				if (fedFlag) {

					System.out
							.println("######################################");
					System.out.println("Analysing repository:"
							+ repo.getRepoName());

					Set<FileVisitOption> opts = Collections.emptySet();
					Files.walkFileTree(dataProv.toPath(), opts,
							Integer.MAX_VALUE, fileWalker);

					long endExp = System.currentTimeMillis();
					long res = (endExp - repo.getHandlerInput().getStartExp()) / 1000;
					System.out.println("Repository parsing duration(seconds):"
							+ res + "\n");

					federation.addRepoName(((File) dataProviders.get(i))
							.getName());

					System.out.println("Number of records:"
							+ repo.getNumberOfFiles());

					federation.appendFreqElements(repo.getElementFrequency());

					federation
							.appendCompletnessElements(
									repo.getElementCompleteness(),
									dataProviders.size());
					federation.appendImportanceElements(
							repo.getElementImportance(), dataProviders.size());

					federation.appendDimensionalityElements(repo
							.getElementDimensions());

					federation.appendEntropyElements(
							repo.computeElementEntropy(), dataProviders.size());

					this.logElementAnalysis(loggerEl, repo.getRepoName(),
							resultsPath);

					repo.computeElementValueFreq(elementVocs, logger);

					repo.getAttributeFrequency(loggerAtt);

					federation.appendFileSize(repo.getFileSizeDistribution());

					federation.appendNoRecords(repo.getNumberOfFiles());
					repo.storeRepoGeneralInfo(true);
					federation.appendInformativeness(repo
							.getAvgRepoInformativeness());
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
					Set<FileVisitOption> opts = Collections.emptySet();
					Files.walkFileTree(dataProv.toPath(), opts,
							Integer.MAX_VALUE, fileWalker);

					long endExp = System.currentTimeMillis();
					long res = (endExp - repo.getHandlerInput().getStartExp()) / 1000;
					System.out.println("Repository parsing duration(s):" + res
							+ "\n");

					System.out.println("Number of records:"
							+ repo.getNumberOfFiles());
					repo.getElementFrequency();
					repo.getElementCompleteness();
					repo.getElementDimensions();
					repo.getElementImportance();
					repo.computeElementEntropy();

					this.logElementAnalysis(loggerEl, repo.getRepoName(),
							resultsPath);

					repo.computeElementValueFreq(elementVocs, logger);

					repo.storeRepoGeneralInfo(false);

					repo.getAttributeFrequency(loggerAtt);

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
			elementDims.clear();
			elementCompletness.clear();

			elementEntropy.clear();
			elementImportance.clear();

		}

		if (fedFlag) {
			try {
				federation.getElementsSFrequency();
				federation.getElementsMCompletness();
				federation.getElementsMImportance();
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
				System.out.println("AVG informativeness(bits):"
						+ federation.getAVGInformativeness());

				federation.storeGeneralInfo2CSV();
				this.logElementAnalysis(loggerEl, "Federation", resultsPath);

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
