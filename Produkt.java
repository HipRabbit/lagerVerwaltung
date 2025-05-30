package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

public class Produkt implements Serializable {
    private static final long serialVersionUID = -5308799831441367738L;
    private static final String PRINT_FORMAT = "[%-20s - %-50s - %-30s - %-40s - %-20s]";
    private int id;
    private String name;
    private String hersteller;
    private double preis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public Produkt() {
    }

    public Produkt(int id, String name, String hersteller, double preis) {
        this.id = id;
        this.name = name != null ? name : "Unbekannt";
        this.hersteller = hersteller != null ? hersteller : "Unbekannt";
        this.preis = preis;
    }

    public String toListString() {
        return String.format(PRINT_FORMAT, id, name, hersteller, preis);
    }

    @Override
    public String toString() {
        return name; // Nur der Produktname wird angezeigt
    }
}