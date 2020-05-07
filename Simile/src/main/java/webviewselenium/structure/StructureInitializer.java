package webviewselenium.structure;

import org.apache.log4j.Logger;
import webviewselenium.constans.SharedConstants;
import webviewselenium.gui.ApplicationLoader;

import java.io.File;

/**
 * Class contains methods that allow to check the correctness of the Simile's structure.
 * When the necessary structure directory is missing - it is initialized.
 */
public class StructureInitializer {
    private static final Logger logger = ApplicationLoader.getLogger();
    private final File directoryThatContainsScans;
    private final File directoryThatContainsResultantImages;

    public StructureInitializer() {
        directoryThatContainsScans = new File(SharedConstants.fullNameOfDirectoryThatContainsScans);
        directoryThatContainsResultantImages = new File(SharedConstants.fullNameOfDirectoryThatContainsResultantImages);
    }

    public void initializeDirectoryStructure() {
        if(!doesDirectoryExist(directoryThatContainsScans)) {
            logger.info("Initializing directory that stores scanned books...(" + directoryThatContainsScans + ")");
            createDirectoryThatStoresScannedBooks();
        }

        if(!doesDirectoryExist(directoryThatContainsResultantImages)) {
            logger.info("Initializing directory that stores resultant images...(" + directoryThatContainsResultantImages + ")");
            createDirectoryThatStoresResultantImages();
        }
    }

    public boolean doesDirectoryExist(File directoryPath) {
        return directoryPath.exists();
    }

    public void createDirectory(File directoryPath) {
        directoryPath.mkdirs();
    }

    public void createDirectoryThatStoresScannedBooks() {
        createDirectory(directoryThatContainsScans);
    }

    public void createDirectoryThatStoresResultantImages() {
        createDirectory(directoryThatContainsResultantImages);
    }
}
