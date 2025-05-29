package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

public class Reorder implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int produktId;
    private int lagerPlatzId;
    private int menge;
    private long bestellDatum;
    private String status;

    public Reorder(int id, int produktId, int lagerPlatzId, int menge, long bestellDatum, String status) {
        this.id = id;
        this.produktId = produktId;
        this.lagerPlatzId = lagerPlatzId;
        this.menge = menge;
        this.bestellDatum = bestellDatum;
        this.status = status;
    }

    // Getter und Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProduktId() { return produktId; }
    public void setProduktId(int produktId) { this.produktId = produktId; }
    public int getLagerPlatzId() { return lagerPlatzId; }
    public void setLagerPlatzId(int lagerPlatzId) { this.lagerPlatzId = lagerPlatzId; }
    public int getMenge() { return menge; }
    public void setMenge(int menge) { this.menge = menge; }
    public long getBestellDatum() { return bestellDatum; }
    public void setBestellDatum(long bestellDatum) { this.bestellDatum = bestellDatum; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Reorder{id=" + id + ", produktId=" + produktId + ", lagerPlatzId=" + lagerPlatzId +
               ", menge=" + menge + ", bestellDatum=" + bestellDatum + ", status=" + status + "}";
    }
}