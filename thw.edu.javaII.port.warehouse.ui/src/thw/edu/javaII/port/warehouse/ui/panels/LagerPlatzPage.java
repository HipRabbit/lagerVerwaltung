package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;

import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.model.common.Cast;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Panel zur Anzeige und Verwaltung der Lagerplatzübersicht im Lagerverwaltungssystem.
 *
 * <p>Diese Klasse erweitert JPanel und bietet eine umfassende Benutzeroberfläche
 * zur Anzeige aller Lagerplätze mit Such-, Sortier- und Verwaltungsfunktionen wie
 * Hinzufügen, Bearbeiten und Löschen von Lagerplätzen.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session ses – aktuelle Benutzersitzung</li>
 *   <li>JTable table – Tabelle zur Anzeige der Lagerplätze</li>
 *   <li>LagerPlatzTableModel tableModel – Datenmodell für die Lagerplatztabelle</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>LagerPlatzPage(Session) – Konstruktor mit UI-Initialisierung</li>
 *   <li>initializeUI() – Erstellt die komplette Benutzeroberfläche</li>
 *   <li>refresh() – Lädt und aktualisiert Lagerplatzdaten vom Server</li>
 *   <li>updateSortIndicator(int, boolean) – Aktualisiert Sortierungsanzeige</li>
 * </ul>
 *
 * @author Bjarne von Appen
 * @author Paul Hartmann
 * @author Lennart Höpfner
 */
public class LagerPlatzPage extends JPanel {
    private static final long serialVersionUID = 328328L;
    
    /** Aktuelle Benutzersitzung */
    private Session ses;
    /** Tabelle zur Anzeige der Lagerplätze */
    private JTable table;
    /** Datenmodell für die Lagerplatztabelle */
    private LagerPlatzTableModel tableModel;

    /**
     * Konstruktor erstellt das Lagerplatzübersichts-Panel.
     *
     * @param ses aktuelle Benutzersitzung
     */
    public LagerPlatzPage(Session ses) {
        this.ses = ses;
        setLayout(new BorderLayout());
        initializeUI();

        // Standard-Sortierung nach Kapazität aufsteigend nach dem Laden der Daten
        tableModel.sortByCapacity(false);
    }

    /**
     * Initialisiert die komplette Benutzeroberfläche des Panels.
     *
     * @author Bjarne von Appen
     */
    private void initializeUI() {
        // Oberes Panel für Titel, Suche und Sortierbuttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Titel-Label
        JLabel lblTitle = new JLabel("Lagerplatzübersicht");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // Suchpanel mit ID-Suche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblIdSearch = new JLabel("Nach ID suchen:");
        JTextField idField = new JTextField(10);
        idField.setToolTipText("Geben Sie die Lagerplatz-ID ein (mind. 4 Ziffern)");
        JButton btnSearch = new JButton("Suchen");
        JButton btnReset = new JButton("Zurücksetzen");

        searchPanel.add(lblIdSearch);
        searchPanel.add(idField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnReset);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Sortierbuttons für ID, Name und Kapazität
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton sortByIdButton = new JToggleButton("Sortieren nach ID");
        JToggleButton sortByNameButton = new JToggleButton("Sortieren nach Name");
        JToggleButton sortByCapacityButton = new JToggleButton("Sortieren nach Kapazität");

        // Event-Handler für ID-Sortierung
        sortByIdButton.addActionListener(e -> {
            boolean ascending = !sortByIdButton.isSelected();
            tableModel.sortById(ascending);
            sortByIdButton.setText(ascending ? "Sortieren nach ID ↑" : "Sortieren nach ID ↓");
            sortByNameButton.setSelected(false);
            sortByNameButton.setText("Sortieren nach Name");
            sortByCapacityButton.setSelected(false);
            sortByCapacityButton.setText("Sortieren nach Kapazität");
            updateSortIndicator(0, ascending); // Spalte 0 (ID)
        });

        // Event-Handler für Name-Sortierung
        sortByNameButton.addActionListener(e -> {
            boolean ascending = !sortByNameButton.isSelected();
            tableModel.sortByName(ascending);
            sortByNameButton.setText(ascending ? "Sortieren nach Name ↑" : "Sortieren nach Name ↓");
            sortByIdButton.setSelected(false);
            sortByIdButton.setText("Sortieren nach ID");
            sortByCapacityButton.setSelected(false);
            sortByCapacityButton.setText("Sortieren nach Kapazität");
            updateSortIndicator(1, ascending); // Spalte 1 (Name)
        });

        // Event-Handler für Kapazitäts-Sortierung
        sortByCapacityButton.addActionListener(e -> {
            boolean ascending = !sortByCapacityButton.isSelected();
            tableModel.sortByCapacity(ascending);
            sortByCapacityButton.setText(ascending ? "Sortieren nach Kapazität ↑" : "Sortieren nach Kapazität ↓");
            sortByIdButton.setSelected(false);
            sortByIdButton.setText("Sortieren nach ID");
            sortByNameButton.setSelected(false);
            sortByNameButton.setText("Sortieren nach Name");
            updateSortIndicator(2, ascending); // Spalte 2 (Kapazität)
        });

        sortPanel.add(sortByIdButton);
        sortPanel.add(sortByNameButton);
        sortPanel.add(sortByCapacityButton);
        topPanel.add(sortPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Button-Panel für CRUD-Operationen
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addButton = new JButton("Lagerplatz anlegen");
        JButton editButton = new JButton("Lagerplatz bearbeiten");
        JButton deleteButton = new JButton("Lagerplatz löschen");

        // Event-Handler für Lagerplatz hinzufügen
        addButton.addActionListener(e -> {
            new AddLagerPlatz(ses, null).setVisible(true);
            refresh(); // Tabelle nach Hinzufügen aktualisieren
        });

        // Event-Handler für Lagerplatz bearbeiten
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                LagerPlatz selectedLagerPlatz = tableModel.getLagerPlatzAt(selectedRow);
                new EditLagerPlatz(ses, null, selectedLagerPlatz).setVisible(true);
                refresh(); // Tabelle nach Bearbeitung aktualisieren
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Lagerplatz aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event-Handler für Lagerplatz löschen
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                LagerPlatz selectedLagerPlatz = tableModel.getLagerPlatzAt(selectedRow);
                new DeleteLagerPlatz(ses, null, selectedLagerPlatz).setVisible(true);
                refresh(); // Tabelle nach Löschung aktualisieren
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie einen Lagerplatz aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Lagerplatztabelle initialisieren
        tableModel = new LagerPlatzTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new SortIndicatorHeaderRenderer(table.getTableHeader().getDefaultRenderer()));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Event-Handler für ID-Suche
        btnSearch.addActionListener(e -> {
            try {
                String idText = idField.getText().trim();
                if (idText.length() < 4) {
                    JOptionPane.showMessageDialog(this, "Bitte geben Sie mindestens 4 Ziffern ein!", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                    return; // Beende die Ausführung, Tabelle bleibt unverändert
                }
                int id = Integer.parseInt(idText);
                LagerPlatz lagerplatz = ses.getCommunicator().getLagerPlatzById(id);
                if (lagerplatz != null && lagerplatz.getId() > 0 && lagerplatz.getName() != null && !lagerplatz.getName().isEmpty()) {
                    tableModel.setSingleLagerPlatz(lagerplatz);
                } else {
                    JOptionPane.showMessageDialog(this, "Lagerplatz mit ID " + id + " nicht gefunden!", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                    // Tabelle unverändert lassen, keine Änderung an tableModel
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige numerische ID ein!", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event-Handler für Suchfeld zurücksetzen
        btnReset.addActionListener(e -> {
            idField.setText("");
            refresh(); // Alle Lagerplätze wieder anzeigen
        });

        refresh(); // Initiale Daten laden
    }

    /**
     * Lädt und aktualisiert die Lagerplatzdaten vom Server.
     *
     * @author Paul Hartmann
     */
    public void refresh() {
        try {
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGERPLATZ);
            deo.setCommand(Command.LIST);
            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                java.util.List<LagerPlatz> lagerPlatzList = Cast.safeListCast(ret.getData(), LagerPlatz.class);
                tableModel.setData(lagerPlatzList);
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Lagerplätze: " + ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                tableModel.setData(new java.util.ArrayList<>());
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Kommunikationsfehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            tableModel.setData(new java.util.ArrayList<>());
        }
    }

    /**
     * Aktualisiert die Sortierungsanzeige im Tabellenkopf.
     *
     * @param columnIndex Index der sortierten Spalte
     * @param ascending   true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    private void updateSortIndicator(int columnIndex, boolean ascending) {
        SortIndicatorHeaderRenderer renderer = (SortIndicatorHeaderRenderer) table.getTableHeader().getDefaultRenderer();
        renderer.setSortColumn(columnIndex, ascending);
        table.getTableHeader().repaint();
    }
}

/**
 * Tabellenmodell für die Anzeige von Lagerplatzdaten.
 *
 * <p>Diese Klasse erweitert AbstractTableModel und stellt Lagerplatzdaten
 * in tabellarischer Form dar mit Sortier- und Filterfunktionen.
 *
 * <p>Attribute:
 * <ul>
 *   <li>List<LagerPlatz> data – Liste der anzuzeigenden Lagerplätze</li>
 *   <li>String[] columnNames – Namen der Tabellenspalten</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>setData(List) – Setzt neue Lagerplatzdaten</li>
 *   <li>getLagerPlatzAt(int) – Gibt Lagerplatz einer bestimmten Zeile zurück</li>
 *   <li>sortById(boolean) – Sortiert nach Lagerplatz-ID</li>
 *   <li>sortByName(boolean) – Sortiert nach Lagerplatzname</li>
 *   <li>sortByCapacity(boolean) – Sortiert nach Kapazität</li>
 *   <li>setSingleLagerPlatz(LagerPlatz) – Zeigt nur einen einzelnen Lagerplatz an</li>
 * </ul>
 *
 * @author Bjarne von Appen
 * @author Paul Hartmann
 * @author Lennart Höpfner
 */
class LagerPlatzTableModel extends javax.swing.table.AbstractTableModel {
    private static final long serialVersionUID = 2013234800154998283L;
    
    /** Liste der anzuzeigenden Lagerplätze */
    private java.util.List<LagerPlatz> data = new java.util.ArrayList<>();
    /** Namen der Tabellenspalten */
    private String[] columnNames = {"ID", "Name", "Kapazität", "Lager"};

    /**
     * Setzt neue Lagerplatzdaten und aktualisiert die Tabelle.
     *
     * @param data neue Liste der Lagerplätze
     */
    public void setData(java.util.List<LagerPlatz> data) {
        this.data = data != null ? data : new java.util.ArrayList<>();
        fireTableDataChanged();
    }

    /**
     * Zeigt nur einen einzelnen Lagerplatz in der Tabelle an.
     *
     * @param lagerplatz der anzuzeigende Lagerplatz
     * @author Paul Hartmann
     */
    public void setSingleLagerPlatz(LagerPlatz lagerplatz) {
        this.data = new ArrayList<>();
        if (lagerplatz != null) {
            this.data.add(lagerplatz);
        }
        fireTableDataChanged();
    }

    /**
     * Gibt den Lagerplatz einer bestimmten Tabellenzeile zurück.
     *
     * @param row Zeilenindex
     * @return LagerPlatz-Objekt der entsprechenden Zeile
     */
    public LagerPlatz getLagerPlatzAt(int row) {
        return data.get(row);
    }

    /**
     * Sortiert die Lagerplatzdaten nach ID.
     *
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void sortById(boolean ascending) {
        Comparator<LagerPlatz> comparator = Comparator.comparingInt(LagerPlatz::getId);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    /**
     * Sortiert die Lagerplatzdaten nach Name.
     *
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void sortByName(boolean ascending) {
        Comparator<LagerPlatz> comparator = Comparator.comparing(LagerPlatz::getName);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }
    
    /**
     * Sortiert die Lagerplatzdaten nach Kapazität.
     *
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void sortByCapacity(boolean ascending) {
        Comparator<LagerPlatz> comparator = Comparator.comparingInt(LagerPlatz::getKapazitaet);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Anzahl der Lagerplätze
     */
    @Override
    public int getRowCount() {
        return data.size();
    }

    /**
     * Gibt die Anzahl der Spalten in der Tabelle zurück.
     *
     * @return Anzahl der Spalten (4)
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Gibt den Wert einer bestimmten Tabellenzelle zurück.
     *
     * @param row Zeilenindex
     * @param col Spaltenindex
     * @return Zellwert (ID, Name, Kapazität oder Lagername)
     */
    @Override
    public Object getValueAt(int row, int col) {
        LagerPlatz lagerPlatz = data.get(row);
        switch (col) {
            case 0: return lagerPlatz.getId();
            case 1: return lagerPlatz.getName();
            case 2: return lagerPlatz.getKapazitaet();
            case 3: return lagerPlatz.getLager_id() != null ? lagerPlatz.getLager_id().getName() : "Unbekannt";
            default: return null;
        }
    }

    /**
     * Gibt den Namen einer bestimmten Spalte zurück.
     *
     * @param col Spaltenindex
     * @return Spaltenname
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
}
