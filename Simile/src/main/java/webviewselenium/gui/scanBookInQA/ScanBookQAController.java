package webviewselenium.gui.scanBookInQA;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import webviewselenium.BookService;
import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;
import webviewselenium.gui.chooseBookMenu.ChooseBookController;
import webviewselenium.bookScan.FolderManager;
import webviewselenium.bookScan.KillScanningProcess;
import webviewselenium.bookScan.RunScannerBuildProcess;
import webviewselenium.bookScan.RunScanningProcess;
import webviewselenium.bookScan.UrlFinder;
import webviewselenium.constans.ConstantFXMLPaths;
import webviewselenium.constans.ConstantServerNames;
import webviewselenium.utilities.pdfUtilities.BookInPDFToPNGConverter;
import webviewselenium.utilities.pdfUtilities.PDFToImageConverter;

public class ScanBookQAController implements Initializable{
	
	// Variables bound with GUI
    @FXML private Button scanReferenceBookButton;
	@FXML private Button chooseBookMenuButton;
	@FXML private ComboBox<String> selectBookComboBox;
	@FXML private TextField branchTextField;	
	@FXML private TextField commitTextFiled;	
	@FXML private ComboBox<String> serverComboBox;
	@FXML private Button submitButton;
	@FXML private Button stopButton;
	@FXML private ProgressBar progressBar;
	@FXML private Button browsePDFButton;
    @FXML private TextField choosenPDFFile;
    
    // Variables bound with GUI
    private Stage theStage;
    
	// Constants bound with logic of the class
	private final Integer MAX_COMMIT_NAME_LENGTH = 6;
	private final Logger LOGGER = Logger.getLogger(ScanReferenceController.class.getSimpleName());

	// Variables bound with logic of the class
	private PDFToImageConverter pdfToImageConverter = new BookInPDFToPNGConverter();
	private List<String> serverNames = ConstantServerNames.getAvailableServers();
	List<String> bookNames = new ArrayList<String>();
	private String pathScanDirectory = "";
	private String directoryIndex = "";
	private String selectedBook = "";
	private String selectedServer = "";
	private String creationDatetime = "";
	private String firstPageURL = "";
	private String selectedItem = "";
	private static String currentPageURL = "";
	private Task<Object> progressTask;
	
	/* Menu navigating methods */
	
    @FXML
    void goToScanReferenceBookMenu(ActionEvent event) {
    	ScanReferenceController scanReferenceController = new ScanReferenceController();
    	scanReferenceController.showStage();
		Stage stage = (Stage) scanReferenceBookButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
	void goToChooseBookMenu(ActionEvent event) {
		ChooseBookController chooseBookController = new ChooseBookController();
		chooseBookController.showStage();
		Stage stage = (Stage) chooseBookMenuButton.getScene().getWindow();
		stage.close();
	}

    @FXML
    void goToSettings(ActionEvent event) {
		//SettingsController settingController = new SettingsController();
		//settingController.showStage();
		//Stage stage = (Stage) scanReferenceBookButton.getScene().getWindow();
        //stage.close();
    }
    
	public void showStage() {
		try {
			URL stageUrl = new File(ConstantFXMLPaths.scanBookInQAMenu).toURI().toURL();
			Parent stageRoot = FXMLLoader.load(stageUrl);
			Stage theStage = new Stage();
			theStage.setScene(new Scene(stageRoot, ConstantFXMLPaths.width, ConstantFXMLPaths.height));
			theStage.setMinWidth(1200);
			theStage.centerOnScreen();
			theStage.toFront();
			theStage.show();
			theStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		        	  killScanningProcess();
		        	  System.exit(0);
		          }
		     });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @FXML
    void serverCombBoxAction(ActionEvent event) {
    	// Remove current books from BookComboBox
    	selectBookComboBox.getItems().removeAll(bookNames);
    	
    	// Get the name of the selected book source
    	selectedItem = serverComboBox.getSelectionModel().getSelectedItem().toString();
    	
    	if (selectedItem.equals("PDF")) {
    		// Prepare components bound with PDF Scanning process    		
    		browsePDFButton.setVisible(true);
    		browsePDFButton.setDisable(false);
    		choosenPDFFile.setVisible(true);
    		choosenPDFFile.setDisable(false);
    		selectBookComboBox.setVisible(false);
    	} else {
			// Get all available books from the selected server
    		selectedItem = "";
    		browsePDFButton.setVisible(false);
    		browsePDFButton.setDisable(true);
    		choosenPDFFile.setVisible(false);
    		choosenPDFFile.setDisable(true);
    		selectBookComboBox.setVisible(true);
	    	bookNames = BookService.getBooksAvailableOnTheServerByServerUrl(serverComboBox.getSelectionModel().getSelectedItem().toString());
			bookNames.forEach(book -> {
				selectBookComboBox.getItems().add(book);
			});
    	}
    }
    
    @FXML
    void bookComboBoxAction(ActionEvent event) {
		submitButton.setText("Scan");
		setCurrentPageURL("");
		killScanningProcess();
    }
    
	/**
	 * Method that calls and runs Scanning Process.
	 * 
	 * @param event 'Scan' button has been pressed.
	 */
	@FXML
	void submit(ActionEvent event) {
		if(submitButton.getText().equals("Scan")) {
			submitButton.setText("Pause");
			progressTask = createProgressTask();
			progressBar.progressProperty().unbind();
			progressBar.progressProperty().bind(progressTask.progressProperty());
			progressTask.messageProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {}
			});
			new Thread(progressTask).start();
		} else if (submitButton.getText().equals("Pause")) {
			submitButton.setText("Scan");
			killScanningProcess();
		}
	}

	/**
	 * Method that kills all processes bound with Scanning Process. 
	 * 
	 * @param event 'Stop' button has been pressed.
	 */
	@FXML
	void stop(ActionEvent event) {
		submitButton.setText("Scan");
		setCurrentPageURL("");
		killScanningProcess();
	}
	
    @FXML
    void browsePDF(ActionEvent event) {
    		FileChooser fileChooser = new FileChooser();
    		fileChooser.setTitle("Browse PDF");
    		fileChooser.getExtensionFilters().addAll(
    		         new ExtensionFilter("PDF Files", "*.pdf"));
    		 File selectedFile = fileChooser.showOpenDialog(theStage);
    		 if (selectedFile != null) {
    			 choosenPDFFile.setText(selectedFile.toString());
    		 }
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Reset current page URL value
		currentPageURL = "";
		
		// Set attributes bound with UI
		progressBar.setStyle("-fx-accent: #440045;");
		
		// Get all available servers
		serverNames.forEach(server -> {
			serverComboBox.getItems().add(server);
		});				
	}
	
	/**
	 * Method processes scanning.
	 * 
	 * @return true
	 */
	public Task<Object> createProgressTask() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				LOGGER.info("Scanning Process is starting...");
				updateProgress(0, -1);
				scanReferenceBookButton.setDisable(true);
				chooseBookMenuButton.setDisable(true);
				
				creationDatetime = getCurrentDatetime();
				if(selectedItem.equals("PDF")) {
					// Get information about the PDF (from PDF Metadata)
					PDDocument document = PDDocument.load(new File(choosenPDFFile.getText()));
					PDDocumentInformation pdfInformation = document.getDocumentInformation();
					selectedBook = pdfInformation.getTitle();
					selectedServer = "PDF";
				}
				else {
					selectedServer = serverComboBox.getSelectionModel().getSelectedItem().toString();
					selectedBook = selectBookComboBox.getSelectionModel().getSelectedItem().toString();
				}
				
				boolean createDirectory = true;
				if(currentPageURL.length() > 0) { createDirectory = false; }
				
				pathScanDirectory = FolderManager.NewScanFolderSetup(selectedBook, "bookID", "999", creationDatetime,
						validateCommitName(commitTextFiled.getText()), branchTextField.getText(), selectedServer, createDirectory);
				directoryIndex = pathScanDirectory.replaceAll("\\D+", "");
				LOGGER.info("Scans will be stored in: " + pathScanDirectory);
				
				if(selectedItem.equals("PDF")) {
					pdfToImageConverter.convertPDF(choosenPDFFile.getText(), pathScanDirectory);					
				} else {
					UrlFinder urlFinder = new UrlFinder();
					firstPageURL = urlFinder.getBookFirstPageUrl(selectedBook, selectedServer);
					
					RunScannerBuildProcess runScannerBuildProcess = new RunScannerBuildProcess();
					runScannerBuildProcess.runScannerBuildProcess();
					if(currentPageURL.length() > 0) {
						String currentDirectoryIndex = Integer.toString((Integer.parseInt(directoryIndex) - 1));
						RunScanningProcess runScanningProcess = new RunScanningProcess(currentPageURL, currentDirectoryIndex);
						setCurrentPageURL("");
						runScanningProcess.runWholeBookScanningProcess();
						
					} else {
						RunScanningProcess runScanningProcess = new RunScanningProcess(firstPageURL, directoryIndex);
						runScanningProcess.runTableOfContentsScanningProcess();
						runScanningProcess.runWholeBookScanningProcess();
					}
				}

				scanReferenceBookButton.setDisable(false);
				chooseBookMenuButton.setDisable(false);
				
				LOGGER.info("Scanning process is ending...");
				updateProgress(1, 1);
				
				return true;
			}
		};
	}
	
	/* Auxiliary methods */
	
	/**
	 * Method validates commit name.
	 * 
	 * @param commitName name that should be validated
	 * @return validated commit name
	 */
	private String validateCommitName(String commitName) {
		if(commitName.length() < MAX_COMMIT_NAME_LENGTH) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(commitName);
			while(stringBuilder.length() < MAX_COMMIT_NAME_LENGTH) {
				stringBuilder.append(' ');
			}
			System.out.println(stringBuilder.toString());
			return stringBuilder.toString();
		}
		if(commitName.length() > MAX_COMMIT_NAME_LENGTH) return commitName.substring(0, MAX_COMMIT_NAME_LENGTH);
		return commitName;		
	}
	
	/** 
	 * Method calculates current datetime.
	 * 
	 * @return current datetime
	 */
	private String getCurrentDatetime() {
		return LocalDateTime.now().getSecond() + "." + LocalDateTime.now().getMinute() + "."
				+ LocalDateTime.now().getHour() + "." + LocalDateTime.now().getDayOfMonth() + "."
				+ LocalDateTime.now().getMonthValue() + "." + LocalDateTime.now().getYear();
	}
	
	/**
	 * Method kills all processes bound with Scanning Process.
	 */
	private void killScanningProcess() {
		KillScanningProcess killScanningProcess = new KillScanningProcess();
		killScanningProcess.killScanningProcess();
	}	
	
	public static void setCurrentPageURL(String currentPageURL) {
		ScanBookQAController.currentPageURL = currentPageURL;
	}	
}