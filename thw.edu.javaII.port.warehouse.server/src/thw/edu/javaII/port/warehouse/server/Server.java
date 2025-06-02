package thw.edu.javaII.port.warehouse.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import thw.edu.javaII.port.warehouse.model.common.Info;
import thw.edu.javaII.port.warehouse.server.data.Database;
import thw.edu.javaII.port.warehouse.server.init.Loading;

/**
 * Startet den Lagerverwaltungsserver, initialisiert die Datenbank und verarbeitet eingehende Client-Verbindungen.
 * 
 * @author Lennart Höpfner
 */
public class Server {
    public static boolean run = true;

    /**
     * Hauptmethode zum Starten des Servers.
     * 
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            System.out.println("Initialisiere Datenbank...");
            Database store = new Database();
            Loading loading = new Loading();
            loading.initLoading(store);
            System.out.println("Datenbank erfolgreich initialisiert");
            System.out.println("Bestandsprüfung für Nachbestellungen abgeschlossen");

            server = new ServerSocket(Info.PORT_SERVER);
            System.out.println("Lagerverwaltungsserver läuft auf Port " + Info.PORT_SERVER);
            while (run) {
                Socket sock = server.accept();
                System.out.println("Neue Client-Verbindung akzeptiert");
                new Service(sock, store).start();
            }
        } catch (Exception e) {
            System.err.println("Serverfehler: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    // Ignorieren
                }
            }
        }
    }
}