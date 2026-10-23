package view;

import controller.DepotController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The main application window for the Depot Manager system.
 *
 * <p>Implements {@link DepotController.DepotListener} (via an anonymous inner class)
 * so it can be notified of model changes and delegate refresh to child panels.</p>
 *
 * <p>Layout:</p>
 * <pre>
 * ┌────────────────────────────────────────────────┐
 * │              Header / Title Bar                │
 * ├─────────────────────┬──────────────────────────┤
 * │  ParcelListPanel    │  CustomerQueuePanel       │
 * ├─────────────────────┴──────────────────────────┤
 * │              ProcessingPanel                    │
 * ├────────────────────────────────────────────────┤
 * │              ControlPanel (buttons)             │
 * └────────────────────────────────────────────────┘
 * </pre>
 */
public class DepotManagerFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private DepotController controller;

    private ParcelListPanel parcelListPanel;
    private CustomerQueuePanel customerQueuePanel;
    private ProcessingPanel processingPanel;
    private ControlPanel controlPanel;

    // Shared list of fees collected during the session
    private List<Double> collectedFees;

    /**
     * Constructs the main application window.
     *
     * @param controller the DepotController to use
     */
    public DepotManagerFrame(DepotController controller) {
        super("📦 Depot Manager — Parcel Collection System");
        this.controller = controller;
        this.collectedFees = new ArrayList<Double>();
        initUI();
        registerWithController();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setPreferredSize(new Dimension(1200, 780));

        // Dark background for content pane
        JPanel contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBackground(new Color(15, 17, 30));
        setContentPane(contentPane);

        // Header
        contentPane.add(buildHeader(), BorderLayout.NORTH);

        // Centre: parcel list + queue side by side, then processing panel below
        parcelListPanel    = new ParcelListPanel(controller);
        customerQueuePanel = new CustomerQueuePanel(controller);
        processingPanel    = new ProcessingPanel(controller);

        JSplitPane topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                parcelListPanel, customerQueuePanel);
        topSplit.setResizeWeight(0.6);
        topSplit.setDividerSize(6);
        topSplit.setBackground(new Color(15, 17, 30));
        topSplit.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                topSplit, wrapWithPadding(processingPanel));
        mainSplit.setResizeWeight(0.65);
        mainSplit.setDividerSize(6);
        mainSplit.setBackground(new Color(15, 17, 30));
        mainSplit.setBorder(BorderFactory.createEmptyBorder(0, 8, 4, 8));

        contentPane.add(mainSplit, BorderLayout.CENTER);

        // Control panel at bottom
        controlPanel = new ControlPanel(controller, collectedFees);
        contentPane.add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(22, 24, 38));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(51, 65, 85)),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));

        JLabel logo = new JLabel("📦 Depot Manager");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(new Color(165, 180, 252));

        JLabel sub = new JLabel("Parcel Collection System  •  University of Hertfordshire");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(100, 116, 139));

        JPanel left = new JPanel(new BorderLayout(0, 2));
        left.setBackground(new Color(22, 24, 38));
        left.add(logo, BorderLayout.NORTH);
        left.add(sub, BorderLayout.SOUTH);
        header.add(left, BorderLayout.WEST);

        return header;
    }

    private JPanel wrapWithPadding(JPanel panel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(15, 17, 30));
        wrapper.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    /**
     * Registers this frame with the controller using an anonymous DepotListener.
     * The frame itself does not need to do any additional work on update because
     * the individual child panels each register themselves independently.
     */
    private void registerWithController() {
        controller.addDepotListener(new DepotController.DepotListener() {
            public void onDepotUpdated() {
                // Child panels already registered; this hook is available
                // for any frame-level reactions (e.g., updating title bar counters).
                int queueSize   = controller.getQueueSnapshot().size();
                int parcelCount = controller.getParcelsAwaitingCollection().size();
                setTitle("📦 Depot Manager  |  Queue: " + queueSize
                        + "  |  Awaiting: " + parcelCount);
            }
        });
    }
}
