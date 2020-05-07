package webviewselenium.generateReport.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import webviewselenium.constans.SharedConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class PdfFactory {

    private String categoryName;
    private String categoryDescription;
    private String categoryPath;
    private Map<Integer, String> categoryScannedImagesPaths;

    public PdfFactory(String categoryName, String categoryDescription, Map<Integer, String> scannedImagesPaths, String savePath) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryScannedImagesPaths = scannedImagesPaths;
        this.categoryPath = savePath;
        System.out.println("categoryDescription: " + categoryDescription);
    }

    public void createPdf() {
        String pdfPath = categoryPath + File.separator + SharedConstants.nameOfCategoryReportPdfFile + ".pdf";

        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists()) {
            try {
                pdfFile.createNewFile();
            } catch (IOException ex) {
            	ex.printStackTrace();
            }
        }
        
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            Chunk chunk = new Chunk("Category: " + categoryName, font);
            document.add(new Paragraph(chunk));
            chunk = new Chunk("Category description: " + categoryDescription, font);
            document.add(new Paragraph(chunk));
            document.add(Chunk.NEWLINE);
            chunk = new Chunk("Related images: ", font);
            document.add(new Paragraph(chunk));
            document.add(Chunk.NEWLINE);

            int numberOfImages = categoryScannedImagesPaths.size();
            for (int i = 0; i < numberOfImages; i++) {

                Image scannedImage = Image.getInstance(categoryScannedImagesPaths.get(i));
                document.add(scannedImage);
                
                int lastIndex = categoryScannedImagesPaths.get(i).lastIndexOf("Scanned");
                String sub1 = categoryScannedImagesPaths.get(i).substring(0, lastIndex);
                String sub2 = categoryScannedImagesPaths.get(i).substring(lastIndex + 7, categoryScannedImagesPaths.get(i).length());
                String result = sub1 + "Template" + sub2;

                Image templateImage = Image.getInstance(result);
                document.add(templateImage);

                document.add(new Paragraph(""));
                document.add(new Paragraph(""));
            }

            document.close();

        } catch (DocumentException ex) {
        	ex.printStackTrace();
        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
  
}
