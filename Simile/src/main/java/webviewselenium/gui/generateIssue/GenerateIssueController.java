  package webviewselenium.gui.generateIssue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import webviewselenium.bookProperties.BookProperties;
import webviewselenium.gui.ApplicationLoader;
import webviewselenium.gui.StageManager;
import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;
import webviewselenium.gui.chooseBookMenu.ChooseBookController;
import webviewselenium.gui.compareBookMenu.CompareBookController;
import webviewselenium.gui.scanBookInQA.ScanBookQAController;
import webviewselenium.constans.ConstantFXMLPaths;
import webviewselenium.constans.SharedConstants;
import webviewselenium.utilities.pdfUtilities.PDFToImageConverter;
import webviewselenium.utilities.pdfUtilities.ReportInPDFToJPGConverter;

public class GenerateIssueController implements Initializable {
	private final static Logger logger = ApplicationLoader.getLogger();
	private StageManager stageManager = new StageManager();
	
	private static String pathToReportDirectory;
	
    private List<String> allCategories = new ArrayList<>();
    //private static String currentCategoryPath = "";
    //private Integer numberOfCategories = 0;
    //private static Integer currentCategoryIndex = 0;
    private static Integer currentImageNumber = 0;

    private static List<Integer> allImagesIndexes;
    private static Integer currentImageIndex = 0;

    private final String scannedImageTemplate = "croppedImagecroppedScannedImage_";
    private final String templateImageTemplate = "croppedImagecroppedTemplateImage_";

    private static String categoryName;
    private static String categoryDescription;
    
	private static BookProperties bookProperties;
	private static BookProperties referenceBookProperties;	

	@FXML
	private ImageView qaBookImage;

	@FXML
	private ImageView referenceBookImage;

	@FXML
	private Button scanBookInQAButton;

	@FXML
	private Button scanReferenceBookButton;

	@FXML
	private Button chooseBookMenuButton;
	
    @FXML
    private Button compareMenuButton;

	@FXML
	private Button generateIssueButton;

	@FXML
	private ScrollPane qaBookScrollPane;

	@FXML
	private ScrollPane referenceBookScrollPane;

	@FXML
	private Button moveLeftButton;

	@FXML
	private Button moveRightButton;

	@FXML
	private ComboBox<String> categoryComboBox;

	@FXML
	private TextArea categoryDescriptionField;
	
    @FXML
    private Button qaBookInfoButton;

    @FXML
    private Button referenceBookInfoButton;

	@FXML
	void goToScanBookInQAMenu(ActionEvent event) {
		ScanBookQAController scanBookQAController = new ScanBookQAController();
		scanBookQAController.showStage();
		stageManager.closeCurrentWindow(scanBookInQAButton);
	}

	@FXML
	void goToScanReferenceBookMenu(ActionEvent event) {
		ScanReferenceController scanReferenceController = new ScanReferenceController();
		scanReferenceController.showStage();
		stageManager.closeCurrentWindow(scanReferenceBookButton);
	}

	@FXML
	void goToChooseBookMenu(ActionEvent event) {
		ChooseBookController chooseBookController = new ChooseBookController();
		chooseBookController.showStage();
	}
	
    @FXML
    void goToCompareMenu(ActionEvent event) {
    	Stage stage = (Stage) compareMenuButton.getScene().getWindow();
        stage.close();
    }
    
	@FXML
	void goToSettings(ActionEvent event) {
		//SettingsController settingController = new SettingsController();
		//settingController.showStage();
		//stageManager.closeCurrentWindow(scanReferenceBookButton);
	}

	@FXML
	void moveLeft(ActionEvent event) {
		if (qaBookImage.getImage() != null) {
			if (currentImageNumber > 0 && currentImageNumber < allImagesIndexes.size()) {
	            currentImageNumber--;
	            currentImageIndex = allImagesIndexes.get(currentImageNumber);
	            try {
	    			String pathToQAImage = GenerateIssueController.pathToReportDirectory + 
	    					File.separator + 
	    					categoryComboBox.getSelectionModel().getSelectedItem().toString() + 
	    					File.separator + 
	    					scannedImageTemplate + 
	    					allImagesIndexes.get(currentImageNumber) + ".png";
	    	        FileInputStream qaBookSource = new FileInputStream(pathToQAImage);
	    			qaBookImage.setImage(new Image(qaBookSource));
	    			qaBookScrollPane.setContent(qaBookImage);
	    			String pathToReferenceImage = GenerateIssueController.pathToReportDirectory + 
	    					File.separator + 
	    					categoryComboBox.getSelectionModel().getSelectedItem().toString() + 
	    					File.separator + 
	    					templateImageTemplate + 
	    					allImagesIndexes.get(currentImageNumber) + ".png";
	    	        FileInputStream referenceBookSource = new FileInputStream(pathToReferenceImage);
	    	        referenceBookImage.setImage(new Image(referenceBookSource));
	    	        referenceBookScrollPane.setContent(referenceBookImage);
	    		} catch (FileNotFoundException ex) {
	    			ex.printStackTrace();
					logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
	    		}
	        }
		}
	}

	@FXML
	void moveRight(ActionEvent event) {
		if (qaBookImage.getImage() != null) {
	        if (currentImageNumber >= 0 && currentImageNumber < allImagesIndexes.size() - 1) {
	            currentImageNumber++;
	            currentImageIndex = allImagesIndexes.get(currentImageNumber);
	            try {
	    			String pathToQAImage = GenerateIssueController.pathToReportDirectory + 
	    					File.separator + 
	    					categoryComboBox.getSelectionModel().getSelectedItem().toString() + 
	    					File.separator + 
	    					scannedImageTemplate + 
	    					allImagesIndexes.get(currentImageNumber) + ".png";
	    	        FileInputStream qaBookSource = new FileInputStream(pathToQAImage);
	    			qaBookImage.setImage(new Image(qaBookSource));
	    			qaBookScrollPane.setContent(qaBookImage);
	    			String pathToReferenceImage = GenerateIssueController.pathToReportDirectory + 
	    					File.separator + 
	    					categoryComboBox.getSelectionModel().getSelectedItem().toString() + 
	    					File.separator + 
	    					templateImageTemplate + 
	    					allImagesIndexes.get(currentImageNumber) + ".png";
	    	        FileInputStream referenceBookSource = new FileInputStream(pathToReferenceImage);
	    	        referenceBookImage.setImage(new Image(referenceBookSource));
	    	        referenceBookScrollPane.setContent(referenceBookImage);
	    		} catch (FileNotFoundException ex) {
					ex.printStackTrace();
					logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
	    		}
	        }
		}
	}
	
	@FXML
	void categoryChoosen(ActionEvent event) {
		currentImageNumber = 0;
		String categoryPath = GenerateIssueController.pathToReportDirectory + 
				File.separator + 
				categoryComboBox.getSelectionModel().getSelectedItem().toString();
        List<String> allImages = findPNGs(categoryPath);
        allImagesIndexes = findImagesIndexes(allImages);
        Collections.sort(allImagesIndexes);
		
		try {
			String pathToQAImage = GenerateIssueController.pathToReportDirectory + 
					File.separator + 
					categoryComboBox.getSelectionModel().getSelectedItem().toString() + 
					File.separator + 
					scannedImageTemplate + 
					allImagesIndexes.get(0) + ".png";
	        FileInputStream qaBookSource = new FileInputStream(pathToQAImage);
			qaBookImage.setImage(new Image(qaBookSource));
			qaBookScrollPane.setContent(qaBookImage);
			String pathToReferenceImage = GenerateIssueController.pathToReportDirectory + 
					File.separator + 
					categoryComboBox.getSelectionModel().getSelectedItem().toString() + 
					File.separator + 
					templateImageTemplate + 
					allImagesIndexes.get(0) + ".png";
	        FileInputStream referenceBookSource = new FileInputStream(pathToReferenceImage);
	        referenceBookImage.setImage(new Image(referenceBookSource));
	        referenceBookScrollPane.setContent(referenceBookImage);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
		}
		
		Map<String, String> categoryInfo = readInformationFromXMLFile(categoryPath);
		categoryName = categoryInfo.get("name");
		categoryDescription = categoryInfo.get("description");
		categoryDescriptionField.setText(categoryInfo.get("name") 
				+ "\n" + categoryInfo.get("description"));
	}

	@FXML
	void generateIssue(ActionEvent event) {
		String categoryPath = GenerateIssueController.pathToReportDirectory + 
				File.separator + 
				categoryComboBox.getSelectionModel().getSelectedItem().toString();
		Map<Integer, String> images = new LinkedHashMap<Integer, String>();
		String pathToCurrentImage = GenerateIssueController.pathToReportDirectory + 
				File.separator + 
				categoryComboBox.getSelectionModel().getSelectedItem().toString() + 
				File.separator + 
				scannedImageTemplate + 
				allImagesIndexes.get(currentImageNumber);
		images.put(0, pathToCurrentImage);
		generatePDF(categoryPath, categoryName, categoryDescription, images);
		PDFToImageConverter pdfToImageConverter = new ReportInPDFToJPGConverter();
		String jpgReportDirectory = categoryPath + File.separator + "jpgReport";
		if(!new File(jpgReportDirectory).exists()) {
			new File(jpgReportDirectory).mkdirs();
		}
		pdfToImageConverter.convertPDF(
				categoryPath + File.separator + SharedConstants.nameOfCategoryReportPdfFile + ".pdf",
				jpgReportDirectory);
	}

	public GenerateIssueController() {}

	public GenerateIssueController(String pathToReportDirectory, BookProperties bookProperties, BookProperties referenceBookProperties) {
		GenerateIssueController.pathToReportDirectory = pathToReportDirectory;
		GenerateIssueController.bookProperties = bookProperties;
		GenerateIssueController.referenceBookProperties = referenceBookProperties;
	}

	public void showStage() {
		try {
			URL stageUrl = new File(ConstantFXMLPaths.generateIssueMenu).toURI().toURL();
			Parent stageRoot = FXMLLoader.load(stageUrl);
			Stage theStage = new Stage();
			//theStage.setScene(new Scene(stageRoot, ConstantFXMLPaths.width, ConstantFXMLPaths.height));
			//theStage.setMinWidth(ConstantFXMLPaths.width);
			theStage.setScene(new Scene(stageRoot, 1366, 750));
			theStage.setMinWidth(1366);
			theStage.centerOnScreen();
			theStage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        File reportDirectory = new File(pathToReportDirectory);
        File[] allFiles = reportDirectory.listFiles();

        for (File categoryDirectory : allFiles) {
            if (!categoryDirectory.getPath().contains(".")) {
            	categoryComboBox.getItems().add(categoryDirectory.getName());
                allCategories.add(categoryDirectory.getPath());
            }
        }
        
        qaBookInfoButton.setTooltip(new Tooltip(
				"Title: " + bookProperties.getBookTitle() + "\n" +
				"Commit: " + bookProperties.getCommitName() + "\n" +
				"Browser: " + bookProperties.getBrowserName() + "\n" +
				"Branch: " + bookProperties.getBranchName() + "\n" +
				"Source: " + bookProperties.getServerName() + "\n" +
				"Date: " + bookProperties.getDate() + "\n"));
		
		referenceBookInfoButton.setTooltip(new Tooltip(
				"Title: " + referenceBookProperties.getBookTitle() + "\n" +
				"Commit: " + referenceBookProperties.getCommitName() + "\n" +
				"Browser: " + referenceBookProperties.getBrowserName() + "\n" +
				"Branch: " + referenceBookProperties.getBranchName() + "\n" +
				"Source: " + referenceBookProperties.getServerName() + "\n" +
				"Date: " + referenceBookProperties.getDate() + "\n"));
	}
	
    private List<String> findPNGs(String pathToReportDirectory) {
        List<String> allPNGs = new ArrayList<>();
        File[] allFiles = new File(pathToReportDirectory).listFiles();
        for (File file : allFiles) {
            if (!file.getPath().contains(".xml")) {
                allPNGs.add(file.getPath());
            }
        }
        return allPNGs;
    }
	
    private List<Integer> findImagesIndexes(List<String> allPNGs) {
        List<Integer> allImagesIndexes = new ArrayList<>();
        allPNGs.forEach(png -> {
            if (png.contains("ScannedImage")) {
                allImagesIndexes.add(Integer.parseInt(png.substring(png.lastIndexOf("_") + 1, png.lastIndexOf("_") + 2)));
            }
        });
        return allImagesIndexes;
    }
    
    private Map<String, String> readInformationFromXMLFile(String pathToCategoryDirecvtory) {
        try {
            File xmlFile = new File(pathToCategoryDirecvtory + File.separator + SharedConstants.nameOfCategoryInfoXmlFile + ".xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();

            // the name of the root element
            NodeList nodeList = document.getElementsByTagName("Category");

            Map<String, String> categoryInformation = new LinkedHashMap<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node theNode = nodeList.item(i);

                if (theNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element theElement = (Element) theNode;

                    String name = theElement.getElementsByTagName("Name").item(0).getTextContent();
                    String description = theElement.getElementsByTagName("Description").item(0).getTextContent();

                    categoryInformation.put("name", name);
                    categoryInformation.put("description", description);
                }
            }
            return categoryInformation;

        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
        }
        return null;
    }
    
	private String readLinksFromXMLFile(String pathToCategoryDirecvtory, String imageIndex) {
		try {
			File xmlFile = new File(pathToCategoryDirecvtory + File.separator
					+ SharedConstants.nameOfLinksXmlFile+ ".xml");
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();

			// the name of the root element
			NodeList nodeList = document.getElementsByTagName("Images");

			String link = "";

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node theNode = nodeList.item(i);

				if (theNode.getNodeType() == Node.ELEMENT_NODE) {
					Element theElement = (Element) theNode;
					
					if(theElement.getElementsByTagName("Index").item(0).getTextContent().equals(imageIndex)) {
						link = theElement.getElementsByTagName("Link").item(0).getTextContent();
					}
				}
			}
			return link;

		} catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
		}
		return null;
	}
    
    private void generatePDF(String path, String categoryName, String categoryDescription, Map<Integer, String> images) {
        String pathToPDF = path + File.separator + SharedConstants.nameOfCategoryReportPdfFile + ".pdf";
        File pdfFile = new File(pathToPDF);
        if (!pdfFile.exists()) {
            try {
                pdfFile.createNewFile();
            } catch (IOException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
            }
        }
        try {
        	String link = readLinksFromXMLFile(path, Integer.toString(allImagesIndexes.get(currentImageNumber)));
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            document.setPageSize(PageSize.A4.rotate());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathToPDF));
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, new BaseColor(68, 0, 69));
            Font linkAttributeFont = FontFactory.getFont(FontFactory.HELVETICA, 12, new BaseColor(68, 0, 69));
            writer.setSpaceCharRatio(PdfWriter.SPACE_CHAR_RATIO_DEFAULT);
            
            Rectangle background = new Rectangle(PageSize.A4.rotate());
            background.setBackgroundColor(new BaseColor(212, 212, 217));   
            document.add(background);
            Rectangle categoryWhiteBackground = new Rectangle(165, 430, 830, 565);
            categoryWhiteBackground.setBackgroundColor(new BaseColor(255,255,255));
            
            document.add(categoryWhiteBackground);

            Paragraph paragraph;
            Chunk header, value;
            
            header = new Chunk("      Category:", font);
            value = new Chunk(categoryName, font);
            paragraph = new Paragraph();
            paragraph.add(header);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(value);            
            document.add(paragraph);
            
            header = new Chunk("   Description:", font);
            value = new Chunk(categoryDescription, font);
            paragraph = new Paragraph();
            paragraph.add(header);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(value);            
            document.add(paragraph);
            
            header = new Chunk("     Book Title:", font);
            value = new Chunk(bookProperties.getBookTitle(), font);
            paragraph = new Paragraph();
            paragraph.add(header);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(value);            
            document.add(paragraph);

            boolean isPlusAtTheEnd = false;
            if(link.charAt(link.length()-1) == '+') {
				link = link.substring(0, link.length() - 1);
				isPlusAtTheEnd = true;
			}
            String location = link.substring(link.lastIndexOf("+") + 1, link.length() - 1);
            header = new Chunk("       Location:", font);
            value = new Chunk(location, font);
            paragraph = new Paragraph();
            paragraph.add(header);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(value);            
            document.add(paragraph);

            if(isPlusAtTheEnd)
            	link += "+";
            link = link.replace("+", "/");
            link = link.replace("[]", ":");
            link = link.replace("_", ":");
            link = link.substring(0, link.length());
            link = link.replace(referenceBookProperties.getServerName(), bookProperties.getServerName());
            header = new Chunk("              Link:", font);
            value = new Chunk(link, linkAttributeFont);
            paragraph = new Paragraph();
            paragraph.add(header);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(value);            
            document.add(paragraph);
            
            header = new Chunk("         Source:", font);
            value = new Chunk(bookProperties.getServerName(), font);
            paragraph = new Paragraph();
            paragraph.add(header);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(value);            
            document.add(paragraph);
            
            header = new Chunk("       Browser:", font);
            value = new Chunk(bookProperties.getBrowserName(), font);
            paragraph = new Paragraph();
            paragraph.add(header);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(value);            
            document.add(paragraph);
            
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            paragraph = new Paragraph();
            paragraph.add(new Chunk("Book in QA", font));
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING); paragraph.add(Chunk.TABBING);
            paragraph.add(new Chunk("Reference Book", font));
            document.add(paragraph);
            document.add(Chunk.NEWLINE);
            
            int numberOfImages = images.size();
            for (int i = 0; i < numberOfImages; i++) {

            	com.itextpdf.text.Image scannedImage = com.itextpdf.text.Image.getInstance(images.get(i) + ".png");

            	if(scannedImage.getWidth() > 420 && scannedImage.getHeight() > 350) {
            		scannedImage.setAbsolutePosition(37, 30);
            		scannedImage.scaleAbsolute(420, 350);
            	}
            	else if(scannedImage.getWidth() > 420 && scannedImage.getHeight() <= 350) {
            		//scannedImage.setAbsolutePosition(37, 420 - scannedImage.getHeight());
            		scannedImage.setAbsolutePosition(37, 380 - scannedImage.getHeight());
            		scannedImage.scaleAbsolute(420, scannedImage.getHeight());
            		//scannedImage.scaleToFit(425, scannedImage.getHeight());
            	} else {
            		//scannedImage.setAbsolutePosition(37, 380 - scannedImage.getHeight());
            		scannedImage.setAbsolutePosition(37, 380 - scannedImage.getHeight());
            		//scannedImage.scaleToFit(scannedImage.getWidth(), scannedImage.getHeight());
            		scannedImage.scaleAbsolute(scannedImage.getWidth(), scannedImage.getHeight());
            	}

                document.add(scannedImage);
                
                int lastIndex = images.get(i).lastIndexOf("Scanned");
                String sub1 = images.get(i).substring(0, lastIndex);
                String sub2 = images.get(i).substring(lastIndex + 7, images.get(i).length());
                String result = sub1 + "Template" + sub2;

                com.itextpdf.text.Image templateImage = com.itextpdf.text.Image.getInstance(result + ".png");
                
            	if(templateImage.getWidth() > 420 && templateImage.getHeight() > 350) {
            		templateImage.scaleAbsolute(325, 295);
            		templateImage.setAbsolutePosition(503, 85);
            	}
            	else if(templateImage.getWidth() > 420 && templateImage.getHeight() <= 350) {
            		//templateImage.setAbsolutePosition(503, 420 - templateImage.getHeight());
            		//templateImage.scaleToFit(325, templateImage.getHeight());
            		templateImage.setAbsolutePosition(503, 380 - scannedImage.getHeight());
            		templateImage.scaleAbsolute(325, scannedImage.getHeight());
            	} else {
            		//templateImage.setAbsolutePosition(503, 380 - templateImage.getHeight());
            		//templateImage.scaleToFit(templateImage.getWidth(), templateImage.getHeight());
            		templateImage.setAbsolutePosition(503, 380 - scannedImage.getHeight());
            		templateImage.scaleAbsolute(scannedImage.getWidth(), scannedImage.getHeight());
            	}  
            	
                document.add(templateImage);

                document.add(new Paragraph(""));
                document.add(new Paragraph(""));
            }

            document.close();
            
            Alert issueGeneratedAlert = new Alert(AlertType.INFORMATION);
            issueGeneratedAlert.setTitle("Information");
            issueGeneratedAlert.setHeaderText("Issue has been generated successfully!");
            issueGeneratedAlert.showAndWait();

        } catch (DocumentException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
        } catch (FileNotFoundException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
        } catch (IOException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
        }
    }

}
