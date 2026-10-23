import controller.DepotController;
import model.InvalidParcelDataException;
import model.Log;
import model.ProcessingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the DepotController class.
 */
public class DepotControllerTest {

    private DepotController controller;

    @BeforeEach
    public void setUp() throws Exception {
        java.lang.reflect.Method reset = Log.class.getDeclaredMethod("resetInstance");
        reset.setAccessible(true);
        reset.invoke(null);

        controller = new DepotController();
    }

    @Test
    public void testAddParcelAndLookUp() throws InvalidParcelDataException {
        controller.addParcel("P001", 2.0, 10.0, 10.0, 10.0, 3);
        assertNotNull(controller.lookUpParcel("P001"));
    }

    @Test
    public void testLookUpMissingParcelReturnsNull() {
        assertNull(controller.lookUpParcel("MISSING"));
    }

    @Test
    public void testAddCustomerAndCheckQueue() {
        controller.addCustomer("Alice", "P001");
        assertFalse(controller.isQueueEmpty());
        assertEquals(1, controller.getQueueSnapshot().size());
    }

    @Test
    public void testProcessNextCustomerSuccess() throws InvalidParcelDataException {
        controller.addParcel("P001", 2.0, 10.0, 10.0, 10.0, 3);
        controller.addCustomer("Alice", "P001");
        ProcessingResult result = controller.processNextCustomer();
        assertTrue(result.isSuccess());
        assertTrue(controller.isQueueEmpty());
    }

    @Test
    public void testProcessNextCustomerEmptyQueue() {
        ProcessingResult result = controller.processNextCustomer();
        assertFalse(result.isSuccess());
    }

    @Test
    public void testGetLastResult() throws InvalidParcelDataException {
        controller.addParcel("P002", 1.0, 5.0, 5.0, 5.0, 0);
        controller.addCustomer("Bob", "P002");
        assertNull(controller.getLastResult());
        controller.processNextCustomer();
        assertNotNull(controller.getLastResult());
    }

    @Test
    public void testGetParcelsAwaitingCollection() throws InvalidParcelDataException {
        controller.addParcel("P001", 2.0, 10.0, 10.0, 10.0, 3);
        controller.addParcel("P002", 1.0, 5.0, 5.0, 5.0, 0);
        assertEquals(2, controller.getParcelsAwaitingCollection().size());
    }

    @Test
    public void testListenerNotified() throws InvalidParcelDataException {
        final boolean[] notified = {false};
        controller.addDepotListener(new DepotController.DepotListener() {
            public void onDepotUpdated() {
                notified[0] = true;
            }
        });
        controller.addParcel("P001", 2.0, 10.0, 10.0, 10.0, 0);
        assertTrue(notified[0]);
    }

    @Test
    public void testInvalidParcelThrows() {
        assertThrows(InvalidParcelDataException.class,
                () -> controller.addParcel("", 0.0, 0.0, 0.0, 0.0, -1));
    }
}
