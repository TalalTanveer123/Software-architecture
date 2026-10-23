package controller;

import model.Customer;
import model.CustomerQueue;
import model.InvalidParcelDataException;
import model.Log;
import model.Parcel;
import model.ParcelMap;
import model.ProcessingResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 * The Controller in this application's MVC structure.
 *
 * <p>DepotController mediates between the model ({@link ParcelMap},
 * {@link CustomerQueue}, {@link Worker}) and any registered View classes.
 * It uses the Observer pattern via the nested {@link DepotListener} interface
 * to notify Views of changes without directly importing any view-package class.</p>
 *
 * <p>IMPORTANT: this class must never import or reference any class from the
 * {@code view} package.</p>
 */
public class DepotController {

    /**
     * Observer-pattern callback interface. View classes implement this interface
     * and register themselves via
     * {@link DepotController#addDepotListener(DepotListener)} to receive
     * notification whenever the model changes.
     */
    public interface DepotListener {
        /**
         * Called by the controller whenever the model state changes (e.g. a
         * customer is processed, a parcel is added, etc.).
         */
        void onDepotUpdated();
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private ParcelMap parcelMap;
    private CustomerQueue customerQueue;
    private Worker worker;
    private ProcessingResult lastResult;
    private int nextSeqNo;

    /** Registered listeners (Views) – kept as an ArrayList, not a lambda. */
    private List<DepotListener> listeners;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a DepotController with empty parcel map and customer queue.
     */
    public DepotController() {
        parcelMap = new ParcelMap();
        customerQueue = new CustomerQueue();
        worker = new Worker();
        lastResult = null;
        nextSeqNo = 1;
        listeners = new ArrayList<DepotListener>();
    }

    // -------------------------------------------------------------------------
    // Observer registration
    // -------------------------------------------------------------------------

    /**
     * Registers a listener to be notified when the model changes.
     *
     * @param listener the listener to register; must not be null
     */
    public void addDepotListener(DepotListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a previously registered listener.
     *
     * @param listener the listener to remove
     */
    public void removeDepotListener(DepotListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners of a model change.
     */
    private void notifyListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDepotUpdated();
        }
    }

    // -------------------------------------------------------------------------
    // Parcel operations
    // -------------------------------------------------------------------------

    /**
     * Adds a new parcel to the depot and logs the event.
     *
     * @param id          parcel ID
     * @param weight      weight in kg
     * @param length      length in cm
     * @param width       width in cm
     * @param height      height in cm
     * @param daysInDepot days in depot
     * @throws InvalidParcelDataException if the parcel data is invalid
     */
    public void addParcel(String id, double weight, double length, double width,
                          double height, int daysInDepot) throws InvalidParcelDataException {
        Parcel parcel = new Parcel(id, weight, length, width, height, daysInDepot);
        parcelMap.addParcel(parcel);
        Log.getInstance().logEvent("Parcel added: " + id);
        notifyListeners();
    }

    /**
     * Directly adds a pre-constructed {@link Parcel} to the depot.
     * Used by DataLoader when populating the initial data set.
     *
     * @param parcel the parcel to add
     */
    public void addParcelDirect(Parcel parcel) {
        parcelMap.addParcel(parcel);
        Log.getInstance().logEvent("Parcel loaded: " + parcel.getId());
        notifyListeners();
    }

    /**
     * Returns the parcel with the given ID, or {@code null} if not found.
     *
     * @param id parcel ID
     * @return the Parcel or null
     */
    public Parcel lookUpParcel(String id) {
        Parcel p = parcelMap.getParcel(id);
        if (p == null) {
            Log.getInstance().logEvent("Look-up failed – parcel not found: " + id);
        }
        return p;
    }

    /**
     * Returns all parcels currently in the depot that have NOT been collected,
     * as an unmodifiable list.
     *
     * @return list of uncollected parcels
     */
    public List<Parcel> getParcelsAwaitingCollection() {
        List<Parcel> result = new ArrayList<Parcel>();
        Collection<Parcel> all = parcelMap.getAllParcels();
        for (Parcel p : all) {
            if (!p.isCollected()) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Returns all parcels (collected and uncollected).
     *
     * @return all parcels
     */
    public Collection<Parcel> getAllParcels() {
        return parcelMap.getAllParcels();
    }

    // -------------------------------------------------------------------------
    // Customer / queue operations
    // -------------------------------------------------------------------------

    /**
     * Adds a new customer to the back of the collection queue and logs the event.
     *
     * @param name     customer name
     * @param parcelId ID of the parcel they wish to collect
     */
    public void addCustomer(String name, String parcelId) {
        Customer customer = new Customer(nextSeqNo++, name, parcelId);
        customerQueue.addCustomer(customer);
        Log.getInstance().logEvent("Customer joined queue: " + customer.getName()
                + " (seq#" + customer.getSeqNo() + ") wants parcel " + parcelId);
        notifyListeners();
    }

    /**
     * Directly adds a pre-constructed {@link Customer} to the queue.
     * Used by DataLoader when populating the initial data set.
     *
     * @param customer the customer to add
     */
    public void addCustomerDirect(Customer customer) {
        customerQueue.addCustomer(customer);
        if (customer.getSeqNo() >= nextSeqNo) {
            nextSeqNo = customer.getSeqNo() + 1;
        }
        Log.getInstance().logEvent("Customer loaded: " + customer.getName());
        notifyListeners();
    }

    /**
     * Returns a snapshot of the current customer queue contents (FIFO order)
     * without modifying the queue.
     *
     * @return queue snapshot
     */
    public Queue<Customer> getQueueSnapshot() {
        return customerQueue.getQueueSnapshot();
    }

    /**
     * Returns {@code true} if the customer queue is empty.
     *
     * @return true if no customers are waiting
     */
    public boolean isQueueEmpty() {
        return customerQueue.isEmpty();
    }

    // -------------------------------------------------------------------------
    // Processing
    // -------------------------------------------------------------------------

    /**
     * Processes the next customer in the queue and stores the result.
     * Notifies all registered listeners after processing.
     *
     * @return the {@link ProcessingResult} for this transaction
     */
    public ProcessingResult processNextCustomer() {
        lastResult = worker.processNextCustomer(customerQueue, parcelMap);
        notifyListeners();
        return lastResult;
    }

    /**
     * Returns the result of the most recent call to
     * {@link #processNextCustomer()}, or {@code null} if no customer has been
     * processed yet.
     *
     * @return the last processing result
     */
    public ProcessingResult getLastResult() {
        return lastResult;
    }

    // -------------------------------------------------------------------------
    // Accessors for model objects (needed by ReportWriter / DataLoader)
    // -------------------------------------------------------------------------

    /** @return the depot's parcel map */
    public ParcelMap getParcelMap() {
        return parcelMap;
    }

    /** @return the customer queue */
    public CustomerQueue getCustomerQueue() {
        return customerQueue;
    }
}
