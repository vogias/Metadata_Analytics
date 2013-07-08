/**
 * 
 */
package analytics.input;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * @author vogias
 * 
 */
public class FSInput extends Input {

	@Override
	public Collection<File> getData(String path, String repoSelection) {
		// TODO Auto-generated method stub
		File mdstore = new File(path);
		List<File> files = new ArrayList<>();

		if (!mdstore.isDirectory()) {
			System.err.println("This is not a directory");
			System.err.println("Exiting...");
			return null;
		} else {

			if (repoSelection.equals("*")) {
				File[] providersDirs = mdstore.listFiles();

				for (int i = 0; i < providersDirs.length; i++) {
					File prDir = providersDirs[i];
					if (prDir.isDirectory())
						files.add(prDir);
				}
			} else {
				String[] repos = repoSelection.split(",");
				for (int i = 0; i < repos.length; i++) {
					File repo = new File(mdstore, repos[i]);

					if (repo != null && repo.isDirectory()) {
						files.add(repo);
					} else
						continue;
				}
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

	@Override
	public List<String> getRepoNames(String path) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		File mdstore = new File(path);
		List<String> files = new ArrayList<>();

		if (!mdstore.isDirectory()) {
			System.err.println("This is not a directory");
			System.err.println("Exiting...");
			return null;
		} else {

			File[] providersDirs = mdstore.listFiles();

			for (int i = 0; i < providersDirs.length; i++) {
				File prDir = providersDirs[i];
				if (prDir.isDirectory())
					files.add(prDir.getName());
			}

			return files;
		}

	}

	@Override
	public String getRepoName() {
		// TODO Auto-generated method stub
		return null;
	}
}
