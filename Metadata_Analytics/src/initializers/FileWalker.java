/**
 * 
 */
package initializers;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author vogias
 * 
 */
public class FileWalker extends SimpleFileVisitor<Path> {

	String fileType = "";

	public FileWalker(String fileType) {
		// TODO Auto-generated constructor stub
		this.fileType = fileType;
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
		return null;
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
