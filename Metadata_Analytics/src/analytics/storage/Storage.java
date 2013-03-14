/**
 * 
 */
package analytics.storage;

import java.util.HashMap;

/**
 * @author vogias
 * 
 */
public abstract class Storage {


	public abstract void storeElementData(HashMap<String, Double> data,
			String metricName,String dataProvider,String analysisType,String headerColumn);
	
	public abstract void storeElementValueData(HashMap<String, Integer> data,
			String metricName,String dataProvider,String analysisType,String headerColumn);
	
	
}
