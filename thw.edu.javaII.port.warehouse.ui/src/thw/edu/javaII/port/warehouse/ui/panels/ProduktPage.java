package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import thw.edu.javaII.port.warehouse.model.Produkt;
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
 * Panel zur Anzeige und Verwaltung der Produktübersicht im Lagerverwaltungssystem.
 *
 * <p>Diese Klasse erweitert JPanel und bietet eine umfassende Benutzeroberfläche
 * zur Anzeige aller Produkte mit Such-, Sortier- und Verwaltungsfunktionen wie
 * Hinzufügen, Bearbeiten und Löschen von Produkten.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session ses – aktuelle Benutzersitzung</li>
 *   <li>JTable table – Tabelle zur Anzeige der Produkte</li>
 *   <li>ProduktTableModel tableModel – Datenmodell für die Produkttabelle</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>ProduktPage(Session) – Konstruktor mit UI-Initialisierung</li>
 *   <li>initializeUI() – Erstellt die komplette Benutzeroberfläche</li>
 *   <li>refresh() – Lädt und aktualisiert Produktdaten vom Server</li>
 *   <li>updateSortIndicator(int, boolean) – Aktualisiert Sortierungsanzeige</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
public class ProduktPage extends JPanel {
    private static final long serialVersionUID = 101010L;
    
    /** Aktuelle Benutzersitzung */
    private Session ses;
    /** Tabelle zur Anzeige der Produkte */
    private JTable table;
    /** Datenmodell für die Produkttabelle */
    private ProduktTableModel tableModel;

    /**
     * Konstruktor erstellt das Produktübersichts-Panel.
     *
     * @param ses aktuelle Benutzersitzung
     */
    public ProduktPage(Session ses) {
        this.ses = ses;
        setLayout(new BorderLayout());
        initializeUI();
    }

    /**
     * Initialisiert die komplette Benutzeroberfläche des Panels.
     *
     * @author Lennart Höpfner
     */
    private void initializeUI() {
        // Oberes Panel für Titel, Suche und Sortierbuttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Titel-Label
        JLabel lblTitle = new JLabel("Produktübersicht");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // Suchpanel mit ID-Suche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblIdSearch = new JLabel("Nach ID suchen:");
        JTextField idField = new JTextField(10);
        idField.setToolTipText("Geben Sie die Produkt-ID ein (mind. 4 Ziffern)");
        JButton btnSearch = new JButton("Suchen");
        JButton btnReset = new JButton("Zurücksetzen");

        searchPanel.add(lblIdSearch);
        searchPanel.add(idField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnReset);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Sortierbuttons für ID, Name und Preis
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton sortByIdButton = new JToggleButton("Sortieren nach ID");
        JToggleButton sortByNameButton = new JToggleButton("Sortieren nach Name");
        JToggleButton sortByPreisButton = new JToggleButton("Sortieren nach Preis");

        // Event-Handler für ID-Sortierung
        sortByIdButton.addActionListener(e -> {
            boolean ascending = !sortByIdButton.isSelected();
            tableModel.sortById(ascending);
            sortByIdButton.setText(ascending ? "Sortieren nach ID ↑" : "Sortieren nach ID ↓");
            sortByNameButton.setSelected(false);
            sortByNameButton.setText("Sortieren nach Name");
            sortByPreisButton.setSelected(false);
            sortByPreisButton.setText("Sortieren nach Preis");
            updateSortIndicator(0, ascending); // Spalte 0 (ID)
        });

        // Event-Handler für Name-Sortierung
        sortByNameButton.addActionListener(e -> {
            boolean ascending = !sortByNameButton.isSelected();
            tableModel.sortByName(ascending);
            sortByNameButton.setText(ascending ? "Sortieren nach Name ↑" : "Sortieren nach Name ↓");
            sortByIdButton.setSelected(false);
            sortByIdButton.setText("Sortieren nach ID");
            sortByPreisButton.setSelected(false);
            sortByPreisButton.setText("Sortieren nach Preis");
            updateSortIndicator(1, ascending); // Spalte 1 (Name)
        });

        // Event-Handler für Preis-Sortierung
        sortByPreisButton.addActionListener(e -> {
            boolean ascending = !sortByPreisButton.isSelected();
            tableModel.sortByPreis(ascending);
            sortByPreisButton.setText(ascending ? "Sortieren nach Preis ↑" : "Sortieren nach Preis ↓");
            sortByIdButton.setSelected(false);
            sortByIdButton.setText("Sortieren nach ID");
            sortByNameButton.setSelected(false);
            sortByNameButton.setText("Sortieren nach Name");
            updateSortIndicator(3, ascending); // Spalte 3 (Preis)
        });

        sortPanel.add(sortByIdButton);
        sortPanel.add(sortByNameButton);
        sortPanel.add(sortByPreisButton);
        topPanel.add(sortPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Button-Panel für CRUD-Operationen
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addButton = new JButton("Produkt anlegen");
        JButton editButton = new JButton("Produkt bearbeiten");
        JButton deleteButton = new JButton("Produkt löschen");

        // Event-Handler für Produkt hinzufügen
        addButton.addActionListener(e -> {
            new AddProdukt(ses, this).setVisible(true);
            refresh(); // Tabelle nach Hinzufügen aktualisieren
        });

        // Event-Handler für Produkt bearbeiten
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Produkt selectedProdukt = tableModel.getProduktAt(selectedRow);
                new EditProdukt(ses, null, selectedProdukt).setVisible(true);
                refresh(); // Tabelle nach Bearbeitung aktualisieren
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Produkt aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event-Handler für Produkt löschen
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Produkt selectedProdukt = tableModel.getProduktAt(selectedRow);
                new DeleteProdukt(ses, null, selectedProdukt).setVisible(true);
                refresh(); // Tabelle nach Löschung aktualisieren
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Produkt aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Produkttabelle initialisieren
        tableModel = new ProduktTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new SortIndicatorHeaderRenderer(table.getTableHeader().getDefaultRenderer()));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Event-Handler für ID-Suche
        btnSearch.addActionListener(e -> {
            String input = idField.getText().trim();
            
            // Validierung: Mindestens 4 Ziffern erforderlich
            if (!input.matches("\\d{4,}")) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie eine ID mit mindestens 4 Ziffern ein!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return; // Beende die Ausführung, Tabelle bleibt unverändert
            }

            try {
                int id = Integer.parseInt(input);
                Produkt produkt = ses.getCommunicator().getProduktById(id);
                if (produkt != null && produkt.getId() > 0) {
                    tableModel.setSingleProdukt(produkt);
                } else {
                    JOptionPane.showMessageDialog(this, "Produkt mit ID " + id + " nicht gefunden!", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                    // Tabelle unverändert lassen
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
            refresh(); // Alle Produkte wieder anzeigen
        });

        refresh(); // Initiale Daten laden
    }

    /**
     * Lädt und aktualisiert die Produktdaten vom Server.
     *
     * @author Lennart Höpfner
     */
    public void refresh() {
        try {
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.PRODUKT);
            deo.setCommand(Command.LIST);
            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                java.util.List<Produkt> produktList = Cast.safeListCast(ret.getData(), Produkt.class);
                tableModel.setData(produktList);
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Produkte: " + ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
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
 * Tabellenmodell für die Anzeige von Produktdaten.
 *
 * <p>Diese Klasse erweitert AbstractTableModel und stellt Produktdaten
 * in tabellarischer Form dar mit Sortier- und Filterfunktionen sowie
 * deutscher Währungsformatierung für Preise.
 *
 * <p>Attribute:
 * <ul>
 *   <li>List<Produkt> data – Liste der anzuzeigenden Produkte</li>
 *   <li>String[] columnNames – Namen der Tabellenspalten</li>
 *   <li>DecimalFormat priceFormat – Formatierung für Preisanzeige</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>ProduktTableModel() – Konstruktor mit Preisformatierung</li>
 *   <li>setData(List) – Setzt neue Produktdaten</li>
 *   <li>getProduktAt(int) – Gibt Produkt einer bestimmten Zeile zurück</li>
 *   <li>sortById(boolean) – Sortiert nach Produkt-ID</li>
 *   <li>sortByName(boolean) – Sortiert nach Produktname</li>
 *   <li>sortByPreis(boolean) – Sortiert nach Preis</li>
 *   <li>setSingleProdukt(Produkt) – Zeigt nur ein einzelnes Produkt an</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
class ProduktTableModel extends javax.swing.table.AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    /** Liste der anzuzeigenden Produkte */
    private java.util.List<Produkt> data = new java.util.ArrayList<>();
    /** Namen der Tabellenspalten */
    private String[] columnNames = {"ID", "Name", "Hersteller", "Preis"};
    /** Formatierung für Preisanzeige in deutscher Notation */
    private final DecimalFormat priceFormat;

    /**
     * Konstruktor initialisiert das Tabellenmodell mit deutscher Preisformatierung.
     */
    public ProduktTableModel() {
        // Deutsche Zahlenformatierung für Preise
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        priceFormat = new DecimalFormat("#,##0.00 €", symbols);
    }

    /**
     * Zeigt nur ein einzelnes Produkt in der Tabelle an.
     *
     * @param produkt das anzuzeigende Produkt
     * @author Lennart Höpfner
     */
    public void setSingleProdukt(Produkt produkt) {
        this.data = new ArrayList<>();
        if (produkt != null) {
            this.data.add(produkt);
        }
        fireTableDataChanged();
    }

    /**
     * Setzt neue Produktdaten und aktualisiert die Tabelle.
     *
     * @param data neue Liste der Produkte
     */
    public void setData(java.util.List<Produkt> data) {
        this.data = data != null ? data : new java.util.ArrayList<>();
        fireTableDataChanged();
    }

    /**
     * Gibt das Produkt einer bestimmten Tabellenzeile zurück.
     *
     * @param row Zeilenindex
     * @return Produkt-Objekt der entsprechenden Zeile
     */
    public Produkt getProduktAt(int row) {
        return data.get(row);
    }

    /**
     * Sortiert die Produktdaten nach ID.
     *
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void sortById(boolean ascending) {
        Comparator<Produkt> comparator = Comparator.comparingInt(Produkt::getId);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    /**
     * Sortiert die Produktdaten nach Name.
     *
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void sortByName(boolean ascending) {
        Comparator<Produkt> comparator = Comparator.comparing(Produkt::getName);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    /**
     * Sortiert die Produktdaten nach Preis.
     *
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void sortByPreis(boolean ascending) {
        Comparator<Produkt> comparator = Comparator.comparing(Produkt::getPreis);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Anzahl der Produkte
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
     * @return Zellwert (ID, Name, Hersteller oder formatierter Preis)
     */
    @Override
    public Object getValueAt(int row, int col) {
        Produkt produkt = data.get(row);
        switch (col) {
            case 0: return produkt.getId();
            case 1: return produkt.getName();
            case 2: return produkt.getHersteller();
            case 3: return priceFormat.format(produkt.getPreis()); // Formatierter Preis
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
