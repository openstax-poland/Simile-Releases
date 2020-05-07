package webviewselenium.gui.chooseBookMenu.utilities.tableOfContents;

import org.junit.jupiter.api.Test;
import webviewselenium.constans.SharedConstants;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TableOfContentsManagerTest {
	private final String NON_EXISTENT_JSON_FILE = "non-existent JSON file";

	@Test
	void printDifferencesBetweenToCs_correctValue() {
		String expectedValue = "Additional subchapters in QA: \n" +
				"\t1 The Entrepreneurial Perspective | 1.2 Entrepreneurial Vision and Goals\n" +
				"\t1 The Entrepreneurial Perspective | 1.3 The Entrepreneurial Mindset\n" +
				"\n" +
				"Missing subchapters in QA: \n" +
				"\t7 Telling Your Entrepreneurial Story and Pitching the Idea | Case Questions";
		assertEquals(expectedValue, TableOfContentsManager.printDifferencesBetweenToCs(SharedConstants.fullNameOfFirstFileThatStoresTestToC, SharedConstants.fullNameOfSecondFileThatStoresTestToC));
	}

	@Test()
	void printDifferencesBetweenToCs_invalidJson() {
		String expectedValue = "Additional subchapters in QA: Unable to parse table of contents...\n" +
				"\n" +
				"Missing subchapters in QA: Unable to parse table of contents...";

		assertEquals(expectedValue, TableOfContentsManager.printDifferencesBetweenToCs(
				NON_EXISTENT_JSON_FILE, NON_EXISTENT_JSON_FILE));
		assertEquals(expectedValue, TableOfContentsManager.printDifferencesBetweenToCs(
				SharedConstants.fullNameOfFirstFileThatStoresInvalidTestToC, SharedConstants.fullNameOfSecondFileThatStoresInvalidTestToC));
	}

	@Test
	void findDifferenceBetweenToCs_correctValue() {
		final int NUMBER_OF_POSSIBLE_KEYS_TO_INDICATE_DIFFERENCES = 2;
		Map<String, List<String>> differencesBetweenToCs = TableOfContentsManager.findDifferencesBetweenToCs(
				SharedConstants.fullNameOfFirstFileThatStoresTestToC, SharedConstants.fullNameOfSecondFileThatStoresTestToC);

		assertEquals(differencesBetweenToCs.size(), NUMBER_OF_POSSIBLE_KEYS_TO_INDICATE_DIFFERENCES);
		assertTrue(differencesBetweenToCs.containsKey(SharedConstants.mapKeyThatIndicatesAdditionalSubchapters));
		assertTrue(differencesBetweenToCs.containsKey(SharedConstants.mapKeyThatIndicatesMissingSubchapters));
	}

	@Test
	void findDifferenceBetweenToCs_invalidJson() {
		Map<String, List<String>> differencesBetweenToCs = TableOfContentsManager.findDifferencesBetweenToCs(
				SharedConstants.fullNameOfFirstFileThatStoresInvalidTestToC, SharedConstants.fullNameOfSecondFileThatStoresInvalidTestToC);
		assertTrue(differencesBetweenToCs.containsKey(SharedConstants.mapKeyThatIndicatesAdditionalSubchapters));
		assertTrue(differencesBetweenToCs.containsKey(SharedConstants.mapKeyThatIndicatesMissingSubchapters));

		differencesBetweenToCs = TableOfContentsManager.findDifferencesBetweenToCs(
				NON_EXISTENT_JSON_FILE, NON_EXISTENT_JSON_FILE);
		assertTrue(differencesBetweenToCs.containsKey(SharedConstants.mapKeyThatIndicatesAdditionalSubchapters));
		assertTrue(differencesBetweenToCs.containsKey(SharedConstants.mapKeyThatIndicatesMissingSubchapters));
	}

	@Test
	void tableOfContentsParsing_correctValue() {
		final int NUMBER_OF_ELEMENTS_FOUND_IN_QA_TABLE_OF_CONTENTS = 181;
		final int NUMBER_OF_ELEMENTS_FOUND_IN_REF_TABLE_OF_CONTENTS = 180;
		System.out.println(TableOfContentsManager.parseJsonToFindToC(SharedConstants.fullNameOfFirstFileThatStoresTestToC));
		assertEquals(TableOfContentsManager.parseJsonToFindToC(SharedConstants.fullNameOfFirstFileThatStoresTestToC).size(), NUMBER_OF_ELEMENTS_FOUND_IN_QA_TABLE_OF_CONTENTS);
		assertEquals(TableOfContentsManager.parseJsonToFindToC(SharedConstants.fullNameOfSecondFileThatStoresTestToC).size(), NUMBER_OF_ELEMENTS_FOUND_IN_REF_TABLE_OF_CONTENTS);
	}

	@Test
	void tableOfContentsParsing_InvalidJson() {
		assertTrue(TableOfContentsManager.parseJsonToFindToC(NON_EXISTENT_JSON_FILE).isEmpty());
		assertTrue(TableOfContentsManager.parseJsonToFindToC(SharedConstants.fullNameOfFirstFileThatStoresInvalidTestToC).isEmpty());
	}
}
