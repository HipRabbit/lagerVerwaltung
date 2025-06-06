package thw.edu.javaII.port.warehouse.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import thw.edu.javaII.port.warehouse.model.LagerBestand;

public class BestandTableModel extends AbstractTableModel {
    
    private static final long serialVersionUID = -6145988449443265248L;
    private List<LagerBestand> data = new ArrayList<>(); // Initialisiere als leere Liste

    public BestandTableModel(List<LagerBestand> data) {
        setData(data);
    }
    
    public void setData(List<LagerBestand> data) {
        this.data = (data != null) ? data : new ArrayList<>();
        fireTableDataChanged();
    }

    public List<LagerBestand> getData() {
        return data;
    }

    public void sortById(boolean ascending) {
        Comparator<LagerBestand> comparator = Comparator.comparingInt(b -> b.getProdukt_id().getId());
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    public void sortByName(boolean ascending) {
        Comparator<LagerBestand> comparator = Comparator.comparing(b -> b.getProdukt_id().getName());
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size(); // Sicher, da data nie null ist
    }

    @Override
    public int getColumnCount() {
        return LagerBestand.columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex).getValueAtColumn(columnIndex);
    }
    
    @Override
    public String getColumnName(int arg0) {    
        if (arg0==0) return "ID";
        if (arg0==1) return "Produkt";
        if (arg0==2) return "Hersteller";
        if (arg0==3) return "Lagerbestand";
        if (arg0==4) return "Lagerplatz";
        if (arg0==5) return "Lager";
        return null;
    }
    
    public void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
            double... percentages) {
        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }
     
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int)
                    (tablePreferredWidth * (percentages[i] / total)));
        }
    }

    public LagerBestand getObjectAt(int selectedRow) {
        return data.get(selectedRow);
    }
}