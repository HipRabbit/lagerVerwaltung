package thw.edu.javaII.port.warehouse.ui.panels;

import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.model.BestellungProdukt;
import thw.edu.javaII.port.warehouse.model.Kunde;
import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.ui.LagerUIHandler;
import thw.edu.javaII.port.warehouse.ui.MenuActionCommands;
import thw.edu.javaII.port.warehouse.ui.common.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dialog zum Hinzufügen neuer Bestellungen im Lagerverwaltungssystem.
 *
 * <p>Diese Klasse erweitert JDialog und bietet eine vollständige Benutzeroberfläche
 * zum Erstellen neuer Bestellungen, einschließlich Kundenauswahl, Produktauswahl,
 * Mengenangaben und Bestandsprüfung.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session ses – aktuelle Benutzersitzung</li>
 *   <li>DefaultTableModel tableModel – Tabellen-Model für Produktliste</li>
 *   <li>List<BestellungProdukt> produkteList – Liste der bestellten Produkte</li>
 *   <li>JComboBox<Kunde> cbKunde – Dropdown für Kundenauswahl</li>
 *   <li>JTable table – Tabelle der bestellten Produkte</li>
 *   <li>JLabel totalLabel – Anzeige des Gesamtpreises</li>
 *   <li>boolean canceled – Flag für Abbruch-Status</li>
 *   <li>JFrame parentFrame – übergeordnetes Fenster</li>
 *   <li>JPanel contentPane – Content-Panel des Hauptfensters</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>AddBestellung(Frame, Session, JPanel) – Konstruktor mit UI-Initialisierung</li>
 *   <li>initializeUI() – Erstellt die Benutzeroberfläche</li>
 *   <li>showCustomerQueryDialog() – Dialog für Kundenauswahl/Erstellung</li>
 *   <li>updateCustomerComboBox() – Aktualisiert Kunden-Dropdown</li>
 *   <li>updateTableRow(int, BestellungProdukt) – Aktualisiert Tabellenzeile</li>
 *   <li>updateTotalLabel() – Berechnet und zeigt Gesamtpreis</li>
 *   <li>navigateToBestellungPage() – Navigation zur Bestellungsübersicht</li>
 * </ul>
 *
 * @author Bjarne von Appen
 */
public class AddBestellung extends JDialog {
    private static final long serialVersionUID = 1L;
    
    /** Aktuelle Benutzersitzung */
    private Session ses;
    /** Tabellen-Model für die Produktliste */
    private DefaultTableModel tableModel;
    /** Liste der bestellten Produkte */
    private List<BestellungProdukt> produkteList = new ArrayList<>();
    /** Dropdown für Kundenauswahl */
    private JComboBox<Kunde> cbKunde;
    /** Tabelle der bestellten Produkte */
    private JTable table;
    /** Anzeige des Gesamtpreises */
    private JLabel totalLabel;
    /** Flag für Abbruch-Status */
    private boolean canceled = false;
    /** Übergeordnetes Fenster */
    private JFrame parentFrame;
    /** Content-Panel des Hauptfensters */
    private JPanel contentPane;

    /**
     * Konstruktor erstellt den Dialog zum Hinzufügen von Bestellungen.
     *
     * @param parent      übergeordnetes Fenster
     * @param session     aktuelle Benutzersitzung
     * @param contentPane Content-Panel des Hauptfensters
     */
    public AddBestellung(Frame parent, Session session, JPanel contentPane) {
        super(parent, "Bestellung hinzufügen", true);
        this.ses = session;
        this.parentFrame = (JFrame) parent;
        this.contentPane = contentPane;
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Window-Closing-Verhalten definieren
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                canceled = true;
                navigateToBestellungPage();
            }
        });

        initializeUI();
        // Kundendialog anzeigen, bei Abbruch zur Bestellungsseite navigieren
        if (!showCustomerQueryDialog()) {
            navigateToBestellungPage();
        }
    }

    /**
     * Initialisiert die komplette Benutzeroberfläche des Dialogs.
     *
     * @author Bjarne von Appen
     */
    private void initializeUI() {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        // Kunden-Dropdown initialisieren
        cbKunde = new JComboBox<>();
        updateCustomerComboBox();

        // Produkt-Dropdown mit verfügbaren Produkten füllen
        JComboBox<Produkt> cbProdukt = new JComboBox<>();
        try {
            List<Produkt> produkte = ses.getCommunicator().getProdukte();
            if (produkte == null || produkte.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keine Produkte gefunden!", "Fehler: Produkte laden", JOptionPane.ERROR_MESSAGE);
            } else {
                for (Produkt produkt : produkte) {
                    cbProdukt.addItem(produkt);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Produkte: " + e.getMessage(),
                    "Fehler: Produkte laden", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // UI-Komponenten erstellen
        JTextField txtAnzahl = new JTextField(5);
        JButton btnAddProdukt = new JButton("Produkt hinzufügen");
        JButton btnRemoveProdukt = new JButton("Produkt entfernen");
        JButton btnChangeQuantity = new JButton("Menge ändern");
        JButton btnSubmit = new JButton("Bestellung abschicken");

        // Tabellen-Model für Bestellungsprodukte
        String[] columns = {"Hersteller", "Produktname", "Anzahl", "Einzelpreis", "Gesamtpreis"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabelle nicht editierbar
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        
        // Währungsformatierung für Preisspalten
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Double) {
                    ((JLabel) c).setText(df.format(value) + " €");
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 3749120934505217279L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Double) {
                    ((JLabel) c).setText(df.format(value) + " €");
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout-Panels erstellen
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Kunde:"));
        inputPanel.add(cbKunde);
        inputPanel.add(new JLabel("Produkt:"));
        inputPanel.add(cbProdukt);
        inputPanel.add(new JLabel("Anzahl:"));
        inputPanel.add(txtAnzahl);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Ihre Bestellung hat einen Gesamtwert von 0,00 €");
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.add(totalLabel);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnAddProdukt);
        buttonPanel.add(btnRemoveProdukt);
        buttonPanel.add(btnChangeQuantity);
        buttonPanel.add(btnSubmit);

        // Layout zusammenfügen
        add(inputPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event-Listener für Buttons
        btnAddProdukt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int anzahl = Integer.parseInt(txtAnzahl.getText());
                    if (anzahl <= 0) {
                        JOptionPane.showMessageDialog(null, "Anzahl muss größer als 0 sein!",
                                "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    Produkt produkt = (Produkt) cbProdukt.getSelectedItem();
                    if (produkt == null) {
                        JOptionPane.showMessageDialog(null, "Bitte wählen Sie ein Produkt aus!",
                                "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    // Verfügbaren Bestand prüfen
                    int availableStock = ses.getCommunicator().getAvailableStockForProduct(produkt.getId());
                    if (availableStock < 0) {
                        JOptionPane.showMessageDialog(null, "Fehler beim Abrufen des Lagerbestands!",
                                "Fehler: Bestandsprüfung", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Prüfen, ob Produkt bereits in der Liste vorhanden
                    BestellungProdukt existingProdukt = produkteList.stream()
                            .filter(bp -> bp.getProdukt().getId() == produkt.getId())
                            .findFirst()
                            .orElse(null);
                    
                    if (existingProdukt != null) {
                        // Produkt bereits vorhanden: Menge erhöhen
                        int neueAnzahl = existingProdukt.getAnzahl() + anzahl;
                        if (neueAnzahl > availableStock) {
                            JOptionPane.showMessageDialog(null, "Nur " + availableStock + " Einheiten von " + produkt.getName() + " verfügbar!",
                                    "Hinweis: Bestand unzureichend", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        int rowIndex = produkteList.indexOf(existingProdukt);
                        existingProdukt.setAnzahl(neueAnzahl);
                        updateTableRow(rowIndex, existingProdukt);
                    } else {
                        // Neues Produkt hinzufügen
                        if (anzahl > availableStock) {
                            JOptionPane.showMessageDialog(null, "Nur " + availableStock + " Einheiten von " + produkt.getName() + " verfügbar!",
                                    "Hinweis: Bestand unzureichend", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        BestellungProdukt bestellungProdukt = new BestellungProdukt(produkt, anzahl);
                        tableModel.addRow(new Object[]{
                                produkt.getHersteller(),
                                produkt.getName(),
                                anzahl,
                                produkt.getPreis(),
                                produkt.getPreis() * anzahl
                        });
                        produkteList.add(bestellungProdukt);
                    }
                    updateTotalLabel();
                    txtAnzahl.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Bitte geben Sie eine gültige Anzahl ein!",
                            "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnRemoveProdukt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String produktName = (String) tableModel.getValueAt(selectedRow, 1);
                    int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Möchten Sie das Produkt '" + produktName + "' aus der Bestellung entfernen?",
                        "Produkt entfernen",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                        produkteList.remove(selectedRow);
                        updateTotalLabel();
                        JOptionPane.showMessageDialog(
                            null,
                            "Produkt '" + produktName + "' wurde aus der Bestellung entfernt.",
                            "Hinweis: Eintrag entfernt",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Bitte wählen Sie ein Produkt aus der Tabelle aus!",
                            "Hinweis: Keine Auswahl", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnChangeQuantity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    BestellungProdukt bp = produkteList.get(selectedRow);
                    String input = JOptionPane.showInputDialog(null, "Neue Menge für " + bp.getProdukt().getName() + ":",
                            bp.getAnzahl());
                    if (input != null) {
                        try {
                            int neueAnzahl = Integer.parseInt(input);
                            int availableStock = ses.getCommunicator().getAvailableStockForProduct(bp.getProdukt().getId());
                            if (neueAnzahl < 0) {
                                JOptionPane.showMessageDialog(null, "Anzahl darf nicht negativ sein!",
                                        "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                            if (neueAnzahl > availableStock) {
                                JOptionPane.showMessageDialog(null, "Nur " + availableStock + " Einheiten von " + bp.getProdukt().getName() + " verfügbar!",
                                        "Hinweis: Bestand unzureichend", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                            bp.setAnzahl(neueAnzahl);
                            updateTableRow(selectedRow, bp);
                            if (neueAnzahl == 0) {
                                tableModel.removeRow(selectedRow);
                                produkteList.remove(selectedRow);
                                JOptionPane.showMessageDialog(null, "Produkt " + bp.getProdukt().getName() + " wurde entfernt (Menge = 0).",
                                        "Hinweis: Produkt entfernt", JOptionPane.INFORMATION_MESSAGE);
                            }
                            updateTotalLabel();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Bitte geben Sie eine gültige Anzahl ein!",
                                    "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Bitte wählen Sie ein Produkt aus der Tabelle aus!",
                            "Hinweis: Keine Auswahl", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Kunde kunde = (Kunde) cbKunde.getSelectedItem();
                    if (kunde == null) {
                        JOptionPane.showMessageDialog(null, "Bitte wählen Sie einen Kunden aus!",
                                "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    if (produkteList.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Bitte fügen Sie mindestens ein Produkt hinzu!",
                                "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    // Bestellung erstellen und speichern
                    Date date = new Date();
                    Bestellung bestellung = new Bestellung(kunde, date, produkteList);
                    boolean success = ses.getCommunicator().addBestellung(bestellung);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Bestellung abgeschickt",
                                "Hinweis: Bestellung abgeschickt", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Bestellungsdetails abrufen und Zusammenfassung anzeigen
                        Bestellung storedBestellung = ses.getCommunicator().getBestellungDetails(bestellung.getId());
                        if (storedBestellung != null) {
                            BestellungZusammenfassung summaryDialog = new BestellungZusammenfassung(
                                    parentFrame, ses, storedBestellung);
                            summaryDialog.setVisible(true);
                        } else {
                            System.out.println("Fallback: Verwende ursprüngliche Bestellung mit ID " + bestellung.getId());
                            BestellungZusammenfassung summaryDialog = new BestellungZusammenfassung(
                                    parentFrame, ses, bestellung);
                            summaryDialog.setVisible(true);
                        }
                        navigateToBestellungPage();
                    } else {
                        JOptionPane.showMessageDialog(null, "Fehler: Bestellung konnte nicht gespeichert werden!",
                                "Fehler: Bestellung speichern", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Fehler beim Speichern der Bestellung: " + ex.getMessage(),
                            "Fehler: Bestellung speichern", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Aktualisiert eine bestimmte Zeile in der Produkttabelle.
     *
     * @param rowIndex Index der zu aktualisierenden Zeile
     * @param bp       BestellungProdukt mit neuen Daten
     * @author Bjarne von Appen
     */
    private void updateTableRow(int rowIndex, BestellungProdukt bp) {
        Produkt p = bp.getProdukt();
        tableModel.setValueAt(p.getHersteller(), rowIndex, 0);
        tableModel.setValueAt(p.getName(), rowIndex, 1);
        tableModel.setValueAt(bp.getAnzahl(), rowIndex, 2);
        tableModel.setValueAt(p.getPreis(), rowIndex, 3);
        tableModel.setValueAt(p.getPreis() * bp.getAnzahl(), rowIndex, 4);
    }

    /**
     * Berechnet und aktualisiert die Gesamtpreis-Anzeige.
     *
     * @author Bjarne von Appen
     */
    private void updateTotalLabel() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        double total = produkteList.stream()
                .mapToDouble(bp -> bp.getProdukt().getPreis() * bp.getAnzahl())
                .sum();
        totalLabel.setText("Ihre Bestellung hat einen Gesamtwert von " + df.format(total) + " €");
    }

    /**
     * Zeigt Dialog zur Kundenauswahl oder -erstellung an.
     *
     * @return true wenn fortgefahren werden soll, false bei Abbruch
     * @author Bjarne von Appen
     */
    private boolean showCustomerQueryDialog() {
        Object[] options = {"Kunde existiert", "Kunde hinzufügen"};
        int choice = JOptionPane.showOptionDialog(this,
                "Existiert der Kunde bereits?",
                "Kundenauswahl",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        
        if (choice == 1) {
            // Neuen Kunden hinzufügen
            EditKunde editKunde = new EditKunde(ses, parentFrame, null);
            editKunde.setVisible(true);
            
            if (editKunde.isCanceled()) {
                canceled = true;
                return false;
            }

            Kunde newKunde = editKunde.getSavedKunde();
            if (newKunde != null) {
                updateCustomerComboBox(newKunde);
                JOptionPane.showMessageDialog(this, "Kunde " + newKunde.toString() + " erfolgreich hinzugefügt. Bitte setzen Sie die Bestellung fort.",
                        "Hinweis: Kunde hinzugefügt", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Kein neuer Kunde wurde hinzugefügt. Bitte wählen Sie einen bestehenden Kunden oder versuchen Sie es erneut.",
                        "Hinweis: Kein Kunde hinzugefügt", JOptionPane.WARNING_MESSAGE);
                canceled = true;
                return false;
            }
        } else if (choice == JOptionPane.CLOSED_OPTION) {
            canceled = true;
            return false;
        }
        return true;
    }

    /**
     * Aktualisiert die Kunden-ComboBox mit allen verfügbaren Kunden.
     */
    private void updateCustomerComboBox() {
        updateCustomerComboBox(null);
    }

    /**
     * Aktualisiert die Kunden-ComboBox und setzt optional einen neuen Kunden an erste Stelle.
     *
     * @param newKunde neuer Kunde, der an erster Stelle stehen soll (optional)
     * @author Bjarne von Appen
     */
    private void updateCustomerComboBox(Kunde newKunde) {
        cbKunde.removeAllItems();
        try {
            List<Kunde> kunden = ses.getCommunicator().getKunden();
            if (kunden == null || kunden.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keine Kunden gefunden!", "Fehler: Kunden laden", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Neuen Kunden zuerst hinzufügen, falls vorhanden
            if (newKunde != null) {
                cbKunde.addItem(newKunde);
            }

            // Restliche Kunden hinzufügen (außer dem neuen)
            for (Kunde kunde : kunden) {
                if (newKunde == null || kunde.getId() != newKunde.getId()) {
                    cbKunde.addItem(kunde);
                }
            }

            // Neuen Kunden als ausgewählt setzen
            if (newKunde != null) {
                cbKunde.setSelectedItem(newKunde);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Kunden: " + e.getMessage(),
                    "Fehler: Kunden laden", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Navigiert zur Bestellungsübersichtsseite und schließt den Dialog.
     *
     * @author Bjarne von Appen
     */
    private void navigateToBestellungPage() {
        dispose();
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, MenuActionCommands.BESTELLUNG_PAGE.toString());
        new LagerUIHandler(ses, parentFrame, contentPane, parentFrame).actionPerformed(event);
    }

    /**
     * Gibt zurück, ob der Dialog abgebrochen wurde.
     *
     * @return true wenn abgebrochen, false sonst
     */
    public boolean isCanceled() {
        return canceled;
    }
}
