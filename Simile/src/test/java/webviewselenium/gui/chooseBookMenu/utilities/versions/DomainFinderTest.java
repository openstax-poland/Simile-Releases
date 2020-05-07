package webviewselenium.gui.chooseBookMenu.utilities.versions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DomainFinderTest {
    private VersionFinder versionFinder = new DomainFinder();

    @Test
    public void findBookDomainNameFromCnxFormat() {
        String rexImagePath = "https_++staging.cnx.org+contents+TqqPA4io@2.2_0_w1UYwa@2.2+Zadania-dodatkowe_1.png";
        String expectedDomain = "https_++staging.cnx";
        assertEquals(versionFinder.find(rexImagePath), expectedDomain);
    }

    @Test
    public void findBookDomainNameFromRexFormat() {
        String cnxImagePath = "https_++staging.openstax.org+books+astronomy+pages+1-1-the-nature-of-astronomy_1.png";
        String expectedDomain = "https_++staging.openstax";
        assertEquals(versionFinder.find(cnxImagePath), expectedDomain);
    }

}
