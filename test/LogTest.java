import model.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the Log singleton class.
 */
public class LogTest {

    @BeforeEach
    public void setUp() throws Exception {
        // Reset the singleton before each test
        java.lang.reflect.Method reset = Log.class.getDeclaredMethod("resetInstance");
        reset.setAccessible(true);
        reset.invoke(null);
    }

    @Test
    public void testGetInstanceReturnsSameInstance() {
        Log a = Log.getInstance();
        Log b = Log.getInstance();
        assertSame(a, b);
    }

    @Test
    public void testLogEventAppendsText() {
        Log log = Log.getInstance();
        log.logEvent("Test event");
        assertTrue(log.getLogContents().contains("Test event"));
    }

    @Test
    public void testMultipleEvents() {
        Log log = Log.getInstance();
        log.logEvent("Event 1");
        log.logEvent("Event 2");
        String contents = log.getLogContents();
        assertTrue(contents.contains("Event 1"));
        assertTrue(contents.contains("Event 2"));
    }

    @Test
    public void testClearRemovesEntries() {
        Log log = Log.getInstance();
        log.logEvent("To be cleared");
        log.clear();
        assertEquals("", log.getLogContents());
    }

    @Test
    public void testWriteToFile() throws IOException {
        Log log = Log.getInstance();
        log.logEvent("Written event");
        File tmp = File.createTempFile("log_test", ".txt");
        tmp.deleteOnExit();
        log.writeToFile(tmp.getAbsolutePath());
        String content = new String(Files.readAllBytes(tmp.toPath()));
        assertTrue(content.contains("Written event"));
    }

    @Test
    public void testNullEventLoggedSafely() {
        Log log = Log.getInstance();
        assertDoesNotThrow(() -> log.logEvent(null));
    }
}
