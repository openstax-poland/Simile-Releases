package webviewselenium.gui.compareBookMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;
import webviewselenium.bookProperties.BookProperties;
import webviewselenium.compareImages.CompareImages;
import webviewselenium.gui.ApplicationLoader;
import webviewselenium.gui.StageManager;
import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;
import webviewselenium.gui.chooseBookMenu.BookChooser;
import webviewselenium.gui.chooseBookMenu.BookChooserImpl;
import webviewselenium.gui.chooseBookMenu.ChooseBookController;
import webviewselenium.gui.generateIssue.GenerateIssueController;
import webviewselenium.gui.reportChange.SelectCroppedPartController;
import webviewselenium.gui.scanBookInQA.ScanBookQAController;
import webviewselenium.constans.ConstantFXMLPaths;
import webviewselenium.constans.SharedConstants;

public class CompareBookController implements Initializable {
	private final static Logger logger = ApplicationLoader.getLogger();
	private final int BOOK_WIDTH = 1349;
	private final int BOOK_HEIGHT = 686;
	private final double ZOOM_IN_SCALE_VALUE = 1.1;
	private final double ZOOM_OUT_SCALE_VALUE = 0.9;
	private final int QA_SCALE_MAX_ZOOM_IN_VALUE = 500;
	private final int QA_SCALE_MIN_ZOOM_IN_VALUE = -500;
	private final int REFERENCE_SCALE_MAX_ZOOM_IN_VALUE = 500;
	private final int REFERENCE_SCALE_MIN_ZOOM_IN_VALUE = -500;
	private static int qaScale = 0;	
	private static int referenceScale = 0;
	
	private StageManager stageManager = new StageManager();
	private BookChooser bookChooser;
	
	private static List<String> pathsToResultantImages;
	private static List<String> pathToTemplateImages;
	private static BookProperties bookProperties;
	private static BookProperties referenceBookProperties;	

	private static String pathResultantImage = null;
	private static String pathTemplateImage = null;
	private static int imageIndex = 0;
	private static String templateDirectoryIndex = "";
	
	private static double qaBookWidth = 1349;
	private static double qaBookHeigt = 686;
	private static double referenceBookWidth = 1349;
	private static double referenceBookHeight = 686;
	
	private final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private final String requiredDate = sdf.format(new Date());
	
	private String pathToCategoryDirectory = SharedConstants.nameOfDirectoryThatContainsResultantReports + File.separator + requiredDate;

	@FXML
	private Button scanBookInQAButton;

	@FXML
	private Button scanReferenceBookButton;

	@FXML
	private Button chooseBookMenuButton;

	@FXML
	private Button pauseButton;

	@FXML
	private Button reportChangeButton;

	@FXML
	private ImageView qaBookImage;

	@FXML
	private ImageView referenceBookImage;
	
    @FXML
    private Button moveLeftButton;
    
    @FXML
    private Button moveRightButton;
    
    @FXML
    private Button generateIssueMenuButton;
    
    @FXML
    private ScrollPane qaBookScrollPane;
    
    @FXML
    private ScrollPane referenceBookScrollPane;
    
    @FXML
    private Button qaBookZoomOutButton;

    @FXML
    private Button qaBookZoomInButton;
    
    @FXML
    private Button referenceBookZoomInButton;

    @FXML
    private Button referenceBookZoomOutButton;
    
    @FXML
    private Button qaBookInfoButton;
    
    @FXML
    private Button referenceBookInfoButton;
    
    @FXML
    private TextField curentScanInfo;

	@FXML
	void goToScanBookInQAMenu(ActionEvent event) {
		//FileDeleter.deleteFiles(ChooseBookController.getListOfImagesThatShouldBeDeleted());
		if(doesUserWantToLeaveMenu()) {
			ScanBookQAController scanBookQAController = new ScanBookQAController();
			scanBookQAController.showStage();
			stageManager.closeCurrentWindow(scanBookInQAButton);
		}
	}

	@FXML
	void goToScanReferenceBookMenu(ActionEvent event) {
		//FileDeleter.deleteFiles(ChooseBookController.getListOfImagesThatShouldBeDeleted());
		if(doesUserWantToLeaveMenu()) {
			ScanReferenceController scanReferenceController = new ScanReferenceController();
			scanReferenceController.showStage();
			stageManager.closeCurrentWindow(scanReferenceBookButton);
		}
	}
	
	@FXML
	void goToChooseBookMenu(ActionEvent event) {
		//FileDeleter.deleteFiles(ChooseBookController.getListOfImagesThatShouldBeDeleted());
		if(doesUserWantToLeaveMenu()) {
			ChooseBookController chooseBookController = new ChooseBookController();
			chooseBookController.showStage();
			stageManager.closeCurrentWindow(chooseBookMenuButton);
		}
	}
	
	@FXML
    void goToGenerateIssueMenu(ActionEvent event) {
		GenerateIssueController generateIssueController = new GenerateIssueController(
				pathToCategoryDirectory, CompareBookController.bookProperties, CompareBookController.referenceBookProperties);
		generateIssueController.showStage();
    }

	@FXML
	void goToSettings(ActionEvent event) {
		//SettingsController settingController = new SettingsController();
		//settingController.showStage();
		//stageManager.closeCurrentWindow(scanReferenceBookButton);
	}

	@FXML
	void pause(ActionEvent event) {

	}

	@FXML
	void reportChange(ActionEvent event) {
		File categoryDirectory = new File(pathToCategoryDirectory);
		if(!categoryDirectory.exists()) {
			categoryDirectory.mkdirs();
		}
		SelectCroppedPartController selectCroppedPartController = new SelectCroppedPartController(
                pathResultantImage = pathsToResultantImages.get(imageIndex),
                //pathTemplateImage = pathResultantImage.replace("Results", templateDirectoryIndex),
				pathTemplateImage = pathToTemplateImages.get(imageIndex),
                pathsToResultantImages.size() - 1 - imageIndex,
                pathToCategoryDirectory);
		selectCroppedPartController.showStage();
	}
	
    @FXML
    void moveLeft(ActionEvent event) {
    	if (imageIndex > 0) {
    		imageIndex--;
    		curentScanInfo.setText("Current Scan: " + (imageIndex + 1));
    	}
    	pathResultantImage = pathsToResultantImages.get(imageIndex);
        //pathTemplateImage = pathResultantImage.replace("Results", templateDirectoryIndex);
		pathTemplateImage = pathToTemplateImages.get(imageIndex);
        
        displayImages(pathResultantImage, pathTemplateImage);
    }
    
    @FXML
    void moveRight(ActionEvent event) {
    	if (imageIndex < pathsToResultantImages.size() - 1) {
    		imageIndex++;
    		curentScanInfo.setText("Current Scan: " + (imageIndex + 1));
    	}
        pathResultantImage = pathsToResultantImages.get(imageIndex);
        //pathTemplateImage = pathResultantImage.replace("Results", templateDirectoryIndex);
		pathTemplateImage = pathToTemplateImages.get(imageIndex);
        
        displayImages(pathResultantImage, pathTemplateImage);
    }
    
    @FXML
    void zoomInQABook(ActionEvent event) {
    	if(qaScale < QA_SCALE_MAX_ZOOM_IN_VALUE) {
        	pathResultantImage = pathsToResultantImages.get(imageIndex);
			pathTemplateImage = pathToTemplateImages.get(imageIndex);

    		try {
    	        FileInputStream qaBookSource = new FileInputStream(pathResultantImage);
    			qaBookImage.setImage(new Image(qaBookSource));
    			qaBookWidth *= ZOOM_IN_SCALE_VALUE;
    			qaBookHeigt *= ZOOM_IN_SCALE_VALUE;
    			qaBookImage.setFitWidth(qaBookWidth);
    			qaBookImage.setFitHeight(qaBookHeigt);			
    			qaBookScrollPane.setContent(qaBookImage);
    		} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				logger.error(CompareBookController.class.getName() + ": " + ex.getMessage());
    		}
    		qaScale++;
    	}
    }

    @FXML
    void zoomOutQABook(ActionEvent event) {
    	if(qaScale > QA_SCALE_MIN_ZOOM_IN_VALUE) {
	    	pathResultantImage = pathsToResultantImages.get(imageIndex);
	        //pathTemplateImage = pathResultantImage.replace("Results", templateDirectoryIndex);
			pathTemplateImage = pathToTemplateImages.get(imageIndex);
	        
			try {
		        FileInputStream qaBookSource = new FileInputStream(pathResultantImage);
				qaBookImage.setImage(new Image(qaBookSource));
				qaBookWidth *= ZOOM_OUT_SCALE_VALUE;
				qaBookHeigt *= ZOOM_OUT_SCALE_VALUE;
				qaBookImage.setFitWidth(qaBookWidth);
				qaBookImage.setFitHeight(qaBookHeigt);			
				qaBookScrollPane.setContent(qaBookImage);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				logger.error(CompareBookController.class.getName() + ": " + ex.getMessage());
			}
			qaScale--;
    	}
    }
    
    @FXML
    void zoomInReferenceBook(ActionEvent event) {
    	if(referenceScale < REFERENCE_SCALE_MAX_ZOOM_IN_VALUE) {
            //pathTemplateImage = pathResultantImage.replace("Results", templateDirectoryIndex);
			pathTemplateImage = pathToTemplateImages.get(imageIndex);

    		try {
    	        FileInputStream qaBookSource = new FileInputStream(pathTemplateImage);
    	        referenceBookImage.setImage(new Image(qaBookSource));
    	        referenceBookWidth *= ZOOM_IN_SCALE_VALUE;
    	        referenceBookHeight *= ZOOM_IN_SCALE_VALUE;
    			referenceBookImage.setFitWidth(referenceBookWidth);
    			referenceBookImage.setFitHeight(referenceBookHeight);			
    			referenceBookScrollPane.setContent(referenceBookImage);
    		} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				logger.error(CompareBookController.class.getName() + ": " + ex.getMessage());
    		}	
    		referenceScale++;
    	}
    }

    @FXML
    void zoomOutReferenceBook(ActionEvent event) {
    	if(referenceScale > REFERENCE_SCALE_MIN_ZOOM_IN_VALUE) {
	        //pathTemplateImage = pathResultantImage.replace("Results", templateDirectoryIndex);
			pathTemplateImage = pathToTemplateImages.get(imageIndex);
	        
			try {
		        FileInputStream qaBookSource = new FileInputStream(pathTemplateImage);    
				referenceBookImage.setImage(new Image(qaBookSource));
				referenceBookWidth *= ZOOM_OUT_SCALE_VALUE;
				referenceBookHeight *= ZOOM_OUT_SCALE_VALUE;
				referenceBookImage.setFitWidth(referenceBookWidth);
				referenceBookImage.setFitHeight(referenceBookHeight);			
				referenceBookScrollPane.setContent(referenceBookImage);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				logger.error(CompareBookController.class.getName() + ": " + ex.getMessage());
			}	
			referenceScale--;
    	}
    }

	public CompareBookController() {}

	public CompareBookController(List<String> pathsToResultantImages, List<String> pathToTemplateImages, BookProperties bookProperties, BookProperties referenceBookProperties) {
		CompareBookController.pathsToResultantImages = pathsToResultantImages;
		CompareBookController.pathToTemplateImages = pathToTemplateImages;
		CompareBookController.bookProperties = bookProperties;
		CompareBookController.referenceBookProperties = referenceBookProperties;

		Collections.sort(CompareBookController.pathsToResultantImages);
		Collections.sort(CompareBookController.pathToTemplateImages);

		imageIndex = 0;

		bookChooser = new BookChooserImpl();
		pathResultantImage = CompareBookController.pathsToResultantImages.get(0);
		pathTemplateImage = CompareBookController.pathToTemplateImages.get(0);//getBaseOfTemplateImage(pathToTemplateImages.get(0))	+ getResultantImageIndex(pathsToResultantImages.get(0));
		//templateDirectoryIndex = pathTemplateImage.substring(pathTemplateImage.lastIndexOf("ScanDB") + 7, pathTemplateImage.lastIndexOf("/https"));
		templateDirectoryIndex = bookChooser.getBookIndex(pathTemplateImage);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			FileInputStream qaBookSource = new FileInputStream(pathResultantImage);	
			qaBookImage.setImage(new Image(qaBookSource));
			qaBookScrollPane.setContent(qaBookImage);
			//qaBookWidth = qaBookImage.getFitWidth();
			//qaBookHeigt = qaBookImage.getFitHeight();
			qaBookImage.setFitWidth(qaBookWidth);
			qaBookImage.setFitHeight(qaBookHeigt);
			FileInputStream referenceBookSource = new FileInputStream(pathTemplateImage);
			//FileInputStream referenceBookSource = new FileInputStream(pathResultantImage.replace("Results", templateDirectoryIndex));
			referenceBookImage.setImage(new Image(referenceBookSource));
			referenceBookScrollPane.setContent(referenceBookImage);
			//referenceBookWidth = referenceBookImage.getFitWidth();
			//referenceBookHeight = referenceBookImage.getFitHeight();
			referenceBookImage.setFitWidth(referenceBookWidth);
			referenceBookImage.setFitHeight(referenceBookHeight);	
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			logger.error(CompareBookController.class.getName() + ": " + ex.getMessage());
		}
		
		qaBookInfoButton.setTooltip(new Tooltip(
				"Title: " + bookProperties.getBookTitle() + "\n" +
				"Commit: " + bookProperties.getCommitName() + "\n" +
				"Browser: " + bookProperties.getBrowserName() + "\n" +
				"Branch: " + bookProperties.getBranchName() + "\n" +
				"Server: " + bookProperties.getServerName() + "\n" +
				"Date: " + bookProperties.getDate() + "\n"));
		
		referenceBookInfoButton.setTooltip(new Tooltip(
				"Title: " + referenceBookProperties.getBookTitle() + "\n" +
				"Commit: " + referenceBookProperties.getCommitName() + "\n" +
				"Browser: " + referenceBookProperties.getBrowserName() + "\n" +
				"Branch: " + referenceBookProperties.getBranchName() + "\n" +
				"Server: " + referenceBookProperties.getServerName() + "\n" +
				"Date: " + referenceBookProperties.getDate() + "\n"));
	}

	public void showStage() {
		try {
			URL stageUrl = new File(ConstantFXMLPaths.compareBookMenu).toURI().toURL();
			Parent stageRoot = FXMLLoader.load(stageUrl);
			Stage theStage = new Stage();
			theStage.setScene(new Scene(stageRoot, ConstantFXMLPaths.width, ConstantFXMLPaths.height));
			theStage.setMinWidth(ConstantFXMLPaths.width);
			theStage.centerOnScreen();
			theStage.toFront();
			theStage.show();
			theStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		        	  //FileDeleter.deleteFiles(ChooseBookController.getListOfImagesThatShouldBeDeleted());
		        	  System.exit(0);	      
		          }
		     }); 
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error(CompareBookController.class.getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * This method gets the file index of the resultant image.
	 * 
	 * @param pathToResultantImage relative path to directory with resultant images
	 * @return file index of the resultant image
	 */
	private String getResultantImageIndex(String pathToResultantImage) {
		return pathToResultantImage.substring(pathToResultantImage.lastIndexOf("_"),
				pathToResultantImage.lastIndexOf(".")) + ".png";
	}

	/**
	 * This method gets the file prefix of the template image.
	 * 
	 * @param pathToTemplateImage relative path to directory with template images
	 * @return file prefix of the template image
	 */
	private String getBaseOfTemplateImage(String pathToTemplateImage) {
		return pathToTemplateImage.substring(0, pathToTemplateImage.lastIndexOf("_"));
	}
	
	/**
	 * Display desired images in prepared ImageView objects. 
	 * 
	 * @param pathResultantImage path to QA Book's image
	 * @param pathTemplateImage path to Reference Book's image
	 */
	private void displayImages(String pathResultantImage, String pathTemplateImage) {
		try {
			FileInputStream qaBookSource = new FileInputStream(pathResultantImage);	
			qaBookImage.setImage(new Image(qaBookSource));
			FileInputStream referenceBookSource = new FileInputStream(pathTemplateImage);
			referenceBookImage.setImage(new Image(referenceBookSource));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			logger.error(CompareBookController.class.getName() + ": " + ex.getMessage());
		}
	}

	private boolean doesUserWantToLeaveMenu() {
		Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmationAlert.setHeaderText("The resulting compared images will be lost!\nAre you sure you want to leave this menu?");
		Optional<ButtonType> result = confirmationAlert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		}
		return false;
	}
}
