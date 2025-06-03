package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Repräsentiert einen Kunden im Lagerverwaltungssystem.
 *
 * <p>Enthaltene Attribute:
 * <ul>
 *   <li>int id – eindeutige Kunden­nummer</li>
 *   <li>String vorname – Vorname des Kunden</li>
 *   <li>String nachname – Nachname des Kunden</li>
 *   <li>String lieferadresse – Adresse für die Lieferung</li>
 *   <li>String rechnungsadresse – Adresse für die Rechnungsstellung</li>
 *   <li>static final String PRINT_FORMAT – Formatvorlage für Listen-Ausgabe</li>
 *   <li>public static final int columnCount – Anzahl der Spalten in Tabellenansicht</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>Konstruktoren</li>
 *   <li>toListString() – formatierte Listen-Ausgabe</li>
 *   <li>toString() – Kurzform für Swing-Komponenten</li>
 *   <li>Getter-/Setter-Methoden</li>
 *   <li>getValueAtColumn(int) – Wert für Tabellenzelle</li>
 * </ul>
 *
 * @author Paul Hartmann
 */
public class Kunde implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String vorname;
    private String nachname;
    private String lieferadresse;
    private String rechnungsadresse;
    
    // Formatvorlage für Ausgabe in Listen (z. B. Console-Tabelle)
    private static final String PRINT_FORMAT =
        "[%-10s - %-15s - %-15s - %-50s - %-50s]";
    
    /** Anzahl der Spalten in der Tabellenansicht */
    public static final int columnCount = 5;

    /**
     * Standard-Konstruktor: erzeugt einen leeren Kunden.
     */
    public Kunde() {
    }

    /**
     * Konstruktor zum vollständigen Anlegen eines Kunden.
     *
     * @param id               eindeutige Kunden­nummer
     * @param vorname          Vorname des Kunden
     * @param nachname         Nachname des Kunden
     * @param lieferadresse    Adresse, an die geliefert wird
     * @param rechnungsadresse Adresse, an die Rechnungen gehen
     */
    public Kunde(int id, String vorname, String nachname,
                 String lieferadresse, String rechnungsadresse) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.lieferadresse = lieferadresse;
        this.rechnungsadresse = rechnungsadresse;
    }

    /**
     * Liefert die eindeutige ID des Kunden.
     *
     * @return Kunden­nummer
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die eindeutige ID des Kunden.
     *
     * @param id neue Kunden­nummer
     */
    public void setId(int id) {
        this.id = id;
    }

    /** 
     * Hier alle weiteren Getter- und Setter-Methoden für vorname, nachname,
     * lieferadresse und rechnungsadresse.
     */
    public String getVorname()        { return vorname; }
    public void setVorname(String v)  { this.vorname = v; }
    public String getNachname()       { return nachname; }
    public void setNachname(String n) { this.nachname = n; }
    public String getLieferadresse()         { return lieferadresse; }
    public void setLieferadresse(String la)  { this.lieferadresse = la; }
    public String getRechnungsadresse()           { return rechnungsadresse; }
    public void setRechnungsadresse(String ra)    { this.rechnungsadresse = ra; }

    /**
     * Gibt eine formatierte Listen-Zeile mit allen Kundenfeldern zurück.
     *
     * @return formatierte String-Repräsentation (alle Spalten)
     * @author Bjarne von Appen
     */
    public String toListString() {
        return String.format(PRINT_FORMAT,
            id, vorname, nachname, lieferadresse, rechnungsadresse);
    }

    /**
     * Liefert eine Kurzform "Vorname Nachname", z. B. für JComboBox.
     *
     * @return voller Name des Kunden
     * @author Bjarne von Appen
     */
    @Override
    public String toString() {
        return vorname + " " + nachname;
    }

    /**
     * Liefert den Wert des Kunden-Objekts in der angegebenen Tabellenspalte.
     *
     * @param column Index der Spalte (0=id, 1=Vorname, …, 4=Rechnungsadresse)
     * @return Objektwert für die Zelle oder null bei ungültigem Index
     * @author Bjarne von Appen
     */
    public Object getValueAtColumn(int column) {
        switch (column) {
            case 0: return getId();
            case 1: return getVorname();
            case 2: return getNachname();
            case 3: return getLieferadresse();
            case 4: return getRechnungsadresse();
            default: return null;
        }
    }
}
