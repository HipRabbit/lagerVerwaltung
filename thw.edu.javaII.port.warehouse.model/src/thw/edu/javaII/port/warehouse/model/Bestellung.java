package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Repräsentiert eine Bestellung im Lagerverwaltungssystem.
 *
 * <p>Enthält folgende Attribute:
 * <ul>
 *   <li>int id – eindeutige Bestellnummer</li>
 *   <li>Kunde kunde – der Auftraggeber</li>
 *   <li>List<BestellungProdukt> produkte – bestellte Produkte</li>
 *   <li>Date datum – gewünschtes Lieferdatum</li>
 *   <li>Date erfassung – Zeitpunkt der Auftragserfassung</li>
 *   <li>Date versand – Versanddatum</li>
 *   <li>Date lieferung – Lieferdatum</li>
 *   <li>Date bezahlung – Zahlungsdatum</li>
 *   <li>String status – aktueller Bestellstatus</li>
 * </ul>
 *
 * <p>Verfügbare Methoden:
 * <ul>
 *   <li>Konstruktoren zum Anlegen einer Bestellung</li>
 *   <li>Getter- und Setter-Methoden für alle Attribute</li>
 *   <li>Berechnung des Gesamtwerts der Bestellung</li>
 * </ul>
 *
 * @author Bjarne von Appen
 */
public class Bestellung implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Kunde kunde;
    private List<BestellungProdukt> produkte;
    private Date datum;
    private Date erfassung;
    private Date versand;
    private Date lieferung;
    private Date bezahlung;
    private String status;

    /**
     * Standard-Konstruktor: legt eine leere Bestellung an.
     *
     * @param – keine Parameter
     */
    public Bestellung() {
        // Leere Produktliste und offener Status als Default
        this.produkte = new ArrayList<>();
        this.status = "offen";
    }

    /**
     * Konstruktor mit allen wichtigen Feldern.
     *
     * @param id       eindeutige Bestellnummer
     * @param kunde    der Kunde, der bestellt
     * @param produkte Liste der bestellten Produkte (null → leere Liste)
     * @param datum    gewünschtes Lieferdatum (null → kein Datum)
     */
    public Bestellung(int id, Kunde kunde, List<BestellungProdukt> produkte, Date datum) {
        this.id = id;
        this.kunde = kunde;
        // Produkte defensiv kopieren oder leere Liste
        this.produkte = (produkte != null ? new ArrayList<>(produkte) : new ArrayList<>());
        // Datum defensiv kopieren
        this.datum = (datum != null ? new Date(datum.getTime()) : null);
        this.status = "offen";
    }

    /**
     * Konstruktor für neue Bestellungen: ID von DB, Erfassungsdatum = jetzt.
     *
     * @param kunde    Kunde, der die Bestellung auslöst
     * @param datum    gewünschtes Lieferdatum (null → aktuelles Datum)
     * @param produkte Liste der Produkte (null → leere Liste)
     */
    public Bestellung(Kunde kunde, Date datum, List<BestellungProdukt> produkte) {
        this.id = 0;  // ID wird später von der Datenbank vergeben
        this.kunde = kunde;
        this.produkte = (produkte != null ? new ArrayList<>(produkte) : new ArrayList<>());
        this.datum = (datum != null ? new Date(datum.getTime()) : new Date());
        // Erfassungszeitpunkt immer "jetzt"
        this.erfassung = new Date();
        this.status = "offen";
    }

    /**
     * Liefert die eindeutige ID der Bestellung.
     *
     * @return Bestell-ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die eindeutige ID der Bestellung.
     *
     * @param id neue Bestell-ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Hier alle weiteren Getter- und Setter-Methoden für die übrigen Attribute. */
    public Kunde getKunde() { return kunde; }
    public void setKunde(Kunde kunde) { this.kunde = kunde; }
    public List<BestellungProdukt> getProdukte() { return produkte; }
    public void setProdukte(List<BestellungProdukt> produkte) {
        // Null-Schutz: immer eine Liste vorhalten
        this.produkte = (produkte != null ? produkte : new ArrayList<>());
    }
    public Date getDatum() {
        if (datum != null) return new Date(datum.getTime());
        if (erfassung != null) return new Date(erfassung.getTime());
        return null;
    }
    public void setDatum(Date datum) {
        this.datum = (datum != null ? new Date(datum.getTime()) : null);
    }
    public Date getErfassung() { return erfassung; }
    public void setErfassung(Date erfassung) {
        this.erfassung = (erfassung != null ? new Date(erfassung.getTime()) : null);
        // Wenn kein Datum gesetzt, mit Erfassungszeit synchronisieren
        if (this.datum == null) {
            this.datum = this.erfassung;
        }
    }
    public Date getVersand() { return versand; }
    public void setVersand(Date versand) { this.versand = versand; }
    public Date getLieferung() { return lieferung; }
    public void setLieferung(Date lieferung) { this.lieferung = lieferung; }
    public Date getBezahlung() { return bezahlung; }
    public void setBezahlung(Date bezahlung) { this.bezahlung = bezahlung; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
	}
