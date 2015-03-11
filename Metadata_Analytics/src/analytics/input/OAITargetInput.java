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
package analytics.input;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.ariadne.util.OaiUtils;
import org.jdom.Element;

import uiuc.oai.OAIRecord;
import uiuc.oai.OAIRecordList;
import uiuc.oai.OAIRepository;

/**
 * @author vogias
 * 
 */
public class OAITargetInput extends Input {

	OAIRepository repos;

	

	@SuppressWarnings("deprecation")
	private Collection<?> getOAIData(String path, String repoSelection) {
		// TODO Auto-generated method stub

		repos = new OAIRepository();
		OAIRecordList records;

		Vector<String> data = new Vector<>();
		try {
			repos.setBaseURL(path);

			System.out.println("Harvesting repository:" + repos.getRepositoryName());
			records = repos.listRecords(repoSelection);

			while (records.moreItems()) {
				OAIRecord item = records.getCurrentItem();
				if (!item.deleted()) {
					Element metadata = item.getMetadata();

					if (metadata != null) {

						String metadata2string = OaiUtils
								.parseLom2Xmlstring(metadata);
						data.addElement(metadata2string);

					}
				}
				records.moveNext();
			}
			System.out.println("Harvesting repository:" + repos.getRepositoryName()+" finished.");

		} catch (uiuc.oai.OAIException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();

			return data;
		}

		return data;

	}

	@Override
	public Collection<?> getData(String path, String repoSelection) {
		// TODO Auto-generated method stub

		return getOAIData(path, repoSelection);

		

	}

	@Override
	public List<String> getRepoNames(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		OAITargetInput input = new OAITargetInput();
		
		Collection<String> data = (Collection<String>) input
				.getData(
						"http://idea-pathway-creator.greenideasproject.org/greenideas/oai",
						"oai_lom");
		Iterator<String> iterator = data.iterator();

		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		System.out.println(input.getRepoName());
	}

	@Override
	public String getRepoName() {
		// TODO Auto-generated method stub
		// OaiPmhServer server = getOaiPmhServer();
		try {
			return repos.getRepositoryName();
		} catch (uiuc.oai.OAIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "UnknownRepoName";
		}
		

	}

}
