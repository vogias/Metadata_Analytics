/*******************************************************************************
 * Copyright (c) 2015 Kostas Vogias.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Kostas Vogias - initial API and implementation
 ******************************************************************************/
package initializers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

public class AgrisFileWalker extends SimpleFileVisitor<Path> {
	String fileType = "";
	Repository repo;
	boolean filterFile = false;
	String filterExpresison;
	Filtering filterObject;
	String[] elements2Analyze;
	String[] elmtVoc;
	SAXParser parser;

	public AgrisFileWalker(String fileType, boolean filterFile,
			String filterExpression, Filtering filterObject, Repository repo,
			String[] elements2Analyze, String[] elementVocs, SAXParser parser) {
		// TODO Auto-generated constructor stub
		this.fileType = fileType;
		this.repo = repo;
		this.filterFile = filterFile;
		this.filterExpresison = filterExpression;
		this.filterObject = filterObject;
		this.elements2Analyze = elements2Analyze;
		this.elmtVoc = elementVocs;
		this.parser = parser;

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
		File bigAgrisFile = file.toFile();
		String fileName = bigAgrisFile.getName();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());

		if (suffix.equals(this.fileType)) {

			BufferedReader reader = null;
			try {

				String sCurrentLine;

				reader = new BufferedReader(new FileReader(bigAgrisFile));
				boolean resourceFound = false;

				File f = null;
				BufferedWriter writer = null;
				while ((sCurrentLine = reader.readLine()) != null) {

					if (sCurrentLine.contains("<ags:resource ")) {

						resourceFound = true;

						// f = new File(sCurrentLine.substring(
						// sCurrentLine.lastIndexOf("=") + 2,
						// sCurrentLine.lastIndexOf(">") - 1)
						// + ".xml");

						f = new File("resource" + ".xml");

						writer = new BufferedWriter(new FileWriter(f));
						writer.append(sCurrentLine);

					} else if (sCurrentLine.contains("</ags:resource>")) {

						if (resourceFound) {

							writer.append(sCurrentLine);
							writer.close();

							if (filterFile) {
								boolean keep = true;
								if (!this.filterObject.filterXML(f,
										this.filterExpresison)) {
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
										repo.parseXML(this.elements2Analyze,
												this.elmtVoc, parser);
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
									repo.parseXML(this.elements2Analyze,
											this.elmtVoc, parser);
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

							resourceFound = false;
							f.delete();
						}

					} else {

						if (resourceFound)
							writer.append(sCurrentLine);
						else
							continue;
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null)
						reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		} else {
			System.err.println("\n" + bigAgrisFile.getName() + " is not a "
					+ fileType + " type of file.\n");

		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		// TODO Auto-generated method stub
		System.err.println(file.getFileName() + " could not be processed.\n");
		return FileVisitResult.CONTINUE;
	}

}
