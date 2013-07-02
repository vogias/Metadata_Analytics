/**
 * 
 */
package analytics.input;

import java.util.Collection;
import java.util.List;

/**
 * @author vogias
 * 
 */
public abstract class Input {

	public abstract Collection<?> getData(String path,String repoSelection);

	public abstract List<String> getRepoNames(String path);

}
