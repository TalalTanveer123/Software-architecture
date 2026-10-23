package controller;

import model.Customer;
import model.CustomerQueue;
import model.Log;
import model.Parcel;
import model.ParcelMap;
import model.ProcessingResult;

/**
 * Calculates the collection fee for a {@link Parcel} and processes the next
 * customer in a {@link CustomerQueue} against a {@link ParcelMap}.
 *
 * <h3>Fee Calculation Formula</h3>
 * <pre>
 *   baseWeight   = weight (kg) * 0.50
 *   baseVolume   = volume (cm³) / 1000 * 0.25
 *   baseTime     = daysInDepot * 0.10
 *   subtotal     = baseWeight + baseVolume + baseTime
 *   if parcel ID starts with 'X' (case-insensitive): apply 10% discount
 *   finalFee     = subtotal * (1.0 - discount)
 * </pre>
 *
 * <p>The minimum fee is £0.00 (fee is never negative).</p>
 */
public class Worker {

    /**
     * Calculates the collection fee for the given parcel using the formula above.
     *
     * @param parcel the parcel for which to calculate the fee; must not be null
     * @return the collection fee in pounds, rounded to two decimal places
     * @throws IllegalArgumentException if parcel is null
     */
    public double calculateFee(Parcel parcel) {
        if (parcel == null) {
            throw new IllegalArgumentException("Parcel must not be null.");
        }
        double baseWeight = parcel.getWeight() * 0.50;
        double baseVolume = (parcel.getVolume() / 1000.0) * 0.25;
        double baseTime   = parcel.getDaysInDepot() * 0.10;
        double subtotal   = baseWeight + baseVolume + baseTime;

        double discount = 0.0;
        if (parcel.getId().toUpperCase().startsWith("X")) {
            discount = 0.10;
        }

        double fee = subtotal * (1.0 - discount);
        if (fee < 0.0) {
            fee = 0.0;
        }
        // Round to 2 decimal places
        fee = Math.round(fee * 100.0) / 100.0;
        return fee;
    }

    /**
     * Processes the next customer in {@code queue} against {@code parcelMap}.
     *
     * <p>Processing steps:</p>
     * <ol>
     *   <li>If the queue is empty, return a failure result with an appropriate message.</li>
     *   <li>Remove the next customer from the front of the queue.</li>
     *   <li>Look up the customer's desired parcel in {@code parcelMap}.</li>
     *   <li>If the parcel is not found, log an error and return a failure result.</li>
     *   <li>If the parcel has already been collected, log an error and return a failure
     *       result.</li>
     *   <li>Calculate the collection fee.</li>
     *   <li>Mark the parcel as collected.</li>
     *   <li>Log the successful transaction.</li>
     *   <li>Return a success result carrying the customer, parcel and fee.</li>
     * </ol>
     *
     * <p>Every significant outcome (success or error) must be recorded to the
     * shared {@link Log}.</p>
     *
     * @param queue      the customer queue to draw from; must not be null
     * @param parcelMap  the depot's parcel store; must not be null
     * @return a {@link ProcessingResult} describing the outcome
     */
    public ProcessingResult processNextCustomer(CustomerQueue queue, ParcelMap parcelMap) {
        if (queue == null || parcelMap == null) {
            throw new IllegalArgumentException("Queue and parcelMap must not be null.");
        }

        Log log = Log.getInstance();

        if (queue.isEmpty()) {
            String msg = "No customers in queue to process.";
            log.logEvent("ERROR: " + msg);
            return new ProcessingResult(false, null, null, 0.0, msg);
        }

        Customer customer = queue.nextCustomer();
        String parcelId = customer.getParcelId();
        Parcel parcel = parcelMap.getParcel(parcelId);

        if (parcel == null) {
            String msg = "Parcel not found: " + parcelId + " for customer " + customer.getName();
            log.logEvent("ERROR: " + msg);
            return new ProcessingResult(false, customer, null, 0.0, msg);
        }

        if (parcel.isCollected()) {
            String msg = "Parcel " + parcelId + " has already been collected. Customer: " + customer.getName();
            log.logEvent("ERROR: " + msg);
            return new ProcessingResult(false, customer, parcel, 0.0, msg);
        }

        double fee = calculateFee(parcel);
        parcel.setCollected(true);

        String msg = "Customer " + customer.getName() + " (seq#" + customer.getSeqNo()
                + ") collected parcel " + parcelId + ". Fee: £" + String.format("%.2f", fee);
        log.logEvent("SUCCESS: " + msg);

        return new ProcessingResult(true, customer, parcel, fee, msg);
    }
}
