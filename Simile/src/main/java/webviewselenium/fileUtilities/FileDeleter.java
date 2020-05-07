package webviewselenium.fileUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/**
 * Class contains methods that allow to split Images/Files.
 * 
 * @author Arkadiusz Sas (arkadiusz.sas@katalysteducation.org)
 *
 */
public class FileDeleter {

	/**
	 *  Method allows to delete all the images from the list.
	 * @param pathsToImages list that contains all the images that should be deleted
	 */
	public static void deleteFiles(List<String> pathsToImages) {
			pathsToImages.forEach(image -> {
				deleteFile(image);
			});
	}
	
	/**
	 * Method allows to delete the file.
	 * @param pathToImage path to the image that should be deleted
	 */
	private static void deleteFile(String pathToImage) {
			new File(pathToImage).delete();
	}

	public static void deleteDirectoryStream(Path path) throws IOException {
		Files.walk(path)
				.sorted(Comparator.reverseOrder())
				.map(Path::toFile)
				.forEach(File::delete);
	}
}
