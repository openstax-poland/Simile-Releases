package webviewselenium.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import webviewselenium.BookService;

public class BookServiceTest {

    @Test
    public void returnsListForSupportedServer() {
        assertTrue(!BookService.getBooksAvailableOnTheServerByServerUrl("https://openstax.org/").isEmpty());
    }

    @Test
    public void returnsNullForUnsupportedServer() {
        assertEquals(BookService.getBooksAvailableOnTheServerByServerUrl("Unsupported server"), null);
    }
}
