package webviewselenium.bookScan;

import java.util.Map;
import java.util.stream.IntStream;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import webviewselenium.constans.SharedConstants;
import webviewselenium.database.BookDatabaseValues;
import webviewselenium.xml.ContentXmlReader;

public class UrlFinder {
	private final static ContentXmlReader availableBooksXml = new ContentXmlReader(
			SharedConstants.fullNameOfFileThatContainsBooksAvailableForScan, SharedConstants.availableBooksRootElementName);

	public String getBookFirstPageUrl(String bookName, String serverUrl) {
		Map<String, String> serverUrlsWithTheirElementNames = BookDatabaseValues.getAllServersUrlAndXmlElementsNames();
		NodeList availableBooks = availableBooksXml.readXmlContent();
		StringBuilder bookXmlElementName = new StringBuilder();

		IntStream.range(0, availableBooks.getLength()).forEach(nodeIndex -> {
			Element bookElement = (Element) availableBooks.item(nodeIndex);
			String foundBookName = bookElement.getAttribute(SharedConstants.availableBooksRootAttributeName).trim();
			if (foundBookName.equals(bookName.trim()))
				bookXmlElementName.append(bookElement.getElementsByTagName(
						serverUrlsWithTheirElementNames.get(serverUrl)).item(0).getTextContent().trim());
		});
		return bookXmlElementName.toString();
	}
}
