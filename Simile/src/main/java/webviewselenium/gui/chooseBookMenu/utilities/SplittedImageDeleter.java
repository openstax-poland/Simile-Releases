package webviewselenium.gui.chooseBookMenu.utilities;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import webviewselenium.bashRunScripts.RunDifferenceComparison;
import webviewselenium.gui.ApplicationLoader;

/**
 * Class contains methods that allow to delete images that were created during Splitting Process. 
 * Splitting Process is to divide a large image into smaller ones.
 * 
 * @author Arkadiusz Sas (arkadiusz.sas@katalysteducation.org)
 */
public class SplittedImageDeleter {
	private final static Logger logger = ApplicationLoader.getLogger();
	private final Integer MAX_IMAGES_DIFFERENCE_VALUE = 1000;
	
	/**
	 * Methos allows to delete all images that were created during Splitting Process. 
	 * Splitting Process is to divide a large image into smaller ones.
	 * 
	 * If provided directory doesn't contain any splitted images, method will do nothing.
	 * 
	 * @param allSplittedImages list of all Splitted Images that should be deleted
	 */
	public void removeAllSplittedImages(List<String> allSplittedImages) {
		allSplittedImages.forEach(splittedImage -> {
			new File(splittedImage).delete();
		});
	}
	
	/**
	 * Method allows to remove Splitted Images that will not be used in the Comparision Process.
	 * 
	 * To decide which pairs of Splitted Images should be deleted method uses Python script - DifferenceComparison.
	 * DifferenceComparison script calculates similarity of provided images. If calculated value is bigger 
	 * than MAX_DIFFERENCE_VALUE then pair of Splitted Images should be deleted.
	 * 
	 * @param resultSplittedImages list of all Splitted Images that are located in the 'Results' directory
	 * @param referenceBookDirectoryIndex index of the directory that contains images that will be used in the Comparison Process
	 */
	public void removeUnusedSplittedImagesinComparision(List<String> resultSplittedImages, String referenceBookDirectoryIndex) {
		RunDifferenceComparison differenceComparisonScriptRunner = new RunDifferenceComparison();

		logger.info("---------------------------------------------");
		logger.info("Difference Comparison is starting...");
		
		resultSplittedImages.forEach(resultSplittedImage -> {
				String pathResultSplittedImage = resultSplittedImage;
				String pathReferenceSplittedImage = resultSplittedImage.replaceAll("Results", referenceBookDirectoryIndex);
				
				// Calculate images difference value
				double imagesDifferenceValue = differenceComparisonScriptRunner.runScript(
							pathResultSplittedImage, pathReferenceSplittedImage);

				logger.info("---------------------------------------------");
				logger.info("RES: " + pathResultSplittedImage);
				logger.info("REF: " + pathReferenceSplittedImage);
				logger.info("DIFFERENCE: " + imagesDifferenceValue);
								
				// Decide if pair of Splitted Images should be deleted
				if(imagesDifferenceValue < MAX_IMAGES_DIFFERENCE_VALUE) {
					new File(pathResultSplittedImage).delete();
					new File(pathReferenceSplittedImage).delete();
				}
			});

		logger.info("---------------------------------------------");
		logger.info("Difference Comparison has finished...");
	}
	
	/**
	 * Method allows to find list of all Splitted Images that are located in the provided directory.
	 * 
	 * @param pathToImageDirectory path to the directory that contains splitted images
	 * @return list of all Splitted Images that are located in the provided directory
	 */
	public List<String> findAllSplittedImages(String pathToImageDirectory) {
		// All images that doesn't contain "_1.png" at the end of the file name are splitted images.
		Pattern pattern = Pattern.compile("([2-9].png|[\\d]{2,}.png)");
		
		return Arrays.asList(new File(pathToImageDirectory).listFiles())
					.stream()
					.map(file -> file.getPath())
					.filter(pattern.asPredicate())
					.collect(Collectors.toList());
	}
}
