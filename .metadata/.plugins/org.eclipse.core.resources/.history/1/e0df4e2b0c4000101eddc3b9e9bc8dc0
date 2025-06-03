package thw.edu.javaII.port.warehouse.ui.model;

import thw.edu.javaII.port.warehouse.model.BestellungProdukt;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BestellungDetailTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private final List<BestellungProdukt> produkte;
    private final String[] columnNames = {"Produktname", "Anzahl", "Einzelpreis (EUR)", "Gesamtpreis (EUR)"};

    public BestellungDetailTableModel(List<BestellungProdukt> produkte) {
        this.produkte = produkte != null ? produkte : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return produkte.size();
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
        BestellungProdukt bp = produkte.get(rowIndex);
        switch (columnIndex) {
            case 0: return bp.getProdukt().getName();
            case 1: return bp.getAnzahl();
            case 2: return bp.getProdukt().getPreis();
            case 3: return bp.getProdukt().getPreis() * bp.getAnzahl();
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return String.class;
            case 1: return Integer.class;
            case 2: case 3: return Double.class;
            default: return Object.class;
        }
    }
}