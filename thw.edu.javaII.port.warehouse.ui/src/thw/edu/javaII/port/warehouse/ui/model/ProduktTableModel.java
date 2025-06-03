package thw.edu.javaII.port.warehouse.ui.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import thw.edu.javaII.port.warehouse.model.Produkt;

/**
 * Table-Model für die Anzeige von Produktdaten in einer JTable.
 *
 * <p>Diese Klasse erweitert AbstractTableModel und stellt Produktdaten
 * in tabellarischer Form dar mit Spalten für ID, Name, Hersteller und Preis.
 *
 * <p>Attribute:
 * <ul>
 *   <li>List<Produkt> produktList – Liste der anzuzeigenden Produkte</li>
 *   <li>String[] columnNames – Namen der Tabellenspalten</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>setProduktList(List) – Setzt neue Produktdaten</li>
 *   <li>getRowCount() – Anzahl der Tabellenzeilen</li>
 *   <li>getColumnCount() – Anzahl der Tabellenspalten</li>
 *   <li>getValueAt(int, int) – Wert einer bestimmten Zelle</li>
 *   <li>getColumnName(int) – Name einer Spalte</li>
 * </ul>
 *
 * @author Bjarne von Appen
 * @author Paul Hartmann
 * @author Lennart Höpfner
 */
public class ProduktTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 92938247L;
    
    /** Liste der anzuzeigenden Produkte */
    private List<Produkt> produktList = new ArrayList<>();
    /** Namen der Tabellenspalten */
    private final String[] columnNames = {"ID", "Name", "Hersteller", "Preis"};

    /**
     * Setzt neue Produktdaten und aktualisiert die Tabelle.
     *
     * @param produktList neue Liste der Produkte
     */
    public void setProduktList(List<Produkt> produktList) {
        this.produktList = produktList;
        fireTableDataChanged(); // Benachrichtigt JTable über Datenänderung
    }

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Anzahl der Produkte
     */
    @Override
    public int getRowCount() {
        return produktList.size();
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
     * @return Wert der Zelle (ID, Name, Hersteller oder Preis)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Produkt produkt = produktList.get(rowIndex);
        switch (columnIndex) {
            case 0: return produkt.getId();          // Produkt-ID
            case 1: return produkt.getName();       // Produktname
            case 2: return produkt.getHersteller();  // Hersteller
            case 3: return produkt.getPreis();      // Preis
            default: return null;
        }
    }
}
