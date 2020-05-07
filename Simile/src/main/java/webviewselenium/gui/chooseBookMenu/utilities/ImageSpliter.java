package webviewselenium.gui.chooseBookMenu.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Class contains methods that allow to split Images.
 * 
 * @author Arkadiusz Sas (arkadiusz.sas@katalysteducation.org)
 */
public class ImageSpliter {
	
	private Integer outputImageHeight;

	public Integer getOutputImageHeight() {
		return outputImageHeight;
	}

	public void setOutputImageHeight(Integer outputImageHeight) {
		this.outputImageHeight = outputImageHeight;
	}
	

	public ImageSpliter(Integer outputImageHeight) {
			this.outputImageHeight = outputImageHeight;
	}
	
	/**
	 * Method allows to split all the images from the list into parts.
	 * @param pathsToImages list that contains all the images that should be splitted into parts.
	 */
	public void splitImages(List<File> pathsToImages) throws IOException {
		pathsToImages.forEach(image -> {
			try { splitImage(image.getPath()); }
			catch (IOException e) { e.printStackTrace(); }
		});
	}
	
	/**
	 * Method allows to split specific image into parts (saves new images as PNGs). 
	 * @param pathToImage path to the image that should be splitted into parts.
	 * @throws IOException 
	 */
	private void splitImage(String pathToImage) throws IOException {
		System.out.println("Splitted Image: " + pathToImage);
		BufferedImage sourceImage = ImageIO.read(new File(pathToImage));
		Integer sourceImageWidth = sourceImage.getWidth();
		Integer sourceImageHeight = sourceImage.getHeight();
		Integer outputImageIndex = 2;
		
		for(int y = 0; y <sourceImageHeight; y += outputImageHeight) {
			File pathToSplittedImage = new File(pathToImage.replaceAll("_1.png", "_" + outputImageIndex + ".png"));
			System.out.println("Path of Splitted Image: " + pathToSplittedImage.getPath());
			if(y + outputImageHeight <= sourceImageHeight) {
				ImageIO.write(sourceImage.getSubimage(0, y, sourceImageWidth, outputImageHeight),
						"png", pathToSplittedImage);
			} else {
				ImageIO.write(sourceImage.getSubimage(0, y, sourceImageWidth, sourceImageHeight - y), 
						"png", pathToSplittedImage);
			}
			outputImageIndex++;
		}
	}
}
