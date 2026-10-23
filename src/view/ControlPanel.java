package view;

import controller.DepotController;
import model.InvalidParcelDataException;
import model.Parcel;
import model.ProcessingResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Holds the action buttons the user interacts with.
 *
 * <p>At minimum this panel contains:</p>
 * <ul>
 *   <li>Process Next Customer button</li>
 *   <li>End Day button</li>
 * </ul>
 *
 * <p>Additional buttons for adding a new parcel and adding a new customer
 * are also provided.</p>
 */
public class ControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private DepotController controller;

    // Collected fees list kept in sync with collections
    private List<Double> collectedFees;

    /**
     * Constructs a ControlPanel linked to the given controller.
     *
     * @param controller      the application's DepotController
     * @param collectedFees   a shared list to which each successful fee is appended
     */
    public ControlPanel(DepotController controller, List<Double> collectedFees) {
        this.controller = controller;
        this.collectedFees = collectedFees;
        initUI();
    }

    private void initUI() {
        setBackground(new Color(22, 24, 38));
        setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(51, 65, 85)));

        JButton processBtn = makeButton("▶  Process Next Customer", new Color(99, 102, 241));
        JButton addParcelBtn = makeButton("➕  Add Parcel", new Color(34, 197, 94));
        JButton addCustomerBtn = makeButton("👤  Add Customer", new Color(168, 85, 247));
        JButton endDayBtn = makeButton("🌙  End Day", new Color(239, 68, 68));

        add(processBtn);
        add(addParcelBtn);
        add(addCustomerBtn);
        add(endDayBtn);

        // --- Process Next Customer ---
        processBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controller.isQueueEmpty()) {
                    JOptionPane.showMessageDialog(ControlPanel.this,
                            "The customer queue is empty.", "Queue Empty",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                ProcessingResult result = controller.processNextCustomer();
                if (result.isSuccess()) {
                    collectedFees.add(result.getFee());
                    JOptionPane.showMessageDialog(ControlPanel.this,
                            result.getMessage(), "Parcel Released",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(ControlPanel.this,
                            result.getMessage(), "Processing Failed",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // --- Add Parcel ---
        addParcelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddParcelDialog();
            }
        });

        // --- Add Customer ---
        addCustomerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddCustomerDialog();
            }
        });

        // --- End Day ---
        endDayBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ControlPanel.this,
                        "End the day and generate the report?", "End of Day",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    generateEndOfDayReport();
                }
            }
        });
    }

    private JButton makeButton(String text, Color accent) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(accent.darker());
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 1, true),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        final Color baseColor = accent.darker();
        final Color hoverColor = accent;

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });

        return btn;
    }

    private void showAddParcelDialog() {
        JTextField idField     = new JTextField(10);
        JTextField weightField = new JTextField(5);
        JTextField lengthField = new JTextField(5);
        JTextField widthField  = new JTextField(5);
        JTextField heightField = new JTextField(5);
        JTextField daysField   = new JTextField(4);

        JPanel panel = new JPanel(new GridLayout(0, 2, 4, 4));
        panel.add(new JLabel("Parcel ID:"));   panel.add(idField);
        panel.add(new JLabel("Weight (kg):")); panel.add(weightField);
        panel.add(new JLabel("Length (cm):")); panel.add(lengthField);
        panel.add(new JLabel("Width (cm):"));  panel.add(widthField);
        panel.add(new JLabel("Height (cm):")); panel.add(heightField);
        panel.add(new JLabel("Days in Depot:")); panel.add(daysField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Parcel",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                double weight = Double.parseDouble(weightField.getText().trim());
                double length = Double.parseDouble(lengthField.getText().trim());
                double width  = Double.parseDouble(widthField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                int days      = Integer.parseInt(daysField.getText().trim());
                controller.addParcel(id, weight, length, width, height, days);
                JOptionPane.showMessageDialog(this, "Parcel '" + id + "' added successfully.",
                        "Parcel Added", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InvalidParcelDataException ex) {
                JOptionPane.showMessageDialog(this, "Invalid parcel data: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddCustomerDialog() {
        JTextField nameField     = new JTextField(15);
        JTextField parcelIdField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 4, 4));
        panel.add(new JLabel("Customer Name:")); panel.add(nameField);
        panel.add(new JLabel("Parcel ID:"));     panel.add(parcelIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Customer to Queue",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String parcelId = parcelIdField.getText().trim();
                controller.addCustomer(name, parcelId);
                JOptionPane.showMessageDialog(this, "Customer '" + name + "' added to queue.",
                        "Customer Added", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Invalid data: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generateEndOfDayReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save End-of-Day Report");
        chooser.setSelectedFile(new java.io.File("end_of_day_report.txt"));
        int choice = chooser.showSaveDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            try {
                util.ReportWriter rw = new util.ReportWriter();
                Collection<Parcel> all = controller.getAllParcels();
                rw.writeReport(all, collectedFees, path);

                // Also write the log
                String logPath = path.replace(".txt", "_log.txt");
                model.Log.getInstance().writeToFile(logPath);

                JOptionPane.showMessageDialog(this,
                        "Report saved to:\n" + path + "\nLog saved to:\n" + logPath,
                        "Report Generated", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error writing report: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
