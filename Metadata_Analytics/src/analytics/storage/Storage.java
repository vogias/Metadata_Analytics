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
package analytics.storage;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;

/**
 * @author vogias
 * 
 */
public abstract class Storage {

	public abstract void storeElementData(HashMap<String, Double> data,
			String metricName, String dataProvider, String analysisType,
			String headerColumn);

	public abstract void storeElementValueData(HashMap<String, Integer> data,
			String metricName, String dataProvider, String analysisType,
			String headerColumn, String element, Logger logger);

	public abstract void storeRepositoryData(String repoName, int noRecords,
			float avgFSize, float storageReq, String schema);

	public abstract void appendRepositoryData(String repoName, int noRecords,
			float avgFSize, float storageReq, String schema) throws IOException;

	public abstract String getElementDataFilePath();

	public abstract String getSpecificElementDataFilePath();

	public abstract String getAttributeDataFilePath();

	public abstract String getGeneralIngoFilePath();
}
