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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import analytics.analyzer.handlers.Repository;
import analytics.analyzer.handlers.XMLHandler;

/**
 * @author vogias
 * 
 */
public class OAI2XMLInput extends XmlHandlerInput {

	@Override
	public void getInputData(Repository repo, String[] elements2Analyze,
			String[] elementsVocs) throws ParserConfigurationException {
		Iterator<String> iterator = (Iterator<String>) repo.getXmls()
				.iterator();
		// int j = 0;

		int size = repo.getXmls().size();
		int count = 1;
		try {
			long startExp = System.currentTimeMillis();
			while (iterator.hasNext()) {
				String xml = iterator.next();
				XMLHandler xmlHandler = new XMLHandler(repo, elements2Analyze,
						elementsVocs);

				InputStream inS = new ByteArrayInputStream(xml.getBytes());

				try {
					System.out.print("Parsing file:" + count + " of " + size);
					count++;
					long start = System.currentTimeMillis();
					xmlHandler.parseDocument(inS);
					long end = System.currentTimeMillis();
					long diff = end - start;
					System.out.print(",Parsing time(ms):" + diff + "\n");
					// j++;
					inS.close();

				} catch (SAXException e) {
					// TODO Auto-generated catch block

					System.err.println("Bad formed xml file:" + xml);
					continue;
				}
			}
			long endExp = System.currentTimeMillis();
			long res = (endExp - startExp) / 1000;
			System.out.println("Repository parsing duration(s):" + res + "\n");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
