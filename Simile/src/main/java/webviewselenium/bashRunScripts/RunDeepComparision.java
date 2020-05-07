package webviewselenium.bashRunScripts;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import webviewselenium.compareImages.IndicateImagesDifferences;
import webviewselenium.constans.SharedConstants;
import webviewselenium.gui.ApplicationLoader;

public class RunDeepComparision extends RunScript {
	private final static Logger logger = ApplicationLoader.getLogger();

	public void runDeepComparision(String pathToScannedImage, String pathToTemplateImage, String pathForResultantImage) {
		try {
			command = Runtime.getRuntime().exec("python3 " + SharedConstants.pathToDeepComparisionScript
													+ " " + pathToScannedImage
													+ " " + pathToTemplateImage
													+ " " + pathForResultantImage);
			BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
			while ((line = br.readLine()) != null)
				LOGGER.info(line);
			command.waitFor();
			exitValue = command.exitValue();
			printExitMessage(exitValue);
			command.destroy();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(RunDeepComparision.class.getName() + ": " + ex.getMessage());
		}
	}
}
