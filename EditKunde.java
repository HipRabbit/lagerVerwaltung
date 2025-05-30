package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import net.miginfocom.swing.MigLayout;
import thw.edu.javaII.port.warehouse.model.Kunde;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.model.deo.Status;
import thw.edu.javaII.port.warehouse.ui.common.Session;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditKunde extends JDialog {
    private static final long serialVersionUID = 11L;
    private Session ses;
    private JTextField txtVorname, txtNachname, txtLieferadresse, txtRechnungsadresse;
    private JButton btnSave, btnCancel;
    private Kunde kunde;
    private Kunde savedKunde;
    private boolean canceled = false;

    public EditKunde(Session ses, JFrame parent, Kunde kunde) {
        super(parent, kunde == null ? "Kunde hinzufügen" : "Kunde bearbeiten", true);
        this.ses = ses;
        this.kunde = kunde;
        this.savedKunde = null;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                canceled = true;
                dispose();
            }
        });

        initializeUI();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[right][grow]", ""));
        setSize(400, 250);

        // Filter für alle Textfelder erstellen


        add(new JLabel("Vorname:"));
        txtVorname = new JTextField(kunde != null ? kunde.getVorname() : "", 20);
        ((AbstractDocument) txtVorname.getDocument()).setDocumentFilter(new AlphanumericFilter(txtVorname));
        add(txtVorname, "growx");

        add(new JLabel("Nachname:"));
        txtNachname = new JTextField(kunde != null ? kunde.getNachname() : "", 20);
        ((AbstractDocument) txtNachname.getDocument()).setDocumentFilter(new AlphanumericFilter(txtNachname));
        add(txtNachname, "growx");

        add(new JLabel("Lieferadresse:"));
        txtLieferadresse = new JTextField(kunde != null ? kunde.getLieferadresse() : "", 20);
        ((AbstractDocument) txtLieferadresse.getDocument()).setDocumentFilter(new AlphanumericFilter(txtLieferadresse));
        add(txtLieferadresse, "growx");

        add(new JLabel("Rechnungsadresse:"));
        txtRechnungsadresse = new JTextField(kunde != null ? kunde.getRechnungsadresse() : "", 20);
        ((AbstractDocument) txtRechnungsadresse.getDocument()).setDocumentFilter(new AlphanumericFilter(txtRechnungsadresse));
        add(txtRechnungsadresse, "growx");

        // Button-Panel mit zentrierter Ausrichtung
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Speichern");
        btnCancel = new JButton("Abbrechen");
        JButton btnCopyAddress = new JButton("Adresse kopieren");
        buttonPanel.add(btnCopyAddress);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, "span 2, align center, gapy 10");

        // Action Listener
        btnCopyAddress.addActionListener(e -> {
            String lieferadresse = txtLieferadresse.getText().trim();
            txtRechnungsadresse.setText(lieferadresse);
        });
        btnSave.addActionListener(e -> saveKunde());
        btnCancel.addActionListener(e -> {
            canceled = true;
            dispose();
        });
    }

    private void saveKunde() {
        try {
            String vorname = txtVorname.getText().trim();
            String nachname = txtNachname.getText().trim();
            String lieferadresse = txtLieferadresse.getText().trim();
            String rechnungsadresse = txtRechnungsadresse.getText().trim();

            if (vorname.isEmpty() || nachname.isEmpty() || lieferadresse.isEmpty() || rechnungsadresse.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (kunde == null) {
                int id = ses.getCommunicator().getNextKundeId();
                if (id == -1) {
                    JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der nächsten Kunden-ID.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                kunde = new Kunde(id, vorname, nachname, lieferadresse, rechnungsadresse);
                boolean success = ses.getCommunicator().addKunde(kunde);
                if (success) {
                    savedKunde = kunde;
                    JOptionPane.showMessageDialog(this, "Kunde erfolgreich hinzugefügt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Fehler beim Hinzufügen des Kunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                kunde.setVorname(vorname);
                kunde.setNachname(nachname);
                kunde.setLieferadresse(lieferadresse);
                kunde.setRechnungsadresse(rechnungsadresse);

                WarehouseDEO deo = new WarehouseDEO();
                deo.setZone(Zone.KUNDE);
                deo.setCommand(Command.UPDATE);
                deo.setData(kunde);

                WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
                if (ret.getStatus() == Status.OK) {
                    savedKunde = kunde;
                    JOptionPane.showMessageDialog(this, "Kunde erfolgreich aktualisiert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Kunde getSavedKunde() {
        return savedKunde;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public class AlphanumericFilter extends DocumentFilter {
        private boolean hasShownWarning = false;
        private JTextField textField;

        public AlphanumericFilter(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }

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
            if (string == null) {
                return;
            }

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