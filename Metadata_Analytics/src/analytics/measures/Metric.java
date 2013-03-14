/**
 * 
 */
package analytics.measures;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author vogias
 * 
 */
public abstract class Metric {

	public abstract HashMap<String,Double> compute(Collection<?> data);
	public abstract void compute(Collection<?> data,String elementName);
}
