import model.InvalidParcelDataException;
import model.Parcel;
import model.ParcelMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the ParcelMap class.
 */
public class ParcelMapTest {

    private ParcelMap map;

    @BeforeEach
    public void setUp() {
        map = new ParcelMap();
    }

    @Test
    public void testEmptyOnConstruction() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testAddAndRetrieve() throws InvalidParcelDataException {
        Parcel p = new Parcel("P001", 1.0, 10.0, 10.0, 10.0, 0);
        map.addParcel(p);
        assertFalse(map.isEmpty());
        assertEquals(1, map.size());
        assertEquals(p, map.getParcel("P001"));
    }

    @Test
    public void testContainsParcel() throws InvalidParcelDataException {
        Parcel p = new Parcel("P002", 1.0, 10.0, 10.0, 10.0, 0);
        map.addParcel(p);
        assertTrue(map.containsParcel("P002"));
        assertFalse(map.containsParcel("UNKNOWN"));
    }

    @Test
    public void testGetNonExistentReturnsNull() {
        assertNull(map.getParcel("MISSING"));
    }

    @Test
    public void testRemoveParcel() throws InvalidParcelDataException {
        Parcel p = new Parcel("P003", 1.0, 10.0, 10.0, 10.0, 0);
        map.addParcel(p);
        Parcel removed = map.removeParcel("P003");
        assertEquals(p, removed);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testAddNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> map.addParcel(null));
    }

    @Test
    public void testGetAllParcels() throws InvalidParcelDataException {
        map.addParcel(new Parcel("A", 1.0, 1.0, 1.0, 1.0, 0));
        map.addParcel(new Parcel("B", 2.0, 2.0, 2.0, 2.0, 1));
        assertEquals(2, map.getAllParcels().size());
    }
}
