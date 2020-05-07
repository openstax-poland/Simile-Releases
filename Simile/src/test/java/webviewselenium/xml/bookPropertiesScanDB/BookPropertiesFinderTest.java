package webviewselenium.xml.bookPropertiesScanDB;

import org.junit.jupiter.api.Test;
import webviewselenium.bookProperties.BookProperties;
import webviewselenium.constans.SharedConstants;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookPropertiesFinderTest {

	private final BookPropertiesFinder bookPropertiesFinder = new BookPropertiesFinder();

	@Test
	public void finderGetsBookPropertiesWithItsPath() {
		BookProperties bookProperties =
				bookPropertiesFinder.findBookProperties(SharedConstants.fullNameOfRealBookInformationScanDB);

		String[] expected = {"Anatomy and Physiology", "https://openstax.org/", "999", "commit", "master",
				"12.00.00.01.01.2020", "99", "ScanDB/1"};
		String[] actual = {bookProperties.getBookTitle(), bookProperties.getServerName(),
				bookProperties.getNumberOfPages(), bookProperties.getCommitName(),
				bookProperties.getBranchName(), bookProperties.getDate(),
				bookProperties.getAvgTime(), bookProperties.getPathToImage()};

		IntStream.range(0, expected.length)
				.forEach(index -> {
					assertEquals(expected[index], actual[index]);
				});
	}
}
