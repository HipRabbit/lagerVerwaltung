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

public class AddLager extends JDialog {
    private static final long serialVersionUID = 2L;
    private Session ses;
    private JTextField txtName, txtOrt, txtArt;
    private JButton btnSave, btnCancel;
    /**
     * Ein Dialogfenster zum Anlegen eines neuen Lagers in der Lagerverwaltung.
     * Ermöglicht die Eingabe von Name, Ort und Art des Lagers mit Validierung auf Buchstaben und Leerzeichen.
     * Die Eingaben werden über den Communicator an den Server gesendet.
     *
     * @author [Lennart Höpfner]
     * @version 1.0
     * @since 2025-06-01
     */
    public AddLager(Session ses, JFrame parent) {
        super(parent, "Lager anlegen", true);
        this.ses = ses;
        initializeUI();
    }
        /**
         * Initialisiert die Benutzeroberfläche des Dialogs mit Eingabefeldern und Buttons.
         */
    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[right][left, grow]", "[]10[]"));
        setSize(400, 200);
        setLocationRelativeTo(getParent());

        // Eingabefelder mit eigenem DocumentFilter
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
    }
    /**
     * Speichert ein neues Lager basierend auf den eingegebenen Daten.
     * Überprüft die Eingaben auf Vollständigkeit und sendet die Anfrage an den Server.
     * Zeigt Erfolgs- oder Fehlermeldungen an.
     */
    private void saveLager() {
        try {
            String name = txtName.getText().trim();
            String ort = txtOrt.getText().trim();
            String art = txtArt.getText().trim();

            if (name.isEmpty() || ort.isEmpty() || art.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Lager lager = new Lager(0, name, ort, art);
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGER);
            deo.setCommand(Command.ADD);
            deo.setData(lager);

            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                JOptionPane.showMessageDialog(this, "Lager erfolgreich angelegt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Ein DocumentFilter, der nur Buchstaben (inkl. Umlaute) und Leerzeichen erlaubt.
     * Zeigt eine Warnung an, wenn ungültige Zeichen eingegeben werden.
     */
    // DocumentFilter für nur Buchstaben und Leerzeichen
    private class LetterOnlyFilter extends DocumentFilter {
        private boolean hasShownWarning = false; // Jede Filter-Instanz hat ihre eigene Variable

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (string.matches("[a-zA-ZäöüÄÖÜß\\s]*")) {
                super.insertString(fb, offset, string, attr);
                hasShownWarning = false; // Zurücksetzen, wenn Eingabe korrekt
            } else {
                handleInvalidInput();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (text.matches("[a-zA-ZäöüÄÖÜß\\s]*")) {
                super.replace(fb, offset, length, text, attrs);
                hasShownWarning = false; // Zurücksetzen, wenn Eingabe korrekt
            } else {
                handleInvalidInput();
            }
        }

        private void handleInvalidInput() {
            Toolkit.getDefaultToolkit().beep();
            if (!hasShownWarning) {
                JOptionPane.showMessageDialog(AddLager.this, 
                    "Nur Buchstaben (a-z, A-Z, Umlaute und Leerzeichen) sind in diesem Feld erlaubt.", 
                    "Ungültige Eingabe", 
                    JOptionPane.WARNING_MESSAGE);
                hasShownWarning = true; // Verhindert erneute Anzeige, bis korrekte Eingabe erfolgt
            }
        }
    }
}