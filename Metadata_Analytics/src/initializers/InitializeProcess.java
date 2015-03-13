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

import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Federation;
import analytics.input.Input;

/**
 * @author vogias
 * 
 */
public abstract class InitializeProcess {

	public abstract boolean pathCheck(Input in, Properties props);

	public abstract List<?> getProvidersData(Input in, Properties props);

	public abstract void doAnalysis(Federation federation,
			List<?> dataProviders, boolean fedFlag, String[] elements2Analyze,
			String elmtVoc, String[] attributes2analyze)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SAXException, ParserConfigurationException;

	public abstract void logElementAnalysis(Logger logger, String providerName,
			String resultsPath);
}
