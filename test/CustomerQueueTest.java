import model.Customer;
import model.CustomerQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the CustomerQueue class.
 */
public class CustomerQueueTest {

    private CustomerQueue queue;

    @BeforeEach
    public void setUp() {
        queue = new CustomerQueue();
    }

    @Test
    public void testEmptyOnConstruction() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void testAddAndPoll() {
        Customer c1 = new Customer(1, "Alice", "P001");
        Customer c2 = new Customer(2, "Bob", "P002");
        queue.addCustomer(c1);
        queue.addCustomer(c2);
        assertEquals(2, queue.size());
        // FIFO: c1 should come out first
        Customer out = queue.nextCustomer();
        assertEquals(c1, out);
        assertEquals(1, queue.size());
    }

    @Test
    public void testPollEmptyReturnsNull() {
        assertNull(queue.nextCustomer());
    }

    @Test
    public void testPeekDoesNotRemove() {
        Customer c = new Customer(1, "Carol", "P003");
        queue.addCustomer(c);
        Customer peeked = queue.peekNextCustomer();
        assertEquals(c, peeked);
        assertEquals(1, queue.size());
    }

    @Test
    public void testAddNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> queue.addCustomer(null));
    }

    @Test
    public void testGetQueueSnapshot() {
        queue.addCustomer(new Customer(1, "Alice", "P001"));
        queue.addCustomer(new Customer(2, "Bob", "P002"));
        // Snapshot does not modify original queue
        queue.getQueueSnapshot();
        assertEquals(2, queue.size());
    }
}
