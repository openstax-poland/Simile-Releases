package gui.chooseBookMenu.utilities;

import org.junit.jupiter.api.Test;
import webviewselenium.gui.chooseBookMenu.books.ScannedBookProperties;
import webviewselenium.gui.chooseBookMenu.utilities.SubchapterFinder;
import webviewselenium.gui.chooseBookMenu.utilities.SubchapterFinderCnx;
import webviewselenium.gui.chooseBookMenu.utilities.SubchapterFinderRex;
import webviewselenium.constans.SharedConstants;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubchapterFinderTest {

    private ScannedBookProperties qaBook;
    private ScannedBookProperties refBook;
    private SubchapterFinder subchapterFinder;

    @Test
    public void returnEmptyListsForTheSameCnxBook() {
        qaBook = new ScannedBookProperties(SharedConstants.cnxQaBook);
        subchapterFinder = new SubchapterFinderCnx();

        Map<String, List<String>> uniqueSubchapters = subchapterFinder.findUniqueSubchapters(qaBook, qaBook);

        assertEquals(uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS).size(), 0);
        assertEquals(uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS).size(), 0);
    }

    @Test
    public void returnEmptyListsForTheSameRexBook() {
        qaBook = new ScannedBookProperties(SharedConstants.rexQaBook);
        subchapterFinder = new SubchapterFinderRex();

        Map<String, List<String>> uniqueSubchapters = subchapterFinder.findUniqueSubchapters(qaBook, qaBook);

        assertEquals(uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS).size(), 0);
        assertEquals(uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS).size(), 0);
    }

    @Test
    public void findUniqueSubchaptersForCnxBooks() {
        qaBook = new ScannedBookProperties(SharedConstants.cnxQaBook);
        refBook = new ScannedBookProperties(SharedConstants.cnxRefBook);
        subchapterFinder = new SubchapterFinderCnx();

        Map<String, List<String>> uniqueSubchapters = subchapterFinder.findUniqueSubchapters(qaBook, refBook);

        assertEquals(uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS).size(), 1);
        assertEquals(uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS).size(), 2);
    }

    @Test
    public void findUniqueSubchaptersForRexBooks() {
        qaBook = new ScannedBookProperties(SharedConstants.rexQaBook);
        refBook = new ScannedBookProperties(SharedConstants.rexRefBook);
        subchapterFinder = new SubchapterFinderRex();

        Map<String, List<String>> uniqueSubchapters = subchapterFinder.findUniqueSubchapters(qaBook, refBook);
        List<String> resultantQaSubchapters = uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS);
        List<String> resultantRefSubchapters = uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS);

        assertEquals(resultantQaSubchapters.size(), 1);
        assertTrue(resultantQaSubchapters.toString().equals("[1-7-medical-imaging]"));
        assertEquals(resultantRefSubchapters.size(), 1);
        assertTrue(resultantRefSubchapters.toString().equals("[1-1-overview-of-anatomy-and-physiology]"));
    }

    @Test
    public void prepareChapterNamesForCnxBooks() {///home/arkadiusz/software/openstax/Simile-Feniks/Similie/resources/Unit Tests
        qaBook = new ScannedBookProperties(SharedConstants.cnxQaBook);
        refBook = new ScannedBookProperties(SharedConstants.cnxRefBook);
        subchapterFinder = new SubchapterFinderCnx();

        Map<String, List<String>> uniqueSubchapters = subchapterFinder.prepareChapterNames(qaBook, refBook);
        List<String> resultantQaSubchapters = uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS);
        List<String> resultantRefSubchapters = uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS);

        assertEquals(resultantQaSubchapters.size(), 1);
        assertTrue(resultantQaSubchapters.toString().equals("[1-1-Rozchodzenie-się-światła_1.png]"));
        assertEquals(uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS).size(), 2);
        assertTrue(resultantRefSubchapters.toString().equals("[Najważniejsze-wzory_1.png, 2-1-Obrazy-tworzone-przez-zwierciadła-płaskie_1.png]"));
    }

    @Test
    public void prepareChapterNamesForRexBooks() {
        qaBook = new ScannedBookProperties(SharedConstants.rexQaBook);
        refBook = new ScannedBookProperties(SharedConstants.rexRefBook);
        subchapterFinder = new SubchapterFinderRex();

        assertEquals(subchapterFinder.prepareChapterNames(qaBook, refBook), null);
    }

    @Test
    public void findUniqueComparisionResultForCnxBooks() {
        qaBook = new ScannedBookProperties(SharedConstants.cnxQaBook);
        refBook = new ScannedBookProperties(SharedConstants.cnxRefBook);
        subchapterFinder = new SubchapterFinderCnx();

        Map<String, List<String>> uniqueSubchapters = subchapterFinder.prepareChapterNames(qaBook, refBook);
        List<String> resultantQaSubchapters = uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS);
        List<String> resultantRefSubchapters = uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS);
        Map<String, String> comparisionResult = subchapterFinder.findUniqueComparisionResult(resultantQaSubchapters, resultantRefSubchapters);

        assertTrue(comparisionResult.get(SubchapterFinder.REDUNDANT_PAGE).contains("+1"));
        assertTrue(comparisionResult.get(SubchapterFinder.REDUNDANT_PAGE_LIST).contains("• 1-1-Rozchodzenie-się-światła_1"));
        assertTrue(comparisionResult.get(SubchapterFinder.ABSENT_PAGE).contains("-2"));
        assertTrue(comparisionResult.get(SubchapterFinder.ABSENT_PAGE_LIST).contains("• Najważniejsze-wzory_1\n\t• 2-1-Obrazy-tworzone-przez-zwierciadła-płaskie_1"));
    }

    @Test
    public void findUniqueComparisionResultForRexBooks() {
        qaBook = new ScannedBookProperties(SharedConstants.rexQaBook);
        refBook = new ScannedBookProperties(SharedConstants.rexRefBook);
        subchapterFinder = new SubchapterFinderRex();

        Map<String, List<String>> uniqueSubchapters = subchapterFinder.findUniqueSubchapters(qaBook, refBook);
        List<String> resultantQaSubchapters = uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS);
        List<String> resultantRefSubchapters = uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS);
        Map<String, String> comparisionResult = subchapterFinder.findUniqueComparisionResult(resultantQaSubchapters, resultantRefSubchapters);

        assertTrue(comparisionResult.get(SubchapterFinder.REDUNDANT_PAGE).contains("+1"));
        assertTrue(comparisionResult.get(SubchapterFinder.REDUNDANT_PAGE_LIST).contains("• 1-7-medical-imaging"));
        assertTrue(comparisionResult.get(SubchapterFinder.ABSENT_PAGE).contains("-1"));
        assertTrue(comparisionResult.get(SubchapterFinder.ABSENT_PAGE_LIST).contains("• 1-1-overview-of-anatomy-and-physiology"));
    }
}
