package webviewselenium.gui;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;

/**
 * Class contains methods that allow to manage Simile's scenes.
 */
public class StageManager {

	public void runScanReferenceBookController() {
		ScanReferenceController scanReferenceController = new ScanReferenceController();
		scanReferenceController.showStage();
	}

	public void closeCurrentWindow(Button theButton) {
		Stage stage = (Stage) theButton.getScene().getWindow();
        stage.close();
	}
	
}
