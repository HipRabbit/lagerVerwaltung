package thw.edu.javaII.port.warehouse.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import thw.edu.javaII.port.warehouse.model.Kunde;

/**
 * Table-Model für die Darstellung von Kunden in einer JTable mit Filter- und Sortierfunktionen.
 *
 * <p>Diese Klasse erweitert AbstractTableModel und bietet erweiterte Funktionalitäten
 * wie Filtern nach Namen und Sortieren nach verschiedenen Kriterien.
 *
 * <p>Attribute:
 * <ul>
 *   <li>List<Kunde> originalData – vollständige, unveränderte Kundenliste</li>
 *   <li>List<Kunde> filteredData – gefilterte und sortierte Kundenliste für Anzeige</li>
 *   <li>String[] columnNames – Namen der Tabellenspalten</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>KundeTableModel(List) – Konstruktor mit Kundenliste</li>
 *   <li>setData(List) – Aktualisiert die Tabellendaten</li>
 *   <li>sortById() – Sortiert nach Kunden-ID</li>
 *   <li>sortByName() – Sortiert alphabetisch nach Namen</li>
 *   <li>filterByName(String) – Filtert nach Namensbestandteilen</li>
 *   <li>getObjectAt(int) – Gibt Kunde einer bestimmten Zeile zurück</li>
 * </ul>
 *
 * @author Paul Hartmann
 */
public class KundeTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    /** Vollständige, unveränderte Kundenliste */
    private List<Kunde> originalData;
    /** Gefilterte und sortierte Kundenliste für die Anzeige */
    private List<Kunde> filteredData;
    /** Namen der Tabellenspalten */
    private String[] columnNames = { "ID", "Vorname", "Nachname", "Lieferadresse", "Rechnungsadresse" };

    /**
     * Konstruktor initialisiert das Table-Model mit einer Liste von Kunden.
     *
     * @param data Liste der Kunden
     */
    public KundeTableModel(List<Kunde> data) {
        // Defensive Kopien erstellen, um Originaldata zu schützen
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    /**
     * Aktualisiert die Tabellendaten und setzt Filter zurück.
     *
     * @param data neue Liste der Kunden
     */
    public void setData(List<Kunde> data) {
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
        fireTableDataChanged(); // Benachrichtigt JTable über Datenänderung
    }

    /**
     * Sortiert die gefilterten Daten nach Kunden-ID aufsteigend.
     *
     * @author Paul Hartmann
     */
    public void sortById() {
        Collections.sort(filteredData, Comparator.comparingInt(Kunde::getId));
        fireTableDataChanged();
    }

    /**
     * Sortiert die gefilterten Daten alphabetisch nach Vor- und Nachname.
     *
     * @author Paul Hartmann
     */
    public void sortByName() {
        Collections.sort(filteredData, Comparator.comparing(
            k -> (k.getVorname() + " " + k.getNachname()), 
            String.CASE_INSENSITIVE_ORDER));
        fireTableDataChanged();
    }

    /**
     * Filtert die Kundenliste nach einem Suchbegriff in Vor- oder Nachname.
     *
     * @param searchTerm Suchbegriff (null oder leer zeigt alle Kunden)
     * @author Paul Hartmann
     */
    public void filterByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // Kein Filter: zeige alle ursprünglichen Daten
            filteredData = new ArrayList<>(originalData);
        } else {
            String lowerCaseTerm = searchTerm.trim().toLowerCase();
            // Filtere nach Vor-, Nachname oder Vollname
            filteredData = originalData.stream()
                    .filter(k -> k.getVorname().toLowerCase().contains(lowerCaseTerm) ||
                                 k.getNachname().toLowerCase().contains(lowerCaseTerm) ||
                                 (k.getVorname() + " " + k.getNachname()).toLowerCase().contains(lowerCaseTerm))
                    .collect(Collectors.toList());
        }
        fireTableDataChanged();
    }

    /**
     * Gibt die Anzahl der gefilterten Zeilen zurück.
     *
     * @return Anzahl der sichtbaren Kunden nach Filterung
     * @author Paul Hartmann
     */
    public int getFilteredRowCount() {
        return filteredData.size();
    }

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Anzahl der gefilterten Kunden
     */
    @Override
    public int getRowCount() {
        return filteredData.size();
    }

    /**
     * Gibt die Anzahl der Spalten in der Tabelle zurück.
     *
     * @return Anzahl der Spalten (aus Kunde.columnCount)
     */
    @Override
    public int getColumnCount() {
        return Kunde.columnCount;
    }

    /**
     * Gibt den Wert einer bestimmten Tabellenzelle zurück.
     *
     * @param rowIndex    Index der Zeile
     * @param columnIndex Index der Spalte
     * @return Wert der Zelle (delegiert an Kunde.getValueAtColumn)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return filteredData.get(rowIndex).getValueAtColumn(columnIndex);
    }

    /**
     * Gibt den Namen einer bestimmten Spalte zurück.
     *
     * @param column Index der Spalte
     * @return Name der Spalte
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Gibt das Kunden-Objekt einer bestimmten Tabellenzeile zurück.
     *
     * @param row Index der Zeile
     * @return Kunde-Objekt der entsprechenden Zeile
     * @author Paul Hartmann
     */
    public Kunde getObjectAt(int row) {
        return filteredData.get(row);
    }
}
