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
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

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
			String[] elementsVocs) throws ParserConfigurationException {
		// TODO Auto-generated method stub
		Iterator<File> iterator = (Iterator<File>) repo.getXmls().iterator();
		// int j = 0;
		File xml = null;
		// try {
		int size = repo.getXmls().size();
		int count = 1;

		long startExp = System.currentTimeMillis();
		while (iterator.hasNext()) {
			xml = iterator.next();

			XMLHandler xmlHandler = new XMLHandler(repo, elements2Analyze,
					elementsVocs);

			InputStream inS = null;
			try {
				inS = new FileInputStream(xml);

				System.out.print("Parsing file:" + count + " of " + size);
				count++;

				long start = System.currentTimeMillis();

				xmlHandler.parseDocument(inS);
				long end = System.currentTimeMillis();
				long diff = end - start;
				System.out.print(",Parsing time(ms):" + diff + "\n");

			} catch (SAXException e) {
				// TODO Auto-generated catch block

				System.err.println("Bad formed xml file:" + xml.getPath());

				continue;
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
		long endExp = System.currentTimeMillis();
		long res = (endExp - startExp)/1000;
		System.out.println("Repository parsing duration(s):" + res + "\n");
		// } catch (IOException ex) {
		// ex.printStackTrace();
		//
		// }
	}
}
