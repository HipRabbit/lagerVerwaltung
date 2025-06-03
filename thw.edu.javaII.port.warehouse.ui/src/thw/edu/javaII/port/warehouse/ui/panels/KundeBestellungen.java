package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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
import javax.swing.SwingConstants;

import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.model.BestellungProdukt;
import thw.edu.javaII.port.warehouse.model.Kunde;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.model.BestellungTableModel;

/**
 * Dialog zur Anzeige aller Bestellungen eines bestimmten Kunden.
 *
 * <p>Diese Klasse erweitert JDialog und zeigt eine tabellarische Übersicht
 * aller Bestellungen eines ausgewählten Kunden mit Gesamtwertberechnung
 * und automatischer Behandlung von leeren Bestellungslisten.
 *
 * <p>Attribute:
 * <ul>
 *   <li>JTable table – Tabelle zur Anzeige der Bestellungen</li>
 *   <li>JScrollPane js – Scroll-Container für die Tabelle</li>
 *   <li>BestellungTableModel model – Datenmodell für die Tabelle</li>
 *   <li>Session ses – aktuelle Benutzersitzung</li>
 *   <li>JFrame parent – übergeordnetes Fenster</li>
 *   <li>Kunde kunde – der Kunde, dessen Bestellungen angezeigt werden</li>
 *   <li>JLabel lblGesamtwert – Anzeige des Gesamtwerts aller Bestellungen</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>KundeBestellungen(Session, JFrame, Kunde) – Konstruktor mit UI-Initialisierung</li>
 *   <li>berechneGesamtwert() – Berechnet den Gesamtwert aller Kundenbestellungen</li>
 * </ul>
 *
 * @author Paul Hartmann
 */
public class KundeBestellungen extends JDialog {
    private static final long serialVersionUID = 1L;
    
    /** Tabelle zur Anzeige der Bestellungen */
    private JTable table;
    /** Scroll-Container für die Tabelle */
    private JScrollPane js;
    /** Datenmodell für die Bestellungstabelle */
    private BestellungTableModel model;
    /** Aktuelle Benutzersitzung */
    private Session ses;
    /** Übergeordnetes Fenster */
    @SuppressWarnings("unused")
    private JFrame parent;
    /** Der Kunde, dessen Bestellungen angezeigt werden */
    private Kunde kunde;
    /** Anzeige des Gesamtwerts aller Bestellungen */
    private JLabel lblGesamtwert;

    /**
     * Konstruktor erstellt den Dialog für Kundenbestellungen.
     *
     * @param ses    aktuelle Benutzersitzung
     * @param parent übergeordnetes Fenster
     * @param kunde  Kunde, dessen Bestellungen angezeigt werden sollen
     */
    public KundeBestellungen(Session ses, JFrame parent, Kunde kunde) {
        super(parent, true); // Setzt den Dialog als modal
        this.ses = ses;
        this.parent = parent;
        this.kunde = kunde;
        
        // Dialog-Größe und Position festlegen
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension(600, 450);
        setBounds(ss.width / 2 - frameSize.width / 2, ss.height / 2 - frameSize.height / 2, frameSize.width, frameSize.height);
        setLayout(new BorderLayout(0, 0));

        // Titel-Label mit Kundenname
        JLabel lblNewLabel = new JLabel("Bestellungen von " + kunde.toString());
        lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblNewLabel, BorderLayout.NORTH);

        // Zentraler Bereich für Bestellungstabelle
        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout());
        
        // Bestellungen des Kunden laden
        List<Bestellung> bestellungen = ses.getCommunicator().getBestellungenByKunde(kunde.getId());
        // Fallback auf leere Liste, falls null
        if (bestellungen == null) {
            bestellungen = new ArrayList<>();
        }
        
        // Prüfung auf leere Bestellungsliste
        if (bestellungen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Bestellungen für Kunde " + kunde.toString() + " gefunden.",
                    "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Dialog schließen, um zur SearchKunde-Ansicht zurückzukehren
            return; // Konstruktor verlassen, um die restliche Initialisierung zu überspringen
        }

        // Tabelle mit Bestellungsdaten erstellen
        model = new BestellungTableModel(bestellungen);
        table = new JTable(model);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.DARK_GRAY);
        js = new JScrollPane(table);
        js.setVisible(true);
        panel.add(js, BorderLayout.CENTER);

        // Südlicher Bereich für Gesamtwert und Buttons
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        add(southPanel, BorderLayout.SOUTH);

        // Gesamtwert-Anzeige
        lblGesamtwert = new JLabel("Gesamtwert: " + new DecimalFormat("#,##0.00").format(berechneGesamtwert()) + " €");
        lblGesamtwert.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        southPanel.add(lblGesamtwert, BorderLayout.NORTH);

        // Button-Panel
        JPanel buttonPane = new JPanel();
        southPanel.add(buttonPane, BorderLayout.SOUTH);

        JButton btnClose = new JButton("Schließen");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnClose);

        // Dialog sichtbar machen, nachdem alles initialisiert wurde
        setVisible(true);
    }

    /**
     * Berechnet den Gesamtwert aller Bestellungen des Kunden.
     *
     * @return Gesamtwert aller Bestellungen in Euro
     * @author Paul Hartmann
     */
    private double berechneGesamtwert() {
        double gesamtwert = 0.0;
        try {
            List<Bestellung> bestellungen = ses.getCommunicator().getBestellungenByKunde(kunde.getId());
            // Fallback für berechneGesamtwert, falls bestellungen null ist
            if (bestellungen == null) {
                bestellungen = new ArrayList<>();
            }
            
            // Durchlaufe alle Bestellungen und summiere Produktpreise
            for (Bestellung bestellung : bestellungen) {
                for (BestellungProdukt bp : bestellung.getProdukte()) {
                    gesamtwert += bp.getAnzahl() * bp.getProdukt().getPreis();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Berechnen des Gesamtwerts: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        return gesamtwert;
    }
}
