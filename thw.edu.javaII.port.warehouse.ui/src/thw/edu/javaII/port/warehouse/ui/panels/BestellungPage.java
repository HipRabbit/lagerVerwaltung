package thw.edu.javaII.port.warehouse.ui.panels;

import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.model.BestellungOverviewTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Panel zur Anzeige und Verwaltung der Bestellungsübersicht im Lagerverwaltungssystem.
 *
 * <p>Diese Klasse erweitert JPanel und bietet eine umfassende Benutzeroberfläche
 * zur Anzeige aller Bestellungen mit Filterfunktionen, Statusanzeigen und
 * Verwaltungsaktionen wie Hinzufügen, Löschen und Detailansicht.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session session – aktuelle Benutzersitzung</li>
 *   <li>JTable bestellungTable – Tabelle zur Anzeige der Bestellungen</li>
 *   <li>BestellungOverviewTableModel tableModel – Datenmodell für die Tabelle</li>
 *   <li>JLabel statusLabel – Anzeige der Bestellungsstatistiken</li>
 *   <li>Date currentStartDate – aktuelles Startdatum für Filter</li>
 *   <li>Date currentEndDate – aktuelles Enddatum für Filter</li>
 *   <li>Double currentMinAmount – aktueller Mindestbetrag für Filter</li>
 *   <li>Integer currentProductId – aktuelle Produkt-ID für Filter</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>BestellungPage(Session) – Konstruktor mit UI-Initialisierung</li>
 *   <li>initializeUI() – Erstellt die komplette Benutzeroberfläche</li>
 *   <li>updateTable() – Lädt und aktualisiert Bestellungsdaten</li>
 *   <li>openDetailView() – Öffnet Detailansicht für ausgewählte Bestellung</li>
 *   <li>deleteBestellung() – Löscht ausgewählte Bestellung</li>
 *   <li>updateStatusCounts() – Aktualisiert Statusstatistiken</li>
 * </ul>
 *
 * @author Bjarne von Appen
 */
public class BestellungPage extends JPanel {
    private static final long serialVersionUID = 6L;
    
    /** Aktuelle Benutzersitzung */
    private final Session session;
    /** Tabelle zur Anzeige der Bestellungen */
    private JTable bestellungTable;
    /** Datenmodell für die Bestellungstabelle */
    private BestellungOverviewTableModel tableModel;
    /** Anzeige der Bestellungsstatistiken */
    private JLabel statusLabel;
    
    // Instanzvariablen für Filterkriterien
    /** Aktuelles Startdatum für Filter */
    private Date currentStartDate;
    /** Aktuelles Enddatum für Filter */
    private Date currentEndDate;
    /** Aktueller Mindestbetrag für Filter */
    private Double currentMinAmount;
    /** Aktuelle Produkt-ID für Filter */
    private Integer currentProductId;

    /**
     * Konstruktor erstellt das Bestellungsübersichts-Panel.
     *
     * @param session aktuelle Benutzersitzung
     */
    public BestellungPage(Session session) {
        this.session = session;
        setLayout(new BorderLayout());
        initializeUI();
    }

    /**
     * Initialisiert die komplette Benutzeroberfläche des Panels.
     *
     * @author Bjarne von Appen
     */
    private void initializeUI() {
        // Titel-Label
        JLabel titleLabel = new JLabel("Bestellungsübersicht", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Bestellungstabelle mit Custom-Renderer für Statusfarben
        tableModel = new BestellungOverviewTableModel(null);
        bestellungTable = new JTable(tableModel);
        bestellungTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bestellungTable.setRowHeight(25);

        // Custom-Renderer für farbliche Kennzeichnung der Bestellungsstatus
        bestellungTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                BestellungOverviewTableModel model = (BestellungOverviewTableModel) table.getModel();

                int modelRow = table.convertRowIndexToModel(row);

                // Statusprüfung für Farbkodierung
                boolean isComplete = model.isBestellungComplete(modelRow);
                boolean hasAtLeastTwoTimestamps = model.countSetTimestamps(modelRow) >= 2;

                if (!isSelected) {
                    if (isComplete) {
                        c.setBackground(new Color(144, 238, 144)); // Grün für vollständige Bestellungen
                    } else if (hasAtLeastTwoTimestamps) {
                        c.setBackground(new Color(255, 255, 204)); // Gelb für in Bearbeitung
                    } else {
                        c.setBackground(table.getBackground()); // Standard für neue Bestellungen
                    }
                } else {
                    // Spezielle Behandlung für ausgewählte Zeilen
                    if (isComplete) {
                        c.setBackground(new Color(144, 238, 144));
                        c.setForeground(Color.BLACK);
                    } else if (hasAtLeastTwoTimestamps) {
                        c.setBackground(new Color(255, 255, 204));
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(table.getSelectionBackground());
                        c.setForeground(table.getSelectionForeground());
                    }
                }
                return c;
            }
        });

        // Renderer auf alle Spalten anwenden
        for (int i = 0; i < bestellungTable.getColumnCount(); i++) {
            bestellungTable.getColumnModel().getColumn(i).setCellRenderer(bestellungTable.getDefaultRenderer(Object.class));
        }

        // Doppelklick-Listener für Detailansicht
        bestellungTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doppelklick erkannt
                    openDetailView();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bestellungTable);
        add(scrollPane, BorderLayout.CENTER);

        // Südlicher Container für Status und Buttons
        JPanel southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));

        // Status-Panel mit Statistiken
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Erfasste Bestellungen: 0 | In Bearbeitung: 0 | Fertige Bestellungen: 0 | Insgesamter Umsatz: 0,00 €");
        statusPanel.add(statusLabel);
        southContainer.add(statusPanel);

        // Filter- und Button-Panel
        JPanel southPanel = new JPanel(new BorderLayout());
        FilterPanel filterPanel = new FilterPanel(session.getCommunicator());
        filterPanel.setFilterListener(new FilterPanel.FilterListener() {
            @Override
            public void onFilter(Date startDate, Date endDate, Double minAmount, Integer productId) {
                // Speichere die Filterkriterien für spätere Verwendung
                currentStartDate = startDate;
                currentEndDate = endDate;
                currentMinAmount = minAmount;
                currentProductId = productId;
                updateTable();
            }

            @Override
            public void onReset() {
                // Setze alle Filterkriterien zurück
                currentStartDate = null;
                currentEndDate = null;
                currentMinAmount = null;
                currentProductId = null;
                updateTable();
            }
        });
        southPanel.add(filterPanel, BorderLayout.NORTH);

        // Button-Panel für Aktionen
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton detailButton = new JButton("Details anzeigen");
        detailButton.addActionListener(e -> openDetailView());
        buttonPanel.add(detailButton);

        JButton addBestellungButton = new JButton("Bestellung hinzufügen");
        addBestellungButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
            AddBestellung dialog = new AddBestellung(parentFrame, session, getParentContentPane());
            if (!dialog.isDisplayable()) {
                return;
            }
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
            dialog.setVisible(true);
            updateTable(); // Aktualisiere Tabelle nach Hinzufügen
        });
        buttonPanel.add(addBestellungButton);

        JButton deleteBestellungButton = new JButton("Bestellung löschen");
        deleteBestellungButton.addActionListener(e -> deleteBestellung());
        buttonPanel.add(deleteBestellungButton);

        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        southContainer.add(southPanel);

        add(southContainer, BorderLayout.SOUTH);

        updateTable(); // Initiale Daten laden
    }

    /**
     * Löscht die ausgewählte Bestellung nach Bestätigung.
     *
     * @author Bjarne von Appen
     */
    private void deleteBestellung() {
        int selectedRow = bestellungTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = bestellungTable.convertRowIndexToModel(selectedRow);
            Bestellung selectedBestellung = tableModel.getBestellungAt(modelRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Möchten Sie die Bestellung mit ID " + selectedBestellung.getId() + " wirklich löschen?",
                "Bestellung löschen",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = session.getCommunicator().deleteBestellung(selectedBestellung);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Bestellung erfolgreich gelöscht!",
                            "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                        updateTable(); // Aktualisiere mit aktuellen Filterkriterien
                    } else {
                        JOptionPane.showMessageDialog(this, "Fehler beim Löschen der Bestellung!",
                            "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Fehler beim Löschen: " + ex.getMessage(),
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine Bestellung aus.",
                "Warnung", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Ermittelt das übergeordnete Content-Panel.
     *
     * @return Content-Panel des übergeordneten JFrame oder null
     */
    private JPanel getParentContentPane() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        if (parent instanceof JFrame) {
            return (JPanel) ((JFrame) parent).getContentPane();
        }
        return null;
    }

    /**
     * Aktualisiert die Statusanzeige mit aktuellen Bestellungsstatistiken.
     *
     * @author Bjarne von Appen
     */
    private void updateStatusCounts() {
        int erfasst = 0;
        int inBearbeitung = 0;
        int fertig = 0;

        // Zähle Bestellungen nach Status
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean isComplete = tableModel.isBestellungComplete(i);
            boolean hasAtLeastTwoTimestamps = tableModel.countSetTimestamps(i) >= 2;

            if (isComplete) {
                fertig++;
            } else if (hasAtLeastTwoTimestamps) {
                inBearbeitung++;
            } else {
                erfasst++;
            }
        }

        // Berechne Gesamtumsatz
        double totalRevenue = tableModel.calculateTotalRevenue();
        String formattedRevenue = tableModel.getCurrencyFormat().format(totalRevenue) + " €";

        // Aktualisiere Status-Label
        statusLabel.setText(String.format("Erfasste Bestellungen: %d | In Bearbeitung: %d | Fertige Bestellungen: %d | Insgesamter Umsatz: %s",
                erfasst, inBearbeitung, fertig, formattedRevenue));
    }

    /**
     * Lädt und aktualisiert die Bestellungsdaten basierend auf aktuellen Filterkriterien.
     *
     * @author Bjarne von Appen
     */
    public void updateTable() { // Geändert von private zu public für externe Aufrufe
        try {
            List<Bestellung> bestellungen;
            // Prüfe ob Filter aktiv sind
            if (currentStartDate != null || currentEndDate != null || currentMinAmount != null || currentProductId != null) {
                // Lade gefilterte Bestellungen
                bestellungen = session.getCommunicator().getFilteredBestellungen(
                    currentStartDate, currentEndDate, currentMinAmount, currentProductId);
                System.out.println("updateTable: Anzahl gefilterter Bestellungen=" + (bestellungen != null ? bestellungen.size() : 0));
            } else {
                // Lade alle Bestellungen
                bestellungen = session.getCommunicator().getBestellungen();
                System.out.println("updateTable: Anzahl geladener Bestellungen=" + (bestellungen != null ? bestellungen.size() : 0));
            }
            
            // Null-Schutz
            if (bestellungen == null) {
                bestellungen = new ArrayList<>();
                System.out.println("updateTable: Bestellungen war null, leere Liste verwendet");
            }
            
            // Aktualisiere Tabelle und Status
            tableModel.setData(bestellungen);
            System.out.println("updateTable: Tabelle aktualisiert mit " + bestellungen.size() + " Bestellungen");
            updateStatusCounts();
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Bestellungen: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Bestellungen: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Öffnet die Detailansicht für die ausgewählte Bestellung.
     *
     * @author Bjarne von Appen
     */
    private void openDetailView() {
        int selectedRow = bestellungTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = bestellungTable.convertRowIndexToModel(selectedRow);
            Bestellung selectedBestellung = tableModel.getBestellungAt(modelRow);
            BestellungDetailDialog detailDialog = new BestellungDetailDialog(null, session, selectedBestellung);
            detailDialog.setVisible(true);
            updateTable(); // Aktualisiere mit aktuellen Filterkriterien
        } else {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine Bestellung aus.", "Warnung", JOptionPane.WARNING_MESSAGE);
        }
    }
}
