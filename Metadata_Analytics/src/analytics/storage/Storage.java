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

	public abstract void storeData(HashMap<String,Double>data,String metricName);
}
