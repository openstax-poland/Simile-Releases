package webviewselenium.compareImages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.github.romankh3.image.comparison.ImageComparison;
import org.apache.log4j.Logger;
import webviewselenium.gui.ApplicationLoader;

public class IndicateImagesDifferences {
    private final static Logger logger = ApplicationLoader.getLogger();
    private static BufferedImage scannedImage;
    private static BufferedImage templateImage;
    private static File resultantImage;

    /**
     * This method is used to draw rectangles that indicate the place where the
     * compared images are different. Then it saves the resultant image to the
     * disk.
     *
     * @param pathToScannedImage relative path to directory with scanned images
     * @param pathToTemplateImage relative path to directory with template
     * images
     * @param pathForResultantImage relative path to directory with resultant
     * images
     */
    public static void findDifferencesBetweenImages(String pathToScannedImage, String pathToTemplateImage, String pathForResultantImage) {
        try {
            scannedImage = ImageIO.read(new File(pathToScannedImage));
            templateImage = ImageIO.read(new File(pathToTemplateImage));
            resultantImage = new File(pathForResultantImage);

            ImageComparison imageComparison = new ImageComparison(templateImage, scannedImage, resultantImage);
            imageComparison.setRectangleLineWidth(2);
            imageComparison.compareImages();
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error(IndicateImagesDifferences.class.getName() + ": " + ex.getMessage());
        }
    }
}
