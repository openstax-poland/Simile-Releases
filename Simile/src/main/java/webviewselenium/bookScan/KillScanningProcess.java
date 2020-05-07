package webviewselenium.bookScan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;
import webviewselenium.constans.SharedConstants;

public class KillScanningProcess {
	
	private final Logger LOGGER = Logger.getLogger(ScanReferenceController.class.getSimpleName());	
	private final String SCRIPT_NAME = SharedConstants.nameOfKillScanningProcessesScript;
	
	private Process command;
	private String line;
	
	private Integer exitValue;

	/**
	 * Method runs script that kills all processes bound with the Scanning Process. 
	 */
	public void killScanningProcess() {
        try {
        	command = Runtime.getRuntime().exec("./" + SCRIPT_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
            while ((line = br.readLine()) != null) 
            	LOGGER.info("line: " + line);
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
