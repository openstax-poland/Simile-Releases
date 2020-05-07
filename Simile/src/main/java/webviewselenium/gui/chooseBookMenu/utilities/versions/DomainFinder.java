package webviewselenium.gui.chooseBookMenu.utilities.versions;

public class DomainFinder implements VersionFinder {

    @Override
    public String find(String path) {
        return path.substring(path.lastIndexOf("https_++"), path.lastIndexOf(".org"));
    }
}
