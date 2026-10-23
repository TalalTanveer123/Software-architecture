package model;

/**
 * Small immutable value class describing the outcome of processing one customer.
 *
 * <p>A ProcessingResult is created by {@code Worker.processNextCustomer()} and
 * carries:</p>
 * <ul>
 *   <li>a success/failure flag,</li>
 *   <li>the {@link Customer} who was processed,</li>
 *   <li>the {@link Parcel} that was found (may be {@code null} on failure),</li>
 *   <li>the collection fee calculated (0.0 on failure),</li>
 *   <li>a human-readable message describing what happened.</li>
 * </ul>
 *
 * <p>All fields are set in the constructor and exposed only through getters;
 * there are no setters (immutable).</p>
 */
public class ProcessingResult {

    private final boolean success;
    private final Customer customer;
    private final Parcel parcel;
    private final double fee;
    private final String message;

    /**
     * Constructs a new ProcessingResult.
     *
     * @param success  {@code true} if the parcel was found and released successfully
     * @param customer the customer who was at the front of the queue
     * @param parcel   the parcel retrieved (may be {@code null} on failure)
     * @param fee      the collection fee charged (0.0 on failure)
     * @param message  a human-readable summary of the outcome
     */
    public ProcessingResult(boolean success, Customer customer, Parcel parcel,
                            double fee, String message) {
        this.success = success;
        this.customer = customer;
        this.parcel = parcel;
        this.fee = fee;
        this.message = message;
    }

    /** @return {@code true} if the parcel was successfully found and released */
    public boolean isSuccess() {
        return success;
    }

    /** @return the customer who was processed */
    public Customer getCustomer() {
        return customer;
    }

    /** @return the parcel retrieved, or {@code null} if not found */
    public Parcel getParcel() {
        return parcel;
    }

    /** @return the collection fee charged; 0.0 on failure */
    public double getFee() {
        return fee;
    }

    /** @return a human-readable description of the processing outcome */
    public String getMessage() {
        return message;
    }

    /**
     * Returns a string representation suitable for logging.
     */
    @Override
    public String toString() {
        return "ProcessingResult{success=" + success
                + ", customer=" + (customer != null ? customer.getName() : "null")
                + ", parcel=" + (parcel != null ? parcel.getId() : "null")
                + ", fee=" + String.format("%.2f", fee)
                + ", message='" + message + "'}";
    }
}
