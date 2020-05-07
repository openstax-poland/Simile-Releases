package webviewselenium.gui.chooseBookMenu;

import webviewselenium.gui.chooseBookMenu.books.ScannedBookProperties;

import java.util.Map;

public interface BookChooser {

	/**
	 * Returns scanned book's directory index, e.g. returns 4 for ScanDB/4.
	 * 
	 * @param pathToScanDirectory path to scan directory of desired book
	 * @return scanned book's directory index
	 */
	public String getBookIndex(String pathToScanDirectory);
	
	/**
	 * Returns all information about selected book, e.g. title, commit name, server name.
	 * 
	 * @param selectedBookDescription full description of selected book
	 * @return all information about selected book
	 */
	public Map<String, String> getBookInfo(String selectedBookDescription);

	public String getBooksSource(ScannedBookProperties qaBook, ScannedBookProperties refBook);
	
}
