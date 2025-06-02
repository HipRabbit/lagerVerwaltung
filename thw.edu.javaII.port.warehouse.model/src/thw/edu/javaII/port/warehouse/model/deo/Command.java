package thw.edu.javaII.port.warehouse.model.deo;

/**
 * Definiert die möglichen Befehle für die Interaktion mit dem Lagerverwaltungssystem.
 * 
 * @author Lennart Höpfner
 */
public enum Command {
    /**
     * Fügt ein neues Objekt hinzu.
     */
    ADD,

    /**
     * Löscht ein Objekt.
     */
    DELETE,

    /**
     * Initialisiert die Datenbasis.
     */
    INIT,

    /**
     * Listet alle Objekte einer Zone auf.
     */
    LIST,

    /**
     * Aktualisiert ein bestehendes Objekt.
     */
    UPDATE,

    /**
     * Sucht nach Objekten basierend auf Kriterien.
     */
    SEARCH,

    /**
     * Ruft ein Objekt anhand seines Modells ab.
     */
    GETBYMODEL,

    /**
     * Ruft Informationen über den Lagerbestand ab.
     */
    BESTAND,

    /**
     * Ruft die Top-Objekte (z.B. nach Bestand oder Wert) ab.
     */
    TOP,

    /**
     * Ruft die Objekte mit niedrigem Bestand ab.
     */
    LOW,

    /**
     * Schließt eine Verbindung oder Sitzung.
     */
    CLOSE,

    /**
     * Beendet die Anwendung oder den Server.
     */
    END,

    /**
     * Ruft ein Objekt anhand seiner ID ab.
     */
    GETBYID,

    /**
     * Filtert Objekte basierend auf spezifischen Kriterien.
     */
    FILTER,

    /**
     * Ermittelt die nächste verfügbare ID.
     */
    GETNEXTID,

    /**
     * Fügt ein Objekt mit zugehörigem Lagerbestand hinzu.
     */
    ADD_WITH_BESTAND,

    /**
     * Führt eine Inventur durch.
     */
    INVENTUR
}