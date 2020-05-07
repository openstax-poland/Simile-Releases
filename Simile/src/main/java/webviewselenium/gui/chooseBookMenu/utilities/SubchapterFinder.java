package webviewselenium.gui.chooseBookMenu.utilities;

import webviewselenium.gui.chooseBookMenu.books.ScannedBookProperties;
import webviewselenium.constans.ConstantMessages;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SubchapterFinder {

	final String QA_UNIQUE_SUBCHAPTERS = "qaUniqueSubchapters";
	final String REF_UNIQUE_SUBCHAPTERS = "refUniqueSubchapters";
	final String REDUNDANT_PAGE = "RedundantPage";
	final String REDUNDANT_PAGE_LIST = "RedundantPageList";
	final String ABSENT_PAGE = "AbsentPage";
	final String ABSENT_PAGE_LIST = "AbsentPageList";
	final String TITLE = "Title";
	final String IMAGE_FILE_EXTENSION = ".png";
	final String SUBCHAPTER_PREFFIX_BULLET = "\t\u2022 ";
	
	/**
	 * Method allows to find all Subchapters for two Scanned Books.
	 * @param qaBook reference to information about QA Book
	 * @param refBook reference to information about Reference Book
	 * @return map that contains all unique Subchapters' names of two books
	 */
	public Map<String, List<String>> findUniqueSubchapters(ScannedBookProperties qaBook, ScannedBookProperties refBook);

	/**
	 * Method allows to prepare found Subchapters' names to the pretty formatting process.
	 * It is useful only for CNX books, because its data has to be modified to use pretty chapter names
	 * instead of unique hashes. REX books have pretty names at the beginning, so it isn't necessary to
	 * implement and use this method for this kind of books.
	 * @param qaBook reference to information about QA Book
	 * @param refBook reference to information about Reference Book
	 * @return map that contains all unique Subchapters' pretty names of two books
	 */
	public Map<String, List<String>> prepareChapterNames(ScannedBookProperties qaBook, ScannedBookProperties refBook);

	/**
	 * Method allows to find unique Subchapters for two lists of Subchapters' names 
	 * 
	 * Possible keys:
	 * 	<ul>
	 * 		<li>Title - title message</li>
	 * 		<li>RedundantPage - redundant pages' title</li>
	 * 		<li>RedundantPageList - detail list [ • chapter name + link] of redundant pages</li>
	 * 		<li>AbsentPage - absent pages' title</li>
	 * 		<li>AbsentPageList - detail list [chapter name + link] of redundant pages</li>
	 * 	</ul>
	 * 
	 * @param qaBookUniqueSubchapters list that contains QA Book's unique Subchapters' names
	 * @param referenceBookUniqueSubchapters list that contains Reference Book's unique Subchapters' names
	 * @return list that contains formatted unique Subchapters' names of two books
	 */
	default public Map<String, String> findUniqueComparisionResult(List<String> qaBookUniqueSubchapters, List<String> referenceBookUniqueSubchapters) {
		Map<String, String> resultantSubchapters = new HashMap<>();
		int numberOfSubchaptersQABook = qaBookUniqueSubchapters.size();
		int numberOfSubchaptersReferenceBook = referenceBookUniqueSubchapters.size();

		// Get the Title
		if(numberOfSubchaptersQABook > 0 || numberOfSubchaptersReferenceBook > 0)
			resultantSubchapters.put(TITLE, ConstantMessages.differencesFoundMessage);
		else
			resultantSubchapters.put(TITLE, ConstantMessages.noDifferencesFoundMessage);

		// Get the RedundantPage
		if(numberOfSubchaptersQABook == 1)
			resultantSubchapters.put(REDUNDANT_PAGE, "+1 " + ConstantMessages.oneSuchapterFoundMessage);
		else if(numberOfSubchaptersQABook > 1)
			resultantSubchapters.put(REDUNDANT_PAGE, "+" + numberOfSubchaptersQABook + " " + ConstantMessages.manySubchaptersFoundMessage);
		else
			resultantSubchapters.put(REDUNDANT_PAGE, ConstantMessages.zeroRedudantSubchaptersFoundMessage);

		final StringBuilder qaResult = new StringBuilder();
		qaBookUniqueSubchapters.forEach(subchapter -> {
			if(subchapter.length() > 0)
				qaResult.append(SUBCHAPTER_PREFFIX_BULLET + subchapter.replace(IMAGE_FILE_EXTENSION, "") + "\n");
		});

		// Get the RedundantPageList
		resultantSubchapters.put(REDUNDANT_PAGE_LIST, qaResult.toString());

		// Get the AbsentPage
		if(numberOfSubchaptersReferenceBook == 1)
			resultantSubchapters.put(ABSENT_PAGE, "-1 " + ConstantMessages.oneSuchapterFoundMessage.replace("in QA", "REF"));
		else if(numberOfSubchaptersReferenceBook > 1)
			resultantSubchapters.put(ABSENT_PAGE, "-" + numberOfSubchaptersReferenceBook + " " + ConstantMessages.manySubchaptersFoundMessage.replace("in QA", "REF"));
		else
			resultantSubchapters.put(ABSENT_PAGE, ConstantMessages.zeroAbsentSubchaptersFoundMessage.replace("in QA", "REF"));

		// Get the AbsentPageList
		final StringBuilder refResult = new StringBuilder();
		referenceBookUniqueSubchapters.forEach(subchapter -> {
			if(subchapter.length() > 0)
				refResult.append(SUBCHAPTER_PREFFIX_BULLET + subchapter.replace(IMAGE_FILE_EXTENSION, "") + "\n");
		});

		if(refResult.toString().length() > 4)
			//uniqueComparisionResultMap.put("AbsentPageList", "\u2022 " + referenceBookUniqueSubchapters.toString());
			resultantSubchapters.put(ABSENT_PAGE_LIST, refResult.toString());

		return resultantSubchapters;
	}
	
	/**
	 * Method allows to get formatted full Difference Report.
	 * @param uniqueComparisionResults list that contains unique Subchapters' names of two books
	 * @param printRedundantPageList if true - redundant page list will be added, if false - redundant page list will be hidden
	 * @param printAbsentPageList if true - absent page list will be added, if false - absent page list will be hidden
	 * @return formatted Difference Report
	 */
	default public String printDifferenceReport(Map<String, String> uniqueComparisionResults, boolean printRedundantPageList, boolean printAbsentPageList) {
		String breakLine = "\n\n";
		String differenceReport = uniqueComparisionResults.get(TITLE) + breakLine;
		differenceReport += uniqueComparisionResults.get(REDUNDANT_PAGE) + breakLine;
		if(printRedundantPageList) differenceReport += uniqueComparisionResults.get(REDUNDANT_PAGE_LIST) + breakLine;
		differenceReport += uniqueComparisionResults.get(ABSENT_PAGE) + breakLine;
		if(printAbsentPageList) differenceReport += uniqueComparisionResults.get(ABSENT_PAGE_LIST) + breakLine;
		return differenceReport;
	}
	
	/**
	 * Method allows to write Difference Report to the file	
	 * @param reportContent content that should be written to the file
	 * @param pathToReportDirectory path where Difference Report file should be saved
	 */
	default public void generateSubchapterDifferencesReport(String reportContent, String pathToReportDirectory) {
		try {
			// Write Difference Report to the file
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathToReportDirectory), false));
			writer.write(reportContent);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
