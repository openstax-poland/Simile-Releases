package webviewselenium.xml.bookPropertiesScanDB;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import webviewselenium.bookProperties.BookProperties;
import webviewselenium.database.BookDatabaseValues;
import webviewselenium.xml.ContentXmlReader;

/**
 * Class contains methods that allow to get information about the specific book from the XML file.
 */
public class BookPropertiesFinder {
	public BookProperties findBookProperties(String xmlPath) {
		ContentXmlReader contentXmlReader = new ContentXmlReader(xmlPath, BookDatabaseValues.mainBookInformationNodeName);
		NodeList nodeList = contentXmlReader.readXmlContent();
		Element element = (Element) nodeList.item(0);

		return new BookProperties.Builder()
				.bookTitle(element.getElementsByTagName(BookDatabaseValues.bookName).item(0).getTextContent().trim())
				.commitName(element.getElementsByTagName(BookDatabaseValues.commitName).item(0).getTextContent().trim())
				.branchName(element.getElementsByTagName(BookDatabaseValues.branchName).item(0).getTextContent().trim())
				.serverName(element.getElementsByTagName(BookDatabaseValues.serverName).item(0).getTextContent().trim())
				.date(element.getElementsByTagName(BookDatabaseValues.dateName).item(0).getTextContent().trim())
				.avgTime(element.getElementsByTagName(BookDatabaseValues.averageTime).item(0).getTextContent().trim())
				.pathToImage(element.getElementsByTagName(BookDatabaseValues.pathName).item(0).getTextContent().trim())
				.numberOfPages(element.getElementsByTagName(BookDatabaseValues.countedPagesName).item(0).getTextContent().trim())
				.build();
	}
}
