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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author vogias
 * 
 */
public class FileSizeMean extends Metric {

	float fileSizeM;

	@Override
	
	public HashMap<String, Double> compute(Collection<?> data) {
		// TODO Auto-generated method stub
		Iterator<File> iterator = (Iterator<File>) data.iterator();
		int i = 0;
		float c2 = 0;
		while (iterator.hasNext()) {
			File file = iterator.next();
			c2 = c2 + file.length();
			
			i++;
		}

		setFileSizeM(c2 / i);
		
		return null;
	}

	@Override
	public void compute(Collection<?> data, String elementName) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the fileSizeM
	 */
	public float getFileSizeM() {
		return fileSizeM;
	}

	/**
	 * @param fileSizeM
	 *            the fileSizeM to set
	 */
	private void setFileSizeM(float fileSizeM) {
		this.fileSizeM = fileSizeM;
	}

}
