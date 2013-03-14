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
			String metricName,String dataProvider,boolean append);
}
