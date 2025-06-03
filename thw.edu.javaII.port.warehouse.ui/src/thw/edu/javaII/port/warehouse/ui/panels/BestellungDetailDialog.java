package thw.edu.javaII.port.warehouse.ui.panels;

import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.model.BestellungProdukt;
import thw.edu.javaII.port.warehouse.ui.LagerUI;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.model.BestellungTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dialog zur detaillierten Anzeige und Bearbeitung von Bestellungen.
 *
 * <p>Diese Klasse erweitert JDialog und bietet eine umfassende Benutzeroberfläche
 * zur Anzeige von Bestellungsdetails, einschließlich Kundeninformationen,
 * Produktlisten und Zeitstempel-Verwaltung für den Bestellungsprozess.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session session – aktuelle Benutzersitzung</li>
 *   <li>Bestellung bestellung – die anzuzeigende/bearbeitende Bestellung</li>
 *   <li>JLabel erfassungLabel – Anzeige des Erfassungszeitpunkts</li>
 *   <li>JLabel versandLabel – Anzeige des Versandzeitpunkts</li>
 *   <li>JLabel lieferungLabel – Anzeige des Lieferzeitpunkts</li>
 *   <li>JLabel bezahlungLabel – Anzeige des Bezahlzeitpunkts</li>
 *   <li>boolean hasChanges – Flag für ungespeicherte Änderungen</li>
 *   <li>JTable table – Tabelle der Bestellungsprodukte</li>
 *   <li>BestellungTableModel tableModel – Datenmodell für die Tabelle</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>BestellungDetailDialog(Frame, Session, Bestellung) – Konstruktor mit UI-Initialisierung</li>
 *   <li>initializeUI() – Erstellt die komplette Benutzeroberfläche</li>
 *   <li>setTimestamp(String) – Setzt Zeitstempel für Bestellungsphasen</li>
 *   <li>areAllTimestampsSet() – Prüft Vollständigkeit der Zeitstempel</li>
 *   <li>saveAndClose() – Speichert Änderungen und schließt Dialog</li>
 * </ul>
 *
 * @author Bjarne von Appen
 */
public class BestellungDetailDialog extends JDialog {
    private static final long serialVersionUID = 5L;
    
    /** Aktuelle Benutzersitzung */
    private final Session session;
    /** Die anzuzeigende/bearbeitende Bestellung */
    private final Bestellung bestellung;
    /** Anzeige des Erfassungszeitpunkts */
    private JLabel erfassungLabel;
    /** Anzeige des Versandzeitpunkts */
    private JLabel versandLabel;
    /** Anzeige des Lieferzeitpunkts */
    private JLabel lieferungLabel;
    /** Anzeige des Bezahlzeitpunkts */
    private JLabel bezahlungLabel;
    /** Flag für ungespeicherte Änderungen */
    private boolean hasChanges = false;
    /** Tabelle der Bestellungsprodukte */
    private JTable table;
    /** Datenmodell für die Produkttabelle */
    private BestellungTableModel tableModel;

    /**
     * Konstruktor erstellt den Dialog für Bestellungsdetails.
     *
     * @param parent     übergeordnetes Fenster
     * @param session    aktuelle Benutzersitzung
     * @param bestellung die anzuzeigende Bestellung
     */
    public BestellungDetailDialog(Frame parent, Session session, Bestellung bestellung) {
        super(parent, "Bestellungsdetails", true);
        this.session = session;
        this.bestellung = bestellung;
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        initializeUI();
    }

    /**
     * Initialisiert die komplette Benutzeroberfläche des Dialogs.
     *
     * @author Bjarne von Appen
     */
    private void initializeUI() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        // Eingabebereich mit Bestellungsinformationen
        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        inputPanel.add(new JLabel("Bestell-ID:"));
        JTextField idField = new JTextField(String.valueOf(bestellung.getId()));
        idField.setEditable(false);
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Kundenname:"));
        JTextField nameField = new JTextField(bestellung.getKunde() != null ?
            bestellung.getKunde().getVorname() + " " + bestellung.getKunde().getNachname() : "Unbekannt");
        nameField.setEditable(false);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Lieferadresse:"));
        JTextField lieferadresseField = new JTextField(bestellung.getKunde() != null ? bestellung.getKunde().getLieferadresse() : "Unbekannt");
        lieferadresseField.setEditable(false);
        inputPanel.add(lieferadresseField);

        inputPanel.add(new JLabel("Rechnungsadresse:"));
        JTextField rechnungsadresseField = new JTextField(bestellung.getKunde() != null ? bestellung.getKunde().getRechnungsadresse() : "Unbekannt");
        rechnungsadresseField.setEditable(false);
        inputPanel.add(rechnungsadresseField);

        inputPanel.add(new JLabel("Datum:"));
        JTextField datumField = new JTextField(bestellung.getDatum() != null ? sdf.format(bestellung.getDatum()) : "Unbekannt");
        datumField.setEditable(false);
        inputPanel.add(datumField);

        // Zeitstempel-Labels für Bestellungsphasen
        inputPanel.add(new JLabel("Erfassung:"));
        erfassungLabel = new JLabel(bestellung.getErfassung() != null ? sdf.format(bestellung.getErfassung()) : "Nicht gesetzt");
        inputPanel.add(erfassungLabel);

        inputPanel.add(new JLabel("Versand:"));
        versandLabel = new JLabel(bestellung.getVersand() != null ? sdf.format(bestellung.getVersand()) : "Nicht gesetzt");
        inputPanel.add(versandLabel);

        inputPanel.add(new JLabel("Lieferung:"));
        lieferungLabel = new JLabel(bestellung.getLieferung() != null ? sdf.format(bestellung.getLieferung()) : "Nicht gesetzt");
        inputPanel.add(lieferungLabel);

        inputPanel.add(new JLabel("Bezahlung:"));
        bezahlungLabel = new JLabel(bestellung.getBezahlung() != null ? sdf.format(bestellung.getBezahlung()) : "Nicht gesetzt");
        inputPanel.add(bezahlungLabel);

        add(inputPanel, BorderLayout.NORTH);

        // Produkttabelle mit Währungsformatierung
        tableModel = new BestellungTableModel(List.of(bestellung));
        table = new JTable(tableModel);
        table.setRowHeight(25);

        DecimalFormat df = new DecimalFormat("#,##0.00");
        DefaultTableCellRenderer priceRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = -5600707399487239230L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) {
                    value = df.format(value) + " €";
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        priceRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(6).setCellRenderer(priceRenderer); // Einzelpreis
        table.getColumnModel().getColumn(7).setCellRenderer(priceRenderer); // Gesamtpreis

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button-Panel mit Gesamtpreis und Aktionen
        JPanel buttonPanel = new JPanel(new BorderLayout());
        List<BestellungProdukt> produkte = bestellung.getProdukte();
        if (produkte == null) {
            produkte = new ArrayList<>();
        }
        double gesamtpreis = produkte.stream()
                .mapToDouble(bp -> bp.getProdukt().getPreis() * bp.getAnzahl())
                .sum();
        JLabel gesamtpreisLabel = new JLabel(String.format("Gesamtpreis: %s €", df.format(gesamtpreis)));
        gesamtpreisLabel.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(gesamtpreisLabel, BorderLayout.NORTH);

        // Zeitstempel-Buttons für Bestellungsphasen
        JPanel timestampPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton erfassungButton = new JButton("Erfassung setzen");
        erfassungButton.addActionListener(e -> setTimestamp("erfassung"));
        timestampPanel.add(erfassungButton);

        JButton versandButton = new JButton("Versand setzen");
        versandButton.addActionListener(e -> setTimestamp("versand"));
        timestampPanel.add(versandButton);

        JButton lieferungButton = new JButton("Lieferung setzen");
        lieferungButton.addActionListener(e -> setTimestamp("lieferung"));
        timestampPanel.add(lieferungButton);

        JButton bezahlungButton = new JButton("Bezahlung setzen");
        bezahlungButton.addActionListener(e -> setTimestamp("bezahlung"));
        timestampPanel.add(bezahlungButton);

        buttonPanel.add(timestampPanel, BorderLayout.CENTER);

        // Speichern/Schließen-Buttons
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(e -> saveAndClose());
        closePanel.add(saveButton);

        JButton closeButton = new JButton("Schließen");
        closeButton.addActionListener(e -> {
            if (hasChanges) {
                int result = JOptionPane.showConfirmDialog(this,
                        "Änderungen wurden vorgenommen, aber nicht gespeichert. Möchten Sie den Dialog wirklich schließen?",
                        "Änderungen nicht gespeichert",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                }
            } else {
                dispose();
            }
        });
        closePanel.add(closeButton);
        buttonPanel.add(closePanel, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Prüft, ob alle Zeitstempel der Bestellung gesetzt sind.
     *
     * @return true wenn alle Zeitstempel vorhanden sind, false sonst
     * @author Bjarne von Appen
     */
    private boolean areAllTimestampsSet() {
        return bestellung.getErfassung() != null &&
               bestellung.getVersand() != null &&
               bestellung.getLieferung() != null &&
               bestellung.getBezahlung() != null;
    }

    /**
     * Setzt einen Zeitstempel für eine bestimmte Bestellungsphase.
     *
     * @param type Art des Zeitstempels ("erfassung", "versand", "lieferung", "bezahlung")
     * @author Bjarne von Appen
     */
    private void setTimestamp(String type) {
        try {
            if (areAllTimestampsSet()) {
                int result = JOptionPane.showConfirmDialog(this,
                        "Alle Timestamps sind bereits gesetzt. Möchten Sie den Timestamp wirklich ändern?",
                        "Timestamp ändern",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (result != JOptionPane.YES_OPTION) {
                    return; // Abbrechen, wenn der Benutzer "Nein" wählt
                }
            }

            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            switch (type) {
                case "erfassung":
                    bestellung.setErfassung(now);
                    erfassungLabel.setText(sdf.format(now));
                    break;
                case "versand":
                    // Zurücksetzen von Lieferung und Bezahlung, wenn Versand neu gesetzt wird
                    if (bestellung.getLieferung() != null || bestellung.getBezahlung() != null) {
                        bestellung.setLieferung(null);
                        bestellung.setBezahlung(null);
                        lieferungLabel.setText("Nicht gesetzt");
                        bezahlungLabel.setText("Nicht gesetzt");
                        hasChanges = true; // Änderungen markieren
                    }
                    bestellung.setVersand(now);
                    versandLabel.setText(sdf.format(now));
                    break;
                case "lieferung":
                    bestellung.setLieferung(now);
                    lieferungLabel.setText(sdf.format(now));
                    break;
                case "bezahlung":
                    bestellung.setBezahlung(now);
                    bezahlungLabel.setText(sdf.format(now));
                    break;
            }
            hasChanges = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Aktualisieren: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Speichert alle Änderungen und schließt den Dialog.
     *
     * @author Bjarne von Appen
     */
    private void saveAndClose() {
        try {
            if (hasChanges) {
                boolean success = session.getCommunicator().updateBestellung(bestellung);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Änderungen erfolgreich gespeichert!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                    // Aktualisiere die übergeordnete Bestellungsseite
                    if (getOwner() instanceof LagerUI) {
                        LagerUI lagerUI = (LagerUI) getOwner();
                        Component currentPanel = lagerUI.getContentPane().getComponent(0);
                        if (currentPanel instanceof BestellungPage) {
                            ((BestellungPage) currentPanel).updateTable();
                        }
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Änderungen!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
