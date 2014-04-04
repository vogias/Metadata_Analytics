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
package xmlHandling;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Repository;

/**
 * @author vogias
 * 
 */
public abstract class XmlHandlerInput {

	public abstract void getInputData(Repository repo,
			String[] elements2Analyze, String[] elementsVocs)
			throws SAXException, ParserConfigurationException;
}
