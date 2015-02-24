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
