package thw.edu.javaII.port.warehouse.model.common;

/**
 * Stellt Konstanten für das Lagerverwaltungssystem bereit, wie Dateinamen, Servereinstellungen und Timeouts.
 * 
 * @author Lennart Höpfner
 */
public class Info {
    /**
     * Der Dateiname für die Speicherung der Lagerverwaltungsdaten.
     */
    public final static String SAVE_FILE_NAME = "Lagverwaltung.dat";

    /**
     * Der Port, der für die Serverkommunikation verwendet wird.
     */
    public final static int PORT_SERVER = 5010;

    /**
     * Der Name des Servers (Host).
     */
    public final static String NAME_SERVER = "localhost";

    /**
     * Das Timeout für Client-Verbindungen in Millisekunden.
     */
    public final static int TIMEOUT_CLIENT = 50000;

    /**
     * Der Dateiname für das Logfile der Lagerverwaltung.
     */
    public final static String LOG_NAME = "warehouse.log";
}