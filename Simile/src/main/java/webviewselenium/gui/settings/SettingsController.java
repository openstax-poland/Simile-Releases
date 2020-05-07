package webviewselenium.gui.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;
import webviewselenium.gui.chooseBookMenu.ChooseBookController;
import webviewselenium.gui.scanBookInQA.ScanBookQAController;
import webviewselenium.constans.ConstantFXMLPaths;
import webviewselenium.constans.SharedConstants;

public class SettingsController {
	
    @FXML
    private Button chooseBookMenu;

	@FXML
	private Button scanReferenceBookButton;

	@FXML
	private Button scanBookInQAButton;

	@FXML
	private Button chooseDirectoryButton;

    @FXML
    private TextArea scanPathTextArea;
	
	@FXML
	void chooseDirectoryForScanStorage(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();

		Stage stage = (Stage) chooseDirectoryButton.getScene().getWindow();

		File file = directoryChooser.showDialog(stage);

		scanPathTextArea.setText(file.toString());
		
		try {
			FileWriter fw = new FileWriter("XMLdb" + File.separator + SharedConstants.nameOfScanPathFile + ".txt");
			fw.write(file.toString() + File.separator);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!new File(file.toString() + "ScanDB").exists()) {
			new File(file.toString() + File.separator + "ScanDB").mkdirs();
		}

		SharedConstants.refreshScanDBPath();
		
		Alert issueGeneratedAlert = new Alert(AlertType.INFORMATION);
        issueGeneratedAlert.setTitle("Information");
        issueGeneratedAlert.setHeaderText("ScanDB path has benn updated to " + file.toString());
        issueGeneratedAlert.showAndWait();
		
	}

	@FXML
	void goToScanBookInQAMenu(ActionEvent event) {
		ScanBookQAController scanBookQAController = new ScanBookQAController();
		scanBookQAController.showStage();
		Stage stage = (Stage) chooseDirectoryButton.getScene().getWindow();
        stage.close();
	}

	@FXML
	void goToScanReferenceBookMenu(ActionEvent event) {
		ScanReferenceController scanReferenceController = new ScanReferenceController();
		scanReferenceController.showStage();
		Stage stage = (Stage) chooseDirectoryButton.getScene().getWindow();
        stage.close();
	}
	
    @FXML
    void goToChooseBookMenu(ActionEvent event) {
    	ChooseBookController chooseBookController = new ChooseBookController();
    	chooseBookController.showStage();
		Stage stage = (Stage) chooseDirectoryButton.getScene().getWindow();
        stage.close();
    }

	@FXML
	void goToSettings(ActionEvent event) {

	}

	public void showStage() {
		try {
			URL url = new File(ConstantFXMLPaths.settingsMenu).toURI().toURL();
			Parent root = FXMLLoader.load(url);
			Stage stage = new Stage();
			stage.setScene(new Scene(root, ConstantFXMLPaths.width, ConstantFXMLPaths.height));
			stage.setMinWidth(ConstantFXMLPaths.width);
			stage.show();
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

}
