package webviewselenium.gui.chooseBookMenu.utilities.tableOfContents;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import webviewselenium.constans.SharedConstants;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Class contains methods that allow to manage ToC's JSON source file.
 * Simile parses JSON source files, it isn't possible to create ones. All those source files are provided by the cnx-book-scanner.
 */
public class TableOfContentsManager {

	public static String printDifferencesBetweenToCs(String pathQaBookToC, String pathRefBookToC) {
		Map<String, List<String>> foundDifferencesBetweenToCs = findDifferencesBetweenToCs(pathQaBookToC, pathRefBookToC);
		StringBuilder resultantDifferences = new StringBuilder();

		resultantDifferences.append("Additional subchapters in QA: ");
		foundDifferencesBetweenToCs.get(SharedConstants.mapKeyThatIndicatesAdditionalSubchapters).forEach(resultantDifferences::append);

		resultantDifferences.append("\n\nMissing subchapters in QA: ");
		foundDifferencesBetweenToCs.get(SharedConstants.mapKeyThatIndicatesMissingSubchapters).forEach(resultantDifferences::append);

		return resultantDifferences.toString();
	}

	public static Map<String, List<String>> findDifferencesBetweenToCs(String pathQaBookToC, String pathRefBookToC) {
		Map<String, List<String>> foundTableOfContentsDifferences = new LinkedHashMap<>();
		List<String> qaBookToC = parseJsonToFindToC(pathQaBookToC);
		List<String> refBookToC = parseJsonToFindToC(pathRefBookToC);
		List<String> unappropriatedBookToC = Collections.singletonList("Unable to parse table of contents...");

		if(isJsonParsedProperly(qaBookToC) && isJsonParsedProperly(refBookToC)) {
			foundTableOfContentsDifferences.put(SharedConstants.mapKeyThatIndicatesAdditionalSubchapters, findUniqueSubchapters(qaBookToC, refBookToC));
			foundTableOfContentsDifferences.put(SharedConstants.mapKeyThatIndicatesMissingSubchapters, findUniqueSubchapters(refBookToC, qaBookToC));
		} else {
			foundTableOfContentsDifferences.put(SharedConstants.mapKeyThatIndicatesAdditionalSubchapters, unappropriatedBookToC);
			foundTableOfContentsDifferences.put(SharedConstants.mapKeyThatIndicatesMissingSubchapters, unappropriatedBookToC);
		}
		return  foundTableOfContentsDifferences;
	}

	public static List<String> parseJsonToFindToC(String pathBookToC) {
		List<String> parsedBookToC = new ArrayList<>();

		findAllMainChapters(pathBookToC).forEach(chapter -> {
			if(isParentChapter(chapter)) {
				parsedBookToC.add(printParentChapterDescription(chapter));
				JSONArray allChaptersModules = (JSONArray) chapter.get(SharedConstants.JSON_CHAPTER_VALUE);
				IntStream.range(0, allChaptersModules.size()).forEach(moduleIndex -> {
					JSONObject chaptersModule = (JSONObject) allChaptersModules.get(moduleIndex);
					parsedBookToC.add(printChildChapterDescription(chapter, chaptersModule));
				});
			}
		});

		return parsedBookToC;
	}

	private static List<JSONObject> findAllMainChapters(String pathBookToC) {
		List<JSONObject> parsedBookChapters = new ArrayList<>();
		Object parsedJsonFile = getParsedJsonFile(pathBookToC);
		JSONArray parsedJson = (JSONArray) parsedJsonFile;

		if(!isJsonFileEmpty(parsedJson)) {
			IntStream.range(0, parsedJson.size()).forEach(i -> {
				JSONObject currentParentObject = (JSONObject) parsedJson.get(i);
				parsedBookChapters.add(currentParentObject);
			});
			return parsedBookChapters;
		}
		return new ArrayList<>();
	}

	private static boolean isParentChapter(JSONObject currentParentObject) {
		return currentParentObject.containsKey(SharedConstants.JSON_CHAPTER_VALUE);
	}

	private static String printParentChapterDescription(JSONObject parentObject) {
		return "\n" + parentObject.get(SharedConstants.JSON_TITLE_VALUE);
	}
	
	private static String printChildChapterDescription(JSONObject parentObject, JSONObject childObject) {
		return "\n\t" + parentObject.get(SharedConstants.JSON_TITLE_VALUE) + " | " + childObject.get(SharedConstants.JSON_TITLE_VALUE);
	}

	private static boolean isJsonFileEmpty(JSONArray parsedJson) {
		return parsedJson.isEmpty();
	}

	private static boolean isJsonParsedProperly(List<String> bookToC) {
		return !bookToC.isEmpty();
	}

	private static List<String> findUniqueSubchapters(List<String> firstBookToC, List<String> secondBookToC) {
		List<String> uniqueSubchapters = new ArrayList<>();
		firstBookToC.forEach(subchapterTitle -> {
			if(!tocContainsSubchapter(secondBookToC, subchapterTitle))
				uniqueSubchapters.add(subchapterTitle);
		});
		return uniqueSubchapters;
	}

	private static boolean tocContainsSubchapter(List<String> bookToC, String subchapter) {
		return bookToC.contains(subchapter);
	}

	private static Object getParsedJsonFile(String pathBookToC) {
		try {
			return new JSONParser().parse(new FileReader(pathBookToC));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return new JSONArray();
	}
}
