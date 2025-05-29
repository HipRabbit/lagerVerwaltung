package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import thw.edu.javaII.port.warehouse.model.Lager;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.ui.common.Session;

public class DeleteLager extends JDialog {
    private static final long serialVersionUID = 8L;
    private Session ses;
    private JLabel lblLagerInfo;
    private JButton btnDelete, btnCancel;
    private Lager selectedLager;

    public DeleteLager(Session ses, JFrame parent) {
        super(parent, "Lager löschen", true);
        this.ses = ses;
        this.selectedLager = null;
        initializeUI();
    }

    public void setSelectedLager(Lager lager) {
        this.selectedLager = lager;
        updateFields();
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[right][left, grow]", "[]10[]"));
        setSize(400, 150);
        setLocationRelativeTo(getParent());

        // Lagerinfo anzeigen
        add(new JLabel("Ausgewähltes Lager:"));
        lblLagerInfo = new JLabel("Kein Lager ausgewählt");
        add(lblLagerInfo, "growx");

        // Buttons in einem zentrierten Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnDelete = new JButton("Löschen");
        btnDelete.addActionListener(e -> deleteLager());
        btnCancel = new JButton("Abbrechen");
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCancel);
        add(buttonPanel, "span 2, center");
    }

    private void updateFields() {
        if (selectedLager != null) {
            lblLagerInfo.setText(selectedLager.getName() + " (ID: " + selectedLager.getId() + ")");
        } else {
            lblLagerInfo.setText("Kein Lager ausgewählt");
        }
    }

    private void deleteLager() {
        try {
            if (selectedLager == null) {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Lager aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie das Lager '" + selectedLager.getName() + "' wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.LAGER);
            deo.setCommand(Command.DELETE);
            deo.setData(selectedLager);

            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == thw.edu.javaII.port.warehouse.model.deo.Status.OK) {
                JOptionPane.showMessageDialog(this, "Lager erfolgreich gelöscht.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}