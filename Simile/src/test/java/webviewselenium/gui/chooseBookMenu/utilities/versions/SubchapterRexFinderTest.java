package webviewselenium.gui.chooseBookMenu.utilities.versions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubchapterRexFinderTest {
    private VersionFinder versionFinder = new SubchapterRexFinder();

    @Test
    public void findSubchapterHashFromCnxFormat() {
        String rexImagePath = "https_++cnx.org+contents+u2KTPvIK@3.38_7mxvEBVi@2+Wst_C4_99p_1.png";
        String expectedSubchaptername = "";
        assertEquals(versionFinder.find(rexImagePath), expectedSubchaptername);
    }

    @Test
    public void findSubchapterHashFromRexFormat() {
        String rexImagePath = "https_++staging.openstax.org+books+astronomy+pages+1-1-the-nature-of-astronomy_1.png";
        String expectedSubchaptername = "1-1-the-nature-of-astronomy";
        assertEquals(versionFinder.find(rexImagePath), expectedSubchaptername);
    }
}
