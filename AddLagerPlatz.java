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

public class AddLagerPlatz extends JDialog {
    private static final long serialVersionUID = 3L;
    private Session ses;
    private JTextField txtId, txtName, txtKapazitaet;
    private JComboBox<Lager> cbLager;
    private JButton btnSave, btnCancel;

    public AddLagerPlatz(Session ses, JFrame parent) {
        super(parent, "Lagerplatz anlegen", true);
        this.ses = ses;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[left][grow, left]", "[]"));
        setSize(450, 300);
        setLocationRelativeTo(getParent());

        // Eingabefelder mit DocumentFilter
        add(new JLabel("ID:"), "align left");
        txtId = new JTextField(20);
        ((AbstractDocument) txtId.getDocument()).setDocumentFilter(new NumberOnlyFilter("ID"));
        add(txtId, "growx, align left");

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

    private void saveLagerPlatz() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            int kapazitaet = Integer.parseInt(txtKapazitaet.getText().trim());
            Lager lager = (Lager) cbLager.getSelectedItem();

            if (name.isEmpty() || kapazitaet <= 0 || lager == null) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder korrekt aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LagerPlatz lagerPlatz = new LagerPlatz(id, name, kapazitaet, lager);
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGERPLATZ);
            deo.setCommand(Command.ADD);
            deo.setData(lagerPlatz);

            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                JOptionPane.showMessageDialog(this, "Lagerplatz erfolgreich angelegt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültige ID oder Kapazität. Bitte geben Sie Zahlen ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DocumentFilter für nur Zahlen
    private class NumberOnlyFilter extends DocumentFilter {
        private boolean hasShownWarning = false;
        private String fieldName;

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

    // DocumentFilter für nur Buchstaben und Leerzeichen

public class LetterOnlyFilter extends DocumentFilter {
    private boolean hasShownWarning = false;
    private JTextField textField;

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