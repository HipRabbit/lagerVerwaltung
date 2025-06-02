package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Repräsentiert einen Lagerplatz im Lagerverwaltungssystem mit Details wie ID, Name, Kapazität und zugehörigem Lager.
 * 
 * @author Lennart Höpfner
 */
public class LagerPlatz implements Serializable {
    private static final long serialVersionUID = -208247320517732519L;
    private static final String PRINT_FORMAT = "[%-20s - %-50s - %-30s - %-40s]";
    private int id;
    private String name;
    private int kapazitaet;
    private Lager lager_id;

    /**
     * Gibt die ID des Lagerplatzes zurück.
     * 
     * @return die ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID des Lagerplatzes.
     * 
     * @param id die zu setzende ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Lagerplatzes zurück.
     * 
     * @return der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Lagerplatzes.
     * 
     * @param name der zu setzende Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die Kapazität des Lagerplatzes zurück.
     * 
     * @return die Kapazität
     */
    public int getKapazitaet() {
        return kapazitaet;
    }

    /**
     * Setzt die Kapazität des Lagerplatzes.
     * 
     * @param kapazitaet die zu setzende Kapazität
     */
    public void setKapazitaet(int kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    /**
     * Gibt das zugehörige Lager des Lagerplatzes zurück.
     * 
     * @return das {@link Lager}-Objekt
     */
    public Lager getLager_id() {
        return lager_id;
    }

    /**
     * Setzt das zugehörige Lager des Lagerplatzes.
     * 
     * @param lager_id das zu setzende {@link Lager}-Objekt
     */
    public void setLager_id(Lager lager_id) {
        this.lager_id = lager_id;
    }

    /**
     * Standardkonstruktor.
     */
    public LagerPlatz() {
    }

    /**
     * Konstruktor mit allen Attributen.
     * 
     * @param id die ID des Lagerplatzes
     * @param name der Name des Lagerplatzes
     * @param kapazitaet die Kapazität des Lagerplatzes
     * @param lager_id das zugehörige {@link Lager}-Objekt
     */
    public LagerPlatz(int id, String name, int kapazitaet, Lager lager_id) {
        this.id = id;
        this.name = name;
        this.kapazitaet = kapazitaet;
        this.lager_id = lager_id;
    }

    /**
     * Gibt eine formatierte String-Darstellung des Lagerplatzes für Listen zurück.
     * 
     * @return die formatierte String-Darstellung
     */
    public String toListString() {
        return String.format(PRINT_FORMAT, id, name, kapazitaet, lager_id.getName());
    }

    /**
     * Gibt eine String-Darstellung des Lagerplatzes zurück, die den Namen und den Namen des zugehörigen Lagers enthält.
     * 
     * @return die String-Darstellung
     */
    public String toString() {
        return getName() + " - " + getLager_id().getName();
    }
}