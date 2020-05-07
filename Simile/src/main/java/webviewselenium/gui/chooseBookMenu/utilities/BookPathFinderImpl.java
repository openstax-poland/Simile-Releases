package webviewselenium.gui.chooseBookMenu.utilities;

import java.io.File;

import webviewselenium.gui.chooseBookMenu.BookChooser;
import webviewselenium.gui.chooseBookMenu.BookChooserImpl;
import webviewselenium.constans.SharedConstants;

public class BookPathFinderImpl implements BookPathFinder{
	
	private BookChooser bookChooser = new BookChooserImpl();

	@Override
	public String getBookPath(String bookDirectory) {		
		return SharedConstants.nameOfBookScanDirectory + File.separator + bookChooser.getBookIndex(bookDirectory) + File.separator;
	}

}
