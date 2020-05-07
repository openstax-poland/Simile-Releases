package webviewselenium.gui.chooseBookMenu.utilities.versions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubchapterHashFinder implements VersionFinder {

    private final String REGEX = "_.{8}@";
    private final Pattern pattern = Pattern.compile(REGEX);

    @Override
    public String find(String path) {
        final Matcher matcher = pattern.matcher(path);
        if(matcher.find())
            return matcher.group().substring(1, matcher.group().length() - 1);
        return "";
    }
}