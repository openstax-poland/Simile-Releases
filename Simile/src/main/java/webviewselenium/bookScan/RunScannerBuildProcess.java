package webviewselenium.bookScan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;

public class RunScannerBuildProcess {
	
	private final Logger LOGGER = Logger.getLogger(ScanReferenceController.class.getSimpleName());	
	
	private Process command;
	private String line;
	
	private Integer exitValue;

	/**
	 * Method builds CNX Book Scanner application. 
	 */
	public void runScannerBuildProcess() {
        try {
        	command = Runtime.getRuntime().exec("yarn --cwd ./cnx-books-scanner build");
            BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
            while ((line = br.readLine()) != null) 
            	LOGGER.info(line);
            command.waitFor();
            exitValue = command.exitValue();
            printExitMessage(exitValue);
            command.destroy();
        } catch (Exception e) {}
	}
	
	private void printExitMessage(int exitValue) {
		if(exitValue == 0) LOGGER.info("exit: " + exitValue);
		else LOGGER.warning("exit: " + exitValue);
	}
}

