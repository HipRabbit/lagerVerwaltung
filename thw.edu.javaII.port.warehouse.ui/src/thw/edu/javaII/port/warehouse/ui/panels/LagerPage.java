package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import thw.edu.javaII.port.warehouse.model.Lager;
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
 * Panel zur Anzeige und Verwaltung der Lagerübersicht im Lagerverwaltungssystem.
 *
 * <p>Diese Klasse erweitert JPanel und bietet eine umfassende Benutzeroberfläche
 * zur Anzeige aller Lager mit Such-, Sortier- und Verwaltungsfunktionen wie
 * Hinzufügen, Bearbeiten und Löschen von Lagern.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session ses – aktuelle Benutzersitzung</li>
 *   <li>JTable table – Tabelle zur Anzeige der Lager</li>
 *   <li>LagerTableModel tableModel – Datenmodell für die Lagertabelle</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>LagerPage(Session) – Konstruktor mit UI-Initialisierung</li>
 *   <li>initializeUI() – Erstellt die komplette Benutzeroberfläche</li>
 *   <li>refresh() – Lädt und aktualisiert Lagerdaten vom Server</li>
 *   <li>updateSortIndicator(int, boolean) – Aktualisiert Sortierungsanzeige</li>
 * </ul>
 *
 * @author Bjarne von Appen
 * @author Paul Hartmann
 * @author Lennart Höpfner
 */
public class LagerPage extends JPanel {
    private static final long serialVersionUID = 10391231L;
    
    /** Aktuelle Benutzersitzung */
    private Session ses;
    /** Tabelle zur Anzeige der Lager */
    private JTable table;
    /** Datenmodell für die Lagertabelle */
    private LagerTableModel tableModel;

    /**
     * Konstruktor erstellt das Lagerübersichts-Panel.
     *
     * @param ses aktuelle Benutzersitzung
     */
    public LagerPage(Session ses) {
        this.ses = ses;
        setLayout(new BorderLayout());
        initializeUI();
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
        JLabel lblTitle = new JLabel("Lagerübersicht");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // Suchpanel mit ID-Suche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblIdSearch = new JLabel("Nach ID suchen:");
        JTextField idField = new JTextField(10);
        idField.setToolTipText("Geben Sie die Lager-ID ein (mind. 4 Ziffern)");
        JButton btnSearch = new JButton("Suchen");
        JButton btnReset = new JButton("Zurücksetzen");

        searchPanel.add(lblIdSearch);
        searchPanel.add(idField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnReset);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Sortierbuttons für ID und Name
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton sortByIdButton = new JToggleButton("Sortieren nach ID");
        JToggleButton sortByNameButton = new JToggleButton("Sortieren nach Name");

        // Event-Handler für ID-Sortierung
        sortByIdButton.addActionListener(e -> {
            boolean ascending = !sortByIdButton.isSelected();
            tableModel.sortById(ascending);
            sortByIdButton.setText(ascending ? "Sortieren nach ID ↑" : "Sortieren nach ID ↓");
            sortByNameButton.setSelected(false);
            sortByNameButton.setText("Sortieren nach Name");
            updateSortIndicator(0, ascending);
        });

        // Event-Handler für Name-Sortierung
        sortByNameButton.addActionListener(e -> {
            boolean ascending = !sortByNameButton.isSelected();
            tableModel.sortByName(ascending);
            sortByNameButton.setText(ascending ? "Sortieren nach Name ↑" : "Sortieren nach Name ↓");
            sortByIdButton.setSelected(false);
            sortByIdButton.setText("Sortieren nach ID");
            updateSortIndicator(1, ascending);
        });

        sortPanel.add(sortByIdButton);
        sortPanel.add(sortByNameButton);
        topPanel.add(sortPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Button-Panel für CRUD-Operationen
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Lager anlegen");
        JButton editButton = new JButton("Lager bearbeiten");
        JButton deleteButton = new JButton("Lager löschen");

        // Event-Handler für Lager hinzufügen
        addButton.addActionListener(e -> {
            AddLager dialog = new AddLager(ses, (JFrame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            refresh(); // Tabelle nach Hinzufügen aktualisieren
        });

        // Event-Handler für Lager bearbeiten
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Lager selectedLager = tableModel.getLagerAt(selectedRow);
                EditLager dialog = new EditLager(ses, (JFrame) SwingUtilities.getWindowAncestor(this));
                dialog.setSelectedLager(selectedLager);
                dialog.setVisible(true);
                refresh(); // Tabelle nach Bearbeitung aktualisieren
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Lager aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event-Handler für Lager löschen
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Lager selectedLager = tableModel.getLagerAt(selectedRow);
                DeleteLager dialog = new DeleteLager(ses, (JFrame) SwingUtilities.getWindowAncestor(this));
                dialog.setSelectedLager(selectedLager);
                dialog.setVisible(true);
                refresh(); // Tabelle nach Löschung aktualisieren
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Lager aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Lagertabelle initialisieren
        tableModel = new LagerTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);

        // Benutzerdefinierten Header-Renderer für Sortierungsanzeige setzen
        table.getTableHeader().setDefaultRenderer(new SortIndicatorHeaderRenderer(table.getTableHeader().getDefaultRenderer()));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Event-Handler für ID-Suche
        btnSearch.addActionListener(e -> {
            String input = idField.getText().trim();
            
            // Validierung: Mindestens 4 Ziffern erforderlich
            if (!input.matches("\\d{4,}")) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie eine ID mit mindestens 4 Ziffern ein!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return; // Beende Ausführung, Tabelle bleibt unverändert
            }

            try {
                int id = Integer.parseInt(input);
                Lager lager = ses.getCommunicator().getLagerById(id);
                if (lager != null && lager.getId() > 0) {
                    // Validierung: Prüfen ob alle Felder gesetzt sind
                    if (lager.getName() == null || lager.getName().isEmpty() ||
                        lager.getOrt() == null || lager.getOrt().isEmpty() ||
                        lager.getArt() == null || lager.getArt().isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Lager mit ID " + id + " ist unvollständig (Name, Ort oder Art fehlen). Es wird nicht angezeigt.", 
                            "Fehler", JOptionPane.ERROR_MESSAGE);
                        // Tabelle unverändert lassen
                    } else {
                        tableModel.setSingleLager(lager);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Kein Lager mit der ID " + id + " gefunden!", 
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                    // Tabelle unverändert lassen
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Bitte geben Sie eine gültige numerische ID ein!", 
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Fehler bei der Kommunikation: " + ex.getMessage(), 
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event-Handler für Suchfeld zurücksetzen
        btnReset.addActionListener(e -> {
            idField.setText("");
            refresh(); // Alle Lager wieder anzeigen
        });

        refresh(); // Initiale Daten laden
    }

    /**
     * Lädt und aktualisiert die Lagerdaten vom Server.
     *
     * @author Paul Hartmann
     */
    public void refresh() {
        try {
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGER);
            deo.setCommand(Command.LIST);
            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                java.util.List<Lager> lagerList = Cast.safeListCast(ret.getData(), Lager.class);
                tableModel.setData(lagerList);
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Lager: " + ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
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
 * Tabellenmodell für die Anzeige von Lagerdaten.
 *
 * <p>Diese Klasse erweitert AbstractTableModel und stellt Lagerdaten
 * in tabellarischer Form dar mit Sortier- und Filterfunktionen.
 *
 * <p>Attribute:
 * <ul>
 *   <li>List<Lager> data – Liste der anzuzeigenden Lager</li>
 *   <li>String[] columnNames – Namen der Tabellenspalten</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>setData(List) – Setzt neue Lagerdaten</li>
 *   <li>getLagerAt(int) – Gibt Lager einer bestimmten Zeile zurück</li>
 *   <li>sortById(boolean) – Sortiert nach Lager-ID</li>
 *   <li>sortByName(boolean) – Sortiert nach Lagername</li>
 *   <li>setSingleLager(Lager) – Zeigt nur ein einzelnes Lager an</li>
 * </ul>
 *
 * @author Bjarne von Appen
 * @author Paul Hartmann
 * @author Lennart Höpfner
 */
class LagerTableModel extends javax.swing.table.AbstractTableModel {
    private static final long serialVersionUID = -5706053428116906813L;
    
    /** Liste der anzuzeigenden Lager */
    private java.util.List<Lager> data = new java.util.ArrayList<>();
    /** Namen der Tabellenspalten */
    private String[] columnNames = {"ID", "Name", "Ort", "Art"};

    /**
     * Setzt neue Lagerdaten und aktualisiert die Tabelle.
     *
     * @param data neue Liste der Lager
     */
    public void setData(java.util.List<Lager> data) {
        this.data = data != null ? data : new java.util.ArrayList<>();
        fireTableDataChanged();
    }

    /**
     * Gibt das Lager einer bestimmten Tabellenzeile zurück.
     *
     * @param row Zeilenindex
     * @return Lager-Objekt der entsprechenden Zeile
     */
    public Lager getLagerAt(int row) {
        return data.get(row);
    }

    /**
     * Sortiert die Lagerdaten nach ID.
     *
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void sortById(boolean ascending) {
        Comparator<Lager> comparator = Comparator.comparingInt(Lager::getId);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    /**
     * Sortiert die Lagerdaten nach Name.
     *
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void sortByName(boolean ascending) {
        Comparator<Lager> comparator = Comparator.comparing(Lager::getName);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    /**
     * Zeigt nur ein einzelnes Lager in der Tabelle an.
     *
     * @param lager das anzuzeigende Lager
     * @author Paul Hartmann
     */
    public void setSingleLager(Lager lager) {
        this.data = new ArrayList<>();
        if (lager != null) {
            this.data.add(lager);
        }
        fireTableDataChanged();
    }

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Anzahl der Lager
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
     * @return Zellwert (ID, Name, Ort oder Art)
     */
    @Override
    public Object getValueAt(int row, int col) {
        Lager lager = data.get(row);
        switch (col) {
            case 0: return lager.getId();
            case 1: return lager.getName();
            case 2: return lager.getOrt();
            case 3: return lager.getArt();
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
