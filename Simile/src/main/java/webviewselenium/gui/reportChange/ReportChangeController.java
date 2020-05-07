package webviewselenium.gui.reportChange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import webviewselenium.gui.ApplicationLoader;
import webviewselenium.gui.StageManager;
import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;
import webviewselenium.gui.chooseBookMenu.ChooseBookController;
import webviewselenium.gui.generateIssue.GenerateIssueController;
import webviewselenium.gui.scanBookInQA.ScanBookQAController;
import webviewselenium.gui.settings.SettingsController;
import webviewselenium.constans.ConstantFXMLPaths;
import webviewselenium.constans.SharedConstants;

public class ReportChangeController implements Initializable {
	private final static Logger logger = ApplicationLoader.getLogger();
	private final int BOOK_WIDTH = 1349;
	private final int BOOK_HEIGHT = 686;
	
	private StageManager stageManager = new StageManager();
	
	private static int croppedImageIndex;
	private static String pathToReportDirectory;
	private static String pathToTemplateImage;
	private static String pathToCroppedImage;
	private static String qaBookLinkName;

	private static String selectedCategory;

	private VBox vboxContent = new VBox();

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
	private Button cancelButton;

	@FXML
	private Button submitButton;

	@FXML
	private ScrollPane qaBookScrollPane;

	@FXML
	private ScrollPane referenceBookScrollPane;

	@FXML
	private ScrollPane categoryScrollPane;

	@FXML
	private Button addCategoryButton;

	@FXML
	private Button refreshButton;

	@FXML
	private TextArea categoryDescriptionField;

	@FXML
	void refreshButton(ActionEvent event) {
		File[] allCategories = new File(pathToReportDirectory).listFiles();

		vboxContent.getChildren().clear();

		for (File categoryDirectory : allCategories) {
			if (categoryDirectory.isDirectory()) {
				Button categoryButton = new Button(categoryDirectory.getName());
				vboxContent.setPrefHeight(vboxContent.getPrefHeight() + categoryButton.getPrefHeight());
				categoryButton.setStyle("-fx-border-color: #666666; -fx-border-width: 3px;");
				categoryButton.setPrefWidth(256);
				vboxContent.getChildren().add(categoryButton);

				EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Map<String, String> values = readInformationFromXMLFile(pathToReportDirectory, categoryButton.getText());
						categoryDescriptionField.setText("Current chosen category: " + values.get("name")
								+ "\nDescription: " + values.get("description"));
						selectedCategory = values.get("name");
						event.consume();
					}
				};
				categoryButton.setOnAction(buttonHandler);
			}
		}
		categoryScrollPane.setContent(vboxContent);
	}

	@FXML
	void addCategory(ActionEvent event) {
		CreateIssueController addCategoryController = new CreateIssueController(ReportChangeController.pathToReportDirectory);
		addCategoryController.showStage();
	}

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
		stageManager.closeCurrentWindow(chooseBookMenuButton);
	}

	@FXML
	void goToSettings(ActionEvent event) {
		SettingsController settingController = new SettingsController();
		settingController.showStage();
		stageManager.closeCurrentWindow(scanReferenceBookButton);
	}
	
	@FXML
	void cancel(ActionEvent event) {
		stageManager.closeCurrentWindow(chooseBookMenuButton);
	}

	@FXML
	void submitReportChange(ActionEvent event) {
		String pathCategory = ReportChangeController.pathToReportDirectory + File.separator + selectedCategory;
		String pathCropped = ReportChangeController.pathToReportDirectory + File.separator + selectedCategory
				+ File.separator + "croppedImagecroppedScannedImage_" + croppedImageIndex;
		String pathTemplate = ReportChangeController.pathToReportDirectory + File.separator + selectedCategory
				+ File.separator + "croppedImagecroppedTemplateImage_" + croppedImageIndex;
		try {
			Files.copy(new File(pathToCroppedImage + ".png").toPath(), new File(pathCropped + ".png").toPath());
			new File(pathToCroppedImage + ".png").delete();
			Files.copy(new File(pathToTemplateImage + ".png").toPath(), new File(pathTemplate + ".png").toPath());
			new File(pathToTemplateImage + ".png").delete();
			addPathToXmlFile(pathCategory, qaBookLinkName, Integer.toString(croppedImageIndex));
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
		}
		stageManager.closeCurrentWindow(submitButton);
	}

	public ReportChangeController() {}

	public ReportChangeController(int croppedImageIndex, String pathToReportDirectory, String qaBookLinkName) {
		ReportChangeController.pathToReportDirectory = pathToReportDirectory;
		ReportChangeController.croppedImageIndex = croppedImageIndex;
		ReportChangeController.pathToCroppedImage = pathToReportDirectory + File.separator
				+ "croppedImagecroppedScannedImage_" + croppedImageIndex;
		ReportChangeController.pathToTemplateImage = pathToReportDirectory + File.separator
				+ "templateImagecroppedTemplateImage_" + croppedImageIndex;
		ReportChangeController.qaBookLinkName = qaBookLinkName;
	}

	public void showStage() {
		try {
			URL stageUrl = new File(ConstantFXMLPaths.reportChangeMenu).toURI().toURL();
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
		FileInputStream qaBookSource;
		FileInputStream referenceBookSource;
		try {
			qaBookSource = new FileInputStream(pathToReportDirectory + File.separator + "croppedImagecroppedScannedImage_" + croppedImageIndex + ".png");
			qaBookImage.setImage(new Image(qaBookSource));
			qaBookScrollPane.setContent(qaBookImage);
			qaBookImage.setFitWidth(BOOK_WIDTH);
			qaBookImage.setFitHeight(BOOK_HEIGHT);
			referenceBookSource = new FileInputStream(pathToReportDirectory + File.separator + "templateImagecroppedTemplateImage_" + croppedImageIndex + ".png");
			referenceBookImage.setImage(new Image(referenceBookSource));
			referenceBookScrollPane.setContent(referenceBookImage);
			referenceBookImage.setFitWidth(BOOK_WIDTH);
			referenceBookImage.setFitHeight(BOOK_HEIGHT);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Returns all information about desired category.
	 * 
	 * @param pathToCategoryDirecvtory path to directory where created categories are stored
	 * @param categoryName name of the desired category
	 * @return all information about desired category
	 */
	private Map<String, String> readInformationFromXMLFile(String pathToCategoryDirecvtory, String categoryName) {
		try {
			File xmlFile = new File(pathToCategoryDirecvtory + File.separator + categoryName + File.separator
					+ SharedConstants.nameOfCategoryInfoXmlFile + ".xml");
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

	/**
	 * Adds a link to the selected book page for the category.
	 * 
	 * @param pathToXmlFile path to XML file that stores information about desired category
	 * @param bookLinkName link to book page
	 * @param imageIndex index of added image
	 */
	private void addPathToXmlFile(String pathToXmlFile, String bookLinkName, String imageIndex) {

		String path = pathToXmlFile + File.separator + SharedConstants.nameOfLinksXmlFile + ".xml";

		if (!new File(path).exists()) {
			try {
				new File(pathToXmlFile).mkdirs();

				DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

				Document doc = documentBuilder.newDocument();
				Element rootElement = doc.createElement("Images");
				doc.appendChild(rootElement);

				Element name = doc.createElement("Index");
				name.appendChild(doc.createTextNode(imageIndex));
				rootElement.appendChild(name);

				Element description = doc.createElement("Link");
				description.appendChild(doc.createTextNode(bookLinkName));
				rootElement.appendChild(description);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(path);

				transformer.transform(source, result);

			} catch (ParserConfigurationException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
			} catch (TransformerConfigurationException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
			} catch (TransformerException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
			}
		} else {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.parse(path);
				Element root = document.getDocumentElement();

				Element newServer = document.createElement("Images");

				Element name = document.createElement("Index");
				name.appendChild(document.createTextNode(imageIndex));
				newServer.appendChild(name);

				Element port = document.createElement("Link");
				port.appendChild(document.createTextNode(bookLinkName));
				newServer.appendChild(port);

				root.appendChild(newServer);

				DOMSource source = new DOMSource(document);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				StreamResult result = new StreamResult(path);
				transformer.transform(source, result);
			} catch (ParserConfigurationException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
			} catch (TransformerConfigurationException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
			} catch (TransformerException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
			} catch (SAXException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
			} catch (IOException ex) {
				ex.printStackTrace();
				logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
			}
		}
	}
}
