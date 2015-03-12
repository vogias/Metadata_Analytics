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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Repository;
import analytics.analyzer.handlers.XMLHandler;

/**
 * @author vogias
 * 
 */
public class FS2XMLInput extends XmlHandlerInput {

	@Override
	public void getInputData(Repository repo, String[] elements2Analyze,
			String[] elementsVocs, SAXParser parser)
			throws ParserConfigurationException {
		// TODO Auto-generated method stub

		File xml = repo.getCurrentXmlFile();

		XMLHandler xmlHandler = new XMLHandler(repo, elements2Analyze,
				elementsVocs);

		InputStream inS = null;
		try {
			inS = new FileInputStream(xml);

			System.out.print("Parsing file:" + getCount() + " Filename:"
					+ xml.getName());

			long start = System.currentTimeMillis();

			xmlHandler.parseDocument(inS, parser);
			long end = System.currentTimeMillis();
			long diff = end - start;
			System.out.print(",Parsing time(ms):" + diff + "\n");

			raiseCount();

		} catch (SAXException e) {
			// TODO Auto-generated catch block

			System.err.println("Bad formed xml file:" + xml.getPath());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (inS != null) {
				try {
					inS.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
