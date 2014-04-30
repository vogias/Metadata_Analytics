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

		String initializerClass = props
				.getProperty(AnalyticsConstants.initializer);
		myClassLoader = ClassLoader.getSystemClassLoader();
		myClass = myClassLoader.loadClass(initializerClass);
		whatInstance = myClass.newInstance();

		InitializeProcess initProcess = (InitializeProcess) whatInstance;

		try {

			if (!initProcess.pathCheck(in, props)) {

				System.out.println("Wrong data providers values.");
				System.out
						.println("Please insert correct data providers values.");
				System.out.println("Exiting...");

				System.exit(-1);
			}

			List<?> dp = initProcess.getProvidersData(in, props);

			Federation federation = null;
			if (fedFlag) {
				System.out
						.println("Federated statistical analysis is activated...");
				federation = new Federation(dp.size());// temporalFlag
			} else {
				System.out
						.println("Federated statistical analysis is deactivated.");
				System.out.println("Analysis initialization...");
			}

			String elements2AnalyzeSTR = props
					.getProperty(AnalyticsConstants.elementValues);
			if (elements2AnalyzeSTR.equals("")) {
				System.err.println("No elements defined.");
				System.err.println("Exiting..");
				System.exit(-1);
			}

			String[] elements2Analyze = elements2AnalyzeSTR.split(",");

			System.out.println("=========Elements to analyze=========");
			for (int i = 0; i < elements2Analyze.length; i++) {

				System.out.println(elements2Analyze[i]);
			}

			System.out.println("======================================");

			String elementsVocs = props
					.getProperty(AnalyticsConstants.elementVocaulary);

			System.out.println("Analyzing...");

			
			initProcess.doAnalysis(federation, dp, fedFlag, elements2Analyze,
					elementsVocs);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}
}
