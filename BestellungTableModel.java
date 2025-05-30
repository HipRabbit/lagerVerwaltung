package thw.edu.javaII.port.warehouse.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.model.BestellungProdukt;

public class BestellungTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<BestellungProduktEntry> entries;
    private String[] columnNames = { "Bestellung-ID", "Kunde ID", "Produkt ID", "Hersteller", "Produktname", "Menge", "Einzelpreis", "Gesamtpreis", "Datum" }; // "Hersteller" hinzugefügt

    private static class BestellungProduktEntry {
        private Bestellung bestellung;
        private BestellungProdukt produkt;

        public BestellungProduktEntry(Bestellung bestellung, BestellungProdukt produkt) {
            this.bestellung = bestellung;
            this.produkt = produkt;
        }
    }

    public BestellungTableModel(List<Bestellung> data) {
        this.entries = new ArrayList<>();
        for (Bestellung bestellung : data) {
            for (BestellungProdukt produkt : bestellung.getProdukte()) {
                entries.add(new BestellungProduktEntry(bestellung, produkt));
            }
        }
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BestellungProduktEntry entry = entries.get(rowIndex);
        Bestellung bestellung = entry.bestellung;
        BestellungProdukt produkt = entry.produkt;
        switch (columnIndex) {
            case 0: return bestellung.getId();
            case 1: return bestellung.getKunde().getId();
            case 2: return produkt.getProdukt().getId();
            case 3: return produkt.getProdukt().getHersteller(); // Hersteller
            case 4: return produkt.getProdukt().getName();
            case 5: return produkt.getAnzahl();
            case 6: return produkt.getProdukt().getPreis(); // Einzelpreis
            case 7: return produkt.getProdukt().getPreis() * produkt.getAnzahl(); // Gesamtpreis
            case 8: return bestellung.getDatum();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: case 1: case 2: return Integer.class;   // Bestellung-ID, Kunde ID, Produkt ID
            case 3: case 4: return String.class;           // Hersteller, Produktname
            case 5: return Integer.class;                  // Menge
            case 6: case 7: return Double.class;          // Einzelpreis, Gesamtpreis
            case 8: return java.util.Date.class;          // Datum
            default: return Object.class;
        }
    }
}