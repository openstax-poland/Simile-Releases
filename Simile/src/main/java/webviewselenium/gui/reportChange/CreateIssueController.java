package webviewselenium.gui.reportChange;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import webviewselenium.gui.StageManager;
import webviewselenium.constans.ConstantFXMLPaths;
import webviewselenium.xml.IssueCategory;
import webviewselenium.xml.IssueCategoryImpl;

public class CreateIssueController {

	private IssueCategory issueCategory = new IssueCategoryImpl();
	private StageManager stageManager = new StageManager();
	
	private static int stageWidth = 500;
	private static int stageHeight = 370;
	
	private static String pathToReportDirectory;
	
	private String categoryName;
	private String categoryDescription;
	
    @FXML
    private TextField categoryNameField;

    @FXML
    private TextField categoryDescriptionField;

    @FXML
    private Button addCategoryButton;

    @FXML
    void addCategory(ActionEvent event) {    	
    	categoryName = categoryNameField.getText();
    	categoryDescription = categoryDescriptionField.getText();
    	
    	String categoryDirectory = issueCategory.getPathOfDesiredCategoryDirectory(pathToReportDirectory, categoryName);
    	String categoryXml = issueCategory.getXmlPathOfDesiredCategory(pathToReportDirectory, categoryName);
    	
    	if(!new File(categoryXml).exists()) {
    		new File(categoryDirectory).mkdirs();    		
    		issueCategory.createIssueCategory(categoryXml, categoryName, categoryDescription);
    	}  
        
    	stageManager.closeCurrentWindow(addCategoryButton);
    }
    
    public CreateIssueController() {}
    
    public CreateIssueController(String pathToReportDirectory) {
    	CreateIssueController.pathToReportDirectory = pathToReportDirectory;
    }
    
    public void showStage() {
		try {
			URL stageUrl = new File(ConstantFXMLPaths.addCategoryMenu).toURI().toURL();
			Parent stageRoot = FXMLLoader.load(stageUrl);
			Stage theStage = new Stage();
			theStage.setScene(new Scene(stageRoot, stageWidth, stageHeight));
			theStage.centerOnScreen();
			theStage.setAlwaysOnTop(true);
			theStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
}
