package thw.edu.javaII.port.warehouse.server.comparator;

import java.util.Comparator;

import thw.edu.javaII.port.warehouse.model.LagerBestand;

/**
 * Comparator für die Sortierung von {@link LagerBestand}-Objekten nach der Anzahl.
 *
 * @author Lennart Höpfner
 */
public class BestandByLagerBestand implements Comparator<LagerBestand> {

    /**
     * Vergleicht zwei {@link LagerBestand}-Objekte basierend auf der Anzahl.
     *
     * @param a das erste {@link LagerBestand}-Objekt
     * @param b das zweite {@link LagerBestand}-Objekt
     * @return ein negativer Wert, wenn a weniger Anzahl hat, ein positiver Wert, wenn a mehr Anzahl hat, oder 0, wenn gleich
     */
    @Override
    public int compare(LagerBestand a, LagerBestand b) {
        return a.getAnzahl() - b.getAnzahl();
    }
}