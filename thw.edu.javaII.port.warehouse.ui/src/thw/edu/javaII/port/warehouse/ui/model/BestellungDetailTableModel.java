package thw.edu.javaII.port.warehouse.ui.model;

import thw.edu.javaII.port.warehouse.model.BestellungProdukt;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table-Model für die Darstellung von Bestellungsdetails in einer JTable.
 *
 * <p>Diese Klasse erweitert AbstractTableModel und stellt die Daten
 * von BestellungProdukt-Objekten in tabellarischer Form dar.
 *
 * <p>Attribute:
 * <ul>
 *   <li>List<BestellungProdukt> produkte – Liste der Bestellungsprodukte</li>
 *   <li>String[] columnNames – Namen der Tabellenspalten</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>BestellungDetailTableModel(List) – Konstruktor mit Produktliste</li>
 *   <li>getRowCount() – Anzahl der Tabellenzeilen</li>
 *   <li>getColumnCount() – Anzahl der Tabellenspalten</li>
 *   <li>getValueAt(int, int) – Wert einer bestimmten Zelle</li>
 *   <li>getColumnClass(int) – Datentyp einer Spalte</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
public class BestellungDetailTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    /** Liste der Bestellungsprodukte */
    private final List<BestellungProdukt> produkte;
    /** Namen der Tabellenspalten */
    private final String[] columnNames = {"Produktname", "Anzahl", "Einzelpreis (EUR)", "Gesamtpreis (EUR)"};

    /**
     * Konstruktor initialisiert das Table-Model mit einer Liste von BestellungProdukt-Objekten.
     *
     * @param produkte Liste der Bestellungsprodukte (null wird als leere Liste behandelt)
     */
    public BestellungDetailTableModel(List<BestellungProdukt> produkte) {
        // Null-Schutz: verwende leere Liste falls null übergeben wird
        this.produkte = produkte != null ? produkte : new ArrayList<>();
    }

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Anzahl der BestellungProdukt-Einträge
     */
    @Override
    public int getRowCount() {
        return produkte.size();
    }

    /**
     * Gibt die Anzahl der Spalten in der Tabelle zurück.
     *
     * @return Anzahl der definierten Spalten (4)
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
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
     * Gibt den Wert einer bestimmten Tabellenzelle zurück.
     *
     * @param rowIndex    Index der Zeile
     * @param columnIndex Index der Spalte
     * @return Wert der Zelle (Produktname, Anzahl, Einzelpreis oder Gesamtpreis)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BestellungProdukt bp = produkte.get(rowIndex);
        switch (columnIndex) {
            case 0: return bp.getProdukt().getName();                           // Produktname
            case 1: return bp.getAnzahl();                                      // Anzahl
            case 2: return bp.getProdukt().getPreis();                          // Einzelpreis
            case 3: return bp.getProdukt().getPreis() * bp.getAnzahl();         // Gesamtpreis
            default: return null;
        }
    }

    /**
     * Gibt den Datentyp einer bestimmten Spalte zurück.
     *
     * @param columnIndex Index der Spalte
     * @return Class-Objekt des Datentyps (String, Integer oder Double)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return String.class;    // Produktname
            case 1: return Integer.class;   // Anzahl
            case 2: 
            case 3: return Double.class;    // Einzelpreis und Gesamtpreis
            default: return Object.class;
        }
    }
}
