package bookScan;

import org.junit.jupiter.api.Test;
import webviewselenium.bookScan.KillScanningProcess;
import webviewselenium.bookScan.RunScanningProcess;
import webviewselenium.fileUtilities.FileDeleter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RunScanningProcessTest {

    private String pathToTestDirectory = "ScanDB" + File.separator + "0";

    @Test
    public void syntaxOfCommandThatStartsCnxBookScannerIsCorrect() {
        RunScanningProcess runScanningProcess = new RunScanningProcess(
                "https://openstax.org/books/introductory-statistics/pages/1-introduction",
                "0");
        String runCnxBookScannerCommand = runScanningProcess.getRunCnxBookScannerCommand();

        boolean isSyntaxCorrect = runCnxBookScannerCommand.contains("yarn") &&
                                    runCnxBookScannerCommand.contains("--cwd") &&
                                    runCnxBookScannerCommand.contains("start") &&
                                    runCnxBookScannerCommand.contains("--url=") &&
                                    runCnxBookScannerCommand.contains("--suffix=") &&
                                    runCnxBookScannerCommand.contains("--path=");

        assertEquals(isSyntaxCorrect, true);
    }

    @Test
    public void cnxBookScannerRunsProperly() throws IOException {
        // If any error appears for this test please verify if URL leads to the last page of the book.
        // It is necessary, because it makes test faster. We don't have to wait for whole book scanning.
        // If the link seems OK, then verify if CNX Book Scanner is included in the Simile project and is build.
        RunScanningProcess runScanningProcess = new RunScanningProcess(
                "https://openstax.org/books/introductory-statistics/pages/index",
                "0");
        KillScanningProcess killScanningProcess = new KillScanningProcess();

        new File(pathToTestDirectory).mkdirs();
        int exitValue = runScanningProcess.runWholeBookScanningProcess();
        FileDeleter.deleteDirectoryStream(Paths.get(pathToTestDirectory));

        // Process which ends with 0 means everything went well.
        assertEquals(exitValue, 0);
        assertFalse(new File(pathToTestDirectory).exists());
    }
}
