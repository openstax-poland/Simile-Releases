package webviewselenium.bookScan;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;
import webviewselenium.gui.scanBookInQA.ScanBookQAController;
import webviewselenium.constans.SharedConstants;

/**
 * Class contains methods that allow to start Scanning Process with CNX Book Scanner.
 *
 * @author Arkadiusz Sas (arkadiusz.sas@katalysteducation.org)
 */
public class RunScanningProcess {
	
	private final Logger LOGGER = Logger.getLogger(RunScanningProcess.class.getSimpleName());

	// Once we try to start CNX Book Scanner we need to specify path to the directory that stores that application.
	private final String cwd = " --cwd ./" + SharedConstants.pathToDirectoryThatContainsCnxBookScanner;

	// Simile uses suffix to recognize if this is an original image or an image that has
	// been created during splitting process. Suffix "_1" means that it is an original image.
	private final String suffix = " --suffix=_1";

	// Flag that causes that cnx-book-scanner scans only Table of Content of the provided book.
	private final String scanOnlyToC = " --only-toc";

	// Relative path to the directory that stores specific scanned book's images, e.g.: ../ScanDB/2.
	// Index at the end of the path should be append after its value will be passed in the constructor, eg. PATH + pathIndex.
	private final String path;

	// CNX Book Scanner scans whole book, but to start the whole process it needs URL to the first page of the book.
	private final String bookUrl;

	private final String runCnxBookScannerCommand;

	private Process executedCommandProcess;
	private String outputLineFromTerminal;
	private Integer exitValue;

	/**
	 * @param bookURL url to the first page of the book that should be scanned
	 * @param pathIndex index of the directory that will be append to the whole path to the scanned book, e.g. ScanDB/pathIndex
	 */
	public RunScanningProcess(String bookURL, String pathIndex) {
		this.bookUrl = " --url=" + bookURL;
		this.path = " --path=.." + File.separator + SharedConstants.nameOfDirectoryThatContainsScans + File.separator + pathIndex;
		runCnxBookScannerCommand = "yarn" + cwd + " start" + bookUrl + suffix + path;
	}

	public Integer runTableOfContentsScanningProcess() {
		return runCommand(runCnxBookScannerCommand + scanOnlyToC);
	}

	public Integer runWholeBookScanningProcess() {
		return runCommand(runCnxBookScannerCommand);
	}

	private Integer runCommand(String command) {
		try {
			executedCommandProcess = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(executedCommandProcess.getInputStream()));
			while ((outputLineFromTerminal = bufferedReader.readLine()) != null) {
				LOGGER.info("line: " + outputLineFromTerminal);
				if(outputLineFromTerminal.contains("https")) {
					ScanReferenceController.setCurrentPageURL(outputLineFromTerminal.substring(outputLineFromTerminal.lastIndexOf("https")));
					ScanBookQAController.setCurrentPageURL(outputLineFromTerminal.substring(outputLineFromTerminal.lastIndexOf("https")));
				}
			}
			executedCommandProcess.waitFor();
			exitValue = executedCommandProcess.exitValue();
			printExitMessage(exitValue);
			executedCommandProcess.destroy();
		} catch (Exception e) {}
		return exitValue;
	}
	
	private void printExitMessage(int exitValue) {
		if(exitValue == 0) LOGGER.info("exit: " + exitValue);
		else LOGGER.warning("exit: " + exitValue);
	}

	public String getRunCnxBookScannerCommand() {
		return runCnxBookScannerCommand;
	}
}
