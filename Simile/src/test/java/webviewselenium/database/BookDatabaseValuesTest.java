package webviewselenium.database;

import org.junit.jupiter.api.Test;
import webviewselenium.constans.SharedConstants;
import webviewselenium.xml.ContentXmlReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookDatabaseValuesTest {
	private final static ContentXmlReader supportedServersXml = new ContentXmlReader(
			SharedConstants.fullNameOfFileThatContainsSupportedServersList, SharedConstants.supportedServersRootElementName);

	@Test
	public void serverUrlsAndServerXmlElementsNamesAreMappedProperly() {
		assertTrue(BookDatabaseValues.getAllServersUrlAndXmlElementsNames().containsKey(BookDatabaseValues.getAllServerUrls().get(0)));
		assertTrue(BookDatabaseValues.getAllServersUrlAndXmlElementsNames().containsValue(BookDatabaseValues.getAllServerXmlElementsNames().get(0)));
		assertTrue(BookDatabaseValues.getAllServersUrlAndXmlElementsNames().containsKey(BookDatabaseValues.getAllServerUrls().get(1)));
		assertTrue(BookDatabaseValues.getAllServersUrlAndXmlElementsNames().containsValue(BookDatabaseValues.getAllServerXmlElementsNames().get(1)));
	}

	@Test
	public void getAllSpecificServerUrls() {
		assertEquals(BookDatabaseValues.getAllServerUrls().size(), supportedServersXml.readXmlContent().getLength());
	}

	@Test
	public void getAllSpecificXmlServerElementsValues() {
		assertEquals(BookDatabaseValues.getAllServerXmlElementsNames().size(), supportedServersXml.readXmlContent().getLength());
	}

	@Test
	public void getServerUrlByServerName() {
		assertEquals("https://openstax.org/", BookDatabaseValues.getServerUrlByServerName("openstax"));
		assertEquals("https://staging.openstax.org/", BookDatabaseValues.getServerUrlByServerName("staging-openstax"));
	}

	@Test
	public void getServerXmlElementNameByServerName() {
		assertEquals("openstaxURL", BookDatabaseValues.getServerXmlElementNameByServerName("openstax"));
		assertEquals("openstaxStagingURL", BookDatabaseValues.getServerXmlElementNameByServerName("staging-openstax"));
	}

	@Test
	public void getServerXmlElementNameByServerUrl() {
		assertEquals("openstaxURL", BookDatabaseValues.getServerXmlElementNameByServerUrl("https://openstax.org/"));
		assertEquals("openstaxStagingURL", BookDatabaseValues.getServerXmlElementNameByServerUrl("https://staging.openstax.org/"));
	}
}
