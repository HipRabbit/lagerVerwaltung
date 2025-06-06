package thw.edu.javaII.port.warehouse.server.init;

import thw.edu.javaII.port.warehouse.init.Initializer;
import thw.edu.javaII.port.warehouse.server.data.Database;
import thw.edu.javaII.port.warehouse.server.data.IStorage;

public class Loading {

    public void initLoading(IStorage store) {
        System.out.println("Starte Initialisierung der Datenbank...");
        try {
            Initializer init = new Initializer();
            System.out.println("Initialisiere Lager...");
            store.initLager(init.getLager());
            System.out.println("Initialisiere Lagerbestand...");
            store.initLagerBestand(init.getLagerbestand());
            System.out.println("Initialisiere Lagerplatz...");
            store.initLagerPlatz(init.getLagerplatz());
            System.out.println("Initialisiere Produkt...");
            store.initProdukt(init.getProdukt());
            System.out.println("Initialisiere Demo...");
            store.initDemo(init.getDemo());
            System.out.println("Initialisiere Kunde...");
            store.initKunde(init.getKunden());
            System.out.println("Initialisiere Bestellung...");
            store.initBestellung(init.getBestellung()); // Korrekter Methodenname
            System.out.println("Initialisiere Nachbestellungen...");
            store.initNachbestellung();
            ((Database)store).initNachbestellungenFromProdukte();
            System.out.println("Datenbankinitialisierung abgeschlossen.");
        } catch (Exception e) {
            System.err.println("Fehler bei der Initialisierung: " + e.getMessage());
            e.printStackTrace();
        }
    }
}