package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

public class Nachbestellung implements Serializable {
    private static final long serialVersionUID = 8230932661185246836L;
    private static final String PRINT_FORMAT = "[%-20s - %-50s - %-30s - %-40s - %-20s]";
    private int pid;
    private String pname;
    private int aktuellerbestand;
    private String phersteller;
    private int anzahlnachbestellung;
    private int zukünftigerbestand;
    private int kapazitaet;

    public Nachbestellung() {
    }

    public Nachbestellung(int pid, String pname, int aktuellerbestand, String phersteller, int anzahlnachbestellung, int zukünftigerbestand, int kapazitaet) {
        super();
        this.pid = pid;
        this.pname = pname;
        this.aktuellerbestand = aktuellerbestand;
        this.phersteller = phersteller;
        this.anzahlnachbestellung = anzahlnachbestellung;
        this.zukünftigerbestand = zukünftigerbestand;
        this.kapazitaet = kapazitaet;
    }
    
    public String toListString() {
        return String.format(PRINT_FORMAT, pid, pname, aktuellerbestand, phersteller, kapazitaet);
    }

    @Override
    public String toString() {
        return pname != null ? pname : "Unbenannte Nachbestellung"; // Zeigt den Namen oder einen Standardwert
    }

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