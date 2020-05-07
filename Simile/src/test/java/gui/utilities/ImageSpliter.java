package gui.utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import webviewselenium.constans.SharedConstants;

class ImageSpliter {

	private File pathToFile = new File(SharedConstants.unitTestsImageSpliter + "Image_1.png");
	
	@Test
	public void splitImage() throws IOException {
		webviewselenium.gui.chooseBookMenu.utilities.ImageSpliter imageSpliter = new webviewselenium.gui.chooseBookMenu.utilities.ImageSpliter(1000);
				imageSpliter.splitImages(Arrays.asList(pathToFile));
		assertEquals(new File(SharedConstants.unitTestsImageSpliter).listFiles().length, 21);
	}

}
