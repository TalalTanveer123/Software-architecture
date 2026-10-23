package view;

import controller.DepotController;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;
import java.util.Queue;

/**
 * Displays the current queue of customers waiting to be served.
 *
 * <p>Shows a table with sequence number, customer name, and the ID of the parcel
 * they wish to collect. Implements {@link DepotController.DepotListener} to
 * refresh automatically when the model changes.</p>
 */
public class CustomerQueuePanel extends JPanel implements DepotController.DepotListener {

    private static final long serialVersionUID = 1L;

    private DepotController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel countLabel;

    private static final String[] COLUMNS = {"#", "Customer Name", "Parcel ID"};

    /**
     * Constructs a CustomerQueuePanel linked to the given controller.
     *
     * @param controller the application's DepotController
     */
    public CustomerQueuePanel(DepotController controller) {
        this.controller = controller;
        initUI();
        controller.addDepotListener(this);
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(30, 32, 48));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(168, 85, 247), 2, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel title = new JLabel("🧍  Customer Queue");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(216, 180, 254));

        countLabel = new JLabel("0 waiting");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(new Color(148, 163, 184));
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 32, 48));
        header.add(title, BorderLayout.WEST);
        header.add(countLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

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
        t.setSelectionBackground(new Color(168, 85, 247, 120));
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(new Color(30, 32, 48));
        t.getTableHeader().setForeground(new Color(216, 180, 254));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    /**
     * Refreshes the panel when the model changes (DepotListener callback).
     */
    @Override
    public void onDepotUpdated() {
        tableModel.setRowCount(0);
        Queue<Customer> snapshot = controller.getQueueSnapshot();
        int count = snapshot.size();
        Iterator<Customer> it = snapshot.iterator();
        while (it.hasNext()) {
            Customer c = it.next();
            tableModel.addRow(new Object[]{
                    c.getSeqNo(),
                    c.getName(),
                    c.getParcelId()
            });
        }
        countLabel.setText(count + " waiting");
    }
}
