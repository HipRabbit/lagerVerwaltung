package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

public class BestellungProdukt implements Serializable {
    private static final long serialVersionUID = 1L;
    private Produkt produkt;
    private int anzahl;

    public BestellungProdukt() {
    }

    public BestellungProdukt(Produkt produkt, int anzahl) {
        this.produkt = produkt;
        this.anzahl = anzahl;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }
}