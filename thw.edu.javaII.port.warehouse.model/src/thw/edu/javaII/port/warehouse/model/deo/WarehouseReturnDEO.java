package thw.edu.javaII.port.warehouse.model.deo;

import java.io.Serializable;

/**
 * Repräsentiert die Rückgabe einer Operation im Lagerverwaltungssystem, bestehend aus Daten, Nachricht und Status.
 * 
 * @author Lennart Höpfner
 */
public class WarehouseReturnDEO implements Serializable {
    private static final long serialVersionUID = 5607081847323905913L;
    private Object data;
    private String message;
    private Status status;

    /**
     * Gibt die Daten der Rückgabe zurück.
     * 
     * @return die Daten der Operation
     */
    public Object getData() {
        return data;
    }

    /**
     * Setzt die Daten der Rückgabe.
     * 
     * @param data die zu setzenden Daten
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Gibt die Nachricht der Rückgabe zurück.
     * 
     * @return die Nachricht der Operation
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setzt die Nachricht der Rückgabe.
     * 
     * @param message die zu setzende Nachricht
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gibt den Status der Rückgabe zurück.
     * 
     * @return der {@link Status} der Operation
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Setzt den Status der Rückgabe.
     * 
     * @param status der zu setzende {@link Status}
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Konstruktor mit Daten, Nachricht und Status.
     * 
     * @param data die Daten der Operation
     * @param message die Nachricht der Operation
     * @param status der Status der Operation
     */
    public WarehouseReturnDEO(Object data, String message, Status status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    /**
     * Standardkonstruktor.
     */
    public WarehouseReturnDEO() {
    }
}