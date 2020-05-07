package webviewselenium.xml;

import org.junit.jupiter.api.Test;
import webviewselenium.constans.SharedConstants;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AvailableScannedBooksFinderTest {
    private AvailableScannedBooksFinder availableScannedBooksFinder = new AvailableScannedBooksFinder();
    private File directoryThatContainsScannedBooks = new File(SharedConstants.nameOfDirectoryThatContainsScans);

    @Test
    public void findBookDomainNameFromCnxFormat() {
        // It is necessary to subtract 1 from the number of expectedNumberOfBooks, because it counts also 'Results' directory.
        Integer expectedNumberOfBooks = directoryThatContainsScannedBooks.listFiles().length - 1;
        Integer calculatedNumberOfBooks = availableScannedBooksFinder.getAllXmlFilesEndsWith(directoryThatContainsScannedBooks).size();
        assertEquals(calculatedNumberOfBooks, expectedNumberOfBooks);
    }

}
