/**
 * 
 */
package analytics.storage;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author vogias
 * 
 */
public abstract class Storage {


	public abstract void storeElementData(HashMap<String, Double> data,
			String metricName,String dataProvider,String analysisType,String headerColumn,boolean temporal);
	
	public abstract void storeElementValueData(HashMap<String, Integer> data,
			String metricName,String dataProvider,String analysisType,String headerColumn,boolean temporal);
	
	public abstract void storeRepositoryData(String repoName,int noRecords,float avgFSize,float storageReq,String schema);
	public abstract void appendRepositoryData(String repoName,int noRecords,float avgFSize,float storageReq,String schema) throws IOException;
	
	public abstract String getElementDataFilePath();
	public abstract String getSpecificElementDataFilePath();
	public abstract String getAttributeDataFilePath();
	public abstract String getGeneralIngoFilePath();
}
