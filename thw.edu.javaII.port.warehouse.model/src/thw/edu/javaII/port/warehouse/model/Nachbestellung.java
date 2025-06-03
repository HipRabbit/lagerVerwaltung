package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Repräsentiert eine Nachbestellung im Lagerverwaltungssystem.
 *
 * <p>Enthält folgende Attribute:
 * <ul>
 *   <li>int pid – Produkt-ID</li>
 *   <li>String pname – Produktname</li>
 *   <li>int aktuellerbestand – aktueller Lagerbestand</li>
 *   <li>String phersteller – Hersteller des Produkts</li>
 *   <li>int anzahlnachbestellung – Menge der Nachbestellung</li>
 *   <li>int zukünftigerbestand – erwarteter Lagerbestand nach Nachbestellung</li>
 * </ul>
 *
 * <p>Verfügbare Methoden:
 * <ul>
 *   <li>Konstruktoren zum Anlegen einer Nachbestellung</li>
 *   <li>toListString() – formatierte Listen-Ausgabe</li>
 *   <li>toString() – Kurzform für Anzeige</li>
 *   <li>Getter- und Setter-Methoden</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
public class Nachbestellung implements Serializable {
    private static final long serialVersionUID = 8230932661185246836L;

    // Formatvorlage für die Listen-Ausgabe
    private static final String PRINT_FORMAT =
        "[%-20s - %-50s - %-30s - %-40s - %-20s - %-20s]";

    private int pid;                      // Produkt-ID
    private String pname;                 // Produktname
    private int aktuellerbestand;         // aktueller Lagerbestand
    private String phersteller;           // Hersteller des Produkts
    private int anzahlnachbestellung;     // Menge der Nachbestellung
    private int zukünftigerbestand;       // erwarteter Lagerbestand nach Nachbestellung

    /**
     * Standard-Konstruktor: erzeugt eine leere Nachbestellung.
     */
    public Nachbestellung() {
        // Keine Initialisierung außer Default-Werten
    }

    /**
     * Konstruktor mit allen Attributen.
     *
     * @param pid                      Produkt-ID
     * @param pname                    Produktname
     * @param aktuellerbestand         aktueller Lagerbestand
     * @param phersteller              Hersteller des Produkts
     * @param anzahlnachbestellung     Menge der Nachbestellung
     * @param zukünftigerbestand       erwarteter Lagerbestand nach Nachbestellung
     */
    public Nachbestellung(int pid,
                          String pname,
                          int aktuellerbestand,
                          String phersteller,
                          int anzahlnachbestellung,
                          int zukünftigerbestand) {
        this.pid = pid;
        this.pname = pname;
        this.aktuellerbestand = aktuellerbestand;
        this.phersteller = phersteller;
        this.anzahlnachbestellung = anzahlnachbestellung;
        this.zukünftigerbestand = zukünftigerbestand;
    }

    /**
     * Gibt eine formatierte Listen-Zeile mit allen Attributen zurück.
     *
     * @return formatierte String-Repräsentation aller Felder
     * @author Lennart Höpfner
     */
    public String toListString() {
        // Verwenden des PRINT_FORMAT für tabellarische Ausgabe
        return String.format(PRINT_FORMAT,
            pid,
            pname,
            aktuellerbestand,
            phersteller,
            anzahlnachbestellung,
            zukünftigerbestand);
    }

    /**
     * Liefert den Produktnamen oder einen Standardtext, falls dieser null ist.
     *
     * @return Produktname oder "Unbenannte Nachbestellung"
     * @author Lennart Höpfner
     */
    @Override
    public String toString() {
        // Zeigt pname oder Fallback-Text
        return pname != null
            ? pname
            : "Unbenannte Nachbestellung";
    }

    /** Getter und Setter für pid */
    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }

    /** Hier alle weiteren Getter- und Setter-Methoden für die übrigen Attribute. */
    public String getPname()               { return pname; }
    public void setPname(String pname)     { this.pname = pname; }
    public int getAktuellerbestand()       { return aktuellerbestand; }
    public void setAktuellerbestand(int aktuellerbestand) {
        this.aktuellerbestand = aktuellerbestand;
    }
    public String getPhersteller()         { return phersteller; }
    public void setPhersteller(String phersteller) {
        this.phersteller = phersteller;
    }
    public int getAnzahlnachbestellung()   { return anzahlnachbestellung; }
    public void setAnzahlnachbestellung(int anzahlnachbestellung) {
        this.anzahlnachbestellung = anzahlnachbestellung;
    }
    public int getZukünftigerbestand()     { return zukünftigerbestand; }
    public void setZukünftigerbestand(int zukünftigerbestand) {
        this.zukünftigerbestand = zukünftigerbestand;
    }
}
