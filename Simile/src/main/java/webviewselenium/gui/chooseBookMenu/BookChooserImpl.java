package webviewselenium.gui.chooseBookMenu;

import webviewselenium.gui.chooseBookMenu.books.ScannedBookProperties;
import webviewselenium.constans.SharedConstants;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class BookChooserImpl implements BookChooser {
	private final String PATH_NAME = SharedConstants.nameOfDirectoryThatContainsScans + File.separator;

	@Override
	public String getBookIndex(String pathToScanDirectory) {
		Integer pathNameIndex = pathToScanDirectory.lastIndexOf(PATH_NAME);
		return pathToScanDirectory.substring(pathNameIndex + PATH_NAME.length());
	}

	@Override
	public Map<String, String> getBookInfo(String selectedBookDescription) {
		Map<String, String> bookInfo = new LinkedHashMap<String, String>();

		System.out.println("selectedBookDescription: " + selectedBookDescription);
		bookInfo.put("bookName", selectedBookDescription.substring(0, 55).trim());
		bookInfo.put("commitName", selectedBookDescription.substring(55, 63).trim());
		bookInfo.put("browserName", selectedBookDescription.substring(63, 71).trim());
		bookInfo.put("branchName", selectedBookDescription.substring(71, 86).trim());
		bookInfo.put("serverName", selectedBookDescription.substring(86, 146).trim());
		bookInfo.put("date", selectedBookDescription.substring(146, 158).trim());
		bookInfo.put("avgTime", selectedBookDescription.substring(158, 166).trim());
		bookInfo.put("pathToImage", selectedBookDescription.substring(selectedBookDescription.lastIndexOf("S")).trim());
		
		return bookInfo;
	}

	@Override
	public String getBooksSource(ScannedBookProperties qaBook, ScannedBookProperties refBook) {
		if(qaBook.isFromCnx() && refBook.isFromCnx())
			return "cnx";
		else if(qaBook.isFromRex() && refBook.isFromRex())
			return "rex";
		return "pdf";
	}


}
