package fileUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import webviewselenium.constans.SharedConstants;

public class ImageFinder {

	@Test
	public void findPNGs() {
		webviewselenium.fileUtilities.ImageFinder imageFinder
				= new webviewselenium.fileUtilities.ImageFinder.ImageFinderBuilder(SharedConstants.unitTestsPathImagesPNG).build();
		assertEquals(imageFinder.findPathsToImages().size(), 2);
	}
	
	@Test
	public void findJPGs() {
		webviewselenium.fileUtilities.ImageFinder imageFinder
				= new webviewselenium.fileUtilities.ImageFinder.ImageFinderBuilder(SharedConstants.unitTestsPathImagesPNG)
						.setImageExtension("JPG").build();
		assertEquals(imageFinder.findPathsToImages().size(), 1);		
	}
}
