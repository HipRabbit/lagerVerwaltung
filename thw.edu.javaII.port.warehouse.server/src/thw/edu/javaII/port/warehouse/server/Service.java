package thw.edu.javaII.port.warehouse.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import thw.edu.javaII.port.warehouse.model.common.Cast;
import thw.edu.javaII.port.warehouse.model.common.Info;
import thw.edu.javaII.port.warehouse.model.deo.Status;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.Lager;
import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.model.Kunde;
import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.model.FilterCriteria;
import thw.edu.javaII.port.warehouse.server.comparator.BestandByLagerBestand;
import thw.edu.javaII.port.warehouse.server.comparator.BestandByProduktAlpha;
import thw.edu.javaII.port.warehouse.server.data.Database;
import thw.edu.javaII.port.warehouse.server.data.IStorage;
import thw.edu.javaII.port.warehouse.server.init.Loading;

/**
 * Service-Klasse, die in einem eigenen Thread läuft und alle
 * Client-Anfragen am Server-Socket entgegennimmt und verarbeitet.
 *
 * <p>Attribute:
 * <ul>
 *   <li>static int count – zählt alle gestarteten Service-Instanzen</li>
 *   <li>int currentNumber – eindeutige Nummer dieser Instanz</li>
 *   <li>Socket sock – Client-Verbindung</li>
 *   <li>ObjectInputStream fromClient – Eingabestream vom Client</li>
 *   <li>ObjectOutputStream toClient – Ausgabestream zum Client</li>
 *   <li>IStorage store – Abstraktion für Datenbankzugriffe</li>
 *   <li>boolean run – Steuerflag für die Hauptschleife</li>
 *   <li>Logger logger – protokolliert Ereignisse und Fehler</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>Service(Socket, IStorage) – Initialisiert Socket, Streams und Storage</li>
 *   <li>run() – Hauptschleife zum Empfang und zur Verarbeitung von DEOs</li>
 *   <li>handleZoneXxx(...) – Handler-Methoden für jede Zone</li>
 *   <li>closeResources() – Schließt Socket und Streams sicher</li>
 * </ul>
 *
 * @author Bjarne von Appen
 * @author Paul Hartmann
 * @author Lennart Höpfner
 */
public class Service extends Thread {
    /** Zählt alle gestarteten Service-Instanzen */
    static int count = 0;
    /** Eindeutige Nummer dieser Instanz */
    private int currentNumber = 0;
    /** Socket-Verbindung zum Client */
    private Socket sock;
    /** Eingabestream vom Client */
    private ObjectInputStream fromClient;
    /** Ausgabestream zum Client */
    private ObjectOutputStream toClient;
    /** Abstraktion für Datenbankzugriffe */
    private IStorage store;
    /** Steuerflag für die Hauptschleife */
    private boolean run;
    /** Protokolliert Ereignisse und Fehler */
    private Logger logger;

    /**
     * Konstruktor initialisiert Socket, Streams und Storage.
     *
     * @param sock  Socket-Verbindung zum Client
     * @param store Implementierung für Datenbankzugriffe
     */
    public Service(Socket sock, IStorage store) {
        try {
            run = true;
            this.sock = sock;
            this.store = store;  // Verwende die übergebene Instanz
            this.logger = System.getLogger(Info.LOG_NAME);
            currentNumber = ++count;
            // Output-Stream zuerst erstellen, um Deadlock zu vermeiden
            toClient = new ObjectOutputStream(sock.getOutputStream());
            fromClient = new ObjectInputStream(sock.getInputStream());
        } catch (IOException e) {
            logger.log(Level.ERROR, "IO-Error bei Client " + currentNumber + ": " + e.getMessage());
        }
    }

    /**
     * Hauptschleife: liest WarehouseDEO-Objekte, dispatcht an passende Handler
     * und schreibt WarehouseReturnDEO-Objekte zurück.
     */
    public void run() {
        logger.log(Level.INFO, "Bearbeitung für Client " + currentNumber + " gestartet");
        try {
            while (run) {
                // DEO vom Client lesen
                WarehouseDEO deoIn = (WarehouseDEO) fromClient.readObject();
                WarehouseReturnDEO deoOut = null;
                
                // Dispatch je nach Zone
                switch (deoIn.getZone()) {
                    case INIT:
                        deoOut = handleZoneInit(deoIn, deoOut);
                        break;
                    case LAGER:
                        deoOut = handleZoneLager(deoIn, deoOut);
                        break;
                    case LAGERBESTAND:
                        deoOut = handleZoneLagerBestand(deoIn, deoOut);
                        break;
                    case LAGERPLATZ:
                        deoOut = handleZoneLagerPlatz(deoIn, deoOut);
                        break;
                    case PRODUKT:
                        deoOut = handleZoneProdukt(deoIn, deoOut);
                        break;
                    case STATISTIK:
                        deoOut = handleZoneStatistik(deoIn, deoOut);
                        break;
                    case GENERAL:
                        deoOut = handleZoneGeneral(deoIn, deoOut);
                        break; 
                    case KUNDE:
                        deoOut = handleZoneKunde(deoIn, deoOut);
                        break;
                    case BESTELLUNG:
                        deoOut = handleZoneBestellung(deoIn, deoOut);
                        break;
                    case NACHBESTELLUNG:
                        deoOut = handleZoneNachbestellung(deoIn, deoOut);
                        break;
                    default:
                        deoOut = new WarehouseReturnDEO(null, "Unbekannte Zone", Status.ERROR);
                        break;
                }
                // Antwort an Client senden
                toClient.writeObject(deoOut);
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "IO-Error bei Client " + currentNumber + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.log(Level.ERROR, "Fehler beim übergebenen Objekt für Client " + currentNumber + ": " + e.getMessage());
        } finally {
            // Ressourcen sicher schließen
            try {
                if (fromClient != null)
                    fromClient.close();
                if (toClient != null)
                    toClient.close();
                if (sock != null)
                    sock.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Fehler beim Schließen der Verbindungen für Client " + currentNumber + ": " + e.getMessage());
            }
        }
        logger.log(Level.INFO, "Protokoll für Client " + currentNumber + " beendet");
    }

    /**
     * Bearbeitet Kunden-Befehle (ADD, DELETE, LIST, UPDATE, SEARCH, GETNEXTID).
     *
     * @param deoIn  übermittelte DEO mit Kommando und Daten
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnis und Status
     */
    private WarehouseReturnDEO handleZoneKunde(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
            case ADD:
                if (deoIn.getData() != null && deoIn.getData() instanceof Kunde) {
                    Kunde k = Cast.safeCast(deoIn.getData(), Kunde.class);
                    try {
                        store.addKunde(k);
                        deoOut = new WarehouseReturnDEO(null, "Kunde erfolgreich angelegt", Status.OK);
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to add Kunde: " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Hinzufügen des Kunden: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case DELETE:
                if (deoIn.getData() != null && deoIn.getData() instanceof Kunde) {
                    Kunde k = Cast.safeCast(deoIn.getData(), Kunde.class);
                    try {
                        store.deleteKunde(k);
                        deoOut = new WarehouseReturnDEO(null, "Kunde erfolgreich gelöscht", Status.OK);
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to delete Kunde ID " + k.getId() + ": " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Löschen des Kunden: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case LIST:
                try {
                    deoOut = new WarehouseReturnDEO(store.getKunden(), "Liste aller Kunden", Status.OK);
                } catch (RuntimeException e) {
                    logger.log(Level.ERROR, "Failed to retrieve Kunden list: " + e.getMessage());
                    deoOut = new WarehouseReturnDEO(null, "Fehler beim Abrufen der Kundenliste: " + e.getMessage(), Status.ERROR);
                }
                break;
            case UPDATE:
                if (deoIn.getData() != null && deoIn.getData() instanceof Kunde) {
                    Kunde k = Cast.safeCast(deoIn.getData(), Kunde.class);
                    try {
                        store.updateKunde(k);
                        deoOut = new WarehouseReturnDEO(null, "Kunde erfolgreich bearbeitet", Status.OK);
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to update Kunde ID " + k.getId() + ": " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Aktualisieren des Kunden: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case SEARCH:
                if (deoIn.getData() != null && deoIn.getData() instanceof Integer) {
                    int id = Cast.safeCast(deoIn.getData(), Integer.class);
                    try {
                        Kunde k = store.getKundeById(id);
                        if (k != null) {
                            deoOut = new WarehouseReturnDEO(k, "Kunde gefunden", Status.OK);
                        } else {
                            deoOut = new WarehouseReturnDEO(null, "Kunde nicht gefunden", Status.ERROR);
                        }
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to search Kunde ID " + id + ": " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Suchen des Kunden: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case GETNEXTID:
                try {
                    int nextId = store.getNextKundeId();
                    logger.log(Level.INFO, "Sende nächste Kunden-ID: " + nextId);
                    deoOut = new WarehouseReturnDEO(nextId, "Nächste Kunden-ID", Status.OK);
                } catch (RuntimeException e) {
                    logger.log(Level.ERROR, "Fehler beim Abrufen der nächsten Kunden-ID: " + e.getMessage());
                    deoOut = new WarehouseReturnDEO(null, "Fehler beim Abrufen der nächsten Kunden-ID", Status.ERROR);
                }
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet Lager-Befehle (ADD, DELETE, LIST, UPDATE, SEARCH).
     *
     * @param deoIn  übermittelte DEO mit Kommando und Daten
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnis und Status
     */
    private WarehouseReturnDEO handleZoneLager(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
        case ADD:
            if (deoIn.getData() != null && deoIn.getData() instanceof Lager) {
                Lager l = Cast.safeCast(deoIn.getData(), Lager.class);
                store.addLager(l);
                deoOut = new WarehouseReturnDEO(null, "Lager erfolgreich angelegt", Status.OK);
            } else {
                deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
            }
            break;
            case DELETE:
                if (deoIn.getData() != null && deoIn.getData() instanceof Lager) {
                    Lager l = Cast.safeCast(deoIn.getData(), Lager.class);
                    store.deleteLager(l);
                    deoOut = new WarehouseReturnDEO(null, "Lager erfolgreich gelöscht", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case INIT:
                deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
                break;
            case LIST:
                deoOut = new WarehouseReturnDEO(store.getLagers(), "Liste aller Lager", Status.OK);
                break;
            case UPDATE:
                if (deoIn.getData() != null && deoIn.getData() instanceof Lager) {
                    Lager l = Cast.safeCast(deoIn.getData(), Lager.class);
                    store.updateLager(l);
                    deoOut = new WarehouseReturnDEO(null, "Lager erfolgreich bearbeitet", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case SEARCH:
                if (deoIn.getData() instanceof Integer) {
                    int id = Cast.safeCast(deoIn.getData(), Integer.class);
                    try {
                        Lager lager = ((Database) store).getLagerById(id);
                        if (lager != null && lager.getId() > 0) {
                            deoOut = new WarehouseReturnDEO(lager, "Lager mit ID " + id + " gefunden", Status.OK);
                        } else {
                            deoOut = new WarehouseReturnDEO(null, "Lager mit ID " + id + " nicht gefunden", Status.ERROR);
                        }
                    } catch (SQLException e) {
                        logger.log(Level.ERROR, "Failed to search Lager ID " + id + ": " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Suchen des Lagers: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet LagerPlatz-Befehle (ADD, DELETE, LIST, UPDATE, SEARCH, GETNEXTID).
     *
     * @param deoIn  übermittelte DEO mit Kommando und Daten
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnis und Status
     */
    private WarehouseReturnDEO handleZoneLagerPlatz(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
            case ADD:
                if (deoIn.getData() != null && deoIn.getData() instanceof LagerPlatz) {
                    LagerPlatz l = Cast.safeCast(deoIn.getData(), LagerPlatz.class);
                    try {
                        // Neue ID von Database holen
                        int newId = store.getNextLagerPlatzId();
                        l.setId(newId); // ID serverseitig setzen
                        store.addLagerPlatz(l);
                        deoOut = new WarehouseReturnDEO(l, "Lagerplatz erfolgreich angelegt mit ID: " + newId, Status.OK);
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to add LagerPlatz: " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Hinzufügen des Lagerplatzes: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case DELETE:
                if (deoIn.getData() != null && deoIn.getData() instanceof LagerPlatz) {
                    LagerPlatz l = Cast.safeCast(deoIn.getData(), LagerPlatz.class);
                    store.deleteLagerPlatz(l);
                    deoOut = new WarehouseReturnDEO(null, "Lagerplatz erfolgreich gelöscht", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case INIT:
                deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
                break;
            case LIST:
                deoOut = new WarehouseReturnDEO(store.getLagerPlatzs(), "Liste aller Lagerplätze", Status.OK);
                break;
            case UPDATE:
                if (deoIn.getData() != null && deoIn.getData() instanceof LagerPlatz) {
                    LagerPlatz l = Cast.safeCast(deoIn.getData(), LagerPlatz.class);
                    store.updateLagerPlatz(l);
                    deoOut = new WarehouseReturnDEO(null, "Lagerplatz erfolgreich bearbeitet", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case SEARCH:
                if (deoIn.getData() instanceof Integer) {
                    int id = Cast.safeCast(deoIn.getData(), Integer.class);
                    try {
                        LagerPlatz lagerPlatz = ((Database) store).getLagerPlatzById(id);
                        if (lagerPlatz != null && lagerPlatz.getId() > 0) {
                            deoOut = new WarehouseReturnDEO(lagerPlatz, "Lagerplatz mit ID " + id + " gefunden", Status.OK);
                        } else {
                            deoOut = new WarehouseReturnDEO(null, "Lagerplatz mit ID " + id + " nicht gefunden", Status.ERROR);
                        }
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to search LagerPlatz ID " + id + ": " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Suchen des Lagerplatzes: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case GETNEXTID:
                try {
                    int nextId = store.getNextLagerPlatzId();
                    logger.log(Level.INFO, "Nächste LagerPlatz-ID für Client " + currentNumber + ": " + nextId);
                    deoOut = new WarehouseReturnDEO(nextId, "Nächste LagerPlatz-ID: " + nextId, Status.OK);
                } catch (RuntimeException e) {
                    logger.log(Level.ERROR, "Fehler beim Abrufen der nächsten LagerPlatz-ID: " + e.getMessage());
                    deoOut = new WarehouseReturnDEO(null, "Fehler beim Abrufen der nächsten LagerPlatz-ID", Status.ERROR);
                }
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet LagerBestand-Befehle (ADD, DELETE, LIST, UPDATE, SEARCH, INVENTUR).
     *
     * @param deoIn  übermittelte DEO mit Kommando und Daten
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnis und Status
     */
    private WarehouseReturnDEO handleZoneLagerBestand(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
        case ADD:
            if (deoIn.getData() != null && deoIn.getData() instanceof LagerBestand) {
                LagerBestand l = Cast.safeCast(deoIn.getData(), LagerBestand.class);
                l.setId(0); // Ignoriere die vom Client gesendete ID
                store.addLagerBestand(l);
                deoOut = new WarehouseReturnDEO(null, "Lagerbestand erfolgreich angelegt", Status.OK);
            } else {
                deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
            }
            break;
            case DELETE:
                if (deoIn.getData() != null && deoIn.getData() instanceof LagerBestand) {
                    LagerBestand l = Cast.safeCast(deoIn.getData(), LagerBestand.class);
                    store.deleteLagerBestand(l);
                    deoOut = new WarehouseReturnDEO(null, "Lagerbestand erfolgreich gelöscht", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case INIT:
                deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
                break;
            case LIST:
                deoOut = new WarehouseReturnDEO(store.getLagerBestands(), "Liste aller Lagerbestände", Status.OK);
                break;
            case UPDATE:
                if (deoIn.getData() != null && deoIn.getData() instanceof LagerBestand) {
                    LagerBestand l = Cast.safeCast(deoIn.getData(), LagerBestand.class);
                    store.updateLagerBestand(l);
                    deoOut = new WarehouseReturnDEO(null, "Lagerbestand erfolgreich bearbeitet", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case SEARCH:
                if (deoIn.getData() != null && deoIn.getData() instanceof String) {
                    String search = Cast.safeCast(deoIn.getData(), String.class);
                    List<LagerBestand> all = store.getLagerBestands();
                    List<LagerBestand> relevantData = new ArrayList<LagerBestand>();
                    // Durchsuche alle Bestände nach dem Suchbegriff
                    for (LagerBestand mod : all) {
                        String searchData = mod.getProdukt_id().getName() + mod.getProdukt_id().getHersteller()
                                + mod.getLagerplatz_id().getName() + mod.getLagerplatz_id().getLager_id().getName()
                                + mod.getLagerplatz_id().getLager_id().getOrt()
                                + mod.getLagerplatz_id().getLager_id().getArt();
                        if (searchData.toLowerCase().contains(search.toLowerCase())) {
                            relevantData.add(mod);
                        }
                    }
                    deoOut = new WarehouseReturnDEO(relevantData, "Lagerbestand durchsucht nach " + search, Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case INVENTUR:
                try {
                    List<LagerBestand> data = store.getLagerBestands();
                    // Sortierung alphabetisch nach Produktname
                    Collections.sort(data, new BestandByProduktAlpha());
                    deoOut = new WarehouseReturnDEO(data, "Inventur erstellt: Liste aller Lagerbestände", Status.OK);
                } catch (RuntimeException e) {
                    logger.log(Level.ERROR, "Fehler bei der Inventur: " + e.getMessage());
                    deoOut = new WarehouseReturnDEO(null, "Fehler bei der Inventur: " + e.getMessage(), Status.ERROR);
                }
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet Produkt-Befehle (ADD, ADD_WITH_BESTAND, DELETE, LIST, UPDATE, GETBYMODEL, SEARCH).
     *
     * @param deoIn  übermittelte DEO mit Kommando und Daten
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnis und Status
     */
    private WarehouseReturnDEO handleZoneProdukt(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
            case ADD:
                if (deoIn.getData() != null && deoIn.getData() instanceof Produkt) {
                    Produkt l = Cast.safeCast(deoIn.getData(), Produkt.class);
                    try {
                        store.addProdukt(l);
                        deoOut = new WarehouseReturnDEO(null, "Produkt erfolgreich angelegt", Status.OK);
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to add Produkt: " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Hinzufügen des Produkts: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case ADD_WITH_BESTAND:
                if (deoIn.getData() != null && deoIn.getData() instanceof Object[]) {
                    Object[] data = (Object[]) deoIn.getData();
                    if (data.length == 2 && data[0] instanceof Produkt && data[1] instanceof LagerBestand) {
                        Produkt produkt = Cast.safeCast(data[0], Produkt.class);
                        LagerBestand lagerBestand = Cast.safeCast(data[1], LagerBestand.class);
                        try {
                            // Produkt und Bestand in einer Transaktion hinzufügen
                            boolean success = ((Database) store).addProduktWithBestand(produkt, lagerBestand);
                            if (success) {
                                deoOut = new WarehouseReturnDEO(null, "Produkt und Lagerbestand erfolgreich angelegt", Status.OK);
                            } else {
                                deoOut = new WarehouseReturnDEO(null, "Fehler beim Hinzufügen von Produkt und Lagerbestand", Status.ERROR);
                            }
                        } catch (RuntimeException e) {
                            logger.log(Level.ERROR, "Failed to add Produkt with Bestand: " + e.getMessage());
                            deoOut = new WarehouseReturnDEO(null, "Fehler beim Hinzufügen von Produkt und Lagerbestand: " + e.getMessage(), Status.ERROR);
                        }
                    } else {
                        deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case DELETE:
                if (deoIn.getData() != null && deoIn.getData() instanceof Produkt) {
                    Produkt l = Cast.safeCast(deoIn.getData(), Produkt.class);
                    try {
                        store.deleteProdukt(l);
                        deoOut = new WarehouseReturnDEO(null, "Produkt erfolgreich gelöscht", Status.OK);
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to delete Produkt ID " + l.getId() + ": " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Löschen des Produkts: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case INIT:
                deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
                break;
            case LIST:
                try {
                    deoOut = new WarehouseReturnDEO(store.getProdukts(), "Liste aller Produkte", Status.OK);
                } catch (RuntimeException e) {
                    logger.log(Level.ERROR, "Failed to retrieve Produkt list: " + e.getMessage());
                    deoOut = new WarehouseReturnDEO(null, "Fehler beim Abrufen der Produktliste: " + e.getMessage(), Status.ERROR);
                }
                break;
            case UPDATE:
                if (deoIn.getData() != null && deoIn.getData() instanceof Produkt) {
                    Produkt l = Cast.safeCast(deoIn.getData(), Produkt.class);
                    try {
                        store.updateProdukt(l);
                        deoOut = new WarehouseReturnDEO(null, "Produkt erfolgreich bearbeitet", Status.OK);
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to update Produkt ID " + l.getId() + ": " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Aktualisieren des Produkts: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case GETBYMODEL:
                if (deoIn.getData() != null && deoIn.getData() instanceof Produkt) {
                    Produkt l = Cast.safeCast(deoIn.getData(), Produkt.class);
                    try {
                        Produkt r = store.getProduktByModel(l);
                        deoOut = new WarehouseReturnDEO(r, "Produkt gefunden bearbeitet", Status.OK);
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to get Produkt by model: " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Suchen des Produkts: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case SEARCH:
                if (deoIn.getData() instanceof Integer) {
                    int id = Cast.safeCast(deoIn.getData(), Integer.class);
                    try {
                        Produkt produkt = ((Database) store).getProduktById(id);
                        if (produkt != null && produkt.getId() > 0) {
                            deoOut = new WarehouseReturnDEO(produkt, "Produkt mit ID " + id + " gefunden", Status.OK);
                        } else {
                            deoOut = new WarehouseReturnDEO(null, "Produkt mit ID " + id + " nicht gefunden", Status.ERROR);
                        }
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Failed to search Produkt ID " + id + ": " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Suchen des Produkts: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet Statistik-Befehle (BESTAND, TOP, LOW).
     *
     * @param deoIn  übermittelte DEO mit Kommando
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnis und Status
     */
    private WarehouseReturnDEO handleZoneStatistik(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
            case ADD:
            case DELETE:
            case INIT:
            case LIST:
            case UPDATE:
                deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
                break;
            case BESTAND:
                List<LagerBestand> data = store.getLagerBestands();
                Collections.sort(data, new BestandByProduktAlpha());
                deoOut = new WarehouseReturnDEO(data, "Liste der Produkte mit Lagerinfo alphabetisch", Status.OK);
                break;
            case TOP:
                data = store.getLagerBestands();
                Collections.sort(data, new BestandByLagerBestand());
                Collections.reverse(data); // Höchste Bestände zuerst
                List<LagerBestand> relevantData = data.stream().limit(10).collect(Collectors.toList());
                deoOut = new WarehouseReturnDEO(relevantData, "Liste der TOP 10 Produkte nach Lagerbestand", Status.OK);
                break;
            case LOW:
                data = store.getLagerBestands();
                Collections.sort(data, new BestandByLagerBestand());
                relevantData = data.stream().limit(10).collect(Collectors.toList());
                deoOut = new WarehouseReturnDEO(relevantData, "Liste der TOP 10 Produkte nach Lagerbestand", Status.OK);
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet INIT-Befehle: lädt Initialdaten ins System.
     *
     * @param deoIn  übermittelte DEO mit INIT-Kommando
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnisstatus
     */
    private WarehouseReturnDEO handleZoneInit(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
            case ADD:
            case DELETE:
            case LIST:
            case UPDATE:
                deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
                break;
            case INIT:
                Loading load = new Loading();
                load.initLoading(store);
                deoOut = new WarehouseReturnDEO(null, "Aktion erfolgreich bearbeitet", Status.OK);
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet allgemeine Steuerkommandos (CLOSE, END).
     *
     * @param deoIn  übermittelte DEO mit Kommando
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnisstatus
     */
    private WarehouseReturnDEO handleZoneGeneral(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
            case ADD:
            case DELETE:
            case INIT:
            case LIST:
            case UPDATE:
                deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
                break;
            case CLOSE:
                deoOut = new WarehouseReturnDEO(null, "Server-Verbindung wird beendet", Status.OK);
                run = false; // Beende nur diese Service-Instanz
                break;
            case END:
                deoOut = new WarehouseReturnDEO(null, "Server-Verbindung und Server wird beendet", Status.OK);
                run = false; // Beende diese Service-Instanz
                Server.run = false; // Beende den gesamten Server
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet Bestellungs-Befehle (ADD, DELETE, LIST, UPDATE, SEARCH, GETBYID, FILTER).
     *
     * @param deoIn  übermittelte DEO mit Kommando und Daten
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnis und Status
     */
    private WarehouseReturnDEO handleZoneBestellung(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
            case ADD:
                if (deoIn.getData() != null && deoIn.getData() instanceof Bestellung) {
                    Bestellung b = Cast.safeCast(deoIn.getData(), Bestellung.class);
                    store.addBestellung(b);
                    deoOut = new WarehouseReturnDEO(null, "Bestellung erfolgreich angelegt", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case DELETE:
                if (deoIn.getData() != null && deoIn.getData() instanceof Bestellung) {
                    Bestellung b = Cast.safeCast(deoIn.getData(), Bestellung.class);
                    store.deleteBestellung(b);
                    deoOut = new WarehouseReturnDEO(null, "Bestellung erfolgreich gelöscht", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case LIST:
                deoOut = new WarehouseReturnDEO(store.getBestellungen(), "Liste aller Bestellungen", Status.OK);
                break;
            case UPDATE:
                if (deoIn.getData() != null && deoIn.getData() instanceof Bestellung) {
                    Bestellung b = Cast.safeCast(deoIn.getData(), Bestellung.class);
                    store.updateBestellung(b);
                    deoOut = new WarehouseReturnDEO(null, "Bestellung erfolgreich bearbeitet", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case SEARCH:
                if (deoIn.getData() instanceof Integer) {
                    int kundeId = Cast.safeCast(deoIn.getData(), Integer.class);
                    List<Bestellung> bestellungen = store.getBestellungenByKundeId(kundeId);
                    if (!bestellungen.isEmpty()) {
                        deoOut = new WarehouseReturnDEO(bestellungen, "Bestellungen für Kunde " + kundeId + " gefunden", Status.OK);
                    } else {
                        deoOut = new WarehouseReturnDEO(null, "Keine Bestellungen für Kunde " + kundeId + " gefunden", Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Ungültige Kunden-ID", Status.ERROR);
                }
                break;
            case GETBYID:
                if (deoIn.getData() instanceof Integer) {
                    int id = Cast.safeCast(deoIn.getData(), Integer.class);
                    Bestellung b = ((Database) store).getBestellungById(id);
                    if (b != null) {
                        deoOut = new WarehouseReturnDEO(b, "Bestellung mit ID " + id + " gefunden", Status.OK);
                    } else {
                        deoOut = new WarehouseReturnDEO(null, "Bestellung mit ID " + id + " nicht gefunden", Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            case FILTER:
                if (deoIn.getData() instanceof FilterCriteria) {
                    FilterCriteria criteria = (FilterCriteria) deoIn.getData();
                    // Filtere Bestellungen nach Kriterien
                    List<Bestellung> bestellungen = ((Database) store).getFilteredBestellungen(
                        criteria.getStartDate(), criteria.getEndDate(), 
                        criteria.getMinAmount(), criteria.getProductId()
                    );
                    logger.log(Level.INFO, "Filterkriterien empfangen: " + criteria);
                    logger.log(Level.INFO, "Gefilterte Bestellungen: " + bestellungen.size());
                    deoOut = new WarehouseReturnDEO(bestellungen, "Gefilterte Bestellungen geladen", Status.OK);
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Ungültige Filterkriterien", Status.ERROR);
                }
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
                break;
        }
        return deoOut;
    }

    /**
     * Bearbeitet Nachbestellungs-Befehle (LIST, UPDATE).
     *
     * @param deoIn  übermittelte DEO mit Kommando und Daten
     * @param deoOut Ausgabe-DEO (wird überschrieben)
     * @return DEO mit Ergebnis und Status
     * @author Lennart Höpfner
     */
    private WarehouseReturnDEO handleZoneNachbestellung(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
        switch (deoIn.getCommand()) {
            case LIST:
                deoOut = new WarehouseReturnDEO(store.getNachbestellungen(), "Liste aller Nachbestellungen", Status.OK);
                break;
            case UPDATE:
                if (deoIn.getData() != null && deoIn.getData() instanceof thw.edu.javaII.port.warehouse.model.Nachbestellung) {
                    thw.edu.javaII.port.warehouse.model.Nachbestellung n = Cast.safeCast(deoIn.getData(), thw.edu.javaII.port.warehouse.model.Nachbestellung.class);
                    // Nachbestellung in der Datenbank aktualisieren
                    store.updateNachbestellung(n);

                    // Den zugehörigen LagerBestand aktualisieren
                    try {
                        LagerBestand matchingBestand = store.getLagerBestandByProduktId(n.getPid());
                        if (matchingBestand != null) {
                            // Neue Menge = alte Menge + Nachbestellmenge
                            int neueAnzahl = matchingBestand.getAnzahl() + n.getAnzahlnachbestellung();
                            matchingBestand.setAnzahl(neueAnzahl);
                            store.updateLagerBestand(matchingBestand);
                            logger.log(Level.INFO, "LagerBestand für Produkt-ID " + n.getPid() + " aktualisiert. Neue Menge: " + neueAnzahl);

                            // Aktualisiere das Nachbestellung-Objekt mit dem neuen aktuellen Bestand
                            n.setAktuellerbestand(neueAnzahl); // Setze den neuen aktuellen Bestand
                            n.setZukünftigerbestand(neueAnzahl); // Optional: Zukünftiger Bestand anpassen
                            store.updateNachbestellung(n); // Speichere die aktualisierte Nachbestellung

                            deoOut = new WarehouseReturnDEO(null, "Nachbestellung und Lagerbestand erfolgreich aktualisiert", Status.OK);
                        } else {
                            logger.log(Level.WARNING, "Kein LagerBestand für Produkt-ID " + n.getPid() + " gefunden");
                            deoOut = new WarehouseReturnDEO(null, "Nachbestellung aktualisiert, aber kein Lagerbestand gefunden", Status.WARNING);
                        }
                    } catch (RuntimeException e) {
                        logger.log(Level.ERROR, "Fehler beim Aktualisieren des Lagerbestands: " + e.getMessage());
                        deoOut = new WarehouseReturnDEO(null, "Fehler beim Aktualisieren des Lagerbestands: " + e.getMessage(), Status.ERROR);
                    }
                } else {
                    deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
                }
                break;
            default:
                deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
                break;
        }
        return deoOut;
    }
}
