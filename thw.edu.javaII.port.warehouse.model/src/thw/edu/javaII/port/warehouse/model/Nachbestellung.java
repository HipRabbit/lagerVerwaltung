package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Repräsentiert eine Nachbestellung im Lagerverwaltungssystem.
 *
 * <p>Enthält alle relevanten Informationen zu einer Nachbestellung,
 * einschließlich Produktdaten, aktuellem Bestand, Nachbestellmenge,
 * zukünftigem Bestand und Lagerplatzkapazität.
 *
 * <p>Attribute:
 * <ul>
 *   <li>int pid – Produkt-ID</li>
 *   <li>String pname – Produktname</li>
 *   <li>int aktuellerbestand – aktueller Lagerbestand</li>
 *   <li>String phersteller – Hersteller des Produkts</li>
 *   <li>int anzahlnachbestellung – Menge der Nachbestellung</li>
 *   <li>int zukünftigerbestand – erwarteter Lagerbestand nach Nachbestellung</li>
 *   <li>int kapazitaet – Kapazität des Lagerplatzes</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>Konstruktoren für leere und vollständige Nachbestellungen</li>
 *   <li>Getter/Setter für alle Attribute</li>
 *   <li>toListString() – Formatierte Ausgabe für Listen</li>
 *   <li>toString() – Kurzform für Anzeige</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
public class Nachbestellung implements Serializable {
    private static final long serialVersionUID = 8230932661185246836L;
    private static final String PRINT_FORMAT = "[%-20s - %-50s - %-30s - %-40s - %-20s]";

    private int pid;                    // Produkt-ID
    private String pname;               // Produktname
    private int aktuellerbestand;       // aktueller Bestand
    private String phersteller;         // Hersteller
    private int anzahlnachbestellung;   // Nachbestellmenge
    private int zukünftigerbestand;     // erwarteter Bestand nach Nachbestellung
    private int kapazitaet;             // Kapazität des Lagerplatzes

    /**
     * Standard-Konstruktor: erzeugt eine leere Nachbestellung.
     */
    public Nachbestellung() {}

    /**
     * Konstruktor zum vollständigen Anlegen einer Nachbestellung.
     *
     * @param pid                  Produkt-ID
     * @param pname                Produktname
     * @param aktuellerbestand     aktueller Bestand
     * @param phersteller          Hersteller
     * @param anzahlnachbestellung Nachbestellmenge
     * @param zukünftigerbestand   erwarteter Bestand nach Nachbestellung
     * @param kapazitaet           Kapazität des Lagerplatzes
     */
    public Nachbestellung(int pid, String pname, int aktuellerbestand, String phersteller,
                          int anzahlnachbestellung, int zukünftigerbestand, int kapazitaet) {
        this.pid = pid;
        this.pname = pname;
        this.aktuellerbestand = aktuellerbestand;
        this.phersteller = phersteller;
        this.anzahlnachbestellung = anzahlnachbestellung;
        this.zukünftigerbestand = zukünftigerbestand;
        this.kapazitaet = kapazitaet;
    }

    /**
     * Gibt eine formatierte Listen-Zeile mit den wichtigsten Feldern zurück.
     *
     * @return formatierte String-Repräsentation der Nachbestellung
     */
    public String toListString() {
        return String.format(PRINT_FORMAT, pid, pname, aktuellerbestand, phersteller, kapazitaet);
    }

    /**
     * Liefert den Produktnamen oder einen Standardtext, falls dieser null ist.
     *
     * @return Produktname oder "Unbenannte Nachbestellung"
     */
    @Override
    public String toString() {
        return pname != null ? pname : "Unbenannte Nachbestellung";
    }

    // Getter und Setter für alle Attribute

    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }
    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getAktuellerbestand() {
        return aktuellerbestand;
    }
    public void setAktuellerbestand(int aktuellerbestand) {
        this.aktuellerbestand = aktuellerbestand;
    }

    public String getPhersteller() {
        return phersteller;
    }
    public void setPhersteller(String phersteller) {
        this.phersteller = phersteller;
    }

    public int getAnzahlnachbestellung() {
        return anzahlnachbestellung;
    }
    public void setAnzahlnachbestellung(int anzahlnachbestellung) {
        this.anzahlnachbestellung = anzahlnachbestellung;
    }

    public int getZukünftigerbestand() {
        return zukünftigerbestand;
    }
    public void setZukünftigerbestand(int zukünftigerbestand) {
        this.zukünftigerbestand = zukünftigerbestand;
    }

    public int getKapazitaet() {
        return kapazitaet;
    }
    public void setKapazitaet(int kapazitaet) {
        this.kapazitaet = kapazitaet;
    }
}
