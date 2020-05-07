package webviewselenium.database;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import webviewselenium.constans.SharedConstants;
import webviewselenium.xml.ContentXmlReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class BookDatabaseValues {
	private final static ContentXmlReader supportedServersXml = new ContentXmlReader(
			SharedConstants.fullNameOfFileThatContainsSupportedServersList, SharedConstants.supportedServersRootElementName);

	public static List<String> getAllServerUrls() {
		return iterateToFindAllSpecificXmlElementsValues(SharedConstants.supportedServersServerUrl, supportedServersXml.readXmlContent());
	}

	public static List<String> getAllServerXmlElementsNames() {
		return iterateToFindAllSpecificXmlElementsValues(SharedConstants.supportedServersServerElement, supportedServersXml.readXmlContent());
	}

	public static Map<String, String> getAllServersUrlAndXmlElementsNames() {
		Map<String, String> allServersUrlAndXmlElementsNames = new LinkedHashMap<>();
		List<String> allServerUrls = getAllServerUrls();
		List<String> allServerXmlElementsNames = getAllServerXmlElementsNames();

		for(int i = 0; i < allServerUrls.size(); i++) {
			allServersUrlAndXmlElementsNames.put(allServerUrls.get(i), allServerXmlElementsNames.get(i));
		}
		return allServersUrlAndXmlElementsNames;
	}

	public static String getServerUrlByServerName(String serverName) {
		return iterateToFindSpecificXmlElementValue(serverName, SharedConstants.supportedServersServerName,
				SharedConstants.supportedServersServerUrl, supportedServersXml.readXmlContent());
	}

	public static String getServerXmlElementNameByServerName(String serverName) {
		return iterateToFindSpecificXmlElementValue(serverName, SharedConstants.supportedServersServerName,
				SharedConstants.supportedServersServerElement, supportedServersXml.readXmlContent());
	}

	public static String getServerXmlElementNameByServerUrl(String serverUrl) {
		return iterateToFindSpecificXmlElementValue(serverUrl, SharedConstants.supportedServersServerUrl,
				SharedConstants.supportedServersServerElement, supportedServersXml.readXmlContent());
	}

	private static List<String> iterateToFindAllSpecificXmlElementsValues(String soughtParameter, NodeList allXmlBooksNodes) {
		List<String> allXmlElementsValues = new ArrayList<>();
		IntStream.range(0, allXmlBooksNodes.getLength()).forEach(nodeIndex -> {
			Element bookElement = (Element) allXmlBooksNodes.item(nodeIndex);
			allXmlElementsValues.add(bookElement.getElementsByTagName(soughtParameter).item(0).getTextContent().trim());
		});
		return allXmlElementsValues;
	}

	private static String iterateToFindSpecificXmlElementValue(String dataParameter, String searchParameter, String soughtParameter, NodeList allXmlBooksNodes) {
		StringBuilder bookXmlElementName = new StringBuilder();
		IntStream.range(0, allXmlBooksNodes.getLength()).forEach(nodeIndex -> {
			Element bookElement = (Element) allXmlBooksNodes.item(nodeIndex);
			String bookUrlFoundInXml = bookElement.getElementsByTagName(searchParameter).item(0).getTextContent().trim();
			String bookElementNameFoundInXml = bookElement.getElementsByTagName(soughtParameter).item(0).getTextContent().trim();
			if (dataParameter.equals(bookUrlFoundInXml))
				bookXmlElementName.append(bookElementNameFoundInXml);
		});
		return bookXmlElementName.toString();
	}


	public static String bookNameScan = "Name";

	// Books stored in the database.
	public static String databaseRootElementName = "Book";

	// Scanned books
	public static String mainBookInformationNodeName = "ScanInfo";
	public static String bookName = "Title";
	public static String serverName = "Server";
	public static String countedPagesName = "Pages";
	public static String commitName = "Commit";
	public static String branchName = "Branch";
	public static String dateName = "Date";
	public static String averageTime = "AverageTime";
	public static String pathName = "Path";

	// issue category
	public static String categoryRootElement = "Category";
	public static String categoryName = "Name";
	public static String categoryDescription = "Description";

}
