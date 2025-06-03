package thw.edu.javaII.port.warehouse.ui.model;

import thw.edu.javaII.port.warehouse.model.Bestellung;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Table-Model für die Übersichtsdarstellung von Bestellungen in einer JTable.
 *
 * <p>Diese Klasse erweitert AbstractTableModel und stellt Bestellungsdaten
 * in einer kompakten Übersichtstabelle dar, einschließlich berechneter Werte
 * wie Gesamtpreis und Produktanzahl.
 *
 * <p>Attribute:
 * <ul>
 *   <li>List<Bestellung> bestellungen – Liste der anzuzeigenden Bestellungen</li>
 *   <li>String[] columnNames – Namen der Tabellenspalten</li>
 *   <li>SimpleDateFormat sdf – Formatierung für Datumsangaben</li>
 *   <li>NumberFormat currencyFormat – Formatierung für Währungsbeträge</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>BestellungOverviewTableModel(List) – Konstruktor mit Bestellungsliste</li>
 *   <li>setData(List) – Aktualisiert die Tabellendaten</li>
 *   <li>getValueAt(int, int) – Wert einer bestimmten Zelle</li>
 *   <li>getBestellungAt(int) – Bestellung einer bestimmten Zeile</li>
 *   <li>calculateTotalRevenue() – Berechnet den Gesamtumsatz</li>
 *   <li>isBestellungComplete(int) – Prüft Vollständigkeit einer Bestellung</li>
 * </ul>
 *
 * @author Bjarne von Appen
 */
public class BestellungOverviewTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    /** Liste der anzuzeigenden Bestellungen */
    private List<Bestellung> bestellungen;
    /** Namen der Tabellenspalten */
    private final String[] columnNames = {"Bestellung-ID", "Kunde", "Datum", "Produktanzahl", "Gesamtpreis"};
    /** Formatierung für Datumsangaben */
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    /** Formatierung für Währungsbeträge */
    private final NumberFormat currencyFormat;

    /**
     * Konstruktor initialisiert das Table-Model mit einer Liste von Bestellungen.
     *
     * @param bestellungen Liste der Bestellungen (null wird als leere Liste behandelt)
     */
    public BestellungOverviewTableModel(List<Bestellung> bestellungen) {
        this.bestellungen = bestellungen != null ? bestellungen : new ArrayList<>();
        // Deutsche Zahlenformatierung für Währungsbeträge
        this.currencyFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        this.currencyFormat.setMinimumFractionDigits(2);
        this.currencyFormat.setMaximumFractionDigits(2);
    }

    /**
     * Aktualisiert die Tabellendaten und benachrichtigt alle Listener.
     *
     * @param bestellungen neue Liste der Bestellungen
     */
    public void setData(List<Bestellung> bestellungen) {
        this.bestellungen = bestellungen != null ? bestellungen : new ArrayList<>();
        fireTableDataChanged(); // Benachrichtigt JTable über Datenänderung
    }

    /**
     * Gibt die Anzahl der Zeilen in der Tabelle zurück.
     *
     * @return Anzahl der Bestellungen
     */
    @Override
    public int getRowCount() {
        return bestellungen.size();
    }

    /**
     * Gibt die Anzahl der Spalten in der Tabelle zurück.
     *
     * @return Anzahl der definierten Spalten (5)
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
     * @return Wert der Zelle (ID, Kunde, Datum, Produktanzahl oder Gesamtpreis)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Bestellung bestellung = bestellungen.get(rowIndex);
        switch (columnIndex) {
            case 0: return bestellung.getId();
            case 1: return bestellung.getKunde() != null ? bestellung.getKunde().toString() : "Unbekannt";
            case 2: return bestellung.getDatum() != null ? sdf.format(bestellung.getDatum()) : "Unbekannt";
            case 3: return bestellung.getProdukte() != null ? bestellung.getProdukte().size() : 0;
            case 4:
                // Berechne Gesamtpreis aller Produkte in der Bestellung
                if (bestellung.getProdukte() != null) {
                    double gesamtpreis = bestellung.getProdukte().stream()
                            .mapToDouble(bp -> bp.getProdukt().getPreis() * bp.getAnzahl())
                            .sum();
                    return currencyFormat.format(gesamtpreis) + " €";
                }
                return currencyFormat.format(0.0) + " €";
            default: return null;
        }
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
            case 0: return Integer.class;   // Bestellung-ID
            case 1: return String.class;    // Kunde
            case 2: return String.class;    // Datum
            case 3: return Integer.class;   // Produktanzahl
            case 4: return String.class;    // Gesamtpreis
            default: return Object.class;
        }
    }

    /**
     * Gibt die Bestellung einer bestimmten Tabellenzeile zurück.
     *
     * @param rowIndex Index der Zeile
     * @return Bestellung-Objekt der entsprechenden Zeile
     * @author Bjarne von Appen
     */
    public Bestellung getBestellungAt(int rowIndex) {
        return bestellungen.get(rowIndex);
    }

    /**
     * Prüft, ob eine Bestellung vollständig abgewickelt ist.
     *
     * @param rowIndex Index der zu prüfenden Bestellung
     * @return true wenn alle Zeitstempel gesetzt sind, false sonst
     * @author Bjarne von Appen
     */
    public boolean isBestellungComplete(int rowIndex) {
        Bestellung bestellung = bestellungen.get(rowIndex);
        return bestellung.getErfassung() != null &&
               bestellung.getVersand() != null &&
               bestellung.getLieferung() != null &&
               bestellung.getBezahlung() != null;
    }

    /**
     * Zählt die Anzahl der gesetzten Zeitstempel einer Bestellung.
     *
     * @param rowIndex Index der zu prüfenden Bestellung
     * @return Anzahl der gesetzten Zeitstempel (0-4)
     * @author Bjarne von Appen
     */
    public int countSetTimestamps(int rowIndex) {
        Bestellung bestellung = bestellungen.get(rowIndex);
        int count = 0;
        if (bestellung.getErfassung() != null) count++;
        if (bestellung.getVersand() != null) count++;
        if (bestellung.getLieferung() != null) count++;
        if (bestellung.getBezahlung() != null) count++;
        return count;
    }

    /**
     * Berechnet den Gesamtumsatz aller angezeigten Bestellungen.
     *
     * @return Gesamtumsatz als double-Wert
     * @author Bjarne von Appen
     */
    public double calculateTotalRevenue() {
        double totalRevenue = 0.0;
        for (Bestellung bestellung : bestellungen) {
            if (bestellung.getProdukte() != null) {
                double gesamtpreis = bestellung.getProdukte().stream()
                        .mapToDouble(bp -> bp.getProdukt().getPreis() * bp.getAnzahl())
                        .sum();
                totalRevenue += gesamtpreis;
            }
        }
        return totalRevenue;
    }

    /**
     * Gibt das verwendete Währungsformat zurück.
     *
     * @return NumberFormat für Währungsdarstellung
     */
    public NumberFormat getCurrencyFormat() {
        return currencyFormat;
    }

    /**
     * Setzt eine einzelne Bestellung als einzigen Tabelleneintrag.
     *
     * @param bestellung die anzuzeigende Bestellung (null für leere Tabelle)
     * @author Bjarne von Appen
     */
    public void setSingleBestellung(Bestellung bestellung) {
        this.bestellungen = new ArrayList<>();
        if (bestellung != null) {
            this.bestellungen.add(bestellung);
        }
        fireTableDataChanged(); // Benachrichtigt JTable über Datenänderung
    }
}
