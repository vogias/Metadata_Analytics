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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.SAXException;

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
	String[] elements2Analyze;
	String[] elmtVoc;
	String[] attributes2analyze;
	SAXParser parser;
	boolean computeEntropy;

	public FileWalker(String fileType, boolean filterFile,
			String filterExpression, Filtering filterObject, Repository repo,
			String[] elements2Analyze, String[] elementVocs,
			String[] attributes2analyze, boolean computeEntropy,
			SAXParser parser) {
		// TODO Auto-generated constructor stub
		this.fileType = fileType;
		this.repo = repo;
		this.filterFile = filterFile;
		this.filterExpresison = filterExpression;
		this.filterObject = filterObject;
		this.elements2Analyze = elements2Analyze;
		this.elmtVoc = elementVocs;
		this.parser = parser;
		this.attributes2analyze = attributes2analyze;
		this.computeEntropy = computeEntropy;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

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
					repo.raiseNumberOfFiles();
					repo.raiseFileSize(f.length());
					try {
						repo.parseXML(this.elements2Analyze, this.elmtVoc,
								this.attributes2analyze, computeEntropy, parser);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} else {
				repo.setCurrentXmlFile(f);
				try {
					repo.parseXML(this.elements2Analyze, this.elmtVoc,
							this.attributes2analyze, computeEntropy, parser);
					repo.raiseNumberOfFiles();
					repo.raiseFileSize(f.length());
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			System.err.println("\n" + f.getName() + " is not a " + fileType
					+ " type of file.\n");

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

}
