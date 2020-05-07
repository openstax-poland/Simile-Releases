package webviewselenium.compareImages;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import webviewselenium.bashRunScripts.RunDeepComparision;
import webviewselenium.gui.ApplicationLoader;

public class CompareImages {
    private final static Logger logger = ApplicationLoader.getLogger();

    /**
     * If the compared images are different, this methods writes the resultant
     * image (with a marked difference) to the disk.
     *
     * @param pathToScannedImage relative path to directory with scanned images
     * @param pathToTemplateImage relative path to directory with template
     * images
     * @param pathForResultantImage relative path to directory with resultant
     * images
     */
    public static void compareImages(String pathToScannedImage, String pathToTemplateImage, String pathForResultantImage) {
        byte[] scannedImageExtractedBytes = extractBytes(pathToScannedImage);
        byte[] templateImageExtractedBytes = extractBytes(pathToTemplateImage);
        if (calculateCRC32(scannedImageExtractedBytes) != calculateCRC32(templateImageExtractedBytes)) {
            IndicateImagesDifferences.findDifferencesBetweenImages(pathToScannedImage, pathToTemplateImage, pathForResultantImage);
        }
    }
    
    public static void deepCompreImages(String pathToScannedImage, String pathToTemplateImage, String pathForResultantImage) {
        byte[] scannedImageExtractedBytes = extractBytes(pathToScannedImage);
        byte[] templateImageExtractedBytes = extractBytes(pathToTemplateImage);
        if (calculateCRC32(scannedImageExtractedBytes) != calculateCRC32(templateImageExtractedBytes)) {
            RunDeepComparision runDeepComparision = new RunDeepComparision();
            runDeepComparision.runDeepComparision(pathToScannedImage, pathToTemplateImage, pathForResultantImage);
        }
    }

    /**
     * This method allows to extract bytes of the image to the array.
     *
     * @param pathToImage relative path to image
     * @return returns extracted bytes of the image to the array
     */
    public static byte[] extractBytes(String pathToImage) {
        try {
            File image = new File(pathToImage);
            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(image);
            WritableRaster raster = bufferedImage.getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
            return (data.getData());
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error(CompareImages.class.getName() + ": " + ex.getMessage());
        }
        return null;
    }

    /**
     * This method calculates the CRC32 value of the element. 
     * 
     * @param data array of extracted bytes of the image
     * @return returns value of the checksum
     */
    public static long calculateCRC32(byte[] data) {
        Checksum checksum = new CRC32();
        checksum.update(data, 0, data.length);
        return checksum.getValue();
    }
}
