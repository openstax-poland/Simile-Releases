package webviewselenium.structure;

import org.junit.jupiter.api.Test;
import webviewselenium.constans.SharedConstants;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class StructureInitializerTest {

    private final StructureInitializer structureInitializer = new StructureInitializer();
    private final File directoryThatContainsScans = new File(SharedConstants.fullNameOfDirectoryThatContainsScans);
    private final File directoryThatContainsResultantImages = new File(SharedConstants.fullNameOfDirectoryThatContainsResultantImages);

    @Test
    public void directoryThatContainsDatabaseFilesExists() {
        File directoryThatContainsDatabaseFiles = new File(SharedConstants.fullNameOfDirectoryThatContainsDatabaseFiles);
        assertTrue(structureInitializer.doesDirectoryExist(directoryThatContainsDatabaseFiles),
                "Directory that contains database files is missing!");
    }

    @Test
    public void directoryThatContainsCnxBookScannerExists() {
        File directoryThatContainsCnxBookScanner = new File(SharedConstants.fullNameOfDirectoryThatContainsCnxBookScanner);
        assertTrue(structureInitializer.doesDirectoryExist(directoryThatContainsCnxBookScanner),
                "Directory that contains cnx-book-scanner is missing!");
    }

    @Test
    public void directoryThatContainsCnxBookScannerBuildsExists() {
        File directoryThatContainsCnxBookScannerBuilds = new File(SharedConstants.fullNameOfDirectoryThatContainsCnxBookScannerBuilds);
        assertTrue(structureInitializer.doesDirectoryExist(directoryThatContainsCnxBookScannerBuilds),
                "Directory that contains cnx-book-scanner's builds is missing!");
    }

    @Test
    public void directoryThatIsNotIncludedIntoStructureNotExist() {
        File directoryThatIsNotIncludedIntoStructure = new File("directoryThatIsNotIncludedIntoStructure");
        assertFalse(structureInitializer.doesDirectoryExist(directoryThatIsNotIncludedIntoStructure));
    }

    @Test
    public void createDirectoriesIncludedIntoStructure() {
        deleteStructureDirectories();

        structureInitializer.createDirectory(directoryThatContainsScans);
        structureInitializer.createDirectory(directoryThatContainsResultantImages);

        assertTrue(structureInitializer.doesDirectoryExist(directoryThatContainsScans),
                "Directory that contains scans has not been initialized!");
        assertTrue(structureInitializer.doesDirectoryExist(directoryThatContainsResultantImages),
                "Directory that contains resultant images has not been initialized!");
    }

    @Test
    public void initializeStructure() {
        deleteStructureDirectories();

        structureInitializer.initializeDirectoryStructure();

        assertTrue(structureInitializer.doesDirectoryExist(directoryThatContainsScans),
                "Directory that contains scans has not been initialized!");
        assertTrue(structureInitializer.doesDirectoryExist(directoryThatContainsResultantImages),
                "Directory that contains resultant images has not been initialized!");
    }

    private void deleteStructureDirectories() {
        if(directoryThatContainsScans.exists())
            directoryThatContainsScans.delete();
        if(directoryThatContainsResultantImages.exists())
            directoryThatContainsResultantImages.delete();
    }
}
