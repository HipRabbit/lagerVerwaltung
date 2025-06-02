package thw.edu.javaII.port.warehouse.server.comparator;

import java.util.Comparator;

import thw.edu.javaII.port.warehouse.model.LagerBestand;

/**
 * Comparator für die alphabetische Sortierung von {@link LagerBestand}-Objekten nach dem Produktnamen.
 *
 * @author Lennart Höpfner
 */
public class BestandByProduktAlpha implements Comparator<LagerBestand> {

    /**
     * Vergleicht zwei {@link LagerBestand}-Objekte basierend auf dem Namen des zugehörigen Produkts.
     *
     * @param a das erste {@link LagerBestand}-Objekt
     * @param b das zweite {@link LagerBestand}-Objekt
     * @return das Ergebnis des alphabetischen Vergleichs der Produktnamen
     */
    @Override
    public int compare(LagerBestand a, LagerBestand b) {
        return a.getProdukt_id().getName().compareTo(b.getProdukt_id().getName());
    }
}