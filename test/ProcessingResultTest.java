import model.Customer;
import model.InvalidParcelDataException;
import model.Parcel;
import model.ProcessingResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the ProcessingResult class.
 */
public class ProcessingResultTest {

    @Test
    public void testSuccessResult() throws InvalidParcelDataException {
        Customer c = new Customer(1, "Alice", "P001");
        Parcel p = new Parcel("P001", 2.0, 10.0, 10.0, 10.0, 3);
        ProcessingResult result = new ProcessingResult(true, c, p, 5.50, "Success");

        assertTrue(result.isSuccess());
        assertEquals(c, result.getCustomer());
        assertEquals(p, result.getParcel());
        assertEquals(5.50, result.getFee(), 0.001);
        assertEquals("Success", result.getMessage());
    }

    @Test
    public void testFailureResult() {
        Customer c = new Customer(2, "Bob", "P999");
        ProcessingResult result = new ProcessingResult(false, c, null, 0.0, "Parcel not found");

        assertFalse(result.isSuccess());
        assertNull(result.getParcel());
        assertEquals(0.0, result.getFee(), 0.001);
    }

    @Test
    public void testToStringContainsMessage() throws InvalidParcelDataException {
        ProcessingResult result = new ProcessingResult(true,
                new Customer(1, "Alice", "P001"),
                new Parcel("P001", 1.0, 10.0, 10.0, 10.0, 0),
                1.5, "OK");
        assertTrue(result.toString().contains("success=true"));
    }
}
