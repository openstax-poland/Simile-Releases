package webviewselenium.xml;

import java.util.List;
import java.util.Map;

public interface ScannedBooksInfo {

	/**
	 * This method allows to get information about all available books.
	 *
	 * @param pathToXMLFile relative path to file which contains information
	 * about all available books
	 * @return information about all available books
	 */
	public Map<String, String> readAllBooksInfo(String pathToXmlFile);
	
	/**
     * This method allows to get information about all other books that have
     * same title as scanned book.
     *
     * @param path relative path to file which contains information about all
     * available books
     * @param bookName title of the scanned book
     * @param localSourceName name of the directory in ScanDB of the scanned book
     * @return information about all other books that have same title as scanned
     * book
     */
    public List<String> readMatchedBooksInfo(String pathToXmlFile, String bookName, String localSourceName, String sourceName);
}
