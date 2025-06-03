package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Modelliert einen Produkt-Eintrag in einer Bestellung.
 *
 * <p>Enthält folgende Attribute:
 * <ul>
 *   <li>Produkt produkt – das bestellte Produkt</li>
 *   <li>int anzahl – Menge des Produkts</li>
 * </ul>
 *
 * <p>Verfügbare Methoden:
 * <ul>
 *   <li>Konstruktoren zur Initialisierung</li>
 *   <li>Getter- und Setter-Methoden für produkt und anzahl</li>
 * </ul>
 *
 * @author Bjarne von Appen
 */
public class BestellungProdukt implements Serializable {
    private static final long serialVersionUID = 1L; // Versions-ID für Serializable
    
    private Produkt produkt; // referenziertes Produkt
    private int anzahl;       // bestellte Menge

    /**
     * Standard-Konstruktor für eine leere BestellungProdukt-Instanz.
     */
    public BestellungProdukt() {
    }

    /**
     * Erzeugt einen neuen BestellungProdukt-Eintrag mit Produkt und Menge.
     *
     * @param produkt das bestellte Produkt
     * @param anzahl  die Menge des Produkts (muss ≥ 1 sein)
     */
    public BestellungProdukt(Produkt produkt, int anzahl) {
        this.produkt = produkt;
        this.anzahl = anzahl;
    }

    /**
     * Gibt das bestellte Produkt zurück.
     *
     * @return das Produkt der Bestellung
     */
    public Produkt getProdukt() {
        return produkt;
    }

    /**
     * Setzt das bestellte Produkt.
     *
     * @param produkt neues Produkt für diesen Eintrag
     */
    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    /** Hier alle weiteren Set- und Get-Methoden für die Attribute der Klasse. */
    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
}
