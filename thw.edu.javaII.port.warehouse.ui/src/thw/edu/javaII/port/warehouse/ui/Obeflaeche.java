package thw.edu.javaII.port.warehouse.ui;

import thw.edu.javaII.port.warehouse.ui.common.Session;

/**
 * Hauptklasse zum Starten der Lagerverwaltungsanwendung.
 *
 * @author Lennart Höpfner
 */
public class Obeflaeche {

    /**
     * Hauptmethode, die die Anwendung startet.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        Session ses = new Session();
        LoginScreen fenster = new LoginScreen(ses);
        fenster.setVisible(true);
        fenster.setResizable(false);
        Starter start = new Starter(ses);
        start.start();
    }
}