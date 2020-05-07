package webviewselenium;

import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import webviewselenium.constans.SharedConstants;
import webviewselenium.database.BookDatabaseValues;
import webviewselenium.xml.ContentXmlReader;

/**
 * Class contains methods that allows to get list of all books that are available on the specific server.
 * As Scanner Controllers use servers' urls as labels - searching in this class is based on the servers' urls.
 */
public class BookService {
	private final static ContentXmlReader dbBooksXml = new ContentXmlReader(SharedConstants.fullNameOfFileThatContainsBooksAvailableForScan, BookDatabaseValues.databaseRootElementName);
	private final static NodeList allXmlBooksNodes = dbBooksXml.readXmlContent();

	private static String providedServerUrl = null;
	private static List<String> booksAvailableOnTheServer = null;

	// Server URL should be provided in the format like it was designed in the supported-servers.xml (<server-url>), e.g. https://openstax.org/.
	public static List<String> getBooksAvailableOnTheServerByServerUrl(String serverUrl) {
        providedServerUrl = serverUrl;
	    booksAvailableOnTheServer = getAvailableBooksByServerUrl();

		if (serverContainsBooks())
            return booksAvailableOnTheServer;
		return null;
	}

	private static List<String> getAvailableBooksByServerUrl() {
		List<String> booksAvailableOnTheServer = new ArrayList<>();
		IntStream.range(0, allXmlBooksNodes.getLength()).forEach(nodeIndex -> {
            Element bookElement = (Element) allXmlBooksNodes.item(nodeIndex);
            String serverXmlElementName = BookDatabaseValues.getServerXmlElementNameByServerUrl(providedServerUrl);

            if (serverExists(serverXmlElementName)) {
                String bookUrl = bookElement.getElementsByTagName(serverXmlElementName).item(0).getTextContent().trim();
                if (bookUrl.length() > 0) {
                        String bookName = bookElement.getAttribute(BookDatabaseValues.bookNameScan).trim();
                        booksAvailableOnTheServer.add(bookName);
                }
            }
		});
		return booksAvailableOnTheServer;
	}

	private static boolean serverContainsBooks() {
		return !booksAvailableOnTheServer.isEmpty();
	}

	private static boolean serverExists(String xmlElementName) {
	    return !xmlElementName.isEmpty();
	}
}