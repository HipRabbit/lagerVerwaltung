package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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

public class KundeBestellungen extends JDialog {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private JScrollPane js;
    private BestellungTableModel model;
    private Session ses;
    @SuppressWarnings("unused")
	private JFrame parent;
    private Kunde kunde;
    private JLabel lblGesamtwert;

    public KundeBestellungen(Session ses, JFrame parent, Kunde kunde) {
        super(parent, true); // Setzt den Dialog als modal
        this.ses = ses;
        this.parent = parent;
        this.kunde = kunde;
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension(600, 450);
        setBounds(ss.width / 2 - frameSize.width / 2, ss.height / 2 - frameSize.height / 2, frameSize.width, frameSize.height);
        setLayout(new BorderLayout(0, 0));

        JLabel lblNewLabel = new JLabel("Bestellungen von " + kunde.toString());
        lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblNewLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout());
        List<Bestellung> bestellungen = ses.getCommunicator().getBestellungenByKunde(kunde.getId());
        if (bestellungen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Bestellungen für Kunde " + kunde.toString() + " gefunden.",
                    "Hinweis", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Dialog schließen, um zur SearchKunde-Ansicht zurückzukehren
            return; // Konstruktor verlassen, um die restliche Initialisierung zu überspringen
        }

        model = new BestellungTableModel(bestellungen);
        table = new JTable(model);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.DARK_GRAY);
        js = new JScrollPane(table);
        js.setVisible(true);
        panel.add(js, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        add(southPanel, BorderLayout.SOUTH);

        lblGesamtwert = new JLabel("Gesamtwert: " + new DecimalFormat("#,##0.00").format(berechneGesamtwert()) + " €");
        lblGesamtwert.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        southPanel.add(lblGesamtwert, BorderLayout.NORTH);

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

    private double berechneGesamtwert() {
        double gesamtwert = 0.0;
        try {
            List<Bestellung> bestellungen = ses.getCommunicator().getBestellungenByKunde(kunde.getId());
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