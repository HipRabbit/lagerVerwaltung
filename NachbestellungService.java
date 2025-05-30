package thw.edu.javaII.port.warehouse.server;

import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.model.Nachbestellung;
import thw.edu.javaII.port.warehouse.server.data.Database;

import java.util.Date;
import java.util.List;

public class NachbestellungService {
    private final Database database;

    public NachbestellungService(Database database) {
        this.database = database;
    }

    public void checkLowStock() {
        List<LagerBestand> bestande = database.getLagerBestands();
        for (LagerBestand bestand : bestande) {
            LagerPlatz platz = database.getLagerPlatzById(bestand.getLagerplatz_id().getId());
            if (bestand.getAnzahl() < platz.getKapazitaet() / 2) {
                int menge = platz.getKapazitaet() - bestand.getAnzahl();
                Nachbestellung reorder = new Nachbestellung(0, bestand.getProdukt_id(), platz, menge, "PENDING", new Date());
                database.addNachbestellung(reorder);
            }
        }
    }
}