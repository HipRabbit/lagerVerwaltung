package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;
import thw.edu.javaII.port.warehouse.model.Lager;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.ui.common.Session;

/**
 * Dialog zur Bearbeitung bestehender Lager im Lagerverwaltungssystem.
 *
 * <p>Diese Klasse erweitert JDialog und bietet eine Benutzeroberfläche
 * zur Bearbeitung von Lagerdaten mit Eingabevalidierung und
 * automatischer Feldaktualisierung basierend auf der Auswahl.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session ses – aktuelle Benutzersitzung</li>
 *   <li>JTextField txtId – Anzeige der Lager-ID (nicht editierbar)</li>
 *   <li>JTextField txtName – Eingabefeld für Lagername</li>
 *   <li>JTextField txtOrt – Eingabefeld für Lagerort</li>
 *   <li>JTextField txtArt – Eingabefeld für Lagerart</li>
 *   <li>JButton btnSave – Button zum Speichern</li>
 *   <li>JButton btnCancel – Button zum Abbrechen</li>
 *   <li>Lager selectedLager – das zu bearbeitende Lager</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>EditLager(Session, JFrame) – Konstruktor mit UI-Initialisierung</li>
 *   <li>setSelectedLager(Lager) – Setzt das zu bearbeitende Lager</li>
 *   <li>initializeUI() – Erstellt die Benutzeroberfläche</li>
 *   <li>updateFields() – Aktualisiert Eingabefelder mit Lagerdaten</li>
 *   <li>saveLager() – Speichert die Änderungen am Lager</li>
 *   <li>LetterOnlyFilter – Eingabefilter für Textfelder</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
public class EditLager extends JDialog {
    private static final long serialVersionUID = 15L;
    
    /** Aktuelle Benutzersitzung */
    private Session ses;
    /** Eingabefelder für Lagerdaten */
    private JTextField txtId, txtName, txtOrt, txtArt;
    /** Buttons für Aktionen */
    private JButton btnSave, btnCancel;
    /** Das zu bearbeitende Lager */
    private Lager selectedLager;

    /**
     * Konstruktor erstellt den Dialog zur Lagerbearbeitung.
     *
     * @param ses    aktuelle Benutzersitzung
     * @param parent übergeordnetes Fenster
     */
    public EditLager(Session ses, JFrame parent) {
        super(parent, "Lager bearbeiten", true);
        this.ses = ses;
        this.selectedLager = null;
        initializeUI();
    }

    /**
     * Setzt das zu bearbeitende Lager und aktualisiert die Eingabefelder.
     *
     * @param lager das zu bearbeitende Lager
     * @author Lennart Höpfner
     */
    public void setSelectedLager(Lager lager) {
        this.selectedLager = lager;
        updateFields();
    }

    /**
     * Initialisiert die Benutzeroberfläche des Dialogs.
     *
     * @author Lennart Höpfner
     */
    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[right][left, grow]", "[]10[]"));
        setSize(400, 250);
        setLocationRelativeTo(getParent());

        // ID-Feld (nicht editierbar)
        add(new JLabel("ID:"));
        txtId = new JTextField(20);
        txtId.setEditable(false); // ID kann nicht geändert werden
        add(txtId, "growx");

        // Eingabefelder mit Textfilter für Validierung
        add(new JLabel("Name:"));
        txtName = new JTextField(20);
        ((AbstractDocument) txtName.getDocument()).setDocumentFilter(new LetterOnlyFilter());
        add(txtName, "growx");

        add(new JLabel("Ort:"));
        txtOrt = new JTextField(20);
        ((AbstractDocument) txtOrt.getDocument()).setDocumentFilter(new LetterOnlyFilter());
        add(txtOrt, "growx");

        add(new JLabel("Art:"));
        txtArt = new JTextField(20);
        ((AbstractDocument) txtArt.getDocument()).setDocumentFilter(new LetterOnlyFilter());
        add(txtArt, "growx");

        // Buttons in einem zentrierten Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Speichern");
        btnSave.addActionListener(e -> saveLager());
        btnCancel = new JButton("Abbrechen");
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, "span 2, center");

        updateFields(); // Initiale Feldaktualisierung
    }

    /**
     * Aktualisiert die Eingabefelder mit den Daten des ausgewählten Lagers.
     *
     * @author Lennart Höpfner
     */
    private void updateFields() {
        if (selectedLager != null) {
            // Felder mit Lagerdaten füllen
            txtId.setText(String.valueOf(selectedLager.getId()));
            txtName.setText(selectedLager.getName());
            txtOrt.setText(selectedLager.getOrt());
            txtArt.setText(selectedLager.getArt());
        } else {
            // Felder leeren, wenn kein Lager ausgewählt
            txtId.setText("");
            txtName.setText("");
            txtOrt.setText("");
            txtArt.setText("");
        }
    }

    /**
     * Speichert die Änderungen am Lager nach Validierung der Eingaben.
     *
     * @author Lennart Höpfner
     */
    private void saveLager() {
        try {
            if (selectedLager == null) {
                JOptionPane.showMessageDialog(this, "Kein Lager ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = txtName.getText().trim();
            String ort = txtOrt.getText().trim();
            String art = txtArt.getText().trim();

            // Validierung der Eingaben
            if (name.isEmpty() || ort.isEmpty() || art.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lagerdaten aktualisieren
            selectedLager.setName(name);
            selectedLager.setOrt(ort);
            selectedLager.setArt(art);

            // Update-Request an Server senden
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGER);
            deo.setCommand(Command.UPDATE);
            deo.setData(selectedLager);

            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                JOptionPane.showMessageDialog(this, "Lager erfolgreich aktualisiert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * DocumentFilter für Eingabefelder, die nur Buchstaben und Leerzeichen akzeptieren.
     *
     * @author Lennart Höpfner
     */
    private class LetterOnlyFilter extends DocumentFilter {
        /** Flag zur Vermeidung mehrfacher Warnmeldungen */
        private boolean hasShownWarning = false;

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (string.matches("[a-zA-ZäöüÄÖÜß\\s]*")) {
                super.insertString(fb, offset, string, attr);
                hasShownWarning = false; // Zurücksetzen bei korrekter Eingabe
            } else {
                handleInvalidInput();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (text.matches("[a-zA-ZäöüÄÖÜß\\s]*")) {
                super.replace(fb, offset, length, text, attrs);
                hasShownWarning = false; // Zurücksetzen bei korrekter Eingabe
            } else {
                handleInvalidInput();
            }
        }

        /**
         * Behandelt ungültige Eingaben mit akustischer und visueller Warnung.
         */
        private void handleInvalidInput() {
            Toolkit.getDefaultToolkit().beep();
            if (!hasShownWarning) {
                JOptionPane.showMessageDialog(EditLager.this, 
                    "Nur Buchstaben (a-z, A-Z, Umlaute und Leerzeichen) sind in diesem Feld erlaubt.", 
                    "Ungültige Eingabe", 
                    JOptionPane.WARNING_MESSAGE);
                hasShownWarning = true; // Verhindert erneute Anzeige bis zur nächsten korrekten Eingabe
            }
        }
    }
}
