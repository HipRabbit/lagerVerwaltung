package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Repräsentiert ein Lager im Lagerverwaltungssystem mit Details wie ID, Name, Ort und Art.
 * 
 * @author Lennart Höpfner
 */
public class Lager implements Serializable {
    private static final long serialVersionUID = 8230932661185246836L;
    private static final String PRINT_FORMAT = "[%-20s - %-50s - %-30s - %-40s]";
    private int id;
    private String name;
    private String ort;
    private String art;

    /**
     * Gibt die ID des Lagers zurück.
     * 
     * @return die ID des Lagers
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID des Lagers.
     * 
     * @param id die zu setzende ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Lagers zurück.
     * 
     * @return der Name des Lagers
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Lagers.
     * 
     * @param name der zu setzende Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Ort des Lagers zurück.
     * 
     * @return der Ort des Lagers
     */
    public String getOrt() {
        return ort;
    }

    /**
     * Setzt den Ort des Lagers.
     * 
     * @param ort der zu setzende Ort
     */
    public void setOrt(String ort) {
        this.ort = ort;
    }

    /**
     * Gibt die Art des Lagers zurück.
     * 
     * @return die Art des Lagers
     */
    public String getArt() {
        return art;
    }

    /**
     * Setzt die Art des Lagers.
     * 
     * @param art die zu setzende Art
     */
    public void setArt(String art) {
        this.art = art;
    }

    /**
     * Standardkonstruktor.
     */
    public Lager() {
    }

    /**
     * Konstruktor mit allen Attributen.
     * 
     * @param id die ID des Lagers
     * @param name der Name des Lagers
     * @param ort der Ort des Lagers
     * @param art die Art des Lagers
     */
    public Lager(int id, String name, String ort, String art) {
        this.id = id;
        this.name = name;
        this.ort = ort;
        this.art = art;
    }

    /**
     * Gibt eine formatierte String-Darstellung des Lagers für Listen zurück.
     * 
     * @return die formatierte String-Darstellung
     */
    public String toListString() {
        return String.format(PRINT_FORMAT, id, name, ort, art);
    }

    /**
     * Gibt eine String-Darstellung des Lagers zurück, die den Namen oder einen Standardwert anzeigt.
     * 
     * @return der Name des Lagers oder "Unbenanntes Lager", wenn der Name null ist
     */
    @Override
    public String toString() {
        return name != null ? name : "Unbenanntes Lager";
    }
}