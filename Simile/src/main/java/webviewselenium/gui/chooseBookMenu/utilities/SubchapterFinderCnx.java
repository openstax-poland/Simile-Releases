package webviewselenium.gui.chooseBookMenu.utilities;

import webviewselenium.gui.chooseBookMenu.books.ScannedBookProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SubchapterFinderCnx implements SubchapterFinder {

    // Pattern to match chapter name from the file, e.g. @3.1+Chapter-Name-Summary_1.png
    private final Pattern pattern = Pattern.compile("@[\\d\\.]+\\+.+.png");

    @Override
    public Map<String, List<String>> findUniqueSubchapters(ScannedBookProperties qaBook, ScannedBookProperties refBook) {
        Map<String, List<String>> uniqueSubchapters = new HashMap<>();

        uniqueSubchapters.put(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS, qaBook.getCnxSubchapters().stream()
                .filter(p -> !refBook.getCnxSubchapters().contains(p))
                .collect(Collectors.toList()));
        uniqueSubchapters.put(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS, refBook.getCnxSubchapters().stream()
                .filter(p -> !qaBook.getCnxSubchapters().contains(p))
                .collect(Collectors.toList()));

        return uniqueSubchapters;
    }

    @Override
    public Map<String, List<String>> prepareChapterNames(ScannedBookProperties qaBook, ScannedBookProperties refBook) {
        Map<String, List<String>> uniqueSubchapters = findUniqueSubchapters(qaBook, refBook);
        Map<String, List<String>> resultantSubchapters = new HashMap<>();
        List<String> qaUniqueSubchapters = uniqueSubchapters.get(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS);
        List<String> refUniqueSubchapters = uniqueSubchapters.get(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS);
        List<String> resultantQaUniqueSubchapters = new ArrayList<>();
        List<String> resultantRefUniqueSubchapters = new ArrayList<>();

        for(int i = 0; i < qaBook.getImages().size(); i++)
        {
            for(int j = 0; j < qaUniqueSubchapters.size(); j++)
            {
                if(qaBook.getImages().get(i).contains(qaUniqueSubchapters.get(j)))
                {
                    Matcher matcher = pattern.matcher(qaBook.getImages().get(i));
                    if(matcher.find())
                    {
                        int index = matcher.group().lastIndexOf("+");
                        resultantQaUniqueSubchapters.add(matcher.group().substring(index + 1));
                    }

                }
            }
        }

        for(int i = 0; i < refBook.getImages().size(); i++)
        {
            for(int j = 0; j < refUniqueSubchapters.size(); j++)
            {
                if(refBook.getImages().get(i).contains(refUniqueSubchapters.get(j)))
                {
                    Matcher matcher = pattern.matcher(refBook.getImages().get(i));
                    if(matcher.find())
                    {
                        int index = matcher.group().lastIndexOf("+");
                        resultantRefUniqueSubchapters.add(matcher.group().substring(index + 1));
                    }

                }
            }
        }

        resultantSubchapters.put(SubchapterFinder.QA_UNIQUE_SUBCHAPTERS, resultantQaUniqueSubchapters);
        resultantSubchapters.put(SubchapterFinder.REF_UNIQUE_SUBCHAPTERS, resultantRefUniqueSubchapters);
        return resultantSubchapters;
    }
}