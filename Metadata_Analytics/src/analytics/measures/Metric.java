/**
 * 
 */
package analytics.measures;

import java.util.Collection;

/**
 * @author vogias
 * 
 */
public abstract class Metric {

	public abstract void compute(Collection<?> data);
	public abstract void compute(Collection<?> data,String elementName);
}
