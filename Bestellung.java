package thw.edu.javaII.port.warehouse.model;

//Neu
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bestellung implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Kunde kunde;
    private List<BestellungProdukt> produkte;
    private Date datum;
    private Date erfassung;
    private Date versand;
    private Date lieferung;
    private Date bezahlung;
    private String status;

    public Bestellung() {
        this.produkte = new ArrayList<>();
        this.status = "offen";
    }

    public Bestellung(int id, Kunde kunde, List<BestellungProdukt> produkte, Date datum) {
        this.id = id;
        this.kunde = kunde;
        this.produkte = produkte != null ? produkte : new ArrayList<>();
        this.datum = datum != null ? new Date(datum.getTime()) : null;
        this.status = "offen";
    }

    public Bestellung(Kunde kunde, Date datum, List<BestellungProdukt> produkte) {
        this.id = 0; // ID wird von der Datenbank generiert
        this.kunde = kunde;
        this.produkte = produkte != null ? produkte : new ArrayList<>();
        this.datum = datum != null ? new Date(datum.getTime()) : new Date(); // Datum auf aktuelle Zeit setzen, wenn null
        this.erfassung = new Date(); // Erfassung wird automatisch auf aktuelle Zeit gesetzt
        this.status = "offen";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public List<BestellungProdukt> getProdukte() {
        return produkte;
    }

    public void setProdukte(List<BestellungProdukt> produkte) {
        this.produkte = produkte != null ? produkte : new ArrayList<>();
    }

    public Date getDatum() {
        return datum != null ? new Date(datum.getTime()) : (erfassung != null ? new Date(erfassung.getTime()) : null);
    }

    public void setDatum(Date datum) {
        this.datum = datum != null ? new Date(datum.getTime()) : null;
    }

    public Date getErfassung() {
        return erfassung;
    }

    public void setErfassung(Date erfassung) {
        this.erfassung = erfassung != null ? new Date(erfassung.getTime()) : null;
        if (this.datum == null) {
            this.datum = this.erfassung; // Synchronisiere datum mit erfassung, wenn datum leer ist
        }
    }

    public Date getVersand() {
        return versand;
    }

    public void setVersand(Date versand) {
        this.versand = versand;
    }

    public Date getLieferung() {
        return lieferung;
    }

    public void setLieferung(Date lieferung) {
        this.lieferung = lieferung;
    }

    public Date getBezahlung() {
        return bezahlung;
    }

    public void setBezahlung(Date bezahlung) {
        this.bezahlung = bezahlung;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}