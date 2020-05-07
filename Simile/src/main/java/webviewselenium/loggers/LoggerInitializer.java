package webviewselenium.loggers;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.DateLayout;
import org.apache.log4j.spi.LoggingEvent;
import webviewselenium.constans.FormattedDates;
import webviewselenium.constans.SharedConstants;
import webviewselenium.gui.ApplicationLoader;

import java.io.File;
import java.io.IOException;

public class LoggerInitializer {
	private final Logger logger;
	private final String loggerFilename;
	private final String loggerFilePath;

	public LoggerInitializer() {
		logger  = Logger.getLogger(ApplicationLoader.class);
		loggerFilename = FormattedDates.getFormattedCurrentDatetime() + ".log";
		loggerFilePath = SharedConstants.fullNameOfDirectoryThatContainsLogs + File.separator + loggerFilename;
	}

	public Logger getInitializedLogger() {
		return initializeLogger();
	}

	public String getLoggerFilePath() {
		return loggerFilePath;
	}

	private Logger initializeLogger() {
		logger.addAppender(initializeAppender());
		return logger;
	}

	private Appender initializeAppender() {
		Appender appender = null;
		try {
			appender = new FileAppender(new DateLayout() {
				@Override
				public String format(LoggingEvent loggingEvent) {
					return getCustomLayout(loggingEvent);
				}

				@Override
				public boolean ignoresThrowable() {
					return false;
				}
			}, loggerFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appender;
	}

	private String getCustomLayout(LoggingEvent loggingEvent) {
		return "[" + FormattedDates.getFormattedCurrentDatetime() + "]"
				+ " - " + loggingEvent.getLevel()
				+ " - " + loggingEvent.getMessage() + "\n";
	}
}
