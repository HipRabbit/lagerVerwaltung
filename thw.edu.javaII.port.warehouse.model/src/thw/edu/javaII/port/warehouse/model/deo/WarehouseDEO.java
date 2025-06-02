package thw.edu.javaII.port.warehouse.model.deo;

import java.io.Serializable;

/**
 * Repräsentiert eine Anfrage oder einen Befehl im Lagerverwaltungssystem, bestehend aus Daten, Zone und Kommando.
 * 
 * @author Lennart Höpfner
 */
public class WarehouseDEO implements Serializable {
    private static final long serialVersionUID = 4403645819092074274L;
    private Object data;
    private Zone zone;
    private Command command;

    /**
     * Gibt die Daten der Anfrage zurück.
     * 
     * @return die Daten der Anfrage
     */
    public Object getData() {
        return data;
    }

    /**
     * Setzt die Daten der Anfrage.
     * 
     * @param data die zu setzenden Daten
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Gibt die Zone der Anfrage zurück.
     * 
     * @return die {@link Zone} der Anfrage
     */
    public Zone getZone() {
        return zone;
    }

    /**
     * Setzt die Zone der Anfrage.
     * 
     * @param zone die zu setzende {@link Zone}
     */
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    /**
     * Gibt das Kommando der Anfrage zurück.
     * 
     * @return das {@link Command} der Anfrage
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Setzt das Kommando der Anfrage.
     * 
     * @param command das zu setzende {@link Command}
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Konstruktor mit Daten, Zone und Kommando.
     * 
     * @param data die Daten der Anfrage
     * @param zone die Zone der Anfrage
     * @param command das Kommando der Anfrage
     */
    public WarehouseDEO(Object data, Zone zone, Command command) {
        this.data = data;
        this.zone = zone;
        this.command = command;
    }

    /**
     * Standardkonstruktor.
     */
    public WarehouseDEO() {
    }
}