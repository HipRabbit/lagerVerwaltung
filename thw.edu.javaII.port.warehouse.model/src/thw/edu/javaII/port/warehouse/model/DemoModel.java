package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

/**
 * Repräsentiert ein Demo-Modell im Lagerverwaltungssystem mit einer ID und einem Namen.
 * 
 * @author Lennart Höpfner
 */
public class DemoModel implements Serializable {
    private static final long serialVersionUID = -474679583809542535L;
    private int id;
    private static final String PRINT_FORMAT = "[%-20s - %-50s]";
    private String name;
    public static final int columnCount = 2;

    /**
     * Standardkonstruktor.
     */
    public DemoModel() {
    }

    /**
     * Konstruktor mit ID und Namen.
     * 
     * @param id die ID des Demo-Modells
     * @param name der Name des Demo-Modells
     */
    public DemoModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gibt eine formatierte String-Darstellung des Demo-Modells für Listen zurück.
     * 
     * @return die formatierte String-Darstellung
     */
    public String toListString() {
        return String.format(PRINT_FORMAT, id, name);
    }

    /**
     * Gibt die ID des Demo-Modells zurück.
     * 
     * @return die ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID des Demo-Modells.
     * 
     * @param id die zu setzende ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt den Namen des Demo-Modells zurück.
     * 
     * @return der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Demo-Modells.
     * 
     * @param name der zu setzende Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Wert einer bestimmten Spalte des Demo-Modells zurück.
     * 
     * @param column die Spaltennummer (0 für ID, 1 für Name)
     * @return der Wert der Spalte oder null, wenn die Spalte ungültig ist
     */
    public Object getValueAtColumn(int column) {
        switch (column) {
            case 0:
                return getId();
            case 1:
                return getName();
            default:
                return null;
        }
    }
}