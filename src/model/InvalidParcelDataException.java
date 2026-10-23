package model;

/**
 * Checked exception thrown by Parcel's constructor when given invalid data.
 * Provided complete – do not modify.
 */
public class InvalidParcelDataException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidParcelDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidParcelDataException(String message) {
        super(message);
    }
}
