package bookScan;

import org.junit.jupiter.api.Test;
import webviewselenium.bookScan.UrlFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlFinderTest {
	private final static UrlFinder urlFinder = new UrlFinder();

	@Test
	public void getServerUrlByServerName() {
		assertEquals("https://openstax.org/books/astronomy/pages/1-introduction",
				urlFinder.getBookFirstPageUrl("Astronomy","https://openstax.org/"));
		assertEquals("https://staging.openstax.org/books/astronomy/pages/1-introduction",
				urlFinder.getBookFirstPageUrl("Astronomy","https://staging.openstax.org/"));
	}
}
