/**
 * 
 */
package analytics.logging;



import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;

/**
 * @author vogias
 * 
 */
public class ConfigureLogger {

	public Logger getLogger(String string, String file) {

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		PatternLayoutEncoder ple = new PatternLayoutEncoder();
		ple.setPattern("%d{YYYY-MM-dd.HH:mm:ss.SSS} %msg%n");
		ple.setContext(lc);
		ple.start();

		FileAppender fileAppender = new FileAppender();
		fileAppender.setFile(file);
		fileAppender.setEncoder(ple);
		fileAppender.setContext(lc);
		fileAppender.setAppend(true);
		fileAppender.start();

		Logger logger = (Logger) LoggerFactory.getLogger(string);
		logger.addAppender(fileAppender);
		logger.setLevel(Level.INFO);
		logger.setAdditive(false);

		return logger;
	}
}
