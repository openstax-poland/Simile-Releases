package webviewselenium.gui.chooseBookMenu.utilities;

import webviewselenium.gui.chooseBookMenu.books.ScannedBookProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubchapterFinderRex implements SubchapterFinder {
    @Override
    public Map<String, List<String>> findUniqueSubchapters(ScannedBookProperties qaBook, ScannedBookProperties refBook) {
        Map<String, List<String>> uniqueSubchapters = new HashMap<>();

        uniqueSubchapters.put(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS, qaBook.getRexSubchapters().stream()
                .filter(p -> !refBook.getRexSubchapters().contains(p))
                .collect(Collectors.toList()));
        uniqueSubchapters.put(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS, refBook.getRexSubchapters().stream()
                .filter(p -> !qaBook.getRexSubchapters().contains(p))
                .collect(Collectors.toList()));

        return uniqueSubchapters;
    }

    @Override
    public Map<String, List<String>> prepareChapterNames(ScannedBookProperties qaBook, ScannedBookProperties refBook) {
        return null;
    }
}
