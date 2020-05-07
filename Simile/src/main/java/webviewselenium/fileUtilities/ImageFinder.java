package webviewselenium.fileUtilities;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import webviewselenium.structureUtilities.ArrayToListConverter;

/**
 * Class contains methods that allow to find Images (JPG/PNG files).
 * 
 * Required parameters: directoryPath
 * Optional parameters: imageExtension
 *
 */
public class ImageFinder {

	private final String directoryPath;
	private final String imageExtension;
	
	public String getDirectoryPath() {
			return directoryPath;
	}

	public String getImageExtension() {
			return imageExtension;
	}
	
	private ImageFinder(ImageFinderBuilder imageFinderBuilder) {
			this.directoryPath = imageFinderBuilder.directoryPath;
			this.imageExtension = imageFinderBuilder.imageExtension;
	}
	
	/**
	 * Method allows to get all paths to the images in the requested directory.
	 * @return list of paths to the images in requested directory
	 */
	public List<String> findPathsToImages() {
			return ArrayToListConverter.covertArrayToList(new File(directoryPath).listFiles())
					.stream()
						.map(imageFile -> imageFile.getName())
						.filter(imagePath -> imagePath.contains(imageExtension.toLowerCase()))
						.collect(Collectors.toList());		
	}
	
	
	/**
	 * Class that builds ImageFinder instance. Used Builder design pattern.
	 * Default value for the image extension is PNG.
	 */
	public static class ImageFinderBuilder {
		private String directoryPath;
		private String imageExtension = "PNG";
		
		public ImageFinderBuilder(String directoryPath) {
				this.directoryPath = directoryPath;
		}
		
		public ImageFinderBuilder setImageExtension(String imageExtension) {
				this.imageExtension = imageExtension;
				return this;
		}
		
		public ImageFinder build() {
				return new ImageFinder(this);
		}
	}
}
