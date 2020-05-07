package webviewselenium.bashRunScripts;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import webviewselenium.constans.SharedConstants;

public class OpenFile extends RunScript {

	public void openDifferenceReportFile() {
        try {
        	command = Runtime.getRuntime().exec("nautilus --browser " + SharedConstants.pathToDifferenceReportFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(command.getInputStream()));
            while ((line = br.readLine()) != null) 
            	LOGGER.info(line);
            command.waitFor();
            exitValue = command.exitValue();
            printExitMessage(exitValue);
            command.destroy();
        } catch (Exception e) {}
	}

}
