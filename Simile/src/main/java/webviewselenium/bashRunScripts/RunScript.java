package webviewselenium.bashRunScripts;

import java.util.logging.Logger;

public class RunScript {
	
	protected final Logger LOGGER = Logger.getLogger(RunScript.class.getSimpleName());	
	
	protected Process command = null;
	protected String line = "";	
	protected Integer exitValue = 0;

	protected void printExitMessage(int exitValue) {
		if(exitValue == 0) LOGGER.info("exit: " + exitValue);
		else LOGGER.warning("exit: " + exitValue);
	}	
}
