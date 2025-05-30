package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.ui.common.Session;

public class DeleteLagerPlatz extends JDialog {
    private static final long serialVersionUID = 9L;
    private Session ses;
    private LagerPlatz lagerPlatz;
    private JButton btnDelete, btnCancel;

    public DeleteLagerPlatz(Session ses, JFrame parent) {
        super(parent, "Lagerplatz löschen", true);
        this.ses = ses;
        initializeUI();
    }

    public DeleteLagerPlatz(Session ses, JFrame parent, LagerPlatz lagerPlatz) {
        super(parent, "Lagerplatz löschen", true);
        this.ses = ses;
        this.lagerPlatz = lagerPlatz;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[right][left, grow]", "[]10[]"));
        setSize(400, 150);
        setLocationRelativeTo(getParent());

        // Anzeige des zu löschenden Lagerplatzes
        if (lagerPlatz != null) {
            add(new JLabel("Lagerplatz:"));
            add(new JLabel(lagerPlatz.getName()), "growx");
        }

        // Buttons in einem zentrierten Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnDelete = new JButton("Löschen");
        btnDelete.addActionListener(e -> deleteLagerPlatz());
        btnCancel = new JButton("Abbrechen");
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCancel);
        add(buttonPanel, "span 2, center");
    }

    private void deleteLagerPlatz() {
        try {
            if (lagerPlatz == null) {
                JOptionPane.showMessageDialog(this, "Kein Lagerplatz ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie den Lagerplatz '" + lagerPlatz.getName() + "' wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGERPLATZ);
            deo.setCommand(Command.DELETE);
            deo.setData(lagerPlatz);

            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                JOptionPane.showMessageDialog(this, "Lagerplatz erfolgreich gelöscht.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

}