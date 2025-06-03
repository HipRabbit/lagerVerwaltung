package thw.edu.javaII.port.warehouse.ui.panels;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;
import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.model.deo.Status;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.ui.common.Communicator;
import thw.edu.javaII.port.warehouse.ui.common.Session;
/**
 * Ein Dialogfenster zum Hinzufügen eines neuen Produkts und seines Lagerbestands.
 * Ermöglicht die Eingabe von Name, Hersteller, Preis, Anzahl und die Auswahl eines Lagerplatzes.
 * Die Eingaben werden über den Communicator an den Server gesendet.
 *
 * @author Lennart Höpfner
 * @version 1.0
 * @since 2025-06-01
 */
public class AddProdukt extends JDialog {
    private static final long serialVersionUID = 4L;
    private JTextField nameField, herstellerField, preisField, anzahlField;
    private JComboBox<LagerPlatz> lagerPlatzComboBox;
    private JButton saveButton, cancelButton;
    private Communicator communicator;

    public AddProdukt(Session session, Component parent) {
        super((JFrame) null, "Produkt hinzufügen", true);
        this.communicator = session.getCommunicator();
        initComponents();
        setLocationRelativeTo(parent);
    }
    /**
     * Initialisiert die Benutzeroberfläche mit Eingabefeldern, einer Lagerplatz-Combobox und Buttons.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Produkt-Name
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        // Hersteller
        JLabel herstellerLabel = new JLabel("Hersteller:");
        herstellerField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(herstellerLabel, gbc);
        gbc.gridx = 1;
        add(herstellerField, gbc);

        // Preis
        JLabel preisLabel = new JLabel("Preis:");
        preisField = new JTextField(20);
        ((AbstractDocument) preisField.getDocument()).setDocumentFilter(new DecimalNumberFilter("Preis"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(preisLabel, gbc);
        gbc.gridx = 1;
        add(preisField, gbc);

        // Anzahl (Lagerbestand)
        JLabel anzahlLabel = new JLabel("Anzahl:");
        anzahlField = new JTextField(10);
        ((AbstractDocument) anzahlField.getDocument()).setDocumentFilter(new IntegerNumberFilter("Anzahl"));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(anzahlLabel, gbc);
        gbc.gridx = 1;
        add(anzahlField, gbc);

        // Lagerplatz
        JLabel lagerPlatzLabel = new JLabel("Lagerplatz:");
        lagerPlatzComboBox = new JComboBox<>();
        loadFreeLagerPlatz();
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(lagerPlatzLabel, gbc);
        gbc.gridx = 1;
        add(lagerPlatzComboBox, gbc);

        // Buttons
        saveButton = new JButton("Speichern");
        cancelButton = new JButton("Abbrechen");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Action listeners
        saveButton.addActionListener(e -> saveProdukt());
        cancelButton.addActionListener(e -> dispose());

        setSize(400, 250);
    }
    /**
     * Lädt verfügbare freie Lagerplätze vom Server und füllt die Combobox.
     * Deaktiviert den Speichern-Button, wenn keine Lagerplätze verfügbar sind.
     */

    private void loadFreeLagerPlatz() {
        LagerPlatz[] freeLagerPlatz = communicator.getFreeLagerPlatz();
        if (freeLagerPlatz != null) {
            for (LagerPlatz lp : freeLagerPlatz) {
                lagerPlatzComboBox.addItem(lp);
            }
        }
        if (lagerPlatzComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Keine freien Lagerplätze verfügbar.", "Warnung", JOptionPane.WARNING_MESSAGE);
            saveButton.setEnabled(false);
        }
    }
    /**
     * Speichert ein neues Produkt und den zugehörigen Lagerbestand.
     * Überprüft die Eingaben auf Korrektheit und sendet die Anfrage an den Server.
     * Zeigt Erfolgs- oder Fehlermeldungen an.
     */

    private void saveProdukt() {
        try {
            String name = nameField.getText().trim();
            String hersteller = herstellerField.getText().trim();
            double preis = Double.parseDouble(preisField.getText().trim());
            int anzahl = Integer.parseInt(anzahlField.getText().trim());
            LagerPlatz lagerPlatz = (LagerPlatz) lagerPlatzComboBox.getSelectedItem();

            if (name.isEmpty() || hersteller.isEmpty() || preis < 0 || anzahl <= 0 || lagerPlatz == null) {
                JOptionPane.showMessageDialog(this, "Bitte alle Felder korrekt ausfüllen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create Produkt without ID; ID will be assigned by Database
            Produkt produkt = new Produkt(0, name, hersteller, preis);
            LagerBestand lagerBestand = new LagerBestand(0, anzahl, produkt, lagerPlatz);

            // Create WarehouseDEO for ADD_WITH_BESTAND
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.PRODUKT);
            deo.setCommand(Command.ADD_WITH_BESTAND);
            deo.setData(new Object[]{produkt, lagerBestand});

            WarehouseReturnDEO ret = communicator.sendRequest(deo);
            if (ret.getStatus() == Status.OK) {
                JOptionPane.showMessageDialog(this, "Produkt und Lagerbestand erfolgreich hinzugefügt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe für Preis oder Anzahl.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Ein DocumentFilter, der nur Dezimalzahlen erlaubt (z. B. 12.34).
     * Zeigt eine Warnung an, wenn ungültige Zeichen eingegeben werden.
     *
     * @param fieldName Der Name des Feldes, um die Fehlermeldung zu personalisieren.
     */

    // DocumentFilter für Dezimalzahlen (Preis)
    private class DecimalNumberFilter extends DocumentFilter {
        private boolean hasShownWarning = false;
        private String fieldName;

        public DecimalNumberFilter(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
            if (newText.matches("[0-9]*\\.?[0-9]*")) {
                super.insertString(fb, offset, string, attr);
                hasShownWarning = false;
            } else {
                handleInvalidInput();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
            if (newText.matches("[0-9]*\\.?[0-9]*")) {
                super.replace(fb, offset, length, text, attrs);
                hasShownWarning = false;
            } else {
                handleInvalidInput();
            }
        }

        private void handleInvalidInput() {
            Toolkit.getDefaultToolkit().beep();
            if (!hasShownWarning) {
                JOptionPane.showMessageDialog(AddProdukt.this,
                    "Nur Zahlen (z. B. 12.34) sind im Feld " + fieldName + " erlaubt.",
                    "Ungültige Eingabe",
                    JOptionPane.WARNING_MESSAGE);
                hasShownWarning = true;
            }
        }
    }
    /**
     * Ein DocumentFilter, der nur Ganzzahlen erlaubt.
     * Zeigt eine Warnung an, wenn ungültige Zeichen eingegeben werden.
     *
     * @param fieldName Der Name des Feldes, um die Fehlermeldung zu personalisieren.
     */

    // DocumentFilter für Ganzzahlen (Anzahl)
    private class IntegerNumberFilter extends DocumentFilter {
        private boolean hasShownWarning = false;
        private String fieldName;

        public IntegerNumberFilter(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
            if (newText.matches("[0-9]*")) {
                super.insertString(fb, offset, string, attr);
                hasShownWarning = false;
            } else {
                handleInvalidInput();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
            if (newText.matches("[0-9]*")) {
                super.replace(fb, offset, length, text, attrs);
                hasShownWarning = false;
            } else {
                handleInvalidInput();
            }
        }

        private void handleInvalidInput() {
            Toolkit.getDefaultToolkit().beep();
            if (!hasShownWarning) {
                JOptionPane.showMessageDialog(AddProdukt.this,
                    "Nur Ganzzahlen sind im Feld " + fieldName + " erlaubt.",
                    "Ungültige Eingabe",
                    JOptionPane.WARNING_MESSAGE);
                hasShownWarning = true;
            }
        }
    }
}