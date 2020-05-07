package webviewselenium.gui.chooseBookMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.control.*;
import org.apache.commons.io.FileUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import webviewselenium.gui.ApplicationLoader;
import webviewselenium.gui.chooseBookMenu.utilities.*;
import webviewselenium.gui.chooseBookMenu.utilities.tableOfContents.TableOfContentsManager;
import webviewselenium.gui.chooseBookMenu.utilities.versions.SubchapterPdfFinder;
import webviewselenium.bookProperties.BookProperties;
import webviewselenium.gui.StageManager;
import webviewselenium.gui.ScanReferenceBook.ScanReferenceController;
import webviewselenium.gui.chooseBookMenu.books.ScannedBookProperties;
import webviewselenium.gui.compareBookMenu.CompareBookController;
import webviewselenium.gui.scanBookInQA.ScanBookQAController;
import webviewselenium.gui.settings.SettingsController;
import webviewselenium.bashRunScripts.OpenFile;
import webviewselenium.compareImages.CompareImages;
import webviewselenium.compareImages.database.FileExplorer;
import webviewselenium.constans.ConstantFXMLPaths;
import webviewselenium.constans.ConstantMessages;
import webviewselenium.constans.SharedConstants;
import webviewselenium.fileUtilities.FileDeleter;
import webviewselenium.xml.AvailableScannedBooksFinder;
import webviewselenium.fileUtilities.ImageFinder;
import webviewselenium.xml.ScannedBooksInfo;
import webviewselenium.xml.ScannedBooksInfoImpl;
import webviewselenium.xml.UpdateXmlBookProperties;
import webviewselenium.xml.UpdateXmlBookPropertiesImpl;

public class ChooseBookController implements Initializable {
	private final static Logger logger = ApplicationLoader.getLogger();

	private StageManager stageManager = new StageManager();
	private BookChooser bookChooser = new BookChooserImpl();
	private BookPathFinder bookPathFinder = new BookPathFinderImpl();
	private ImageSpliter imageSpliter = new ImageSpliter(1500);

	private String pathToDirectoryThatContainsScannedBooks = SharedConstants.fullNameOfDirectoryThatContainsScans;
	private String pathToDirectoryThatContainsResultantImages = SharedConstants.fullNameOfDirectoryThatContainsResultantImages;
	private String nameOfBooksInfoXmlFile = SharedConstants.nameOfBookInfoXmlFile;

	private static List<String> allAvailableScannedBooks;
	private Map<String, String> availableBooks = new LinkedHashMap<>();
	private Map<String, String> differencesResults;

	private static List<String> listOfScannedImages;
	private static List<String> listOfTemplateImages;
	private static List<String> listOfResultantImages;

	private static int listOfScannedImagesSize;
	private static int listOfTemplateImagesSize;
	private static int listOfResultantImagesSize;

	private static List<String> listOfImagesThatShouldBeDeleted;

	String pathToQABookDirectory = "";
	String qaBookDirectoryIndex = "";
	String pathToSelectedQABookDirectory = "";

	String pathToReferenceBookDirectory = "";
	String referenceBookDirectoryIndex = "";
	String pathToSelectedReferenceBookDirectory = "";

	private boolean printRedundantPageList = false;
	private boolean printAbsentPageList = false;

	private ComparisionType comparisionType = null;
	private boolean isComparisonDone = false;
	private BookProperties bookProperties;
	private BookProperties referenceBookProperties;

	private AvailableScannedBooksFinder availableScannedBooksFinder;
	private ScannedBooksInfo scannedBooksInfo;
	private SubchapterFinder subchapterFinder;

	private Task progressTask;

	@FXML
	private Button scanBookInQAButton;

	@FXML
	private Button scanReferenceBookButton;

	@FXML
	private Button compareMenuButton;

	@FXML
	private ComboBox<String> bookInQAComboBox;

	@FXML
	private ComboBox<String> referenceBookComboBox;

	@FXML
	private TextArea differencesTextArea;

	@FXML
	private Hyperlink expandRedudantPagesDetailsHyperlink;

	@FXML
	private Hyperlink differencesOpenInFileHyperlink;

	@FXML
	private Button submitButton;

	@FXML
	private Button deepScanButton;

	@FXML
	private ProgressBar progress;

	public static List<String> getListOfImagesThatShouldBeDeleted() {
		return listOfImagesThatShouldBeDeleted;
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
	void goToSettings(ActionEvent event) {
		SettingsController settingController = new SettingsController();
		settingController.showStage();
		stageManager.closeCurrentWindow(scanReferenceBookButton);
	}

	@FXML
	void goToCompareMenu(ActionEvent event) {
		if (isComparisonDone && listOfResultantImagesSize > 0) {
			CompareBookController compareBookController = new CompareBookController(
					listOfResultantImages,
					listOfTemplateImages,
					bookProperties,
					referenceBookProperties);
			compareBookController.showStage();
			stageManager.closeCurrentWindow(submitButton);
		} else {
			showNoDifferencesFoundAlert();
		}
	}

	@FXML
	void runQuickCompare(ActionEvent event) {
		runComparisonProcess(ComparisionType.QuickCompare);
	}

	@FXML
	void runDeepCompare(ActionEvent event) {
		runComparisonProcess(ComparisionType.DeepCompare);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		differencesOpenInFileHyperlink.setVisible(false);
		expandRedudantPagesDetailsHyperlink.setVisible(false);
		scannedBooksInfo = new ScannedBooksInfoImpl();

		// Progress bar is not initialized in the .fxml, so it is necessary to set all the properties in the code.
		progress.setStyle("-fx-accent: #440045;");
		compareMenuButton.setCursor(Cursor.DEFAULT);

		// Books in QA - get all available scanned books
		availableScannedBooksFinder = new AvailableScannedBooksFinder();
		allAvailableScannedBooks = availableScannedBooksFinder.getAllXmlFilesEndsWith(new File(pathToDirectoryThatContainsScannedBooks));

		allAvailableScannedBooks.forEach(booksXmlInfoFile -> {

			availableBooks = scannedBooksInfo.readAllBooksInfo(booksXmlInfoFile);
			availableBooks.keySet().stream().forEach(key -> {
				bookInQAComboBox.getItems().add(availableBooks.get(key));
			});
		});

		// Reference books - get all matched books
		bookInQAComboBox.setOnAction(e -> {
			referenceBookComboBox.getItems().clear();
			String selectedBookInfo = bookInQAComboBox.getSelectionModel().getSelectedItem().toString();
			String selectedBookName = selectedBookInfo.substring(0, 55).trim();
			String selectedBookSourceName = selectedBookInfo.substring(86, 146).trim();
			String selectedBookDate = selectedBookInfo.substring(166).trim();

			allAvailableScannedBooks.forEach(book -> {
				List<String> booksWithTheSameTitle = scannedBooksInfo.readMatchedBooksInfo(book, selectedBookName,
						selectedBookDate, selectedBookSourceName);
				booksWithTheSameTitle.forEach(bookWithTheSameTitle -> {
					referenceBookComboBox.getItems().add(bookWithTheSameTitle);
				});
			});
		});

		referenceBookComboBox.setOnAction(e -> {
			submitButton.setDisable(false);
			deepScanButton.setDisable(false);

			String pathToQABookDirectory = bookPathFinder.getBookPath(bookInQAComboBox.getSelectionModel().getSelectedItem().toString());
			String pathToReferenceBookDirectory = bookPathFinder.getBookPath(referenceBookComboBox.getSelectionModel().getSelectedItem().toString());

			// Remove all old splitted images if still exsist
			SplittedImageDeleter splittedImageDeleter = new SplittedImageDeleter();
			List<String> splittedImagesFromQADirectory = splittedImageDeleter.findAllSplittedImages(pathToQABookDirectory);
			splittedImageDeleter.removeAllSplittedImages(splittedImagesFromQADirectory);
			List<String> splittedImagesFromReferenceDirectory = splittedImageDeleter.findAllSplittedImages(pathToReferenceBookDirectory);
			splittedImageDeleter.removeAllSplittedImages(splittedImagesFromReferenceDirectory);

			// Get properties of the scanned books
			ScannedBookProperties qaBook = new ScannedBookProperties(bookInQAComboBox.getSelectionModel().getSelectedItem());
			ScannedBookProperties refBook = new ScannedBookProperties(referenceBookComboBox.getSelectionModel().getSelectedItem());

			// Get the source of the books
			String source = bookChooser.getBooksSource(qaBook, refBook);

			String s = qaBook.getImages().get(0).substring(qaBook.getImages().get(0).lastIndexOf("_") - 1);
			if(s.startsWith("+")) {
				qaBook.setImages(qaBook.getImages().stream()
						.map(chapterName -> {
							int index = chapterName.lastIndexOf("_") - 1;
							if(chapterName.substring(index).startsWith("+"))
								//str.substring(0, index) + str.substring(index+1);
								chapterName = chapterName.substring(0, index) + chapterName.substring(index + 1);
							return chapterName;
						})
						.collect(Collectors.toList())
				);
			}

			List<String> qaUniqueSubchapters = null;
			List<String> refUniqueSubchapters = null;
			Map<String, List<String>> uniqueSubchapters = null;

			// Depending on the source, prepare the data for comparison
			switch (source) {
				case "cnx":
					subchapterFinder = new SubchapterFinderCnx();
					uniqueSubchapters = subchapterFinder.prepareChapterNames(qaBook, refBook);
					qaUniqueSubchapters = uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS);
					refUniqueSubchapters = uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS);
					break;

				case "rex":
					subchapterFinder = new SubchapterFinderRex();
					uniqueSubchapters = subchapterFinder.findUniqueSubchapters(qaBook, refBook);
					qaUniqueSubchapters = uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS);
					refUniqueSubchapters = uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS);
					break;

				default:
					qaUniqueSubchapters = new ArrayList<>();
					refUniqueSubchapters = new ArrayList<>();
			}

			/*differencesTextArea.setText(printDifferencesBetweenToCs(
					qaBook.getScanPath() + File.separator + SharedConstants.fullNameOfFileThatStoresToC,
					refBook.getScanPath() + File.separator + SharedConstants.fullNameOfFileThatStoresToC
			));*/
			differencesTextArea.setText(TableOfContentsManager.printDifferencesBetweenToCs(
					qaBook.getScanPath() + File.separator + SharedConstants.fullNameOfFileThatStoresToC,
					refBook.getScanPath() + File.separator + SharedConstants.fullNameOfFileThatStoresToC));
		});
	}

	public void showStage() {
		try {
			URL stageUrl = new File(ConstantFXMLPaths.chooseBookMenu).toURI().toURL();
			Parent stageRoot = FXMLLoader.load(stageUrl);
			Stage theStage = new Stage();
			theStage.setScene(new Scene(stageRoot, ConstantFXMLPaths.width, ConstantFXMLPaths.height));
			theStage.setMinWidth(1200);
			theStage.centerOnScreen();
			theStage.toFront();
			theStage.show();
			theStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					List<String> imagesThatShouldBeDeleted = ChooseBookController.getListOfImagesThatShouldBeDeleted();
					if (imagesThatShouldBeDeleted != null) FileDeleter.deleteFiles(imagesThatShouldBeDeleted);
					System.exit(0);
				}
			});
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

	@FXML
	void openDifferencesInFile(ActionEvent event) {
		OpenFile openFileScript = new OpenFile();
		openFileScript.openDifferenceReportFile();

	}

	@FXML
	void expandRedundantPagesDetails(ActionEvent event) {
		if (expandRedudantPagesDetailsHyperlink.getText().equals("Expand")) {
			expandRedudantPagesDetailsHyperlink.setText("Collapse");
			printRedundantPageList = true;
			printAbsentPageList = true;
			differencesTextArea.setText(subchapterFinder.printDifferenceReport(differencesResults, printRedundantPageList, printAbsentPageList));
		} else if (expandRedudantPagesDetailsHyperlink.getText().equals("Collapse")) {
			expandRedudantPagesDetailsHyperlink.setText("Expand");
			printRedundantPageList = false;
			printAbsentPageList = false;
			differencesTextArea.setText(subchapterFinder.printDifferenceReport(differencesResults, printRedundantPageList, printAbsentPageList));
		}
	}

	public Task createProgressTask() {
		return new Task() {
			@Override
			protected Object call() throws Exception {

				compareMenuButton.setDisable(true);
				submitButton.setDisable(true);
				deepScanButton.setDisable(true);

				// Run ProgressBar
				updateProgress(0, -1);
				listOfResultantImagesSize = 0; //???

				// Get properties of the scanned books
				ScannedBookProperties qaBook = new ScannedBookProperties(bookInQAComboBox.getSelectionModel().getSelectedItem());
				ScannedBookProperties refBook = new ScannedBookProperties(referenceBookComboBox.getSelectionModel().getSelectedItem());

				// Get full paths to images from QA Book and Reference Book
				qaBook.setImages(FileExplorer.findAllPNGs(qaBook.getScanPath(), qaBook.getScanPath().getPath()));
				refBook.setImages(FileExplorer.findAllPNGs(refBook.getScanPath(), refBook.getScanPath().getPath()));

				// Get the source of the books
				String source = bookChooser.getBooksSource(qaBook, refBook);

				// Depending on the source, prepare the data for comparison
				switch (source) {
					case "cnx":
						// Delete chapters that are not common to both books
						qaBook.setSubchapters(qaBook.removeDifferentCnxSubchapters(refBook.getCnxSubchapters()));
						refBook.setSubchapters(refBook.removeDifferentCnxSubchapters(qaBook.getCnxSubchapters()));

						// Delete paths that are not contain common subchapters
						qaBook.setImages(qaBook.removeUnusedCnxPaths(refBook.getCnxSubchapters()));
						refBook.setImages(refBook.removeUnusedCnxPaths(qaBook.getCnxSubchapters()));
						break;

					case "rex":
						// Delete chapters that are not common to both books
						qaBook.setSubchapters(qaBook.removeDifferentRexSubchapters(refBook.getRexSubchapters()));
						refBook.setSubchapters(refBook.removeDifferentRexSubchapters(qaBook.getRexSubchapters()));

						// Delete paths that are not contain common subchapters
						qaBook.setImages(qaBook.removeUnusedRexPaths(refBook.getRexSubchapters()));
						refBook.setImages(refBook.removeUnusedRexPaths(qaBook.getRexSubchapters()));
						break;

					case "pdf":
						SubchapterPdfFinder subchapterPdfFinder = new SubchapterPdfFinder();
						final List<String> qaSubchapters = new ArrayList<>();
						qaBook.getImages().forEach(x -> {
							qaSubchapters.add(subchapterPdfFinder.find(x));
						});


						final List<String> refSubchapters = new ArrayList<>();
						refBook.getImages().forEach(x -> {
							refSubchapters.add(subchapterPdfFinder.find(x));
						});

						List<String> refS = refSubchapters.stream()
								.filter(x -> qaSubchapters.contains(x))
								.collect(Collectors.toList());

						List<String> qaS = qaSubchapters.stream()
								.filter(x -> refSubchapters.contains(x))
								.collect(Collectors.toList());

						qaBook.setSubchapters(qaS);
						refBook.setSubchapters(refS);

						List<String> qaImages = qaBook.getImages();

						qaImages = qaImages.stream()
								.filter(x -> qaS.contains(x.substring(x.lastIndexOf("https") + 6, x.length() - 6)))
								.collect(Collectors.toList());

						List<String> refImages = refBook.getImages();
						refImages = refImages.stream()
								.filter(x -> refS.contains(x.substring(x.lastIndexOf("https") + 6, x.length() - 6)))
								.collect(Collectors.toList());

						qaBook.setImages(qaImages);
						refBook.setImages(refImages);
						break;

					default:
						return 0;
				}

				// Update book's path list size
				qaBook.setNumberOfScansInPath(qaBook.getImages().size());
				refBook.setNumberOfScansInPath(refBook.getImages().size());

				// Delete all files from ScanDB/Results directory
				try {
					FileUtils.cleanDirectory(new File(pathToDirectoryThatContainsResultantImages));
				} catch (IOException e) {
					e.printStackTrace();
				}

				long comparisionStartTime = System.currentTimeMillis();

				logger.info("----------------------------------");
				logger.info("Comparison Process is starting...");

				// Compare QA Book and Reference Book
				for (int i = 0; i < qaBook.getImages().size(); i++) {
					String pathToResultantImage = refBook.getImages().get(i).replace(refBook.getScanPath().getPath(), pathToDirectoryThatContainsResultantImages);

					logger.info("----------------------------------");
					logger.info("QA: " + qaBook.getImages().get(i));
					logger.info("REF: " + refBook.getImages().get(i));
					logger.info("RES: " + pathToResultantImage);

					if (comparisionType.equals(ComparisionType.QuickCompare))
						CompareImages.compareImages(qaBook.getImages().get(i), refBook.getImages().get(i), pathToResultantImage);
					else if (comparisionType.equals(ComparisionType.DeepCompare))
						CompareImages.deepCompreImages(qaBook.getImages().get(i), refBook.getImages().get(i), pathToResultantImage);
					updateProgress(i + 1, qaBook.getNumberOfScansInPath() + 5);
					logger.info("DONE");
				}

				logger.info("----------------------------------");
				logger.info("Comparison Process has finished...");

				long comparisionEndTime = System.currentTimeMillis();
				int comparisionTime = (int) ((comparisionEndTime - comparisionStartTime) / 1000);

				String qaBookFullDescription = bookInQAComboBox.getSelectionModel().getSelectedItem();
				final Map<String, String> allQABookInformation = bookChooser.getBookInfo(qaBookFullDescription);

				String referenceBookFullDescription = referenceBookComboBox.getSelectionModel().getSelectedItem();
				final Map<String, String> allReferenceBookInformation = bookChooser.getBookInfo(referenceBookFullDescription);

				UpdateXmlBookProperties updateXmlBookProperties = new UpdateXmlBookPropertiesImpl();
				allAvailableScannedBooks.forEach(pathToBook -> {
					updateXmlBookProperties.updateAverageTimeValue(pathToBook, comparisionTime,
							Integer.parseInt(allQABookInformation.get("avgTime")),
							allQABookInformation.get("bookName"), allQABookInformation.get("commitName"));
				});

				bookProperties = new BookProperties.Builder()
						.bookTitle(allQABookInformation.get("bookName"))
						.commitName(allQABookInformation.get("commitName"))
						.branchName(allQABookInformation.get("branchName"))
						.serverName(allQABookInformation.get("serverName"))
						.date(allQABookInformation.get("date"))
						.avgTime(allQABookInformation.get(Integer.toString(comparisionTime)))
						.pathToImage(allQABookInformation.get("pathToImage"))
						.build();

				referenceBookProperties = new BookProperties.Builder()
						.bookTitle(allReferenceBookInformation.get("bookName"))
						.commitName(allReferenceBookInformation.get("commitName"))
						.branchName(allReferenceBookInformation.get("branchName"))
						.serverName(allReferenceBookInformation.get("serverName"))
						.date(allReferenceBookInformation.get("date"))
						.avgTime(allReferenceBookInformation.get(Integer.toString(comparisionTime)))
						.pathToImage(allReferenceBookInformation.get("pathToImage"))
						.build();

				// Find resultant images
				ImageFinder imageFinder = new ImageFinder.ImageFinderBuilder(pathToDirectoryThatContainsResultantImages).build();
				List<String> resultantImages = imageFinder.findPathsToImages();

				ImageSpliter imageSpliter = new ImageSpliter(1500);
				// Split resultant images into smaller pieces
				imageSpliter.splitImages(resultantImages.stream()
						.map(image -> new File(pathToDirectoryThatContainsResultantImages + File.separator + image))
						.collect(Collectors.toList()));
				// Remove original resultant images
				FileDeleter.deleteFiles(resultantImages.stream()
						.map(image -> pathToDirectoryThatContainsResultantImages + File.separator + image)
						.collect(Collectors.toList()));
				// Split reference images into smaller pieces
				imageSpliter.splitImages(resultantImages.stream()
						.map(image -> new File(refBook.getScanPath().getPath() + File.separator + image))
						.collect(Collectors.toList()));

				// Create list of resultant images and their reference images
				listOfResultantImages = imageFinder.findPathsToImages().stream()
						.map(image -> pathToDirectoryThatContainsResultantImages + File.separator + image)
						.collect(Collectors.toList());
				listOfTemplateImages = listOfResultantImages.stream()
						.map(image -> image.replace(pathToDirectoryThatContainsResultantImages, refBook.getScanPath().getPath()))
						.collect(Collectors.toList());

				SplittedImageDeleter splittedImageDeleter = new SplittedImageDeleter();
				splittedImageDeleter.removeUnusedSplittedImagesinComparision(listOfResultantImages, refBook.getScanIndex());

				// Create list of resultant images and their reference images
				listOfResultantImages = imageFinder.findPathsToImages().stream()
						.map(image -> pathToDirectoryThatContainsResultantImages + File.separator + image)
						.collect(Collectors.toList());
				listOfTemplateImages = listOfResultantImages.stream()
						.map(image -> image.replace(pathToDirectoryThatContainsResultantImages, refBook.getScanPath().getPath()))
						.collect(Collectors.toList());

				listOfResultantImagesSize = listOfResultantImages.size();

				updateProgress(qaBook.getNumberOfScansInPath() + 5, qaBook.getNumberOfScansInPath() + 5);

				compareMenuButton.setCursor(Cursor.HAND);
				isComparisonDone = true;

				submitButton.setDisable(false);
				deepScanButton.setDisable(false);
				compareMenuButton.setDisable(false);

				return true;
			}
		};
	}

	/**
	 * Function allows to set the comparison type.
	 *
	 * @param comparisionType chosen comparision type
	 */
	private void setComparisionType(ComparisionType comparisionType) {
		if (comparisionType.equals(ComparisionType.QuickCompare))
			this.comparisionType = ComparisionType.QuickCompare;
		else if (comparisionType.equals(ComparisionType.DeepCompare))
			this.comparisionType = ComparisionType.DeepCompare;
	}

	/**
	 * Function allows to start comparision process
	 *
	 * @param comparisionType chosen comparision type
	 */
	private void runComparisonProcess(ComparisionType comparisionType) {
		setComparisionType(comparisionType);
		progressTask = createProgressTask();
		progress.progressProperty().unbind();
		progress.progressProperty().bind(progressTask.progressProperty());
		progressTask.messageProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			}
		});
		new Thread(progressTask).start();
	}

	/**
	 * Function allows to show alert that informs there is no difference between chosen books.
	 */
	private void showNoDifferencesFoundAlert() {
		Alert differencesNotFoundAlert = new Alert(Alert.AlertType.INFORMATION);
		differencesNotFoundAlert.setTitle(ConstantMessages.informationHeader);
		differencesNotFoundAlert.setHeaderText(ConstantMessages.noDifferencesAfterComparisionMessage);
		differencesNotFoundAlert.showAndWait();
	}

	private String printDifferencesBetweenToCs(String pathQaBookToC, String pathRefBookToC) {
		return TableOfContentsManager.printDifferencesBetweenToCs(pathQaBookToC, pathRefBookToC);
	}
}