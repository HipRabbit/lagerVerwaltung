package thw.edu.javaII.port.warehouse.ui.model;

import thw.edu.javaII.port.warehouse.model.Bestellung;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BestellungOverviewTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<Bestellung> bestellungen;
    private final String[] columnNames = {"Bestellung-ID", "Kunde", "Datum", "Produktanzahl", "Gesamtpreis"};
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private final NumberFormat currencyFormat;

    public BestellungOverviewTableModel(List<Bestellung> bestellungen) {
        this.bestellungen = bestellungen != null ? bestellungen : new ArrayList<>();
        this.currencyFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        this.currencyFormat.setMinimumFractionDigits(2);
        this.currencyFormat.setMaximumFractionDigits(2);
    }

    public void setData(List<Bestellung> bestellungen) {
        this.bestellungen = bestellungen != null ? bestellungen : new ArrayList<>();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return bestellungen.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Bestellung bestellung = bestellungen.get(rowIndex);
        switch (columnIndex) {
            case 0: return bestellung.getId();
            case 1: return bestellung.getKunde() != null ? bestellung.getKunde().toString() : "Unbekannt";
            case 2: return bestellung.getDatum() != null ? sdf.format(bestellung.getDatum()) : "Unbekannt";
            case 3: return bestellung.getProdukte() != null ? bestellung.getProdukte().size() : 0;
            case 4:
                if (bestellung.getProdukte() != null) {
                    double gesamtpreis = bestellung.getProdukte().stream()
                            .mapToDouble(bp -> bp.getProdukt().getPreis() * bp.getAnzahl())
                            .sum();
                    return currencyFormat.format(gesamtpreis) + " €";
                }
                return currencyFormat.format(0.0) + " €";
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return Integer.class;
            case 4: return String.class;
            default: return Object.class;
        }
    }

    public Bestellung getBestellungAt(int rowIndex) {
        return bestellungen.get(rowIndex);
    }

    public boolean isBestellungComplete(int rowIndex) {
        Bestellung bestellung = bestellungen.get(rowIndex);
        return bestellung.getErfassung() != null &&
               bestellung.getVersand() != null &&
               bestellung.getLieferung() != null &&
               bestellung.getBezahlung() != null;
    }

    public int countSetTimestamps(int rowIndex) {
        Bestellung bestellung = bestellungen.get(rowIndex);
        int count = 0;
        if (bestellung.getErfassung() != null) count++;
        if (bestellung.getVersand() != null) count++;
        if (bestellung.getLieferung() != null) count++;
        if (bestellung.getBezahlung() != null) count++;
        return count;
    }

    public double calculateTotalRevenue() {
        double totalRevenue = 0.0;
        for (Bestellung bestellung : bestellungen) {
            if (bestellung.getProdukte() != null) {
                double gesamtpreis = bestellung.getProdukte().stream()
                        .mapToDouble(bp -> bp.getProdukt().getPreis() * bp.getAnzahl())
                        .sum();
                totalRevenue += gesamtpreis;
            }
        }
        return totalRevenue;
    }

    public NumberFormat getCurrencyFormat() {
        return currencyFormat;
    }
}