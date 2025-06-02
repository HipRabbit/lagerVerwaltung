package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.model.deo.Status;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.ui.common.Session;
/**
 * Ein Dialogfenster zum Ändern des Lagerbestands eines Produkts.
 * Ermöglicht das Hinzufügen oder Entfernen von Mengen und aktualisiert den Bestand über den Communicator.
 *
 * @author [Lennart Höpfner]
 * @version 1.0
 * @since 2025-06-01
 */
public class ChangeLagerBestand extends JDialog {

    private static final long serialVersionUID = -836302868167902266L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    /**
     * Erstellt das Dialogfenster mit den Details des ausgewählten Lagerbestands.
     *
     * @param l  Der zu bearbeitende Lagerbestand.
     * @param ses Die aktuelle Sitzung mit dem Communicator.
     */
    public ChangeLagerBestand(LagerBestand l, Session ses) {
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension(450, 300);
        setBounds(ss.width / 2 - frameSize.width / 2, ss.height / 2 - frameSize.height / 2, 450, 365);
        getContentPane().setLayout(new BorderLayout());
        JLabel lblNewLabel = new JLabel("Produkt - Lagerbestand bearbeiten");
        lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(lblNewLabel, BorderLayout.NORTH);
        contentPanel.setLayout(new MigLayout("", "[99.00][grow][][]", "[][][][][][][][][][]"));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JLabel lblNewLabel_1 = new JLabel("Produkt-ID:");
        contentPanel.add(lblNewLabel_1, "cell 0 0");

        JLabel lblNewLabel_2 = new JLabel(l.getProdukt_id().getId() + "");
        contentPanel.add(lblNewLabel_2, "cell 1 0");

        JLabel lblNewLabel_3 = new JLabel("Name:");
        contentPanel.add(lblNewLabel_3, "cell 0 1");

        JLabel lblNewLabel_5 = new JLabel(l.getProdukt_id().getName());
        contentPanel.add(lblNewLabel_5, "cell 1 1");

        JLabel lblNewLabel_4 = new JLabel("Hersteller:");
        contentPanel.add(lblNewLabel_4, "cell 0 2");

        JLabel lblNewLabel_6 = new JLabel(l.getProdukt_id().getHersteller());
        contentPanel.add(lblNewLabel_6, "cell 1 2");

        JLabel lblNewLabel_7 = new JLabel("Lager:");
        contentPanel.add(lblNewLabel_7, "cell 0 3");

        JLabel lblNewLabel_8 = new JLabel(l.getLagerplatz_id().getLager_id().getName());
        contentPanel.add(lblNewLabel_8, "cell 1 3");

        JLabel lblNewLabel_9 = new JLabel("Lagerplatz:");
        contentPanel.add(lblNewLabel_9, "cell 0 4");

        JLabel lblNewLabel_11 = new JLabel(l.getLagerplatz_id().getName());
        contentPanel.add(lblNewLabel_11, "cell 1 4");

        JLabel lblNewLabel_10 = new JLabel("Bestand:");
        contentPanel.add(lblNewLabel_10, "cell 0 5");

        JLabel lblNewLabel_12 = new JLabel(l.getAnzahl() + "");
        contentPanel.add(lblNewLabel_12, "cell 1 5");

        // Neues Label für Kapazität hinzufügen
        JLabel lblNewLabel_17 = new JLabel("Kapazität:");
        contentPanel.add(lblNewLabel_17, "cell 0 7");

        JLabel lblNewLabel_18 = new JLabel(l.getLagerplatz_id().getKapazitaet() + "");
        contentPanel.add(lblNewLabel_18, "cell 1 7");

        JLabel lblNewLabel_14 = new JLabel("");
        contentPanel.add(lblNewLabel_14, "cell 0 9 4 1");

        JButton btnNewButton_1 = new JButton("-");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int change = Integer.parseInt(textField.getText());
                    if (change < 0) {
                        JOptionPane.showMessageDialog(ChangeLagerBestand.this,
                            "Die Eingabe darf nicht negativ sein.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int newAnzahl = l.getAnzahl() - change;
                    if (newAnzahl < 0) {
                        JOptionPane.showMessageDialog(ChangeLagerBestand.this,
                            "Der Bestand kann nicht unter 0 fallen.", "Kapazitätsfehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    l.setAnzahl(newAnzahl);
                    WarehouseReturnDEO response = ses.getCommunicator().updateLagerBestand(l);
                    if (response != null && response.getStatus() == Status.OK) {
                        lblNewLabel_12.setText(l.getAnzahl() + "");
                        textField.setText("");
                        lblNewLabel_14.setText("Es wurden Waren im Wert von " + change * l.getProdukt_id().getPreis() + "€ aus dem Lager entnommen.");
                    } else {
                        // Server hat die Aktualisierung abgelehnt
                        JOptionPane.showMessageDialog(ChangeLagerBestand.this,
                            "Fehler beim Aktualisieren des Bestands: " + (response != null ? response.getMessage() : "Keine Antwort vom Server"),
                            "Serverfehler", JOptionPane.ERROR_MESSAGE);
                        // Bestand zurücksetzen
                        l.setAnzahl(l.getAnzahl() + change);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ChangeLagerBestand.this,
                        "Bitte geben Sie eine gültige Zahl ein.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnNewButton = new JButton("+");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int change = Integer.parseInt(textField.getText());
                    if (change < 0) {
                        JOptionPane.showMessageDialog(ChangeLagerBestand.this,
                            "Die Eingabe darf nicht negativ sein.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int newAnzahl = l.getAnzahl() + change;
                    int kapazitaet = l.getLagerplatz_id().getKapazitaet();
                    // Debugging-Ausgaben
                    System.out.println("Aktueller Bestand: " + l.getAnzahl());
                    System.out.println("Änderung: " + change);
                    System.out.println("Neuer Bestand: " + newAnzahl);
                    System.out.println("Kapazität des Lagerplatzes: " + kapazitaet);
                    System.out.println("Lagerplatz-ID: " + l.getLagerplatz_id().getId());
                    System.out.println("Lagerplatz-Name: " + l.getLagerplatz_id().getName());
                    if (newAnzahl > kapazitaet) {
                        JOptionPane.showMessageDialog(ChangeLagerBestand.this,
                            "Die Kapazität des Lagerplatzes (" + kapazitaet + ") würde überschritten werden.",
                            "Kapazitätsfehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    l.setAnzahl(newAnzahl);
                    WarehouseReturnDEO response = ses.getCommunicator().updateLagerBestand(l);
                    if (response != null && response.getStatus() == Status.OK) {
                        lblNewLabel_12.setText(l.getAnzahl() + "");
                        textField.setText("");
                        lblNewLabel_14.setText("Es wurden Waren im Wert von " + change * l.getProdukt_id().getPreis() + "€ ins Lager gebucht.");
                    } else {
                        // Server hat die Aktualisierung abgelehnt
                        JOptionPane.showMessageDialog(ChangeLagerBestand.this,
                            "Fehler beim Aktualisieren des Bestands: " + (response != null ? response.getMessage() : "Keine Antwort vom Server"),
                            "Serverfehler", JOptionPane.ERROR_MESSAGE);
                        // Bestand zurücksetzen
                        l.setAnzahl(l.getAnzahl() - change);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ChangeLagerBestand.this,
                        "Bitte geben Sie eine gültige Zahl ein.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lblNewLabel_15 = new JLabel("Einzelpreis:");
        contentPanel.add(lblNewLabel_15, "cell 0 6");

        JLabel lblNewLabel_16 = new JLabel(l.getProdukt_id().getPreis() + "€");
        contentPanel.add(lblNewLabel_16, "cell 1 6");

        JLabel lblNewLabel_13 = new JLabel("Verändern");
        contentPanel.add(lblNewLabel_13, "cell 0 8,alignx trailing");

        textField = new JTextField();
        contentPanel.add(textField, "cell 1 8,growx");
        textField.setColumns(10);
        contentPanel.add(btnNewButton, "cell 2 8");
        contentPanel.add(btnNewButton_1, "cell 3 8");

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        JButton okButton = new JButton("Schließen");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
    }
}