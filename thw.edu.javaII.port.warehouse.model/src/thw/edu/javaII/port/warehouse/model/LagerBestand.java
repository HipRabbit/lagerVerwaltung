package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Repräsentiert den Bestand eines Produkts an einem bestimmten Lagerplatz im Lagerverwaltungssystem.
 * 
 * @author Lennart Höpfner
 */
public class LagerBestand implements Serializable {
    private static final long serialVersionUID = 5335970888396140828L;
    private static final String PRINT_FORMAT = "[%-20s - %-50s - %-30s - %-40s]";
    private int id;
    private int anzahl;
    private Produkt produkt_id;
    private LagerPlatz lagerplatz_id;
    public static final int columnCount = 6;

    /**
     * Gibt die ID des Lagerbestands zurück.
     * 
     * @return die ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID des Lagerbestands.
     * 
     * @param id die zu setzende ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt die Anzahl des Produkts im Lagerbestand zurück.
     * 
     * @return die Anzahl
     */
    public int getAnzahl() {
        return anzahl;
    }

    /**
     * Setzt die Anzahl des Produkts im Lagerbestand.
     * 
     * @param anzahl die zu setzende Anzahl
     */
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    /**
     * Gibt das Produkt des Lagerbestands zurück.
     * 
     * @return das {@link Produkt}-Objekt
     */
    public Produkt getProdukt_id() {
        return produkt_id;
    }

    /**
     * Setzt das Produkt des Lagerbestands.
     * 
     * @param produkt_id das zu setzende {@link Produkt}-Objekt
     */
    public void setProdukt_id(Produkt produkt_id) {
        this.produkt_id = produkt_id;
    }

    /**
     * Gibt den Lagerplatz des Lagerbestands zurück.
     * 
     * @return das {@link LagerPlatz}-Objekt
     */
    public LagerPlatz getLagerplatz_id() {
        return lagerplatz_id;
    }

    /**
     * Setzt den Lagerplatz des Lagerbestands.
     * 
     * @param lagerplatz_id das zu setzende {@link LagerPlatz}-Objekt
     */
    public void setLagerplatz_id(LagerPlatz lagerplatz_id) {
        this.lagerplatz_id = lagerplatz_id;
    }

    /**
     * Standardkonstruktor.
     */
    public LagerBestand() {
    }

    /**
     * Konstruktor mit allen Attributen.
     * 
     * @param id die ID des Lagerbestands
     * @param anzahl die Anzahl des Produkts
     * @param produkt_id das {@link Produkt}-Objekt
     * @param lagerplatz_id das {@link LagerPlatz}-Objekt
     */
    public LagerBestand(int id, int anzahl, Produkt produkt_id, LagerPlatz lagerplatz_id) {
        this.id = id;
        this.anzahl = anzahl;
        this.produkt_id = produkt_id;
        this.lagerplatz_id = lagerplatz_id;
    }

    /**
     * Gibt eine formatierte String-Darstellung des Lagerbestands für Listen zurück.
     * 
     * @return die formatierte String-Darstellung
     */
    public String toListString() {
        return String.format(PRINT_FORMAT, id, anzahl, produkt_id.getName(), lagerplatz_id.getName());
    }

    /**
     * Gibt den Wert einer bestimmten Spalte des Lagerbestands zurück.
     * 
     * @param column die Spaltennummer (0 für Produkt-ID, 1 für Produktname, 2 für Hersteller, 3 für Anzahl, 4 für Lagerplatzname, 5 für Lagernamen)
     * @return der Wert der Spalte oder null, wenn die Spalte ungültig ist
     */
    public Object getValueAtColumn(int column) {
        switch (column) {
            case 0:
                return getProdukt_id().getId();
            case 1:
                return getProdukt_id().getName();
            case 2:
                return getProdukt_id().getHersteller();
            case 3:
                return getAnzahl();
            case 4:
                return getLagerplatz_id().getName();
            case 5:
                return getLagerplatz_id().getLager_id().getName();
            default:
                return null;
        }
    }
}