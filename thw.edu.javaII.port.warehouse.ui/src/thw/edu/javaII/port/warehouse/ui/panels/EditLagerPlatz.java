package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;
import thw.edu.javaII.port.warehouse.model.Lager;
import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.model.common.Cast;
import thw.edu.javaII.port.warehouse.ui.common.Session;
/**
 * Ein Dialogfenster zum Bearbeiten eines bestehenden Lagerplatzes in der Lagerverwaltung.
 * Ermöglicht die Änderung von Name, Kapazität und zugehörigem Lager.
 * Die Änderungen werden über den Communicator an den Server gesendet.
 *
 * @author Lennart Höpfner
 * @version 1.0
 * @since 2025-06-01
 */
public class EditLagerPlatz extends JDialog {
    private static final long serialVersionUID = 14L;
    private Session ses;
    private LagerPlatz lagerPlatz;
    private JTextField txtName, txtKapazitaet;
    private JComboBox<Lager> cbLager;
    private JButton btnSave, btnCancel;
    /**
     * Erstellt ein neues Dialogfenster zum Bearbeiten eines Lagerplatzes.
     *
     * @param ses    Die aktuelle Sitzung mit dem Communicator.
     * @param parent Das übergeordnete JFrame, relativ zu dem der Dialog positioniert wird.
     */
    public EditLagerPlatz(Session ses, JFrame parent) {
        super(parent, "Lagerplatz bearbeiten", true);
        this.ses = ses;
        initializeUI();
    }
    /**
     * Erstellt ein neues Dialogfenster zum Bearbeiten eines spezifischen Lagerplatzes.
     *
     * @param ses        Die aktuelle Sitzung mit dem Communicator.
     * @param parent     Das übergeordnete JFrame, relativ zu dem der Dialog positioniert wird.
     * @param lagerPlatz Der zu bearbeitende Lagerplatz.
     */
    public EditLagerPlatz(Session ses, JFrame parent, LagerPlatz lagerPlatz) {
        super(parent, "Lagerplatz bearbeiten", true);
        this.ses = ses;
        this.lagerPlatz = lagerPlatz;
        initializeUI();
    }
    /**
     * Initialisiert die Benutzeroberfläche mit Eingabefeldern, einer Lager-Combobox und Buttons.
     * Füllt die Felder mit den aktuellen Daten des Lagerplatzes.
     */
    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[right][left, grow]", "[]10[]"));
        setSize(400, 250);
        setLocationRelativeTo(getParent());

        // Eingabefelder
        add(new JLabel("Name:"));
        txtName = new JTextField(20);
        ((AbstractDocument) txtName.getDocument()).setDocumentFilter(new LetterOnlyFilter(txtName));
        add(txtName, "growx");

        add(new JLabel("Kapazität:"));
        txtKapazitaet = new JTextField(20);
        ((AbstractDocument) txtKapazitaet.getDocument()).setDocumentFilter(new NumberOnlyFilter("Kapazität"));
        add(txtKapazitaet, "growx");

        add(new JLabel("Lager:"));
        cbLager = new JComboBox<>();
        loadLager();
        add(cbLager, "growx");

        // Buttons in einem zentrierten Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Speichern");
        btnSave.addActionListener(e -> saveLagerPlatz());
        btnCancel = new JButton("Abbrechen");
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, "span 2, center");

        updateFields();
    }
    /**
     * Lädt die verfügbaren Lager vom Server und füllt die Combobox.
     * Zeigt eine Fehlermeldung an, wenn ein Fehler auftritt.
     */
    private void loadLager() {
        try {
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGER);
            deo.setCommand(Command.LIST);
            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                java.util.List<Lager> lagerList = Cast.safeListCast(ret.getData(), Lager.class);
                cbLager.removeAllItems();
                if (lagerList != null) {
                    for (Lager lager : lagerList) {
                        cbLager.addItem(lager);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Lager: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Aktualisiert die Eingabefelder mit den aktuellen Daten des Lagerplatzes.
     */
    private void updateFields() {
        if (lagerPlatz != null) {
            txtName.setText(lagerPlatz.getName());
            txtKapazitaet.setText(String.valueOf(lagerPlatz.getKapazitaet()));
            cbLager.setSelectedItem(lagerPlatz.getLager_id());
        } else {
            txtName.setText("");
            txtKapazitaet.setText("");
            cbLager.setSelectedItem(null);
        }
    }
    /**
     * Speichert die Änderungen am Lagerplatz.
     * Überprüft die Eingaben auf Korrektheit und sendet die Anfrage an den Server.
     * Zeigt Erfolgs- oder Fehlermeldungen an.
     *
     * @throws NumberFormatException Wenn die Kapazität keine gültige Zahl ist.
     */
    private void saveLagerPlatz() {
        try {
            if (lagerPlatz == null) {
                JOptionPane.showMessageDialog(this, "Kein Lagerplatz ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = txtName.getText().trim();
            int kapazitaet = Integer.parseInt(txtKapazitaet.getText().trim());
            Lager lager = (Lager) cbLager.getSelectedItem();

            if (name.isEmpty() || kapazitaet <= 0 || lager == null) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder korrekt aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            lagerPlatz.setName(name);
            lagerPlatz.setKapazitaet(kapazitaet);
            lagerPlatz.setLager_id(lager);

            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGERPLATZ);
            deo.setCommand(Command.UPDATE);
            deo.setData(lagerPlatz);

            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                JOptionPane.showMessageDialog(this, "Lagerplatz erfolgreich aktualisiert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültige Kapazität. Bitte geben Sie eine Zahl ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Ein DocumentFilter, der nur Zahlen erlaubt.
     * Zeigt eine Warnung an, wenn ungültige Zeichen eingegeben werden.
     */
    // DocumentFilter für nur Zahlen
    private class NumberOnlyFilter extends DocumentFilter {
        private boolean hasShownWarning = false;
        private String fieldName;
        /**
         * Erstellt einen neuen Filter für Ganzzahlen.
         *
         * @param fieldName Der Name des Feldes, um die Fehlermeldung zu personalisieren.
         */
        public NumberOnlyFilter(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if (string.matches("[0-9]*")) {
                super.insertString(fb, offset, string, attr);
                hasShownWarning = false;
            } else {
                handleInvalidInput();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (text.matches("[0-9]*")) {
                super.replace(fb, offset, length, text, attrs);
                hasShownWarning = false;
            } else {
                handleInvalidInput();
            }
        }
        /**
         * Behandelt ungültige Eingaben durch einen akustischen Hinweis und eine Warnmeldung.
         */
        private void handleInvalidInput() {
            Toolkit.getDefaultToolkit().beep();
            if (!hasShownWarning) {
                JOptionPane.showMessageDialog(EditLagerPlatz.this,
                    "Nur Zahlen sind im Feld " + fieldName + " erlaubt.",
                    "Ungültige Eingabe",
                    JOptionPane.WARNING_MESSAGE);
                hasShownWarning = true;
            }
        }
    }
    /**
     * Ein DocumentFilter, der nur Buchstaben, Zahlen und Leerzeichen erlaubt.
     * Markiert das Feld mit roter Umrandung bei ungültiger Eingabe und zeigt eine Warnung an.
     */
    public class LetterOnlyFilter extends DocumentFilter {
        private boolean hasShownWarning = false;
        private JTextField textField;
        /**
         * Erstellt einen neuen Filter für Buchstaben, Zahlen und Leerzeichen.
         *
         * @param textField Das zugehörige Textfeld, um die Umrandung zu ändern.
         */
        public LetterOnlyFilter(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }

            if (text.matches("[a-zA-ZäöüÄÖÜß0-9\\s]*")) { // Hinzufügung von 0-9
                super.replace(fb, offset, length, text, attrs);
                textField.setBorder(UIManager.getBorder("TextField.border"));
                hasShownWarning = false;
            } else {
                Toolkit.getDefaultToolkit().beep();
                textField.setBorder(BorderFactory.createLineBorder(Color.RED));
                if (!hasShownWarning) {
                    JOptionPane.showMessageDialog(textField.getTopLevelAncestor(),
                            "Nur Buchstaben (a-z, A-Z, Umlaute), Zahlen (0-9) und Leerzeichen sind im Feld Name erlaubt.",
                            "Ungültige Eingabe",
                            JOptionPane.WARNING_MESSAGE);
                    hasShownWarning = true;
                }
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }

            if (string.matches("[a-zA-ZäöüÄÖÜß0-9\\s]*")) { // Hinzufügung von 0-9
                super.insertString(fb, offset, string, attr);
                textField.setBorder(UIManager.getBorder("TextField.border"));
                hasShownWarning = false;
            } else {
                Toolkit.getDefaultToolkit().beep();
                textField.setBorder(BorderFactory.createLineBorder(Color.RED));
                if (!hasShownWarning) {
                    JOptionPane.showMessageDialog(textField.getTopLevelAncestor(),
                            "Nur Buchstaben (a-z, A-Z, Umlaute), Zahlen (0-9) und Leerzeichen sind im Feld Name erlaubt.",
                            "Ungültige Eingabe",
                            JOptionPane.WARNING_MESSAGE);
                    hasShownWarning = true;
                }
            }
        }
    }
}