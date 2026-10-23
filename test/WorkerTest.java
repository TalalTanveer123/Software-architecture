import controller.Worker;
import model.Customer;
import model.CustomerQueue;
import model.InvalidParcelDataException;
import model.Log;
import model.Parcel;
import model.ParcelMap;
import model.ProcessingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the Worker class.
 */
public class WorkerTest {

    private Worker worker;
    private CustomerQueue queue;
    private ParcelMap parcelMap;

    @BeforeEach
    public void setUp() throws Exception {
        // Reset Log singleton
        java.lang.reflect.Method reset = Log.class.getDeclaredMethod("resetInstance");
        reset.setAccessible(true);
        reset.invoke(null);

        worker = new Worker();
        queue = new CustomerQueue();
        parcelMap = new ParcelMap();
    }

    // --- Fee calculation ---

    @Test
    public void testFeeNormalParcel() throws InvalidParcelDataException {
        // weight=2, vol=10*10*10=1000cm³, days=3
        // baseWeight = 2*0.5 = 1.0
        // baseVolume = (1000/1000)*0.25 = 0.25
        // baseTime = 3*0.1 = 0.3
        // subtotal = 1.55, no discount
        // fee = 1.55
        Parcel p = new Parcel("P001", 2.0, 10.0, 10.0, 10.0, 3);
        double fee = worker.calculateFee(p);
        assertEquals(1.55, fee, 0.01);
    }

    @Test
    public void testFeeXParcelHasDiscount() throws InvalidParcelDataException {
        // id starts with X -> 10% discount
        Parcel p = new Parcel("X001", 2.0, 10.0, 10.0, 10.0, 3);
        double normalFee = worker.calculateFee(new Parcel("P001", 2.0, 10.0, 10.0, 10.0, 3));
        double discFee = worker.calculateFee(p);
        assertTrue(discFee < normalFee);
        assertEquals(normalFee * 0.9, discFee, 0.01);
    }

    @Test
    public void testFeeNullParcelThrows() {
        assertThrows(IllegalArgumentException.class, () -> worker.calculateFee(null));
    }

    // --- processNextCustomer ---

    @Test
    public void testProcessSuccessful() throws InvalidParcelDataException {
        Parcel p = new Parcel("P001", 2.0, 10.0, 10.0, 10.0, 3);
        parcelMap.addParcel(p);
        queue.addCustomer(new Customer(1, "Alice", "P001"));

        ProcessingResult result = worker.processNextCustomer(queue, parcelMap);

        assertTrue(result.isSuccess());
        assertEquals("Alice", result.getCustomer().getName());
        assertEquals("P001", result.getParcel().getId());
        assertTrue(result.getFee() > 0);
        assertTrue(p.isCollected());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testProcessEmptyQueueReturnsFailure() {
        ProcessingResult result = worker.processNextCustomer(queue, parcelMap);
        assertFalse(result.isSuccess());
        assertNull(result.getCustomer());
    }

    @Test
    public void testProcessParcelNotFound() {
        queue.addCustomer(new Customer(1, "Bob", "MISSING"));
        ProcessingResult result = worker.processNextCustomer(queue, parcelMap);
        assertFalse(result.isSuccess());
        assertNull(result.getParcel());
        assertEquals("Bob", result.getCustomer().getName());
    }

    @Test
    public void testProcessAlreadyCollectedParcel() throws InvalidParcelDataException {
        Parcel p = new Parcel("P002", 1.0, 5.0, 5.0, 5.0, 0);
        p.setCollected(true);
        parcelMap.addParcel(p);
        queue.addCustomer(new Customer(1, "Carol", "P002"));

        ProcessingResult result = worker.processNextCustomer(queue, parcelMap);
        assertFalse(result.isSuccess());
    }

    @Test
    public void testNullQueueThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> worker.processNextCustomer(null, parcelMap));
    }

    @Test
    public void testNullParcelMapThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> worker.processNextCustomer(queue, null));
    }
}
