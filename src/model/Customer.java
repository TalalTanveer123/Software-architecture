package model;

/**
 * Domain class representing a customer waiting to collect a parcel.
 *
 * <p>A Customer has a queue sequence number (assigned when they join the queue),
 * a name, and the ID of the parcel they wish to collect.</p>
 *
 * <p>The constructor validates its arguments:</p>
 * <ul>
 *   <li>seqNo must be &gt; 0.</li>
 *   <li>name must be non-null and non-blank.</li>
 *   <li>parcelId must be non-null and non-blank.</li>
 * </ul>
 *
 * <p>If any argument is invalid the constructor throws
 * {@link IllegalArgumentException}.</p>
 */
public class Customer {

    private int seqNo;
    private String name;
    private String parcelId;

    /**
     * Constructs a new Customer.
     *
     * @param seqNo    queue sequence number; must be &gt; 0
     * @param name     customer's name; must be non-null and non-blank
     * @param parcelId ID of the parcel they wish to collect; must be non-null and non-blank
     * @throws IllegalArgumentException if any argument fails validation
     */
    public Customer(int seqNo, String name, String parcelId) {
        if (seqNo <= 0) {
            throw new IllegalArgumentException("Sequence number must be > 0. Got: " + seqNo);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name must be non-null and non-blank.");
        }
        if (parcelId == null || parcelId.trim().isEmpty()) {
            throw new IllegalArgumentException("Parcel ID must be non-null and non-blank.");
        }
        this.seqNo = seqNo;
        this.name = name.trim();
        this.parcelId = parcelId.trim();
    }

    /** @return the queue sequence number */
    public int getSeqNo() {
        return seqNo;
    }

    /** @return the customer's name */
    public String getName() {
        return name;
    }

    /** @return the ID of the parcel this customer wishes to collect */
    public String getParcelId() {
        return parcelId;
    }

    /**
     * Returns a string representation of the customer.
     */
    @Override
    public String toString() {
        return "Customer{seqNo=" + seqNo + ", name='" + name + "', parcelId='" + parcelId + "'}";
    }
}
