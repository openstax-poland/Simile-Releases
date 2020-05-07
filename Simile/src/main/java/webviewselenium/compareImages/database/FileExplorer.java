package webviewselenium.compareImages.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileExplorer {

    static List<String> allXmlFiles = new ArrayList<>();

    /**
     * This method allows to get list of all PNGs located in request directory
     * and its subdirectories.
     *
     * @param pathToDirectory relative path to the directory to be searched
     * @return list of all PNGs located in request directory and its
     * subdirectories
     */
    public static List<String> findAllPNGs(File pathToDirectory) {
        File[] files = pathToDirectory.listFiles();
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    findAllPNGs(file);
                } else if (file.isFile() && file.getCanonicalPath().endsWith("png")) {
                    allXmlFiles.add(file.getCanonicalPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allXmlFiles;
    }

    /**
     * This method allows to get list of all PNGs (that matches expected directory name)
     * located in request directory and its subdirectories.
     *
     * @param pathToDirectory relative path to the directory to be searched
     * @param expectedDirectoryName name of the directory that is expected
     * @return list of all PNGs (that matches expected directory name) located in request directory and its subdirectories
     */
    public static List<String> findAllPNGs(File pathToDirectory, String expectedDirectoryName) {
        return findAllPNGs(pathToDirectory).stream()
                .distinct()
                .filter(path -> path.contains(expectedDirectoryName))
                .collect(Collectors.toList());
    }
}
