package webviewselenium.bashRunScripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import webviewselenium.constans.SharedConstants;

/**
 * Class contains method that allows to run DifferenceComparison Python script.
 * Path to the DifferenceComparison script: Scripts/DifferenceComparison.py
 * 
 * @author Arkadiusz Sas (arkadiusz.sas@katalysteducation.org)
 */
public class RunDifferenceComparison extends RunScript {
	private final String PYTHON_CMD = "python3";
	private final String LOGGER_RUNNING_INFO = "Running DifferenceComparison Script";
	
	/**
	 * Method allows to run DifferenceComparison Python script.
	 * 
	 * @param pathToResultImage path to the Result Image (image that was created during Comaprison Process in Simile)
	 * @param pathToReferenceImage path to the Reference Image (image that is the template of created ResultImage)
	 * @return calculated images difference value
	 */
	public double runScript(String pathToResultImage, String pathToReferenceImage) {
		try {
			LOGGER.info(logInfoAboutRunning(pathToResultImage, pathToReferenceImage));
			command = Runtime.getRuntime().exec(PYTHON_CMD + " " + SharedConstants.pathToDifferenceComparisonScript
					+ " " + pathToResultImage
					+ " " + pathToReferenceImage);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
			
			while ((line = br.readLine()) != null) 
				return Double.parseDouble(line);
			
			command.waitFor();
			exitValue = command.exitValue();
			printExitMessage(exitValue);
			command.destroy();
		} catch (IOException | InterruptedException e) {
			LOGGER.warning("Script failed to run.");
			e.printStackTrace();
		}
		
		return 0.0;
	}
	
	/**
	 * Method allows to log information about running Python script and its parameters.
	 * 
	 * @param pathToResultImage pathToResultImage path to the Result Image (image that was created during Comaprison Process in Simile)
	 * @param pathToReferenceImage path to the Reference Image (image that is the template of created ResultImage)
	 * @return formatted log information about running Python script and its parameters
	 */
	private String logInfoAboutRunning(String pathToResultImage, String pathToReferenceImage) {
		return LOGGER_RUNNING_INFO + ": " + pathToResultImage + ", " + pathToReferenceImage;
	}
}
