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
 * Dialog zum Hinzufügen neuer Lagerplätze im Lagerverwaltungssystem.
 *
 * <p>Diese Klasse erweitert JDialog und bietet eine Benutzeroberfläche
 * zum Erstellen neuer Lagerplätze mit Eingabevalidierung und automatischer
 * ID-Generierung durch den Server.
 *
 * <p>Attribute:
 * <ul>
 *   <li>Session ses – aktuelle Benutzersitzung</li>
 *   <li>JTextField txtName – Eingabefeld für Lagerplatzname</li>
 *   <li>JTextField txtKapazitaet – Eingabefeld für Kapazität</li>
 *   <li>JComboBox<Lager> cbLager – Dropdown für Lagerauswahl</li>
 *   <li>JButton btnSave – Button zum Speichern</li>
 *   <li>JButton btnCancel – Button zum Abbrechen</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>AddLagerPlatz(Session, JFrame) – Konstruktor mit UI-Initialisierung</li>
 *   <li>initializeUI() – Erstellt die Benutzeroberfläche</li>
 *   <li>loadLager() – Lädt verfügbare Lager in das Dropdown</li>
 *   <li>saveLagerPlatz() – Speichert den neuen Lagerplatz</li>
 *   <li>NumberOnlyFilter – Eingabefilter für Zahlen</li>
 *   <li>LetterOnlyFilter – Eingabefilter für Text</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
public class AddLagerPlatz extends JDialog {
    private static final long serialVersionUID = 3L;
    
    /** Aktuelle Benutzersitzung */
    private Session ses;
    /** Eingabefeld für Lagerplatzname */
    private JTextField txtName;
    /** Eingabefeld für Kapazität */
    private JTextField txtKapazitaet;
    /** Dropdown für Lagerauswahl */
    private JComboBox<Lager> cbLager;
    /** Button zum Speichern */
    private JButton btnSave;
    /** Button zum Abbrechen */
    private JButton btnCancel;

    /**
     * Konstruktor erstellt den Dialog zum Hinzufügen von Lagerplätzen.
     *
     * @param ses    aktuelle Benutzersitzung
     * @param parent übergeordnetes Fenster
     */
    public AddLagerPlatz(Session ses, JFrame parent) {
        super(parent, "Lagerplatz anlegen", true);
        this.ses = ses;
        initializeUI();
    }

    /**
     * Initialisiert die Benutzeroberfläche des Dialogs.
     *
     * @author Lennart Höpfner
     */
    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[left][grow, left]", "[]"));
        setSize(450, 250); // Kleinere Höhe, da ID-Feld entfernt
        setLocationRelativeTo(getParent());

        // Eingabefelder mit DocumentFilter
        add(new JLabel("Name:"), "align left");
        txtName = new JTextField(20);
        ((AbstractDocument) txtName.getDocument()).setDocumentFilter(new LetterOnlyFilter(txtName));
        add(txtName, "growx, align left");

        add(new JLabel("Kapazität:"), "align left");
        txtKapazitaet = new JTextField(20);
        ((AbstractDocument) txtKapazitaet.getDocument()).setDocumentFilter(new NumberOnlyFilter("Kapazität"));
        add(txtKapazitaet, "growx, align left");

        add(new JLabel("Lager:"), "align left");
        cbLager = new JComboBox<>();
        loadLager();
        add(cbLager, "growx, align left");

        // Buttons
        btnSave = new JButton("Speichern");
        btnSave.addActionListener(e -> saveLagerPlatz());
        btnCancel = new JButton("Abbrechen");
        btnCancel.addActionListener(e -> dispose());
        add(btnSave, "split 2, right");
        add(btnCancel);
    }

    /**
     * Lädt alle verfügbaren Lager in das Dropdown-Menü.
     *
     * @author Lennart Höpfner
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
     * Speichert den neuen Lagerplatz nach Validierung der Eingaben.
     *
     * @author Lennart Höpfner
     */
    private void saveLagerPlatz() {
        try {
            String name = txtName.getText().trim();
            int kapazitaet = Integer.parseInt(txtKapazitaet.getText().trim());
            Lager lager = (Lager) cbLager.getSelectedItem();

            if (name.isEmpty() || kapazitaet <= 0 || lager == null) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder korrekt aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ID wird serverseitig generiert, daher 0 setzen
            LagerPlatz lagerPlatz = new LagerPlatz(0, name, kapazitaet, lager);
            WarehouseReturnDEO ret = ses.getCommunicator().addLagerPlatz(lagerPlatz);

            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                JOptionPane.showMessageDialog(this, "Lagerplatz erfolgreich angelegt: " + ret.getMessage(), "Erfolg", JOptionPane.INFORMATION_MESSAGE);
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
     * DocumentFilter für Eingabefelder, die nur Zahlen akzeptieren.
     *
     * @author Lennart Höpfner
     */
    private class NumberOnlyFilter extends DocumentFilter {
        private boolean hasShownWarning = false;
        private String fieldName;

        /**
         * Konstruktor für den Zahlen-Filter.
         *
         * @param fieldName Name des Feldes für Fehlermeldungen
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
         * Behandelt ungültige Eingaben mit Warnung.
         */
        private void handleInvalidInput() {
            Toolkit.getDefaultToolkit().beep();
            if (!hasShownWarning) {
                JOptionPane.showMessageDialog(AddLagerPlatz.this,
                    "Nur Zahlen sind im Feld " + fieldName + " erlaubt.",
                    "Ungültige Eingabe",
                    JOptionPane.WARNING_MESSAGE);
                hasShownWarning = true;
            }
        }
    }

    /**
     * DocumentFilter für Eingabefelder, die nur Buchstaben, Zahlen und Leerzeichen akzeptieren.
     *
     * @author Lennart Höpfner
     */
    public class LetterOnlyFilter extends DocumentFilter {
        private boolean hasShownWarning = false;
        private JTextField textField;

        /**
         * Konstruktor für den Text-Filter.
         *
         * @param textField das zu überwachende Textfeld
         */
        public LetterOnlyFilter(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if (text.matches("[a-zA-ZäöüÄÖÜß0-9\\s]*")) {
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
            if (string == null) return;
            if (string.matches("[a-zA-ZäöüÄÖÜß0-9\\s]*")) {
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
