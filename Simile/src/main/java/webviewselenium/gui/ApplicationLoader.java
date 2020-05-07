package webviewselenium.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import webviewselenium.loggers.LoggerInitializer;
import webviewselenium.structure.StructureInitializer;

/**
 * Class contains methods that allow to prepare and run Application. That's the point where application starts.
 * Default format for version name: dd-mm-yyyy-nn. That's also the point where shared Logger is initialized,
 * you can use it in the whole application.
 */
public class ApplicationLoader extends Application {
	private static final Logger logger = new LoggerInitializer().getInitializedLogger();
	private static final String currentVersion = "3004202000";

	private final StructureInitializer structureInitializer;
    private final StageManager stageManager;

	public ApplicationLoader() {
		structureInitializer = new StructureInitializer();
		stageManager = new StageManager();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
        structureInitializer.initializeDirectoryStructure();
		stageManager.runScanReferenceBookController();
		logCurrentVersion();
	}

	private void logCurrentVersion() {
		logger.info("Version: " + currentVersion);
    }

	public static Logger getLogger() {
		return logger;
	}
}
