package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Singleton class that records every significant event during the application's
 * lifetime to a shared {@link StringBuffer}, and can write that buffer to a text
 * file on request.
 *
 * <p>There is exactly one instance of this class for the whole application.
 * Obtain it via {@link #getInstance()}; the constructor is private.</p>
 *
 * <p>Thread-safety: this implementation is not intended to be used from multiple
 * threads simultaneously; basic synchronisation is provided via {@code synchronized}
 * on {@link #getInstance()} to satisfy the Singleton contract.</p>
 */
public class Log {

    /** The single instance (lazy initialisation). */
    private static Log instance;

    /** The buffer that accumulates all log entries. */
    private StringBuffer buffer;

    /**
     * Private constructor – prevents direct instantiation.
     */
    private Log() {
        buffer = new StringBuffer();
    }

    /**
     * Returns the single, shared instance of Log. Creates it on first call.
     *
     * @return the singleton Log instance
     */
    public static synchronized Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    /**
     * Appends a timestamped entry to the event log.
     *
     * @param event a description of the event; must not be null
     */
    public void logEvent(String event) {
        if (event == null) {
            event = "null";
        }
        buffer.append(event).append(System.lineSeparator());
    }

    /**
     * Returns the full contents of the log buffer as a String.
     *
     * @return the accumulated log text
     */
    public String getLogContents() {
        return buffer.toString();
    }

    /**
     * Clears all entries from the log buffer.
     */
    public void clear() {
        buffer = new StringBuffer();
    }

    /**
     * Writes the current log buffer to the specified file, overwriting any
     * existing content.
     *
     * @param filePath path to the file to write; must not be null or blank
     * @throws IOException if the file cannot be written
     */
    public void writeToFile(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or blank.");
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(buffer.toString());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // ignore close exception
                }
            }
        }
    }

    /**
     * Resets the singleton instance. Intended for use in tests only.
     * Not part of the public API; the test suite may call this via reflection.
     */
    static synchronized void resetInstance() {
        instance = null;
    }
}
