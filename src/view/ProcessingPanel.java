package view;

import controller.DepotController;
import model.Customer;
import model.Parcel;
import model.ProcessingResult;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the customer, parcel and fee from the most recent processing result.
 *
 * <p>Implements {@link DepotController.DepotListener} to automatically refresh
 * when the model changes.</p>
 */
public class ProcessingPanel extends JPanel implements DepotController.DepotListener {

    private static final long serialVersionUID = 1L;

    private DepotController controller;

    private JLabel statusLabel;
    private JLabel customerLabel;
    private JLabel parcelIdLabel;
    private JLabel feeLabel;
    private JLabel weightLabel;
    private JLabel volumeLabel;
    private JLabel daysLabel;
    private JLabel messageLabel;

    /**
     * Constructs a ProcessingPanel linked to the given controller.
     *
     * @param controller the application's DepotController
     */
    public ProcessingPanel(DepotController controller) {
        this.controller = controller;
        initUI();
        controller.addDepotListener(this);
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));
        setBackground(new Color(30, 32, 48));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 197, 94), 2, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel title = new JLabel("⚡  Current Processing");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(134, 239, 172));
        add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 6, 6));
        grid.setBackground(new Color(30, 32, 48));

        grid.add(makeKey("Status:"));
        statusLabel = makeValue("—");
        grid.add(statusLabel);

        grid.add(makeKey("Customer:"));
        customerLabel = makeValue("—");
        grid.add(customerLabel);

        grid.add(makeKey("Parcel ID:"));
        parcelIdLabel = makeValue("—");
        grid.add(parcelIdLabel);

        grid.add(makeKey("Weight:"));
        weightLabel = makeValue("—");
        grid.add(weightLabel);

        grid.add(makeKey("Volume:"));
        volumeLabel = makeValue("—");
        grid.add(volumeLabel);

        grid.add(makeKey("Days in Depot:"));
        daysLabel = makeValue("—");
        grid.add(daysLabel);

        grid.add(makeKey("Fee Charged:"));
        feeLabel = makeValue("—");
        grid.add(feeLabel);

        add(grid, BorderLayout.CENTER);

        messageLabel = new JLabel("  Awaiting first transaction...");
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        messageLabel.setForeground(new Color(148, 163, 184));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        add(messageLabel, BorderLayout.SOUTH);
    }

    private JLabel makeKey(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(148, 163, 184));
        return l;
    }

    private JLabel makeValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(226, 232, 240));
        return l;
    }

    /**
     * Refreshes the panel when the model changes (DepotListener callback).
     */
    @Override
    public void onDepotUpdated() {
        ProcessingResult result = controller.getLastResult();
        if (result == null) {
            return;
        }

        if (result.isSuccess()) {
            statusLabel.setText("✅ Success");
            statusLabel.setForeground(new Color(134, 239, 172));
            Customer c = result.getCustomer();
            customerLabel.setText(c.getName() + " (seq#" + c.getSeqNo() + ")");
            Parcel p = result.getParcel();
            parcelIdLabel.setText(p.getId());
            weightLabel.setText(String.format("%.2f kg", p.getWeight()));
            volumeLabel.setText(String.format("%.0f cm³", p.getVolume()));
            daysLabel.setText(p.getDaysInDepot() + " days");
            feeLabel.setText(String.format("£%.2f", result.getFee()));
            feeLabel.setForeground(new Color(250, 204, 21));
        } else {
            statusLabel.setText("❌ Failed");
            statusLabel.setForeground(new Color(248, 113, 113));
            Customer c = result.getCustomer();
            customerLabel.setText(c != null ? c.getName() : "—");
            parcelIdLabel.setText(c != null ? c.getParcelId() : "—");
            weightLabel.setText("—");
            volumeLabel.setText("—");
            daysLabel.setText("—");
            feeLabel.setText("£0.00");
            feeLabel.setForeground(new Color(226, 232, 240));
        }
        messageLabel.setText("  " + result.getMessage());
    }
}
