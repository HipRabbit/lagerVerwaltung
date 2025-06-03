package thw.edu.javaII.port.warehouse.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.model.BestellungProdukt;

/**
 * Table-Model für die detaillierte Anzeige von Bestellungen und deren Produkten.
 *
 * <p>Diese Klasse erweitert AbstractTableModel und stellt Bestellungsdaten
 * in einer flachen Tabellenstruktur dar, wobei jede Zeile ein Produkt
 * einer Bestellung repräsentiert. Dadurch können alle Bestellungsdetails
 * in einer einzigen Tabelle angezeigt werden.
 *
 * <p>Attribute:
 * <ul>
 *   <li>List<BestellungProduktEntry> entries – Liste der Bestellungs-Produkt-Einträge</li>
 *   <li>String[] columnNames – Namen der Tabellenspalten</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>BestellungTableModel(List) – Konstruktor mit Bestellungsliste</li>
 *   <li>getRowCount() – Anzahl der Tabellenzeilen</li>
 *   <li>getColumnCount() – Anzahl der Tabellenspalten</li>
 *   <li>getValueAt(int, int) – Wert einer bestimmten Zelle</li>
 *   <li>getColumnClass(int) – Datentyp einer Spalte</li>
 * </ul>
 *
 * @author Bjarne von Appen
 */
public class BestellungTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    /** Liste der Bestellungs-Produkt-Einträge */
    private List<BestellungProduktEntry> entries;
    /** Namen der Tabellenspalten */
    private String[] columnNames = { "Bestellung-ID", "Kunde ID", "Produkt ID", "Hersteller", "Produktname", "Menge", "Einzelpreis", "Gesamtpreis", "Datum" };

    /**
     * Innere Klasse zur Verknüpfung von Bestellung und BestellungProdukt.
     * 
     * <p>Diese Hilfsklasse ermöglicht es, jede Zeile der Tabelle eindeutig
     * einer Bestellung und einem darin enthaltenen Produkt zuzuordnen.
     *
     * @author Bjarne von Appen
     */
    private static class BestellungProduktEntry {
        /** Die zugehörige Bestellung */
        private Bestellung bestellung;
        /** Das Produkt innerhalb der Bestellung */
        private BestellungProdukt produkt;

        /**
         * Konstruktor erstellt einen neuen Bestellungs-Produkt-Eintrag.
         *
         * @param bestellung die zugehörige Bestellung
         * @param produkt    das Produkt innerhalb der Bestellung
         */
        public BestellungProduktEntry(Bestellung bestellung, BestellungProdukt produkt) {
            this.bestellung = bestellung;
            this.produkt = produkt;
        }
    }

    /**
     * Konstruktor erstellt das Table-Model aus einer Liste von Bestellungen.
     *
     * @param data Liste der Bestellungen
     */
    public BestellungTableModel(List<Bestellung> data) {
        this.entries = new ArrayList<>();
        // Flache Struktur erstellen: jedes Produkt jeder Bestellung wird zu einem Eintrag
        for (Bestellung bestellung : data) {
            for (BestellungProdukt produkt : bestellung.getProdukte()) {
                entries.add(new BestellungProduktEntry(bestellung, produkt));
            }
        }
    }

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Anzahl der Bestellungs-Produkt-Einträge
     */
    @Override
    public int getRowCount() {
        return entries.size();
    }

    /**
     * Gibt die Anzahl der Spalten in der Tabelle zurück.
     *
     * @return Anzahl der definierten Spalten (9)
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Gibt den Wert einer bestimmten Tabellenzelle zurück.
     *
     * @param rowIndex    Index der Zeile
     * @param columnIndex Index der Spalte
     * @return Wert der Zelle (Bestellungs-ID, Kunde-ID, Produkt-ID, Hersteller, Produktname, Menge, Einzelpreis, Gesamtpreis oder Datum)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BestellungProduktEntry entry = entries.get(rowIndex);
        Bestellung bestellung = entry.bestellung;
        BestellungProdukt produkt = entry.produkt;
        
        switch (columnIndex) {
            case 0: return bestellung.getId();                                    // Bestellung-ID
            case 1: return bestellung.getKunde().getId();                         // Kunde ID
            case 2: return produkt.getProdukt().getId();                          // Produkt ID
            case 3: return produkt.getProdukt().getHersteller();                  // Hersteller
            case 4: return produkt.getProdukt().getName();                        // Produktname
            case 5: return produkt.getAnzahl();                                   // Menge
            case 6: return produkt.getProdukt().getPreis();                       // Einzelpreis
            case 7: return produkt.getProdukt().getPreis() * produkt.getAnzahl(); // Gesamtpreis
            case 8: return bestellung.getDatum();                                 // Datum
            default: return null;
        }
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
     * Gibt den Datentyp einer bestimmten Spalte zurück.
     *
     * @param columnIndex Index der Spalte
     * @return Class-Objekt des Datentyps
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: case 1: case 2: return Integer.class;   // Bestellung-ID, Kunde ID, Produkt ID
            case 3: case 4: return String.class;            // Hersteller, Produktname
            case 5: return Integer.class;                    // Menge
            case 6: case 7: return Double.class;            // Einzelpreis, Gesamtpreis
            case 8: return java.util.Date.class;            // Datum
            default: return Object.class;
        }
    }
}
