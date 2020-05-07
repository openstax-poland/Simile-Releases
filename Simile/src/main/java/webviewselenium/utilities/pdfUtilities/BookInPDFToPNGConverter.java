package webviewselenium.utilities.pdfUtilities;

import java.io.File;

/**
 * Class contains methods that allow to convert Book in PDF format to PNGs.
 * 
 * @author Arkadiusz Sas (arkadiusz.sas@katalysteducation.org)
 */
public class BookInPDFToPNGConverter implements PDFToImageConverter {

	private final String NAME_FORMAT = "https_%d_1.%s";
	private final String IMAGE_FORMAT = "png";
    private boolean readyForConversion;
	
	@Override
	public void convertPDF(String pdfFilePath, String outputDirectoryPath) {
		readyForConversion = verifyCorrectnessOfPdfFile(new File(pdfFilePath)); 
		
		if(readyForConversion) {
			convertToImage(pdfFilePath, outputDirectoryPath, outputDirectoryPath + File.separator + NAME_FORMAT, IMAGE_FORMAT);
		}
	}

}
