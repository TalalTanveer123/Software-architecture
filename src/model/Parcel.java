package model;

/**
 * Domain class representing a parcel in the depot.
 *
 * <p>A Parcel has a unique string ID, a weight (kg), three dimensions
 * (length, width, height in cm), a count of the number of days it has been
 * sitting in the depot, and a boolean flag indicating whether it has already
 * been collected.</p>
 *
 * <p>The constructor validates every argument and throws
 * {@link InvalidParcelDataException} if any value is invalid:</p>
 * <ul>
 *   <li>id must be non-null and non-blank.</li>
 *   <li>weight must be &gt; 0.</li>
 *   <li>length, width, height must each be &gt; 0.</li>
 *   <li>daysInDepot must be &gt;= 0.</li>
 * </ul>
 */
public class Parcel {

    private String id;
    private double weight;
    private double length;
    private double width;
    private double height;
    private int daysInDepot;
    private boolean collected;

    /**
     * Constructs a new Parcel, validating all supplied data.
     *
     * @param id         unique parcel identifier; must be non-null and non-blank
     * @param weight     weight in kg; must be &gt; 0
     * @param length     length in cm; must be &gt; 0
     * @param width      width in cm; must be &gt; 0
     * @param height     height in cm; must be &gt; 0
     * @param daysInDepot number of days in depot; must be &gt;= 0
     * @throws InvalidParcelDataException if any argument fails its validation rule
     */
    public Parcel(String id, double weight, double length, double width,
                  double height, int daysInDepot) throws InvalidParcelDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidParcelDataException("Parcel ID must be non-null and non-blank.");
        }
        if (weight <= 0) {
            throw new InvalidParcelDataException("Weight must be greater than 0. Got: " + weight);
        }
        if (length <= 0) {
            throw new InvalidParcelDataException("Length must be greater than 0. Got: " + length);
        }
        if (width <= 0) {
            throw new InvalidParcelDataException("Width must be greater than 0. Got: " + width);
        }
        if (height <= 0) {
            throw new InvalidParcelDataException("Height must be greater than 0. Got: " + height);
        }
        if (daysInDepot < 0) {
            throw new InvalidParcelDataException("Days in depot must be >= 0. Got: " + daysInDepot);
        }
        this.id = id.trim();
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.daysInDepot = daysInDepot;
        this.collected = false;
    }

    /** @return the unique parcel ID */
    public String getId() {
        return id;
    }

    /** @return the weight in kg */
    public double getWeight() {
        return weight;
    }

    /** @return the length in cm */
    public double getLength() {
        return length;
    }

    /** @return the width in cm */
    public double getWidth() {
        return width;
    }

    /** @return the height in cm */
    public double getHeight() {
        return height;
    }

    /** @return the number of days this parcel has been in the depot */
    public int getDaysInDepot() {
        return daysInDepot;
    }

    /**
     * Sets the number of days this parcel has been in the depot.
     * @param daysInDepot must be &gt;= 0
     */
    public void setDaysInDepot(int daysInDepot) {
        this.daysInDepot = daysInDepot;
    }

    /** @return true if this parcel has been collected */
    public boolean isCollected() {
        return collected;
    }

    /** Marks this parcel as collected. */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    /**
     * Calculates and returns the volume of the parcel in cubic centimetres.
     *
     * @return length * width * height
     */
    public double getVolume() {
        return length * width * height;
    }

    /**
     * Returns a string representation of this parcel, including all fields.
     */
    @Override
    public String toString() {
        return "Parcel{id='" + id + "', weight=" + weight + "kg, dimensions="
                + length + "x" + width + "x" + height + "cm, daysInDepot="
                + daysInDepot + ", collected=" + collected + "}";
    }
}
