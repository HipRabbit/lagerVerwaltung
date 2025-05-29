package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import thw.edu.javaII.port.warehouse.model.Kunde;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.model.KundeTableModel;

public class SearchKunde extends JPanel {
    private static final long serialVersionUID = 123012318L;
    private JTable table;
    private JTextField idField;
    private JTextField nameFilterField;
    private JScrollPane js;
    private KundeTableModel model;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnShowBestellungen;

    public SearchKunde(Session ses, JFrame parent) {
        setLayout(new BorderLayout(0, 0));

        JLabel lblNewLabel = new JLabel("Kundendatenbank");
        lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        add(lblNewLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0 };
        gbl_panel.rowHeights = new int[] { 30, 30, 30, 30, 20, 0 };
        gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
        panel.setLayout(gbl_panel);

        // ID-Suche
        JPanel panel_1 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 0;
        panel.add(panel_1, gbc_panel_1);

        JLabel lblIdSearch = new JLabel("Nach ID suchen:");
        panel_1.add(lblIdSearch);

        idField = new JTextField();
        idField.setColumns(10);
        idField.setToolTipText("Geben Sie die Kunden-ID ein");
        panel_1.add(idField);

        JButton btnSearch = new JButton("Suchen");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText().trim());
                    Kunde kunde = ses.getCommunicator().getKundeById(id);
                    if (kunde != null) {
                        List<Kunde> result = new ArrayList<>();
                        result.add(kunde);
                        model.setData(result);
                        nameFilterField.setText(""); // Reset Name filter
                    } else {
                        JOptionPane.showMessageDialog(null, "Kunde mit ID " + id + " nicht gefunden!",
                                "Hinweis: Kunde nicht gefunden", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Bitte geben Sie eine gültige numerische ID ein!",
                            "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel_1.add(btnSearch);

        // Neuer Zurücksetzen-Button für ID-Suche
        JButton btnResetId = new JButton("Zurücksetzen (ID)");
        btnResetId.addActionListener(e -> {
            idField.setText(""); // ID-Feld zurücksetzen
            nameFilterField.setText(""); // Namensfilter zurücksetzen, um Konsistenz zu wahren
            model.setData(ses.getCommunicator().getKunden()); // Tabelle auf vollständige Kundenliste zurücksetzen
        });
        panel_1.add(btnResetId);

        // Namenssuche
        JPanel panel_4 = new JPanel();
        FlowLayout flowLayout_4 = (FlowLayout) panel_4.getLayout();
        flowLayout_4.setAlignment(FlowLayout.LEFT);
        GridBagConstraints gbc_panel_4 = new GridBagConstraints();
        gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_4.gridx = 0;
        gbc_panel_4.gridy = 1;
        panel.add(panel_4, gbc_panel_4);

        JLabel lblNameSearch = new JLabel("Nach Name filtern:");
        panel_4.add(lblNameSearch);

        nameFilterField = new JTextField();
        nameFilterField.setColumns(20);
        nameFilterField.setToolTipText("Geben Sie Vor- oder Nachname ein");
        panel_4.add(nameFilterField);

        JButton btnResetFilter = new JButton("Zurücksetzen");
        btnResetFilter.addActionListener(e -> {
            nameFilterField.setText("");
            idField.setText(""); // ID-Feld ebenfalls zurücksetzen, um Konsistenz zu wahren
            model.setData(ses.getCommunicator().getKunden());
        });
        panel_4.add(btnResetFilter);

        // DocumentListener für dynamische Namensfilterung
        nameFilterField.getDocument().addDocumentListener(new DocumentListener() {
            private boolean wasEmptyLastTime = true;

            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String searchTerm = nameFilterField.getText().trim();
                model.filterByName(searchTerm);
                boolean isEmpty = searchTerm.isEmpty();
                if (!isEmpty && model.getFilteredRowCount() == 0 && !wasEmptyLastTime) {
                    JOptionPane.showMessageDialog(null, "Keine Kunden mit dem Namen \"" + searchTerm + "\" gefunden!",
                            "Hinweis: Keine Ergebnisse", JOptionPane.INFORMATION_MESSAGE);
                }
                wasEmptyLastTime = isEmpty;
            }
        });

        // Buttons für Aktionen
        JPanel panel_2 = new JPanel();
        FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
        flowLayout_2.setAlignment(FlowLayout.RIGHT);
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_2.gridx = 0;
        gbc_panel_2.gridy = 2;
        panel.add(panel_2, gbc_panel_2);

        JButton btnNewKunde = new JButton("Neuer Kunde");
        btnNewKunde.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EditKunde dialog = new EditKunde(ses, parent, null);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
                dialog.setVisible(true);
                // Aktualisiere die Kundenliste nach dem Schließen des Dialogs
                model.setData(ses.getCommunicator().getKunden());
                model.fireTableDataChanged();
            }
        });
        panel_2.add(btnNewKunde);

        btnEdit = new JButton("Bearbeiten");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Kunde kunde = model.getObjectAt(selectedRow);
                    EditKunde dialog = new EditKunde(ses, parent, kunde);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
                    dialog.setVisible(true);
                    // Aktualisiere die Kundenliste nach dem Schließen des Dialogs
                    model.setData(ses.getCommunicator().getKunden());
                    model.fireTableDataChanged();
                }
            }
        });
        panel_2.add(btnEdit);

        btnDelete = new JButton("Löschen");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Kunde kunde = model.getObjectAt(selectedRow);
                    if (kunde != null) {
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Möchten Sie den Kunden '" + kunde.getVorname() + " " + kunde.getNachname() + "' wirklich löschen?",
                                "Kunde löschen", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            if (ses.getCommunicator().deleteKunde(kunde)) {
                                JOptionPane.showMessageDialog(null, "Kunde erfolgreich gelöscht!",
                                        "Hinweis: Erfolg", JOptionPane.INFORMATION_MESSAGE);
                                model.setData(ses.getCommunicator().getKunden());
                                model.fireTableDataChanged();
                            } else {
                                JOptionPane.showMessageDialog(null, "Fehler beim Löschen des Kunden!",
                                        "Fehler: Löschen", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
        panel_2.add(btnDelete);

        btnShowBestellungen = new JButton("Bestellungen anzeigen");
        btnShowBestellungen.setEnabled(false);
        btnShowBestellungen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Kunde kunde = model.getObjectAt(selectedRow);
                    KundeBestellungen dialog = new KundeBestellungen(ses, parent, kunde);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    // Modalität wird jetzt in KundeBestellungen gesetzt
                    // dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
                    // setVisible wird in KundeBestellungen aufgerufen
                    // dialog.setVisible(true);
                }
            }
        });
        panel_2.add(btnShowBestellungen);

        // Sortierungs-Buttons
        JPanel panel_3 = new JPanel();
        FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
        flowLayout_3.setAlignment(FlowLayout.RIGHT);
        GridBagConstraints gbc_panel_3 = new GridBagConstraints();
        gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_3.gridx = 0;
        gbc_panel_3.gridy = 3;
        panel.add(panel_3, gbc_panel_3);

        JButton btnSortId = new JButton("Sortieren nach ID");
        btnSortId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.sortById();
            }
        });
        panel_3.add(btnSortId);

        JButton btnSortName = new JButton("Sortieren nach Name");
        btnSortName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.sortByName();
            }
        });
        panel_3.add(btnSortName);

        JLabel lblNewLabel_1 = new JLabel("Ergebnisse");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 4;
        panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

        model = new KundeTableModel(ses.getCommunicator().getKunden());
        table = new JTable(model);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.DARK_GRAY);
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = table.getSelectedRowCount() == 1;
            btnEdit.setEnabled(rowSelected);
            btnDelete.setEnabled(rowSelected);
            btnShowBestellungen.setEnabled(rowSelected);
        });

        js = new JScrollPane(table);
        js.setVisible(true);
        GridBagConstraints gbc_table = new GridBagConstraints();
        gbc_table.fill = GridBagConstraints.BOTH;
        gbc_table.gridx = 0;
        gbc_table.gridy = 5;
        gbc_table.weighty = 1.0;
        panel.add(js, gbc_table);
    }
}