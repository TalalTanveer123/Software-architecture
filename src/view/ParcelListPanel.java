package view;

import controller.DepotController;
import model.Parcel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Displays the list of parcels still awaiting collection.
 *
 * <p>This panel shows a table with parcel ID, weight, volume, and days in depot
 * for every uncollected parcel. It implements {@link DepotController.DepotListener}
 * so it is automatically refreshed when the model changes.</p>
 */
public class ParcelListPanel extends JPanel implements DepotController.DepotListener {

    private static final long serialVersionUID = 1L;

    private DepotController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel countLabel;

    private static final String[] COLUMNS = {"Parcel ID", "Weight (kg)", "Volume (cm³)", "Days in Depot"};

    /**
     * Constructs a ParcelListPanel linked to the given controller.
     *
     * @param controller the application's DepotController
     */
    public ParcelListPanel(DepotController controller) {
        this.controller = controller;
        initUI();
        controller.addDepotListener(this);
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(30, 32, 48));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(99, 102, 241), 2, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Header
        JLabel title = new JLabel("📦  Parcels Awaiting Collection");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(165, 180, 252));

        countLabel = new JLabel("0 parcels");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(new Color(148, 163, 184));
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 32, 48));
        header.add(title, BorderLayout.WEST);
        header.add(countLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(new Color(30, 32, 48));
        scroll.getViewport().setBackground(new Color(22, 24, 38));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);
    }

    private void styleTable(JTable t) {
        t.setBackground(new Color(22, 24, 38));
        t.setForeground(new Color(226, 232, 240));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(26);
        t.setGridColor(new Color(51, 65, 85));
        t.setSelectionBackground(new Color(99, 102, 241, 120));
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(new Color(30, 32, 48));
        t.getTableHeader().setForeground(new Color(165, 180, 252));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
    }

    /**
     * Refreshes the panel when the model changes (DepotListener callback).
     */
    @Override
    public void onDepotUpdated() {
        tableModel.setRowCount(0);
        List<Parcel> parcels = controller.getParcelsAwaitingCollection();
        for (Parcel p : parcels) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    String.format("%.2f", p.getWeight()),
                    String.format("%.0f", p.getVolume()),
                    p.getDaysInDepot()
            });
        }
        countLabel.setText(parcels.size() + " parcel" + (parcels.size() != 1 ? "s" : ""));
    }
}
