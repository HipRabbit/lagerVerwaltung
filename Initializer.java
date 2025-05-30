package thw.edu.javaII.port.warehouse.init;

import java.util.ArrayList;
import java.util.List;
import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.model.DemoModel;
import thw.edu.javaII.port.warehouse.model.Lager;
import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.model.Kunde;


public class Initializer {
    private List<Lager> lager;
    private List<LagerPlatz> lagerplatz;
    private List<LagerBestand> lagerbestand;
    private List<Produkt> produkt;
    private List<DemoModel> demo;
    private List<Kunde> kunden;
    private List<Bestellung> bestellung;

    public Initializer() {
        lager = new ArrayList<>();
        lagerplatz = new ArrayList<>();
        lagerbestand = new ArrayList<>();
        produkt = new ArrayList<>();
        demo = new ArrayList<>();
        kunden = new ArrayList<>();
        bestellung = new ArrayList<>();
        init();
    }

    public List<Lager> getLager() {
        return lager;
    }

    public List<LagerPlatz> getLagerplatz() {
        return lagerplatz;
    }

    public List<LagerBestand> getLagerbestand() {
        return lagerbestand;
    }

    public List<Produkt> getProdukt() {
        return produkt;
    }

    public List<DemoModel> getDemo() {
        return demo;
    }

    public List<Kunde> getKunden() {
        return kunden;
    }
    public List<Bestellung> getBestellung(){
    	return bestellung;
    }

    

    private void init() {
        lager.add(new Lager(1000, "Hauptlager", "Heilbronn", "Hochregallager"));
        lager.add(new Lager(1100, "Verkauflager", "Stuttgart", "Halle"));
        lager.add(new Lager(1200, "Garage", "Heilbronn", "Halle"));
        lager.add(new Lager(1300, "Sperrgut", "Ludwigsburg", "Freigelände"));
        lager.add(new Lager(1400, "Hafenlager", "Hamburg", "Halle"));

        produkt.add(new Produkt(1000, "Kombi-Limousine", "Mercedes-Benz", 80000.0d));
        produkt.add(new Produkt(1010, "Mars-Schokoriegel", "Nestle", 0.8d));
        produkt.add(new Produkt(1020, "DIN-A4 Ordner", "Leitz", 1.5d));
        produkt.add(new Produkt(1030, "Flachstahl 4x40x6000", "Thysen-Krupp", 120.93d));
        produkt.add(new Produkt(1040, "Steuerplatine", "Eigenproduktion", 250.0d));
        produkt.add(new Produkt(1050, "Mutter M8", "Schmitz Schrauben", 0.1d));
        produkt.add(new Produkt(1060, "Pistenraupe", "PistenBully", 280000.0d));
        produkt.add(new Produkt(1070, "Bierfass", "Küfferei Horn", 50.0d));
        produkt.add(new Produkt(1080, "Bierkisten", "Kastenbau Leer", 12.0d));
        produkt.add(new Produkt(1090, "Transistor BC547", "Conrad Electronik", 0.01d));
        produkt.add(new Produkt(1100, "Bildschirm 24 Zoll", "Dell", 150.0d));
        produkt.add(new Produkt(1110, "Notebook 13 Zoll", "Lenovo", 850.0d));
        produkt.add(new Produkt(1120, "Festplatte SSD 1TB", "Toschiba", 220.0d));
        produkt.add(new Produkt(1130, "Akkuschrauber", "Makita", 300.0d));
        produkt.add(new Produkt(1140, "Apfelbaum", "Bauer Roth", 500.0d));
        produkt.add(new Produkt(1150, "Leimbinder 5m x 3,5m", "Tischler Tisch", 5580.0d));
        produkt.add(new Produkt(1160, "Druckerpapier", "Office Discount", 4.98d));
        produkt.add(new Produkt(1170, "Kugelschreiber", "Office Discount", 1.12d));
        produkt.add(new Produkt(1180, "Schraube M8x40", "Schmitz Schrauben", 0.41d));
        produkt.add(new Produkt(1190, "Schraube M6X40", "Schmitz Schrauben", 0.35d));
        produkt.add(new Produkt(1200, "Backofen AS400", "Bosch", 657.0d));
        produkt.add(new Produkt(1210, "Bounty-Schokoriegel", "Nestle", 0.75d));
        produkt.add(new Produkt(1220, "Kartoffelchips", "Funny", 1.23d));
        produkt.add(new Produkt(1230, "Kugellager", "Boie", 125.12d));
        produkt.add(new Produkt(1240, "Kabeltrommel", "Lapp Kabel", 32.50d));
        produkt.add(new Produkt(1250, "LED-Glühbirne GU10", "Philips", 14.25d));
        produkt.add(new Produkt(1260, "Trox-Schraube 6x25", "Schmitz Schrauben", 0.28d));
        produkt.add(new Produkt(1270, "Schweißgerät", "Ansamann", 799.0d));
        produkt.add(new Produkt(1280, "Batterie AA", "Varta", 0.87d));
        produkt.add(new Produkt(1290, "Hohlraumdübel 8x120", "Fischer", 1.23d));

        lagerplatz.add(new LagerPlatz(1010, "A1", 200, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1020, "A2", 400, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1030, "A3", 1000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1040, "B1", 100, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1050, "B2", 500, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1060, "B3", 800, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1070, "C1", 1500, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1080, "C2", 40000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1090, "C3", 40000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1110, "C4", 40000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1120, "C5", 40000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1130, "D1", 500, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1140, "D2", 500, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1150, "D3", 500, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1160, "D4", 500, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1170, "D5", 500, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1180, "E1", 1000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1190, "E2", 1000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1200, "E3", 1000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1210, "E4", 1000, getLagerByID(1000)));
        lagerplatz.add(new LagerPlatz(1220, "E5", 1000, getLagerByID(1000)));

        lagerplatz.add(new LagerPlatz(2110, "V1", 2000, getLagerByID(1100)));
        lagerplatz.add(new LagerPlatz(2120, "V2", 1000, getLagerByID(1100)));
        lagerplatz.add(new LagerPlatz(2130, "V3", 200, getLagerByID(1100)));
        lagerplatz.add(new LagerPlatz(2140, "V4", 3000, getLagerByID(1100)));
        lagerplatz.add(new LagerPlatz(2150, "V5", 5000, getLagerByID(1100)));
        lagerplatz.add(new LagerPlatz(2160, "V6", 400, getLagerByID(1100)));
        lagerplatz.add(new LagerPlatz(2170, "V7", 3000, getLagerByID(1100)));
        lagerplatz.add(new LagerPlatz(2180, "V8", 5000, getLagerByID(1100)));

        lagerplatz.add(new LagerPlatz(3210, "10", 200, getLagerByID(1200)));
        lagerplatz.add(new LagerPlatz(3220, "11", 200, getLagerByID(1200)));
        lagerplatz.add(new LagerPlatz(3230, "12", 200, getLagerByID(1200)));
        lagerplatz.add(new LagerPlatz(3240, "13", 200, getLagerByID(1200)));
        lagerplatz.add(new LagerPlatz(3250, "14", 200, getLagerByID(1200)));

        lagerplatz.add(new LagerPlatz(4310, "Zone 1", 10, getLagerByID(1300)));
        lagerplatz.add(new LagerPlatz(4320, "Zone 2", 30, getLagerByID(1300)));
        lagerplatz.add(new LagerPlatz(4330, "Zone 3", 10, getLagerByID(1300)));
        lagerplatz.add(new LagerPlatz(4340, "Zone 4", 20, getLagerByID(1300)));
        lagerplatz.add(new LagerPlatz(4350, "Zone 5", 20, getLagerByID(1300)));

        lagerplatz.add(new LagerPlatz(5410, "Terminal1-1", 5000, getLagerByID(1400)));
        lagerplatz.add(new LagerPlatz(5420, "Terminal1-2", 5000, getLagerByID(1400)));
        lagerplatz.add(new LagerPlatz(5430, "Terminal1-3", 5000, getLagerByID(1400)));
        lagerplatz.add(new LagerPlatz(5440, "Terminal1-4", 5000, getLagerByID(1400)));
        lagerplatz.add(new LagerPlatz(5450, "Terminal1-5", 5000, getLagerByID(1400)));
        lagerplatz.add(new LagerPlatz(5460, "Terminal1-6", 5000, getLagerByID(1400)));
        lagerplatz.add(new LagerPlatz(5470, "Terminal1-7", 5000, getLagerByID(1400)));

        lagerbestand.add(new LagerBestand(1000, 10, getProduktByID(1000), getLagerPlatzByID(3210)));
        lagerbestand.add(new LagerBestand(1010, 100, getProduktByID(1010), getLagerPlatzByID(2130)));
        lagerbestand.add(new LagerBestand(1020, 334, getProduktByID(1020), getLagerPlatzByID(1020)));
        lagerbestand.add(new LagerBestand(1030, 10, getProduktByID(1030), getLagerPlatzByID(4340)));
        lagerbestand.add(new LagerBestand(1040, 106, getProduktByID(1040), getLagerPlatzByID(1010)));
        lagerbestand.add(new LagerBestand(1050, 1000, getProduktByID(1050), getLagerPlatzByID(1070)));
        lagerbestand.add(new LagerBestand(1060, 2, getProduktByID(1060), getLagerPlatzByID(4310)));
        lagerbestand.add(new LagerBestand(1070, 100, getProduktByID(1070), getLagerPlatzByID(1040)));
        lagerbestand.add(new LagerBestand(1080, 500, getProduktByID(1080), getLagerPlatzByID(1050)));
        lagerbestand.add(new LagerBestand(1090, 1000, getProduktByID(1090), getLagerPlatzByID(1030)));
        lagerbestand.add(new LagerBestand(1100, 76, getProduktByID(1100), getLagerPlatzByID(2120)));
        lagerbestand.add(new LagerBestand(1110, 120, getProduktByID(1110), getLagerPlatzByID(2110)));
        lagerbestand.add(new LagerBestand(1120, 100, getProduktByID(1120), getLagerPlatzByID(5410)));
        lagerbestand.add(new LagerBestand(1130, 33, getProduktByID(1130), getLagerPlatzByID(5420)));
        lagerbestand.add(new LagerBestand(1140, 24, getProduktByID(1140), getLagerPlatzByID(4320)));
        lagerbestand.add(new LagerBestand(1150, 5, getProduktByID(1150), getLagerPlatzByID(4330)));
        lagerbestand.add(new LagerBestand(1160, 365, getProduktByID(1160), getLagerPlatzByID(5430)));
        lagerbestand.add(new LagerBestand(1170, 578, getProduktByID(1170), getLagerPlatzByID(1060)));
        lagerbestand.add(new LagerBestand(1180, 10543, getProduktByID(1180), getLagerPlatzByID(1080)));
        lagerbestand.add(new LagerBestand(1190, 20543, getProduktByID(1190), getLagerPlatzByID(1090)));
        lagerbestand.add(new LagerBestand(1200, 50, getProduktByID(1200), getLagerPlatzByID(3220)));
        lagerbestand.add(new LagerBestand(1210, 487, getProduktByID(1210), getLagerPlatzByID(2140)));
        lagerbestand.add(new LagerBestand(1220, 765, getProduktByID(1220), getLagerPlatzByID(2150)));
        lagerbestand.add(new LagerBestand(1230, 239, getProduktByID(1230), getLagerPlatzByID(1130)));
        lagerbestand.add(new LagerBestand(1240, 209, getProduktByID(1240), getLagerPlatzByID(1140)));
        lagerbestand.add(new LagerBestand(1250, 398, getProduktByID(1250), getLagerPlatzByID(2160)));
        lagerbestand.add(new LagerBestand(1260, 30547, getProduktByID(1260), getLagerPlatzByID(1120)));
        lagerbestand.add(new LagerBestand(1270, 10, getProduktByID(1270), getLagerPlatzByID(3230)));
        lagerbestand.add(new LagerBestand(1280, 300, getProduktByID(1280), getLagerPlatzByID(1150)));
        lagerbestand.add(new LagerBestand(1290, 678, getProduktByID(1290), getLagerPlatzByID(1160)));

        demo.add(new DemoModel(1, "Hallo"));
        demo.add(new DemoModel(2, "Welt"));
        demo.add(new DemoModel(3, "Erde"));
        
        
//Neu
        kunden.add(new Kunde(1, "Max", "Mustermann", "Musterstraße 1, 80331 München", "Musterstraße 1, 80331 München"));
        kunden.add(new Kunde(2, "Erika", " Beispiel", "Beispielweg 2, 50667 Köln", "Beispielweg 2, 50667 Köln"));
        kunden.add(new Kunde(3, "Anna",  "Schmidt", "Schillerstraße 10, 70173 Stuttgart", "Schillerstraße 10, 70173 Stuttgart"));
        kunden.add(new Kunde(4, "Klaus",  "Meier", "Goethestraße 5, 70173 Stuttgart", "Goethestraße 5, 70173 Stuttgart"));
        kunden.add(new Kunde(5, "Sophie", "Weber", "Bahnhofstraße 20, 80331 München", "Bahnhofstraße 20, 80331 München"));
        
    }

       

    private Lager getLagerByID(int id) {
        for (Lager l : lager) {
            if (l.getId() == id)
                return l;
        }
        return null;
    }

    private Produkt getProduktByID(int id) {
        for (Produkt l : produkt) {
            if (l.getId() == id)
                return l;
        }
        return null;
    }

    private LagerPlatz getLagerPlatzByID(int id) {
        for (LagerPlatz l : lagerplatz) {
            if (l.getId() == id)
                return l;
        }
        return null;
    }
    //Neu

}