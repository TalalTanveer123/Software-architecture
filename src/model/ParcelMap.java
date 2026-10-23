package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Container class holding every {@link Parcel} known to the depot, keyed by parcel ID.
 *
 * <p>Chosen data structure: {@link HashMap}. A Map is appropriate because the primary
 * operation is look-up by unique ID, which Map provides in O(1) average time,
 * and each parcel ID is unique by definition.</p>
 */
public class ParcelMap {

    /** The underlying map: parcel ID -> Parcel */
    private Map<String, Parcel> parcels;

    /**
     * Constructs an empty ParcelMap.
     */
    public ParcelMap() {
        parcels = new HashMap<String, Parcel>();
    }

    /**
     * Adds a parcel to the map. If a parcel with the same ID already exists it is
     * replaced.
     *
     * @param parcel the parcel to add; must not be null
     */
    public void addParcel(Parcel parcel) {
        if (parcel == null) {
            throw new IllegalArgumentException("Parcel must not be null.");
        }
        parcels.put(parcel.getId(), parcel);
    }

    /**
     * Returns the parcel with the given ID, or {@code null} if no such parcel exists.
     *
     * @param id the parcel ID to look up
     * @return the matching Parcel, or null
     */
    public Parcel getParcel(String id) {
        if (id == null) {
            return null;
        }
        return parcels.get(id);
    }

    /**
     * Returns {@code true} if a parcel with the given ID exists in the map.
     *
     * @param id the parcel ID to check
     * @return true if present
     */
    public boolean containsParcel(String id) {
        if (id == null) {
            return false;
        }
        return parcels.containsKey(id);
    }

    /**
     * Removes the parcel with the given ID from the map.
     *
     * @param id the parcel ID to remove
     * @return the removed Parcel, or null if it was not present
     */
    public Parcel removeParcel(String id) {
        if (id == null) {
            return null;
        }
        return parcels.remove(id);
    }

    /**
     * Returns a collection of all parcels currently in the map (both collected and
     * uncollected).
     *
     * @return all parcels; never null
     */
    public Collection<Parcel> getAllParcels() {
        return parcels.values();
    }

    /**
     * Returns the number of parcels in the map.
     *
     * @return size of the map
     */
    public int size() {
        return parcels.size();
    }

    /**
     * Returns {@code true} if the map contains no parcels.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return parcels.isEmpty();
    }
}
