/**
 * 
 */
package entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
		String dataInputClass = props.getProperty(constants.inputClass);

		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();

		Class myClass = myClassLoader.loadClass(dataInputClass);

		Object whatInstance = myClass.newInstance();
		Input in = (Input) whatInstance;

		try {

			Collection<File> dataProviders = (Collection<File>) in
					.getData(props.getProperty(constants.mdstorePath));
			List<File> dp = (List<File>) dataProviders;

			Federation federation = new Federation(dp.size());

			for (int i = 0; i < dp.size(); i++) {

				String[] extensions = { "xml" };
				FileUtils utils = new FileUtils();
				Collection<File> xmls = utils.listFiles(dp.get(i), extensions,
						true);

				Repository repo = new Repository(xmls);
				repo.setRepoName(dp.get(i).getName());
				federation.addRepoName(dp.get(i).getName());
				repo.setRecordsNum(xmls.size());

				// System.out.println("Data Provider:" + repo.getRepoName());
				// System.out.println("Number Of Records:" +
				// repo.getRecordsNum());

				/*
				 * Iterator<File> iterator = xmls.iterator(); int j = 0; while
				 * (iterator.hasNext()) { File xml = iterator.next(); XMLHandler
				 * xmlHandler = new XMLHandler(repo); InputStream inS = new
				 * FileInputStream(xml); xmlHandler.parseDocument(inS); j++; }
				 */

				// System.out
				// .println("-------Computing Repository Level Element Frequency-------");
				System.out
						.println("Analysing repository:" + repo.getRepoName());
				System.out.println("Number of records:" + xmls.size());

				federation.appendFreqElements(repo.getElementFrequency());
				// System.out.println("-------Done-------");

				// System.out
				// .println("-------Computing Repository Level Element Completeness-------");
				federation.appendCompletnessElements(repo
						.getElementCompleteness());

				// System.out.println("-------Done-------");

				// System.out
				// .println("---------------Computing Element Maximum dimensionality--------------");

				federation.appendDimensionalityElements(repo
						.getElementDimensions());
				// System.out.println("---------------Done--------------");

				// System.out.println("Computing Elements' relative entropy...");

				federation.appendEntropyElements(repo.computeElementEntropy());
				// FileUtils.deleteDirectory(new File("buffer"));
				// System.out.println("Done...");

				repo.computeElementValueFreq(props
						.getProperty(constants.elementValues));
				FileUtils.deleteDirectory(new File("buffer"));

				// System.out
				// .println("-------Computing Repository Level Attribute Frequency-------");
				repo.getAttributeFrequency();
				// // repo.showAttributes();
				// System.out.println("-------Done-------");

				// System.out
				// .println("-------Computing Repository Level File Size Mean-------");

				federation.appendFileSize(repo.getFileSizeDistribution(xmls));
				System.out.println("Repository:" + repo.getRepoName()
						+ " analysis completed.");
				// System.out.println("-------Done-------"); // FileSizeMean

			}

			federation.getElementsSFrequency();
			federation.getElementsMCompletness();
			federation.getElementsMaxDimensionality();
			federation.getElementsMEntropy();
			federation.getAttributesSumFreq();
			federation.getElementValueSumFreq(props
					.getProperty(constants.elementValues));
			System.out.println("Average file size:"
					+ federation.getAverageFileSize() + " Bytes");

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}
}
