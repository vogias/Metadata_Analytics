/**
 * 
 */
package analytics.input;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.print.attribute.standard.PDLOverrideSupported;

import org.apache.commons.io.FileUtils;

/**
 * @author vogias
 * 
 */
public class FSInput extends Input {

	@Override
	public Collection<File> getData(String path) {
		// TODO Auto-generated method stub
		File mdstore = new File(path);
		List<File> files = new ArrayList<>();

		if (!mdstore.isDirectory()) {
			System.err.println("This is not a directory");
			System.err.println("Exiting...");
			return null;
		} else {

			File[] providersDirs = mdstore.listFiles();

			for (int i = 0; i < providersDirs.length; i++) {
				File prDir = providersDirs[i];
				if (prDir.isDirectory())
					files.add(prDir);
			}

			return files;
		}

	}

	public Collection<File> getXMLs(File dataProviderDir) {

		String[] extensions = { "xml" };
		FileUtils utils = new FileUtils();

		Collection<File> files = utils.listFiles(dataProviderDir, extensions,
				true);
		return files;

	}
}
