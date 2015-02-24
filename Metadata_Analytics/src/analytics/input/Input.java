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
import java.util.List;

/**
 * @author vogias
 * 
 */
public abstract class Input {

	public abstract Collection<?> getData(String path,String repoSelection);

	public abstract List<String> getRepoNames(String path);
	
	public abstract String getRepoName();//for oai-pmh implementation

}
