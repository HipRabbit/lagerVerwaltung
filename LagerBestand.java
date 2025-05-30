package thw.edu.javaII.port.warehouse.model;

import java.io.Serializable;

public class LagerBestand implements Serializable {
	private static final long serialVersionUID = 5335970888396140828L;
	private static final String PRINT_FORMAT = "[%-20s - %-50s - %-30s - %-40s]";
	private int id;
	private int anzahl;
	private Produkt produkt_id;
	private LagerPlatz lagerplatz_id;
	public static final int columnCount = 6;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public Produkt getProdukt_id() {
		return produkt_id;
	}

	public void setProdukt_id(Produkt produkt_id) {
		this.produkt_id = produkt_id;
	}

	public LagerPlatz getLagerplatz_id() {
		return lagerplatz_id;
	}

	public void setLagerplatz_id(LagerPlatz lagerplatz_id) {
		this.lagerplatz_id = lagerplatz_id;
	}

	public LagerBestand() {

	}

	public LagerBestand(int id, int anzahl, Produkt produkt_id, LagerPlatz lagerplatz_id) {
		super();
		this.id = id;
		this.anzahl = anzahl;
		this.produkt_id = produkt_id;
		this.lagerplatz_id = lagerplatz_id;
	}

	public String toListString() {
		return String.format(PRINT_FORMAT, id, anzahl, produkt_id.getName(), lagerplatz_id.getName());
	}

	public Object getValueAtColumn(int column) {
		switch (column) {
		case 0:
			return getProdukt_id().getId();
		case 1:
			return getProdukt_id().getName();
		case 2:
			return getProdukt_id().getHersteller();
		case 3:
			return getAnzahl();
		case 4:
			return getLagerplatz_id().getName();
		case 5:
			return getLagerplatz_id().getLager_id().getName();
		default:
			return null;
		}
	}

}