package webviewselenium.gui.chooseBookMenu.utilities.versions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubchapterPdfFinder implements VersionFinder {
    private final String REGEX = "https_[\\d]+_\\d";
    private final Pattern pattern = Pattern.compile(REGEX);

    @Override
    public String find(String path) {
        final Matcher matcher = pattern.matcher(path);
        if(matcher.find()) {
            return matcher.group().substring(matcher.group().lastIndexOf("https") + 6, matcher.group().length() - 2);
        }

        return "";
    }
}
