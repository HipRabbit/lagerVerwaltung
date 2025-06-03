package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import thw.edu.javaII.port.warehouse.model.Nachbestellung;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.ui.common.Session;

/**
 * Dialog zur Änderung der Nachbestellmenge für ausgewählte Nachbestellungen.
 *
 * <p>Diese Klasse erweitert JDialog und bietet eine einfache Benutzeroberfläche
 * zur Eingabe und Speicherung einer neuen Nachbestellmenge mit Validierung
 * und Fehlerbehandlung.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session ses – aktuelle Benutzersitzung</li>
 *   <li>JTextField txtAnzahl – Eingabefeld für neue Anzahl</li>
 *   <li>JButton btnSave – Button zum Speichern</li>
 *   <li>JButton btnCancel – Button zum Abbrechen</li>
 *   <li>Nachbestellung selectedNachbestellung – die zu bearbeitende Nachbestellung</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>AnzahlNachbestellung(Session, JFrame) – Konstruktor mit UI-Initialisierung</li>
 *   <li>setSelectedNachbestellung(Nachbestellung) – Setzt die zu bearbeitende Nachbestellung</li>
 *   <li>initializeUI() – Erstellt die Benutzeroberfläche</li>
 *   <li>saveAnzahl() – Speichert die neue Nachbestellmenge</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
public class AnzahlNachbestellung extends JDialog {
    private static final long serialVersionUID = 15L;
    
    /** Aktuelle Benutzersitzung */
    private final Session ses;
    /** Eingabefeld für neue Anzahl */
    private JTextField txtAnzahl;
    /** Buttons für Aktionen */
    private JButton btnSave, btnCancel;
    /** Die zu bearbeitende Nachbestellung */
    private Nachbestellung selectedNachbestellung;

    /**
     * Konstruktor erstellt den Dialog zur Änderung der Nachbestellmenge.
     *
     * @param ses    aktuelle Benutzersitzung
     * @param parent übergeordnetes Fenster
     */
    public AnzahlNachbestellung(Session ses, JFrame parent) {
        super(parent, "Nachbestellen", true);
        this.ses = ses;
        initializeUI();
    }

    /**
     * Setzt die zu bearbeitende Nachbestellung und aktualisiert das Eingabefeld.
     *
     * @param nachbestellung die zu bearbeitende Nachbestellung
     * @author Lennart Höpfner
     */
    public void setSelectedNachbestellung(Nachbestellung nachbestellung) {
        this.selectedNachbestellung = nachbestellung;
        if (txtAnzahl != null && nachbestellung != null) {
            txtAnzahl.setText(String.valueOf(nachbestellung.getAnzahlnachbestellung()));
        }
    }

    /**
     * Initialisiert die Benutzeroberfläche des Dialogs.
     *
     * @author Lennart Höpfner
     */
    private void initializeUI() {
        setLayout(new MigLayout("wrap 2", "[right][grow]"));
        setSize(300, 150);
        setLocationRelativeTo(getParent());

        // Eingabefeld für neue Anzahl
        add(new JLabel("Neue Anzahl:"));
        txtAnzahl = new JTextField(10);
        add(txtAnzahl, "growx");

        // Speichern-Button
        btnSave = new JButton("Abschicken");
        btnSave.addActionListener(e -> saveAnzahl());

        // Abbrechen-Button
        btnCancel = new JButton("Abbrechen");
        btnCancel.addActionListener(e -> dispose());

        // Button-Panel mit zentrierter Ausrichtung
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, "span 2, center");

        // Benutzerfreundlichkeitsverbesserungen
        txtAnzahl.requestFocusInWindow(); // Fokus auf Textfeld setzen
        getRootPane().setDefaultButton(btnSave); // Enter löst Speichern aus
    }

    /**
     * Speichert die neue Nachbestellmenge nach Validierung der Eingabe.
     *
     * @author Lennart Höpfner
     */
    private void saveAnzahl() {
        try {
            if (selectedNachbestellung == null) {
                JOptionPane.showMessageDialog(this, "Keine Nachbestellung ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Eingabe validieren
            int neueAnzahl = Integer.parseInt(txtAnzahl.getText().trim());
            if (neueAnzahl < 0) throw new NumberFormatException();

            // Nachbestellung aktualisieren
            selectedNachbestellung.setAnzahlnachbestellung(neueAnzahl);

            // Update-Request an Server senden
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.NACHBESTELLUNG);
            deo.setCommand(Command.UPDATE);
            deo.setData(selectedNachbestellung);

            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                JOptionPane.showMessageDialog(this, "Anzahl erfolgreich geändert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige ganze Zahl ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
