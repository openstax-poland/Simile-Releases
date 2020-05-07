package webviewselenium.xml;

import webviewselenium.constans.SharedConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvailableScannedBooksFinder {
    
    private List<String> allXmlFilesWithRequiredEnding = new ArrayList<>();

    public List<String> getAllXmlFilesEndsWith(File directoryName) {
        File[] allFiles = directoryName.listFiles();
        try {
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    getAllXmlFilesEndsWith(file);
                } else if (file.isFile() && file.getCanonicalPath().endsWith(SharedConstants.nameOfBookInfoXmlFile + ".xml")) {
                    allXmlFilesWithRequiredEnding.add(file.getCanonicalPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allXmlFilesWithRequiredEnding;
    }
}
