package webviewselenium.utilities.pdfUtilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

/**
 * Interface contains methods that allow to convert PDF to Images.
 * 
 * @author Arkadiusz Sas (arkadiusz.sas@katalysteducation.org)
 */
public interface PDFToImageConverter {
	
	public final Logger LOGGER = Logger.getLogger(BookInPDFToPNGConverter.class.getName());
	public final String PDF_NOT_EXISTS_WARNING_MESSAGE = "PDF File not exists!";

	/**
	 * Method allows to convert PDF file to the specific Image file.
	 * 
	 * @param pdfFilePath path to PDF file that should be converted to Images
	 * @param outputFilePath path to output directory that should store created Images
	 */
	public void convertPDF(String pdfFilePath, String outputDirectoryPath);
    
	/**
	 * Method allows to convert PDF file to the Image file.
	 * Single PDF page is converted to single Image file.
	 * 
	 * @param pdfFilePath path to PDF file that should be converted to Images
	 * @param outputDirectoryPath path to output directory that should store created Images
	 * @param nameFormat output file's path
	 * @param imageFormat extenstion of the Image (all available extensions: JPEG, JPG, GIF, TIFF, PNG) 
	 */
	default void convertToImage(String pdfFilePath, String outputDirectoryPath, String nameFormat, String imageFormat) {
		try {
			PDDocument document = PDDocument.load(new File(pdfFilePath));
			document.close();
			
	        Stream.iterate(0, pageNum -> pageNum + 1)
	        	.limit(document.getNumberOfPages())
	        	.forEach(page -> {
	        		try {
	        			PDDocument document2 = PDDocument.load(new File(pdfFilePath));
	        			PDFRenderer pdfRenderer2 = new PDFRenderer(document2);
						BufferedImage bufferedImage = pdfRenderer2.renderImageWithDPI(page.intValue(), 300, ImageType.RGB);
						ImageIOUtil.writeImage(bufferedImage, String.format(nameFormat, page.intValue() + 1, imageFormat), 300);
						document2.close();
					} catch(IOException exception) {
						LOGGER.log(Level.SEVERE, null, exception);
					}
	        	});
	        
		} catch(IOException exception) {
			LOGGER.log(Level.SEVERE, null, exception);
		} 
	}
	
	/**
	 * Method allows to verify if requested PDF file exists and all properties are correct.
	 * Default implementation has been prepared, because every conversion case should contains the same validation process.
	 * 
	 * @param sourceFile path to PDF file that should be validated
	 * @return true if validation was successful, false if validation was not successful
	 */
    default boolean verifyCorrectnessOfPdfFile(File sourceFile) {
        if (sourceFile == null || !sourceFile.exists() || !sourceFile.getPath().endsWith(".pdf")) {
        	LOGGER.warning(PDF_NOT_EXISTS_WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
