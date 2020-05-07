package webviewselenium.gui.chooseBookMenu.utilities.versions;

public interface VersionFinder {

    /**
     * Method allows to find version name of the requested book from the path, e.g. subchapter name, domain name, recipe version etc.
     * @return version name of the requested resource
     */
    public String find(String path);

}
