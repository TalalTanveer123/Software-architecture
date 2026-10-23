package util;

import model.Parcel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Writes the end-of-day report to a text file.
 *
 * <p>The report contains:</p>
 * <ol>
 *   <li>A header with the report date.</li>
 *   <li>A section listing every parcel that was collected during the day, with
 *       its ID and the fee charged.</li>
 *   <li>A section listing every parcel still awaiting collection.</li>
 *   <li>A summary: total number of parcels collected, total fees taken, and
 *       total number of parcels still outstanding.</li>
 * </ol>
 */
public class ReportWriter {

    /**
     * Writes the end-of-day report for the given parcel collection to the
     * specified file path.
     *
     * @param parcels  all parcels known to the depot (collected and uncollected)
     * @param fees     parallel list of fees – {@code fees.get(i)} is the fee charged
     *                 for {@code collectedParcels.get(i)}. The mapping is by order:
     *                 the caller must ensure the lists are aligned.
     *                 If this is not available, pass an empty list and fees will be
     *                 shown as 0.00.
     * @param filePath path to write the report to
     * @throws IOException if the file cannot be written
     */
    public void writeReport(Collection<Parcel> parcels, List<Double> fees, String filePath)
            throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or blank.");
        }

        List<Parcel> collected   = new ArrayList<Parcel>();
        List<Parcel> outstanding = new ArrayList<Parcel>();

        for (Parcel p : parcels) {
            if (p.isCollected()) {
                collected.add(p);
            } else {
                outstanding.add(p);
            }
        }

        double totalFees = 0.0;
        for (int i = 0; i < fees.size(); i++) {
            totalFees += fees.get(i);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("          DEPOT END-OF-DAY REPORT\n");
        sb.append("=================================================\n\n");

        sb.append("PARCELS COLLECTED TODAY\n");
        sb.append("-------------------------------------------------\n");
        if (collected.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (int i = 0; i < collected.size(); i++) {
                Parcel p = collected.get(i);
                double fee = (i < fees.size()) ? fees.get(i) : 0.0;
                sb.append(String.format("  %-12s  Fee: £%.2f%n", p.getId(), fee));
            }
        }
        sb.append("\n");

        sb.append("PARCELS AWAITING COLLECTION\n");
        sb.append("-------------------------------------------------\n");
        if (outstanding.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (Parcel p : outstanding) {
                sb.append(String.format("  %-12s  Weight: %.2fkg  Days: %d%n",
                        p.getId(), p.getWeight(), p.getDaysInDepot()));
            }
        }
        sb.append("\n");

        sb.append("=================================================\n");
        sb.append("SUMMARY\n");
        sb.append(String.format("  Parcels collected : %d%n", collected.size()));
        sb.append(String.format("  Total fees taken  : £%.2f%n", totalFees));
        sb.append(String.format("  Parcels outstanding: %d%n", outstanding.size()));
        sb.append("=================================================\n");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sb.toString());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
