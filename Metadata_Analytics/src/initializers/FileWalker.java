/**
 * 
 */
package initializers;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

import analytics.analyzer.handlers.Repository;
import analytics.filtering.Filtering;

/**
 * @author vogias
 * 
 */
public class FileWalker extends SimpleFileVisitor<Path> {

	String fileType = "";
	Repository repo;
	boolean filterFile = false;
	String filterExpresison;
	Filtering filterObject;

	public FileWalker(String fileType, boolean filterFile,
			String filterExpression, Filtering filterObject, Repository repo) {
		// TODO Auto-generated constructor stub
		this.fileType = fileType;
		this.repo = repo;
		this.filterFile = filterFile;
		this.filterExpresison = filterExpression;
		this.filterObject = filterObject;
	}

	// @Override
	// public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes
	// attrs)
	// throws IOException {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		// TODO Auto-generated method stub
		File f = file.toFile();
		String fileName = f.getName();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());

		if (suffix.equals(this.fileType)) {

			if (filterFile) {
				boolean keep = true;
				if (!this.filterObject.filterXML(f, this.filterExpresison)) {
					System.out.println("File:" + f.getName()
							+ " is filtered out.");
					keep = false;
				} else {
					System.out.println("File:" + f.getName()
							+ " is kept in xmls' collection.");
					keep = true;
				}
				if (keep) {
					repo.setCurrentXmlFile(f);
					
				}
			} else {

			}

		} else {
			System.err.println("Not an " + fileType + " type of file.");

		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		// TODO Auto-generated method stub
		System.err.println(file + " could not be processed.");
		return FileVisitResult.CONTINUE;
	}

	// @Override
	// public FileVisitResult postVisitDirectory(Path dir, IOException exc)
	// throws IOException {
	// // TODO Auto-generated method stub
	// return null;
	// }

}
