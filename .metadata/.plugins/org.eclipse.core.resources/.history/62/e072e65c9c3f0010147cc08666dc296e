package thw.edu.javaII.port.warehouse.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import thw.edu.javaII.port.warehouse.model.common.Cast;
import thw.edu.javaII.port.warehouse.model.deo.Status;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.Lager;
import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.server.comparator.BestandByLagerBestand;
import thw.edu.javaII.port.warehouse.server.comparator.BestandByProduktAlpha;
import thw.edu.javaII.port.warehouse.server.data.Database;
import thw.edu.javaII.port.warehouse.server.data.IStorage;
import thw.edu.javaII.port.warehouse.server.init.Loading;

public class Service extends Thread {
	static int count = 0;
	private int currentNumber = 0;
	private Socket sock;
	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private IStorage store;
	private boolean run;

	public Service(Socket sock) {
		try {
			run = true;
			this.sock = sock;
			this.store = new Database();
			currentNumber = ++count;
			toClient = new ObjectOutputStream(sock.getOutputStream());
			fromClient = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			System.out.println("IO-Error bei Client " + currentNumber);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Fehler bei der Erzeugung der Datenbank");
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Bearbeitung fuer Client " + currentNumber + " gestartet");
		try {
			while (run) {
				WarehouseDEO deoIn = (WarehouseDEO) fromClient.readObject();
				WarehouseReturnDEO deoOut = null;
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
				default:
					deoOut = new WarehouseReturnDEO(null, "Unbekannte Zone", Status.ERROR);
					break;
				}
				toClient.writeObject(deoOut);
			}
		} catch (IOException e) {
			System.out.println("IO-Error bei Client " + currentNumber);
		} catch (ClassNotFoundException e) {
			System.out.println("Fehler beim uebergeben Objekt");
		} finally {
			try {
				if (fromClient != null)
					fromClient.close();
				if (toClient != null)
					toClient.close();
				if (sock != null)
					sock.close();
			} catch (IOException e) {
				;
			}

		}
		System.out.println("Protokoll fuer Client " + currentNumber + " beendet");
	}

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
		default:
			deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
			break;
		}
		return deoOut;
	}

	private WarehouseReturnDEO handleZoneLagerPlatz(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
		switch (deoIn.getCommand()) {
		case ADD:
			if (deoIn.getData() != null && deoIn.getData() instanceof LagerPlatz) {
				LagerPlatz l = Cast.safeCast(deoIn.getData(), LagerPlatz.class);
				store.addLagerPlatz(l);
				deoOut = new WarehouseReturnDEO(null, "Lagerplatz erfolgreich angelegt", Status.OK);
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
		default:
			deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
			break;
		}
		return deoOut;
	}

	private WarehouseReturnDEO handleZoneLagerBestand(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
		switch (deoIn.getCommand()) {
		case ADD:
			if (deoIn.getData() != null && deoIn.getData() instanceof LagerBestand) {
				LagerBestand l = Cast.safeCast(deoIn.getData(), LagerBestand.class);
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
		default:
			deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
			break;
		}
		return deoOut;
	}

	private WarehouseReturnDEO handleZoneProdukt(WarehouseDEO deoIn, WarehouseReturnDEO deoOut) {
		switch (deoIn.getCommand()) {
		case ADD:
			if (deoIn.getData() != null && deoIn.getData() instanceof Produkt) {
				Produkt l = Cast.safeCast(deoIn.getData(), Produkt.class);
				store.addProdukt(l);
				deoOut = new WarehouseReturnDEO(null, "Produkt erfolgreich angelegt", Status.OK);
			} else {
				deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
			}
			break;
		case DELETE:
			if (deoIn.getData() != null && deoIn.getData() instanceof Produkt) {
				Produkt l = Cast.safeCast(deoIn.getData(), Produkt.class);
				store.deleteProdukt(l);
				deoOut = new WarehouseReturnDEO(null, "Produkt erfolgreich gelöscht", Status.OK);
			} else {
				deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
			}
			break;
		case INIT:
			deoOut = new WarehouseReturnDEO(null, "Für die Zone nicht unterstütztes Kommando", Status.INFO);
			break;
		case LIST:
			deoOut = new WarehouseReturnDEO(store.getProdukts(), "Liste aller Produkte", Status.OK);
			break;
		case UPDATE:
			if (deoIn.getData() != null && deoIn.getData() instanceof Produkt) {
				Produkt l = Cast.safeCast(deoIn.getData(), Produkt.class);
				store.updateProdukt(l);
				deoOut = new WarehouseReturnDEO(null, "Produkt erfolgreich bearbeitet", Status.OK);
			} else {
				deoOut = new WarehouseReturnDEO(null, "Falsche Daten übergeben", Status.ERROR);
			}
			break;
		case GETBYMODEL:
			if (deoIn.getData() != null && deoIn.getData() instanceof Produkt) {
				Produkt l = Cast.safeCast(deoIn.getData(), Produkt.class);
				Produkt r = store.getProduktByModel(l);
				deoOut = new WarehouseReturnDEO(r, "Produkt gefunden bearbeitet", Status.OK);
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
			deoOut = new WarehouseReturnDEO(data, "Liste der Prdoukte mit Lagerinfo alphabetisch", Status.OK);
			break;
		case TOP:
			data = store.getLagerBestands();
			Collections.sort(data, new BestandByLagerBestand());
			Collections.reverse(data);
			List<LagerBestand> relevantData = data.stream().limit(10).collect(Collectors.toList());
			deoOut = new WarehouseReturnDEO(relevantData, "Liste der TOP 10 Prdoukte nach Lagerbestand", Status.OK);
			break;
		case LOW:
			data = store.getLagerBestands();
			Collections.sort(data, new BestandByLagerBestand());
			relevantData = data.stream().limit(10).collect(Collectors.toList());
			deoOut = new WarehouseReturnDEO(relevantData, "Liste der TOP 10 Prdoukte nach Lagerbestand", Status.OK);
			break;
		default:
			deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
			break;
		}
		return deoOut;
	}

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
			run = false;
			break;
		case END:
			deoOut = new WarehouseReturnDEO(null, "Server-Verbindung und Server wird beendet", Status.OK);
			run = false;
			Server.run = false;
			break;
		default:
			deoOut = new WarehouseReturnDEO(null, "Unbekanntes Kommando", Status.ERROR);
			break;
		}
		return deoOut;
	}
}
