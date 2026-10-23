import model.InvalidParcelDataException;
import model.Parcel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the Parcel class.
 */
public class ParcelTest {

    // --- Valid construction ---

    @Test
    public void testValidParcelConstruction() throws InvalidParcelDataException {
        Parcel p = new Parcel("P001", 2.5, 30.0, 20.0, 15.0, 3);
        assertEquals("P001", p.getId());
        assertEquals(2.5, p.getWeight(), 0.001);
        assertEquals(30.0, p.getLength(), 0.001);
        assertEquals(20.0, p.getWidth(), 0.001);
        assertEquals(15.0, p.getHeight(), 0.001);
        assertEquals(3, p.getDaysInDepot());
        assertFalse(p.isCollected());
    }

    @Test
    public void testZeroDaysInDepotIsValid() throws InvalidParcelDataException {
        Parcel p = new Parcel("P002", 1.0, 10.0, 10.0, 10.0, 0);
        assertEquals(0, p.getDaysInDepot());
    }

    // --- Invalid construction ---

    @Test
    public void testNullIdThrows() {
        assertThrows(InvalidParcelDataException.class, () ->
                new Parcel(null, 1.0, 10.0, 10.0, 10.0, 0));
    }

    @Test
    public void testBlankIdThrows() {
        assertThrows(InvalidParcelDataException.class, () ->
                new Parcel("  ", 1.0, 10.0, 10.0, 10.0, 0));
    }

    @Test
    public void testZeroWeightThrows() {
        assertThrows(InvalidParcelDataException.class, () ->
                new Parcel("P003", 0.0, 10.0, 10.0, 10.0, 0));
    }

    @Test
    public void testNegativeWeightThrows() {
        assertThrows(InvalidParcelDataException.class, () ->
                new Parcel("P003", -1.0, 10.0, 10.0, 10.0, 0));
    }

    @Test
    public void testZeroLengthThrows() {
        assertThrows(InvalidParcelDataException.class, () ->
                new Parcel("P003", 1.0, 0.0, 10.0, 10.0, 0));
    }

    @Test
    public void testZeroWidthThrows() {
        assertThrows(InvalidParcelDataException.class, () ->
                new Parcel("P003", 1.0, 10.0, 0.0, 10.0, 0));
    }

    @Test
    public void testZeroHeightThrows() {
        assertThrows(InvalidParcelDataException.class, () ->
                new Parcel("P003", 1.0, 10.0, 10.0, 0.0, 0));
    }

    @Test
    public void testNegativeDaysThrows() {
        assertThrows(InvalidParcelDataException.class, () ->
                new Parcel("P003", 1.0, 10.0, 10.0, 10.0, -1));
    }

    // --- Volume ---

    @Test
    public void testVolume() throws InvalidParcelDataException {
        Parcel p = new Parcel("P004", 1.0, 10.0, 5.0, 2.0, 0);
        assertEquals(100.0, p.getVolume(), 0.001);
    }

    // --- Setters ---

    @Test
    public void testSetCollected() throws InvalidParcelDataException {
        Parcel p = new Parcel("P005", 1.0, 10.0, 10.0, 10.0, 0);
        p.setCollected(true);
        assertTrue(p.isCollected());
    }

    @Test
    public void testSetDaysInDepot() throws InvalidParcelDataException {
        Parcel p = new Parcel("P006", 1.0, 10.0, 10.0, 10.0, 0);
        p.setDaysInDepot(5);
        assertEquals(5, p.getDaysInDepot());
    }
}
