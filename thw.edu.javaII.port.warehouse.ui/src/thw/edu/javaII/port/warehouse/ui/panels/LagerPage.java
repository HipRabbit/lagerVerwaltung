package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import thw.edu.javaII.port.warehouse.model.Lager;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.model.common.Cast;
import java.util.Comparator;
import java.util.Collections;

public class LagerPage extends JPanel {
	private static final long serialVersionUID = 10391231L;
    private Session ses;
    private JTable table;
    private LagerTableModel tableModel;

    public LagerPage(Session ses) {
        this.ses = ses;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Oberes Panel für Titel und Sortierbuttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Titel
        JLabel lblTitle = new JLabel("Lagerübersicht");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblTitle, BorderLayout.NORTH);

        // Sortierbuttons (links oberhalb der Tabelle)
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton sortByIdButton = new JToggleButton("Sortieren nach ID");
        JToggleButton sortByNameButton = new JToggleButton("Sortieren nach Name");

        sortByIdButton.addActionListener(e -> {
            boolean ascending = !sortByIdButton.isSelected();
            tableModel.sortById(ascending);
            sortByIdButton.setText(ascending ? "Sortieren nach ID ↑" : "Sortieren nach ID ↓");
            sortByNameButton.setSelected(false);
            sortByNameButton.setText("Sortieren nach Name");
            updateSortIndicator(0, ascending); // Spalte 0 (ID)
        });

        sortByNameButton.addActionListener(e -> {
            boolean ascending = !sortByNameButton.isSelected();
            tableModel.sortByName(ascending);
            sortByNameButton.setText(ascending ? "Sortieren nach Name ↑" : "Sortieren nach Name ↓");
            sortByIdButton.setSelected(false);
            sortByIdButton.setText("Sortieren nach ID");
            updateSortIndicator(1, ascending); // Spalte 1 (Name)
        });

        sortPanel.add(sortByIdButton);
        sortPanel.add(sortByNameButton);
        topPanel.add(sortPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Button-Panel mit zentrierter Ausrichtung (nur für die anderen Buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Lager anlegen");
        JButton editButton = new JButton("Lager bearbeiten");
        JButton deleteButton = new JButton("Lager löschen");

        addButton.addActionListener(e -> {
            AddLager dialog = new AddLager(ses, (JFrame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            refresh();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Lager selectedLager = tableModel.getLagerAt(selectedRow);
                EditLager dialog = new EditLager(ses, (JFrame) SwingUtilities.getWindowAncestor(this));
                dialog.setSelectedLager(selectedLager);
                dialog.setVisible(true);
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Lager aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Lager selectedLager = tableModel.getLagerAt(selectedRow);
                DeleteLager dialog = new DeleteLager(ses, (JFrame) SwingUtilities.getWindowAncestor(this));
                dialog.setSelectedLager(selectedLager);
                dialog.setVisible(true);
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Lager aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new LagerTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);

        // Setze benutzerdefinierten Header-Renderer für Sortierungsanzeige
        table.getTableHeader().setDefaultRenderer(new SortIndicatorHeaderRenderer(table.getTableHeader().getDefaultRenderer()));

        add(new JScrollPane(table), BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        try {
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGER);
            deo.setCommand(Command.LIST);
            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                java.util.List<Lager> lagerList = Cast.safeListCast(ret.getData(), Lager.class);
                tableModel.setData(lagerList);
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Lager: " + ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
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

class LagerTableModel extends javax.swing.table.AbstractTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5706053428116906813L;
	private java.util.List<Lager> data = new java.util.ArrayList<>();
    private String[] columnNames = {"ID", "Name", "Ort", "Art"};

    public void setData(java.util.List<Lager> data) {
        this.data = data != null ? data : new java.util.ArrayList<>();
        fireTableDataChanged();
    }

    public Lager getLagerAt(int row) {
        return data.get(row);
    }

    public void sortById(boolean ascending) {
        Comparator<Lager> comparator = Comparator.comparingInt(Lager::getId);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(data, comparator);
        fireTableDataChanged();
    }

    public void sortByName(boolean ascending) {
        Comparator<Lager> comparator = Comparator.comparing(Lager::getName);
        if (!ascending) {
            comparator = comparator.reversed();
        }
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
        Lager lager = data.get(row);
        switch (col) {
            case 0: return lager.getId();
            case 1: return lager.getName();
            case 2: return lager.getOrt();
            case 3: return lager.getArt();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
}