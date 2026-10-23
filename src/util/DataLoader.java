package util;

import model.Customer;
import model.InvalidParcelDataException;
import model.Parcel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads {@link Parcel} and {@link Customer} data from comma-separated text files.
 *
 * <h3>Parcel file format</h3>
 * <p>Each line: {@code id,weight,length,width,height,daysInDepot}</p>
 * <ul>
 *   <li>id – String</li>
 *   <li>weight – double (kg)</li>
 *   <li>length, width, height – double (cm)</li>
 *   <li>daysInDepot – int</li>
 * </ul>
 * <p>Blank lines and lines beginning with {@code #} are ignored.</p>
 *
 * <h3>Customer file format</h3>
 * <p>Each line: {@code seqNo,name,parcelId}</p>
 * <ul>
 *   <li>seqNo – int</li>
 *   <li>name – String</li>
 *   <li>parcelId – String</li>
 * </ul>
 * <p>Blank lines and lines beginning with {@code #} are ignored.</p>
 */
public class DataLoader {

    /**
     * Reads parcels from the file at {@code filePath}.
     *
     * @param filePath path to the parcels CSV file; must not be null or blank
     * @return list of successfully parsed Parcel objects; never null
     * @throws IOException if the file cannot be read
     */
    public List<Parcel> loadParcels(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or blank.");
        }
        List<Parcel> parcels = new ArrayList<Parcel>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    // skip malformed lines silently
                    continue;
                }
                try {
                    String id = parts[0].trim();
                    double weight = Double.parseDouble(parts[1].trim());
                    double length = Double.parseDouble(parts[2].trim());
                    double width  = Double.parseDouble(parts[3].trim());
                    double height = Double.parseDouble(parts[4].trim());
                    int daysInDepot = Integer.parseInt(parts[5].trim());
                    Parcel parcel = new Parcel(id, weight, length, width, height, daysInDepot);
                    parcels.add(parcel);
                } catch (NumberFormatException e) {
                    // skip malformed numeric fields
                } catch (InvalidParcelDataException e) {
                    // skip invalid parcel data
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return parcels;
    }

    /**
     * Reads customers from the file at {@code filePath}.
     *
     * @param filePath path to the customers CSV file; must not be null or blank
     * @return list of successfully parsed Customer objects; never null
     * @throws IOException if the file cannot be read
     */
    public List<Customer> loadCustomers(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or blank.");
        }
        List<Customer> customers = new ArrayList<Customer>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",", 3);
                if (parts.length < 3) {
                    continue;
                }
                try {
                    int seqNo = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String parcelId = parts[2].trim();
                    Customer customer = new Customer(seqNo, name, parcelId);
                    customers.add(customer);
                } catch (NumberFormatException e) {
                    // skip malformed numeric fields
                } catch (IllegalArgumentException e) {
                    // skip invalid customer data
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return customers;
    }
}
