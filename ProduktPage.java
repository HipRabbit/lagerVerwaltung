package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.model.common.Cast;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

public class ProduktPage extends JPanel {
	private static final long serialVersionUID = 101010L;
    private Session ses;
    private JTable table;
    private ProduktTableModel tableModel;

    public ProduktPage(Session ses) {
        this.ses = ses;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Oberes Panel für Titel, Suche und Sortierbuttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Titel
        JLabel lblTitle = new JLabel("Produktübersicht");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // Suchpanel (oben links)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblIdSearch = new JLabel("Nach ID suchen:");
        JTextField idField = new JTextField(10);
        idField.setToolTipText("Geben Sie die Produkt-ID ein");
        JButton btnSearch = new JButton("Suchen");
        JButton btnReset = new JButton("Zurücksetzen");

        searchPanel.add(lblIdSearch);
        searchPanel.add(idField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnReset);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton sortByIdButton = new JToggleButton("Sortieren nach ID");
        JToggleButton sortByNameButton = new JToggleButton("Sortieren nach Name");
        JToggleButton sortByPreisButton = new JToggleButton("Sortieren nach Preis");

        sortByIdButton.addActionListener(e -> {
            boolean ascending = !sortByIdButton.isSelected();
            tableModel.sortById(ascending);
            sortByIdButton.setText(ascending ? "Sortieren nach ID ↑" : "Sortieren nach ID ↓");
            sortByNameButton.setSelected(false);
            sortByNameButton.setText("Sortieren nach Name");
            sortByPreisButton.setSelected(false);
            sortByPreisButton.setText("Sortieren nach Preis");
            updateSortIndicator(0, ascending); // Spalte 0 (ID)
        });

        sortByNameButton.addActionListener(e -> {
            boolean ascending = !sortByNameButton.isSelected();
            tableModel.sortByName(ascending);
            sortByNameButton.setText(ascending ? "Sortieren nach Name ↑" : "Sortieren nach Name ↓");
            sortByIdButton.setSelected(false);
            sortByIdButton.setText("Sortieren nach ID");
            sortByPreisButton.setSelected(false);
            sortByPreisButton.setText("Sortieren nach Preis");
            updateSortIndicator(1, ascending); // Spalte 1 (Name)
        });

        sortByPreisButton.addActionListener(e -> {
            boolean ascending = !sortByPreisButton.isSelected();
            tableModel.sortByPreis(ascending);
            sortByPreisButton.setText(ascending ? "Sortieren nach Preis ↑" : "Sortieren nach Preis ↓");
            sortByIdButton.setSelected(false);
            sortByIdButton.setText("Sortieren nach ID");
            sortByNameButton.setSelected(false);
            sortByNameButton.setText("Sortieren nach Name");
            updateSortIndicator(3, ascending); // Spalte 3 (Preis)
        });

        sortPanel.add(sortByIdButton);
        sortPanel.add(sortByNameButton);
        sortPanel.add(sortByPreisButton);
        topPanel.add(sortPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addButton = new JButton("Produkt anlegen");
        JButton editButton = new JButton("Produkt bearbeiten");
        JButton deleteButton = new JButton("Produkt löschen");

        addButton.addActionListener(e -> {
            new AddProdukt(ses, this).setVisible(true);
            refresh();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Produkt selectedProdukt = tableModel.getProduktAt(selectedRow);
                new EditProdukt(ses, null, selectedProdukt).setVisible(true);
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Produkt aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Produkt selectedProdukt = tableModel.getProduktAt(selectedRow);
                new DeleteProdukt(ses, null, selectedProdukt).setVisible(true);
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Produkt aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new ProduktTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new SortIndicatorHeaderRenderer(table.getTableHeader().getDefaultRenderer()));
        add(new JScrollPane(table), BorderLayout.CENTER);
        
     // Event-Handler für Suche
        btnSearch.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Produkt produkt = ses.getCommunicator().getProduktById(id);
                if (produkt != null && produkt.getId() > 0) { // Überprüfe, ob ein gültiges Lager zurückgegeben wurde
                    tableModel.setSingleProdukt(produkt);
                } else {
                    JOptionPane.showMessageDialog(this, "Produt mit ID " + id + " nicht gefunden!", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setSingleProdukt(null);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige numerische ID ein!", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event-Handler für Zurücksetzen
        btnReset.addActionListener(e -> {
            idField.setText("");
            refresh();
        });


        refresh();
    }

    public void refresh() {
        try {
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.PRODUKT);
            deo.setCommand(Command.LIST);
            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                java.util.List<Produkt> produktList = Cast.safeListCast(ret.getData(), Produkt.class);
                tableModel.setData(produktList);
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Produkte: " + ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                tableModel.setData(new java.util.ArrayList<>());
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Kommunikationsfehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            tableModel.setData(new java.util.ArrayList<>());
        }
    }

    private void updateSortIndicator(int columnIndex, boolean ascending) {
        SortIndicatorHeaderRenderer renderer = (SortIndicatorHeaderRenderer) table.getTableHeader().getDefaultRenderer();
        renderer.setSortColumn(columnIndex, ascending);
        table.getTableHeader().repaint();
    }
}

class ProduktTableModel extends javax.swing.table.AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.util.List<Produkt> data = new java.util.ArrayList<>();
    private String[] columnNames = {"ID", "Name", "Hersteller", "Preis"};
    private final DecimalFormat priceFormat;

    public ProduktTableModel() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        priceFormat = new DecimalFormat("#,##0.00 €", symbols);
    }
    public void setSingleProdukt(Produkt produkt) {
        this.data = new ArrayList<>();
        if (produkt != null) {
            this.data.add(produkt);
        }
        fireTableDataChanged();
    }

    public void setData(java.util.List<Produkt> data) {
        this.data = data != null ? data : new java.util.ArrayList<>();
        fireTableDataChanged();
    }

    public Produkt getProduktAt(int row) {
        return data.get(row);
    }

    public void sortById(boolean ascending) {
        Comparator<Produkt> comparator = Comparator.comparingInt(Produkt::getId);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    public void sortByName(boolean ascending) {
        Comparator<Produkt> comparator = Comparator.comparing(Produkt::getName);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }
    public void sortByPreis(boolean ascending) {
        Comparator<Produkt> comparator = Comparator.comparing(Produkt::getPreis);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(data, comparator);
        fireTableDataChanged();
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
    public Object getValueAt(int row, int col) {
        Produkt produkt = data.get(row);
        switch (col) {
            case 0: return produkt.getId();
            case 1: return produkt.getName();
            case 2: return produkt.getHersteller();
            case 3: return priceFormat.format(produkt.getPreis());
            default: return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
}