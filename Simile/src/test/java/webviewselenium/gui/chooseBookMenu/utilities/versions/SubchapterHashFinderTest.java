package webviewselenium.gui.chooseBookMenu.utilities.versions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubchapterHashFinderTest {
    private VersionFinder versionFinder = new SubchapterHashFinder();

    @Test
    public void findSubchapterHashFromCnxFormat() {
        String rexImagePath = "https_++cnx.org+contents+u2KTPvIK@3.38_7mxvEBVi@2+Wst_C4_99p_1.png";
        String expectedSubchaptername = "7mxvEBVi";
        assertEquals(versionFinder.find(rexImagePath), expectedSubchaptername);
    }

    @Test
    public void findSubchapterHashFromCnxFormatThatContains_() {
        String rexImagePath = "https_++katalyst01.cnx.org+contents+TqqPA4io@1.153_gge_5CDx@3+11-1-Toczenie-si_C4_99-cia_C5_82_1.png";
        String expectedSubchaptername = "gge_5CDx";
        assertEquals(versionFinder.find(rexImagePath), expectedSubchaptername);
    }

    @Test
    public void findSubchapterHashFromRexFormat() {
        String rexImagePath = "https_++staging.openstax.org+books+astronomy+pages+1-1-the-nature-of-astronomy_1.png";
        String expectedSubchaptername = "";
        assertEquals(versionFinder.find(rexImagePath), expectedSubchaptername);
    }
}
