/**
 * 
 */
package analytics.measures;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author vogias
 * 
 */
public class FileSizeMean extends Metric {

	@Override
	public void compute(Collection<?> data) {
		// TODO Auto-generated method stub
		Iterator<File> iterator = (Iterator<File>) data.iterator();
		//float c = 0;
		int i = 0;
		float c2 = 0;
		while (iterator.hasNext()) {
			File file = iterator.next();
			//c = (float) (Math.log(file.length()) / Math.log(2));
			c2 = c2 + file.length();
			//System.out.println("File:" + file.getName());
			//System.out.println("File size(bytes):" + file.length());
			//System.out.println("Files size log2(bytes):" + c);
			i++;
		}
		System.out.println("File size mean:" + c2 / i+" bytes");
	}

	@Override
	public void compute(Collection<?> data, String elementName) {
		// TODO Auto-generated method stub
		
	}

}
