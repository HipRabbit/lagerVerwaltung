package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Repräsentiert ein Produkt im Lagerverwaltungssystem mit Details wie ID, Name, Hersteller und Preis.
 * 
 * @author Lennart Höpfner
 */
public class Produkt implements Serializable {
    private static final long serialVersionUID = -5308799831441367738L;
    private static final String PRINT_FORMAT = "[%-20s - %-50s - %-30s - %-40s - %-20s]";
    private int id;
    private String name;
    private String hersteller;
    private double preis;

    /**
     * Gibt die ID des Produkts zurück.
     * 
     * @return die ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID des Produkts.
     * 
     * @param id die zu setzende ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Produkts zurück.
     * 
     * @return der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Produkts.
     * 
     * @param name der zu setzende Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Hersteller des Produkts zurück.
     * 
     * @return der Hersteller
     */
    public String getHersteller() {
        return hersteller;
    }

    /**
     * Setzt den Hersteller des Produkts.
     * 
     * @param hersteller der zu setzende Hersteller
     */
    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    /**
     * Gibt den Preis des Produkts zurück.
     * 
     * @return der Preis
     */
    public double getPreis() {
        return preis;
    }

    /**
     * Setzt den Preis des Produkts.
     * 
     * @param preis der zu setzende Preis
     */
    public void setPreis(double preis) {
        this.preis = preis;
    }

    /**
     * Standardkonstruktor.
     */
    public Produkt() {
    }

    /**
     * Konstruktor mit allen Attributen.
     * 
     * @param id die ID des Produkts
     * @param name der Name des Produkts
     * @param hersteller der Hersteller des Produkts
     * @param preis der Preis des Produkts
     */
    public Produkt(int id, String name, String hersteller, double preis) {
        this.id = id;
        this.name = name != null ? name : "Unbekannt";
        this.hersteller = hersteller != null ? hersteller : "Unbekannt";
        this.preis = preis;
    }

    /**
     * Gibt eine formatierte String-Darstellung des Produkts für Listen zurück.
     * 
     * @return die formatierte String-Darstellung
     */
    public String toListString() {
        return String.format(PRINT_FORMAT, id, name, hersteller, preis);
    }

    /**
     * Gibt eine String-Darstellung des Produkts zurück, die den Namen enthält.
     * 
     * @return der Name des Produkts
     */
    @Override
    public String toString() {
        return name;
    }
}