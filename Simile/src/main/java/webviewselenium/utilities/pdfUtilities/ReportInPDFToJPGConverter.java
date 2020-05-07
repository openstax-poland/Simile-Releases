package webviewselenium.utilities.pdfUtilities;

import java.io.File;

/**
 * Class contains methods that allow to convert Issue Report in PDF format to JPGs.
 * 
 * @author Arkadiusz Sas (arkadiusz.sas@katalysteducation.org)
 */
public class ReportInPDFToJPGConverter implements PDFToImageConverter {

	private final String NAME_FORMAT = "Report_%d.%s";
	private final String IMAGE_FORMAT = "jpg";
    private boolean readyForConversion;

	@Override
	public void convertPDF(String pdfFilePath, String outputDirectoryPath) {
		readyForConversion = verifyCorrectnessOfPdfFile(new File(pdfFilePath)); 
		
		if(readyForConversion) {
			convertToImage(pdfFilePath, outputDirectoryPath, outputDirectoryPath + File.separator + NAME_FORMAT, IMAGE_FORMAT);
		}		
	}
}
