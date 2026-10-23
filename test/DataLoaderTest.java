import model.Log;
import util.DataLoader;
import model.Parcel;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the DataLoader class.
 */
public class DataLoaderTest {

    private DataLoader loader;

    @BeforeEach
    public void setUp() throws Exception {
        loader = new DataLoader();
        // Reset Log
        java.lang.reflect.Method reset = Log.class.getDeclaredMethod("resetInstance");
        reset.setAccessible(true);
        reset.invoke(null);
    }

    @Test
    public void testLoadParcels() throws IOException {
        File tmp = File.createTempFile("parcels_test", ".txt");
        tmp.deleteOnExit();
        FileWriter fw = new FileWriter(tmp);
        fw.write("# comment\n");
        fw.write("P001,2.5,30.0,20.0,15.0,3\n");
        fw.write("X001,4.5,35.0,28.0,22.0,10\n");
        fw.write("\n");
        fw.write("P002,1.0,10.0,10.0,10.0,0\n");
        fw.close();

        List<Parcel> parcels = loader.loadParcels(tmp.getAbsolutePath());
        assertEquals(3, parcels.size());
        assertEquals("P001", parcels.get(0).getId());
        assertEquals(2.5, parcels.get(0).getWeight(), 0.001);
        assertEquals("X001", parcels.get(1).getId());
    }

    @Test
    public void testLoadCustomers() throws IOException {
        File tmp = File.createTempFile("customers_test", ".txt");
        tmp.deleteOnExit();
        FileWriter fw = new FileWriter(tmp);
        fw.write("# comment\n");
        fw.write("1,Alice Johnson,P001\n");
        fw.write("2,Bob Smith,X001\n");
        fw.close();

        List<Customer> customers = loader.loadCustomers(tmp.getAbsolutePath());
        assertEquals(2, customers.size());
        assertEquals("Alice Johnson", customers.get(0).getName());
        assertEquals("P001", customers.get(0).getParcelId());
        assertEquals(2, customers.get(1).getSeqNo());
    }

    @Test
    public void testSkipsMalformedLines() throws IOException {
        File tmp = File.createTempFile("parcels_bad", ".txt");
        tmp.deleteOnExit();
        FileWriter fw = new FileWriter(tmp);
        fw.write("BADLINE\n");
        fw.write("P001,2.5,30.0,20.0,15.0,3\n");
        fw.close();

        List<Parcel> parcels = loader.loadParcels(tmp.getAbsolutePath());
        assertEquals(1, parcels.size());
    }

    @Test
    public void testBlankFilePathThrows() {
        assertThrows(IllegalArgumentException.class, () -> loader.loadParcels(""));
        assertThrows(IllegalArgumentException.class, () -> loader.loadCustomers(""));
    }
}
