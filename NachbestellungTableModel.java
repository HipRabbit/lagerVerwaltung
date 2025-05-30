package thw.edu.javaII.port.warehouse.ui.model;

import thw.edu.javaII.port.warehouse.model.Nachbestellung;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import java.util.ArrayList;
import java.util.List;

public class NachbestellungTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<Nachbestellung> data = new ArrayList<>();
    private final String[] columnNames = {"ID", "Produkt", "Lagerplatz", "Menge", "Status", "Datum", "Aktion"};

    public NachbestellungTableModel(List<Nachbestellung> data) {
        setData(data);
    }

    public void setData(List<Nachbestellung> data) {
        this.data = (data != null) ? data : new ArrayList<>();
        fireTableDataChanged();
    }

    public List<Nachbestellung> getData() {
        return data;
    }

    @Override
    public int getRowCount() {
        return data.size();
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
        Nachbestellung nachbestellung = data.get(rowIndex);
        switch (columnIndex) {
            case 0: return nachbestellung.getId();
            case 1: return nachbestellung.getProdukt().getName();
            case 2: return nachbestellung.getLagerPlatz().getName();
            case 3: return nachbestellung.getMenge();
            case 4: return nachbestellung.getStatus();
            case 5: return nachbestellung.getDatum();
            case 6: return nachbestellung.getStatus().equals("PENDING") ? "Bestätigen/Abbrechen" : "";
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 6 && data.get(rowIndex).getStatus().equals("PENDING");
    }

    public Nachbestellung getObjectAt(int rowIndex) {
        return data.get(rowIndex);
    }

    public void setJTableColumnsWidth(JTable table, int tablePreferredWidth, double... percentages) {
        double total = 0;
        for (double percentage : percentages) {
            total += percentage;
        }
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int) (tablePreferredWidth * (percentages[i] / total)));
        }
    }
}