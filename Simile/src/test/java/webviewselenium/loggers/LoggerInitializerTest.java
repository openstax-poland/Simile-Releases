package webviewselenium.loggers;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import webviewselenium.constans.SharedConstants;
import webviewselenium.gui.ApplicationLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoggerInitializerTest {
	private final LoggerInitializer loggerInitializer = new LoggerInitializer();

	@Test
	void initializeLogger_correctValue() {
		Logger initializedLogger = loggerInitializer.getInitializedLogger();
		assertEquals(ApplicationLoader.class.getName(), initializedLogger.getName());
		assertTrue(loggerInitializer.getLoggerFilePath().contains(SharedConstants.fullNameOfDirectoryThatContainsLogs));
	}
}
