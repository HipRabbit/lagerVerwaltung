package thw.edu.javaII.port.warehouse.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import thw.edu.javaII.port.warehouse.model.Kunde;

public class KundeTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<Kunde> originalData; // Speichert die vollständige Liste
    private List<Kunde> filteredData; // Speichert die gefilterte Liste
    private String[] columnNames = { "ID", "Vorname", "Nachname", "Lieferadresse", "Rechnungsadresse" };

    public KundeTableModel(List<Kunde> data) {
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
    }

    public void setData(List<Kunde> data) {
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
        fireTableDataChanged();
    }

    public void sortById() {
        Collections.sort(filteredData, Comparator.comparingInt(Kunde::getId));
        fireTableDataChanged();
    }

    public void sortByName() {
        Collections.sort(filteredData, Comparator.comparing(k -> (k.getVorname() + " " + k.getNachname()), String.CASE_INSENSITIVE_ORDER));
        fireTableDataChanged();
    }

    public void filterByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            filteredData = new ArrayList<>(originalData);
        } else {
            String lowerCaseTerm = searchTerm.trim().toLowerCase();
            filteredData = originalData.stream()
                    .filter(k -> k.getVorname().toLowerCase().contains(lowerCaseTerm) ||
                                 k.getNachname().toLowerCase().contains(lowerCaseTerm) ||
                                 (k.getVorname() + " " + k.getNachname()).toLowerCase().contains(lowerCaseTerm))
                    .collect(Collectors.toList());
        }
        fireTableDataChanged();
    }

    public int getFilteredRowCount() {
        return filteredData.size();
    }

    @Override
    public int getRowCount() {
        return filteredData.size();
    }

    @Override
    public int getColumnCount() {
        return Kunde.columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return filteredData.get(rowIndex).getValueAtColumn(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Kunde getObjectAt(int row) {
        return filteredData.get(row);
    }
}