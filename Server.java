package thw.edu.javaII.port.warehouse.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import thw.edu.javaII.port.warehouse.model.common.Info;
import thw.edu.javaII.port.warehouse.server.data.Database;
import thw.edu.javaII.port.warehouse.server.init.Loading;

public class Server {
    public static boolean run = true;

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            System.out.println("Initialisiere Datenbank...");
            Database store = new Database();  // Zentrale Datenbankinstanz
            Loading loading = new Loading();
            loading.initLoading(store);
            System.out.println("Datenbank erfolgreich initialisiert");
         // Nach der Datenbankinitialisierung in main()
            NachbestellungService nachbestellungService = new NachbestellungService(store);
            nachbestellungService.checkLowStock();
            System.out.println("Bestandsprüfung für Nachbestellungen abgeschlossen");

            server = new ServerSocket(Info.PORT_SERVER);
            System.out.println("Lagerverwaltungsserver läuft auf Port " + Info.PORT_SERVER);
            while (run) {
                Socket sock = server.accept();
                System.out.println("Neue Client-Verbindung akzeptiert");
                new Service(sock, store).start();  // Übergib die Instanz an Service
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