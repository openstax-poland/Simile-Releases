package webviewselenium.gui.chooseBookMenu.books;

import webviewselenium.gui.chooseBookMenu.BookChooser;
import webviewselenium.gui.chooseBookMenu.BookChooserImpl;
import webviewselenium.gui.chooseBookMenu.utilities.versions.*;
import webviewselenium.compareImages.database.FileExplorer;
import webviewselenium.constans.SharedConstants;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class contains the necessary properties to collect information about the set of compared book pages.
 */
public class ScannedBookProperties {
    private BookChooser bookChooser;
    private VersionFinder versionFinder;

    private String scanIndex;
    private File scanPath;
    private Integer numberOfScansInPath;
    private List<String> images;
    private List<String> subchapters;
    private String domainName;

    public ScannedBookProperties(String rawBookDescription) {
        this.bookChooser = new BookChooserImpl();
        this.scanIndex = bookChooser.getBookIndex(rawBookDescription);
        this.scanPath = new File(SharedConstants.fullNameOfDirectoryThatContainsScans + File.separator + this.scanIndex + File.separator);
        this.numberOfScansInPath = this.scanPath.listFiles().length;
        this.images = FileExplorer.findAllPNGs(getScanPath(), getScanPath().getPath());
    }

    public BookChooser getBookChooser() {
        return bookChooser;
    }

    public void setBookChooser(BookChooser bookChooser) {
        this.bookChooser = bookChooser;
    }

    public String getScanIndex() {
        return scanIndex;
    }

    public void setScanIndex(String scanIndex) {
        this.scanIndex = scanIndex;
    }

    public File getScanPath() {
        return scanPath;
    }

    public void setScanPath(File scanPath) {
        this.scanPath = scanPath;
    }

    public Integer getNumberOfScansInPath() {
        return numberOfScansInPath;
    }

    public void setNumberOfScansInPath(Integer numberOfScansInPath) {
        this.numberOfScansInPath = numberOfScansInPath;
    }

    public List<String> getImages() {
        return images.stream()
                .filter(image -> image.contains(scanPath.getPath() + File.separator))
                .sorted()
                .collect(Collectors.toList());
    }

    public void setImages(List<String> images) { this.images = images; }

    public String getDomainName() {
        versionFinder = new DomainFinder();
        return versionFinder.find(this.images.get(0));
    }

    public void setSubchapters(List<String> subchapters) {
        this.subchapters = subchapters;
    }

    public List<String> getCnxSubchapters() {
        versionFinder = new SubchapterHashFinder();
        return getImages().stream()
                .map(image -> versionFinder.find(image))
                .filter(subchapter -> subchapter.length() == 8)
                .collect(Collectors.toList());
    }

    public List<String> getRexSubchapters() {
        versionFinder = new SubchapterRexFinder();
        return getImages().stream()
                .map(image -> versionFinder.find(image))
                .collect(Collectors.toList());
    }

    public String getCnxSubchapter(String path) {
        versionFinder = new SubchapterHashFinder();
        return versionFinder.find(path);
    }

    public String getRexSubchapter(String path) {
        versionFinder = new SubchapterRexFinder();
        return versionFinder.find(path);
    }

    public String getPdfSubchapter(String path) {
        versionFinder = new SubchapterPdfFinder();
        return versionFinder.find(path);
    }

    public List<String> removeDifferentCnxSubchapters(List<String> subchaptersThatShouldNotBeDeleted) {
        return getCnxSubchapters().stream()
                .filter(subchapter -> subchaptersThatShouldNotBeDeleted.contains(subchapter))
                .collect(Collectors.toList());
    }

    public List<String> removeDifferentRexSubchapters(List<String> subchaptersThatShouldNotBeDeleted) {
        return getRexSubchapters().stream()
                .filter(subchapter -> subchaptersThatShouldNotBeDeleted.contains(subchapter))
                .collect(Collectors.toList());
    }

   /* public List<String> removeDifferentPdfSubchapters(List<String> subchaptersThatShouldNotBeDeleted) {

    }*/

    public List<String> removeUnusedCnxPaths(List<String> pathsThatShouldNotBeDeleted) {
        return getImages().stream()
                .filter(path -> pathsThatShouldNotBeDeleted.contains(getCnxSubchapter(path)))
                .collect(Collectors.toList());
    }

    public List<String> removeUnusedRexPaths(List<String> pathsThatShouldNotBeDeleted) {
        return getImages().stream()
                .filter(path -> pathsThatShouldNotBeDeleted.contains(getRexSubchapter(path)))
                .collect(Collectors.toList());
    }

    public boolean isFromCnx() {
        if (getCnxSubchapters().size() > 0)
            return true;
        return false;
    }

    public boolean isFromRex() {
        if (getRexSubchapters().size() > 0)
            return true;
        return false;
    }
}
