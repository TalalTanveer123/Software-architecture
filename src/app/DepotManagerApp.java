package app;

import controller.DepotController;
import model.Customer;
import model.Log;
import model.Parcel;
import util.DataLoader;
import view.DepotManagerFrame;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Application entry point.
 *
 * <p>Wires up the model, controller and view, loads the initial data files,
 * and launches the Swing GUI on the Event Dispatch Thread.</p>
 */
public class DepotManagerApp {

    /** Default path to the parcels data file. */
    private static final String PARCELS_FILE = "data/parcels.txt";

    /** Default path to the customers data file. */
    private static final String CUSTOMERS_FILE = "data/customers.txt";

    /**
     * Application entry point.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Set Nimbus look-and-feel for a modern appearance on all platforms
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // fall back to default L&F
        }

        // Override UI manager defaults for a dark theme
        UIManager.put("OptionPane.background", new java.awt.Color(30, 32, 48));
        UIManager.put("Panel.background", new java.awt.Color(30, 32, 48));
        UIManager.put("OptionPane.messageForeground", new java.awt.Color(226, 232, 240));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // 1. Create the controller
                DepotController controller = new DepotController();

                // 2. Load initial data
                DataLoader loader = new DataLoader();

                File parcelsFile = new File(PARCELS_FILE);
                if (parcelsFile.exists()) {
                    try {
                        List<Parcel> parcels = loader.loadParcels(PARCELS_FILE);
                        for (int i = 0; i < parcels.size(); i++) {
                            controller.addParcelDirect(parcels.get(i));
                        }
                        Log.getInstance().logEvent("Loaded " + parcels.size() + " parcels from " + PARCELS_FILE);
                    } catch (Exception e) {
                        System.err.println("Could not load parcels: " + e.getMessage());
                    }
                } else {
                    System.out.println("Parcels file not found at: " + PARCELS_FILE + " – starting with empty depot.");
                    Log.getInstance().logEvent("No parcels file found – depot starts empty.");
                }

                File customersFile = new File(CUSTOMERS_FILE);
                if (customersFile.exists()) {
                    try {
                        List<Customer> customers = loader.loadCustomers(CUSTOMERS_FILE);
                        for (int i = 0; i < customers.size(); i++) {
                            controller.addCustomerDirect(customers.get(i));
                        }
                        Log.getInstance().logEvent("Loaded " + customers.size() + " customers from " + CUSTOMERS_FILE);
                    } catch (Exception e) {
                        System.err.println("Could not load customers: " + e.getMessage());
                    }
                } else {
                    System.out.println("Customers file not found at: " + CUSTOMERS_FILE + " – starting with empty queue.");
                    Log.getInstance().logEvent("No customers file found – queue starts empty.");
                }

                // 3. Create and show the main window
                DepotManagerFrame frame = new DepotManagerFrame(controller);
                frame.setVisible(true);
            }
        });
    }
}
