package webviewselenium.xml;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import webviewselenium.constans.SharedConstants;
import webviewselenium.database.BookDatabaseValues;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContentXmlReaderTest {

	private final String xmlPath = SharedConstants.fullNameOfSampleBookInformationScanDB;

	@Test
	public void readsProperlyScanDBBookInformation() {
		ContentXmlReader contentXmlReader = new ContentXmlReader(xmlPath, BookDatabaseValues.mainBookInformationNodeName);

		NodeList nodeList = contentXmlReader.readXmlContent();
		Element element = (Element) nodeList.item(0);

		String[] expected = { "title", "server", "pages", "commit", "branch", "date", "averageTime", "path" };
		String[] actual = {
				element.getElementsByTagName(BookDatabaseValues.bookName).item(0).getTextContent(),
				element.getElementsByTagName(BookDatabaseValues.serverName).item(0).getTextContent(),
				element.getElementsByTagName(BookDatabaseValues.countedPagesName).item(0).getTextContent(),
				element.getElementsByTagName(BookDatabaseValues.commitName).item(0).getTextContent(),
				element.getElementsByTagName(BookDatabaseValues.branchName).item(0).getTextContent(),
				element.getElementsByTagName(BookDatabaseValues.dateName).item(0).getTextContent(),
				element.getElementsByTagName(BookDatabaseValues.averageTime).item(0).getTextContent(),
				element.getElementsByTagName(BookDatabaseValues.pathName).item(0).getTextContent()
		};

		IntStream.range(0, expected.length)
				.forEach(index -> {
					assertEquals(expected[index], actual[index]);
				});
	}

}
