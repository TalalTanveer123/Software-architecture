import model.Customer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the Customer class.
 */
public class CustomerTest {

    @Test
    public void testValidConstruction() {
        Customer c = new Customer(1, "Alice", "P001");
        assertEquals(1, c.getSeqNo());
        assertEquals("Alice", c.getName());
        assertEquals("P001", c.getParcelId());
    }

    @Test
    public void testZeroSeqNoThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(0, "Alice", "P001"));
    }

    @Test
    public void testNegativeSeqNoThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(-1, "Alice", "P001"));
    }

    @Test
    public void testNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(1, null, "P001"));
    }

    @Test
    public void testBlankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(1, "  ", "P001"));
    }

    @Test
    public void testNullParcelIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(1, "Alice", null));
    }

    @Test
    public void testBlankParcelIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Customer(1, "Alice", "  "));
    }

    @Test
    public void testToStringContainsName() {
        Customer c = new Customer(2, "Bob", "P002");
        assertTrue(c.toString().contains("Bob"));
    }
}
