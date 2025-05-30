package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

public class Kunde implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String vorname;
    private String nachname;
    private String lieferadresse;
    private String rechnungsadresse;
    private static final String PRINT_FORMAT = "[%-10s - %-15s - %-15s - %-50s - %-50s]";
    public static final int columnCount = 5;

    public Kunde() {
    }

    public Kunde(int id, String vorname, String nachname, String lieferadresse, String rechnungsadresse) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.lieferadresse = lieferadresse;
        this.rechnungsadresse = rechnungsadresse;
    }

    public String toListString() {
        return String.format(PRINT_FORMAT, id, vorname, nachname, lieferadresse, rechnungsadresse);
    }

    @Override
    public String toString() {
        return vorname + " " + nachname; // Für JComboBox
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getLieferadresse() {
        return lieferadresse;
    }

    public void setLieferadresse(String lieferadresse) {
        this.lieferadresse = lieferadresse;
    }

    public String getRechnungsadresse() {
        return rechnungsadresse;
    }

    public void setRechnungsadresse(String rechnungsadresse) {
        this.rechnungsadresse = rechnungsadresse;
    }

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