package thw.edu.javaII.port.warehouse.server.comparator;

import java.util.Comparator;

import thw.edu.javaII.port.warehouse.model.LagerBestand;

public class BestandByLagerBestand implements Comparator<LagerBestand> {
	public int compare(LagerBestand a, LagerBestand b) {
		return a.getAnzahl() - b.getAnzahl();
	}
}
