package webviewselenium.constans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class provides a set of paths to all necessary files.
 */
public class SharedConstants {
    public static String basicPath = "";

    // Path to the directory that contains produced scans
    public static String nameOfDirectoryThatContainsScans = "ScanDB";
    public static String fullNameOfDirectoryThatContainsScans = basicPath + nameOfDirectoryThatContainsScans;

    // Path to the directory that contains resultant images
    public static String nameOfDirectoryThatContainsResultantImages = "Results";
    public static String fullNameOfDirectoryThatContainsResultantImages = fullNameOfDirectoryThatContainsScans + File.separator + nameOfDirectoryThatContainsResultantImages;

    // Path to the directory that contains resultant reports
    public static String nameOfDirectoryThatContainsResultantReports = "reports";
    public static String fullNameOfDirectoryThatContainsResultantReports = basicPath + nameOfDirectoryThatContainsResultantReports;
	public static final String nameOfDirectoryThatContainsLogs = "logs";
    public static final String fullNameOfDirectoryThatContainsLogs = fullNameOfDirectoryThatContainsResultantReports + File.separator + nameOfDirectoryThatContainsLogs;

    // Path to the text file that stores information about subchapters difference
    public static String pathToDifferenceReportFile = "." + File.separator + fullNameOfDirectoryThatContainsResultantReports + File.separator + "Difference_Report.txt";

    // Table of Contents (TOC)
	public static final String mapKeyThatIndicatesAdditionalSubchapters = "additional";
	public static final String mapKeyThatIndicatesMissingSubchapters = "missing";
    public static final String fullNameOfFileThatStoresToC = "toc_1.json";
    public static final String JSON_TITLE_VALUE = "title";
    public static final String JSON_CHAPTER_VALUE = "chapters";


    // Path to the directory that contains database files.
    public static String pathToDirectoryThatContainsDatabaseFiles = "XMLdb";
    public static String fullNameOfDirectoryThatContainsDatabaseFiles = basicPath + pathToDirectoryThatContainsDatabaseFiles;
    public static String fullNameOfFileThatContainsSupportedServersList = fullNameOfDirectoryThatContainsDatabaseFiles + File.separator + "supported-servers.xml";
	public static String fullNameOfFileThatContainsBooksAvailableForScan = fullNameOfDirectoryThatContainsDatabaseFiles + File.separator + "BooksAvailableForScan.xml";

		// XMLs
		// 1.1. supported-servers.xml
		public static final String supportedServersRootElementName = "server";
		public static final String supportedServersServerName = "server-name";
		public static final String supportedServersServerUrl = "server-url";
		public static final String supportedServersServerElement = "server-element";
		// 1.2. BooksAvailableForScan.xml
		public static final String availableBooksRootElementName = "Book";
		public static final String availableBooksRootAttributeName = "Name";

    // Path to the directory that contains CNX Books Scanner
    public static String pathToDirectoryThatContainsCnxBookScanner = "cnx-books-scanner";
    public static String fullNameOfDirectoryThatContainsCnxBookScanner = basicPath + pathToDirectoryThatContainsCnxBookScanner;
    public static String fullNameOfDirectoryThatContainsCnxBookScannerBuilds = fullNameOfDirectoryThatContainsCnxBookScanner + File.separator + "build";

    // Names of relevant files
    public static String nameOfBookInfoXmlFile = "info";

    ///// TEST RESOURCES /////
	public static String nameOfDirectoryThatContainsTestResources = "src" + File.separator + "test" + File.separator + "resources" + File.separator;
	public static String nameOfDirectoryThatContainsXmlResources = "xmls" + File.separator;
	public static String fullNameOfSampleBookInformationScanDB =
			nameOfDirectoryThatContainsTestResources + nameOfDirectoryThatContainsXmlResources + "sample-book-information-scandb.xml";
	public static String fullNameOfRealBookInformationScanDB =
			nameOfDirectoryThatContainsTestResources + nameOfDirectoryThatContainsXmlResources + "real-book-information-scandb.xml";

	public static final String fullNameOfFirstFileThatStoresTestToC = "resources/Unit Tests/ScanDB Test Cases/996/" + fullNameOfFileThatStoresToC;
	public static final String fullNameOfSecondFileThatStoresTestToC = "resources/Unit Tests/ScanDB Test Cases/997/" + fullNameOfFileThatStoresToC;
	public static final String fullNameOfFirstFileThatStoresInvalidTestToC = "resources/Unit Tests/ScanDB Test Cases/998/" + fullNameOfFileThatStoresToC;
	public static final String fullNameOfSecondFileThatStoresInvalidTestToC = "resources/Unit Tests/ScanDB Test Cases/999/" + fullNameOfFileThatStoresToC;

    // Name of the ScannebBookProperties for REX books provided for tests
    public static String rexQaBook = "Anatomy and Physiology  asdfbb  Chrome      asdfghjkl         https://openstax.org/  25.11.2019       4ScanDB/996";
    public static String rexRefBook = "Anatomy and Physiology  zxcvhh  Chrome        zxcvbnm         https://openstax.org/37.26.12.25.11.2019       4ScanDB/997";

    // Name of the ScannebBookProperties for CNX books provided for tests
    public static String cnxQaBook = "Fizyka dla szkół wyższych. Tom 3  stage   Chrome          stage https://staging.openstax.org/33.43.12.24.3.2020       0ScanDB/998";
    public static String cnxRefBook = "Fizyka dla szkół wyższych. Tom 3  stage2  Chrome          stage https://staging.openstax.org/33.43.12.24.3.2020       0ScanDB/999";




    // Name of the ScannebBookProperties for PDF books provided for tests
	public static String nameOfScriptsDirectory = "Scripts";
	public static String nameOfScanPathFile = "ScanPath";
    public static String nameOfBookScanDirectory = basicPath + nameOfDirectoryThatContainsScans;
    


    public static String nameOfLinksXmlFile = "links";
    public static String nameOfCategoryInfoXmlFile = "category";
    public static String nameOfCategoryImagesXmlFile = "category-images";
    public static String nameOfCategoryReportPdfFile = "category-report";
    
    public static String nameOfXmlConfigFile = "Config";
    public static String pathToXmlConfig = "XMLdb" + File.separator + nameOfXmlConfigFile + ".xml";

    
    public static String nameOfScanningServiceDirectory = "cnx-books-scanner";
    public static String nameOfScanningServiceScript = "index.ts";
    
    public static String nameOfCNXBookScannerDirectory = "cnx-books-scanner";
    public static String nameOfKillScanningProcessesScript = "script.sh";
    
    // DeepComparison script paths
    public static String nameOfDeepComparisionScript = "DeepComparisionScript.py";
    public static String pathToDeepComparisionScript = nameOfScriptsDirectory + File.separator + nameOfDeepComparisionScript;
    
    // DifferenceComparison script paths
    public static String nameOfDifferenceComaprisonScript = "DifferenceComparison.py";
    public static String pathToDifferenceComparisonScript = nameOfScriptsDirectory + File.separator + nameOfDifferenceComaprisonScript;
    
    // Unit Tests paths
    public static String unitTestsPath = "resources" + File.separator + "Unit Tests" + File.separator;
    public static String unitTestsPathImagesPNG = unitTestsPath + "Image Finder" + File.separator;
    public static String unitTestsImageSpliter = unitTestsPath + "Image Spliter" + File.separator;
    public static String unitTestsImageDeleter = unitTestsPath + "Image Deleter" + File.separator;
    
    public static void refreshScanDBPath() {
    	
    	File file = new File("XMLdb" + File.separator + SharedConstants.nameOfScanPathFile + ".txt");

		try {
	    	BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
	    	String thePath; 

			while ((thePath = bufferedReader.readLine()) != null) {
				SharedConstants.basicPath = thePath;
			}
			
			bufferedReader.close();

		} catch (IOException e0) {
			e0.printStackTrace();
		} 
		
    	SharedConstants.nameOfBookScanDirectory = SharedConstants.basicPath + "ScanDB";
		SharedConstants.fullNameOfDirectoryThatContainsResultantImages = SharedConstants.basicPath + "ScanDB" + File.separator + "Results";
    }
    
}
