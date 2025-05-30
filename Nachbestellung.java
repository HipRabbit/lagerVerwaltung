package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;
import java.util.Date;

public class Nachbestellung implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Produkt produkt;
    private LagerPlatz lagerPlatz;
    private int menge;
    private String status;
    private Date datum;

    public Nachbestellung() {
    }

    public Nachbestellung(int id, Produkt produkt, LagerPlatz lagerPlatz, int menge, String status, Date datum) {
        this.id = id;
        this.produkt = produkt;
        this.lagerPlatz = lagerPlatz;
        this.menge = menge;
        this.status = status;
        this.datum = datum;
    }

    // Getter und Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Produkt getProdukt() { return produkt; }
    public void setProdukt(Produkt produkt) { this.produkt = produkt; }
    public LagerPlatz getLagerPlatz() { return lagerPlatz; }
    public void setLagerPlatz(LagerPlatz lagerPlatz) { this.lagerPlatz = lagerPlatz; }
    public int getMenge() { return menge; }
    public void setMenge(int menge) { this.menge = menge; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getDatum() { return datum; }
    public void setDatum(Date datum) { this.datum = datum; }

    public String toListString() {
        return String.format("[%-10s - %-30s - %-20s - %-10s - %-10s - %-20s]",
                id, produkt.getName(), lagerPlatz.getName(), menge, status, datum);
    }
}