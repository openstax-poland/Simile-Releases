package fileUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import webviewselenium.constans.SharedConstants;

class FileDeleter {

	@Test
	public void removeImage() throws IOException {
			String pathToOriginalImage = SharedConstants.unitTestsImageDeleter + "Image_1.png";
			String copiedFilePath = SharedConstants.unitTestsImageDeleter + "Image_2.png";
			FileUtils.copyFile(new File(pathToOriginalImage), new File(copiedFilePath));
			
			webviewselenium.fileUtilities.FileDeleter.deleteFiles(Arrays.asList(copiedFilePath));
			
			assertEquals(new File(SharedConstants.unitTestsImageDeleter).list().length, 1);
	}
}
