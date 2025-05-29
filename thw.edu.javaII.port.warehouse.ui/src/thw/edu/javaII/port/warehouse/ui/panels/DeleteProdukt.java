package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseDEO;
import thw.edu.javaII.port.warehouse.model.deo.WarehouseReturnDEO;
import thw.edu.javaII.port.warehouse.model.deo.Zone;
import thw.edu.javaII.port.warehouse.model.deo.Command;
import thw.edu.javaII.port.warehouse.model.deo.Status;
import thw.edu.javaII.port.warehouse.model.common.Cast;
import thw.edu.javaII.port.warehouse.ui.common.Session;

public class DeleteProdukt extends JDialog {
    private static final long serialVersionUID = 10L;
    private Session ses;
    private Produkt produkt;
    private JButton btnDelete, btnCancel;

    public DeleteProdukt(Session ses, JFrame parent, Produkt produkt) {
        super(parent, "Produkt löschen", true);
        this.ses = ses;
        this.produkt = produkt;
        initializeUI();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new MigLayout("fill, wrap 2", "[right][left, grow]", "[]10[]"));
        setSize(400, 150);

        add(new JLabel("Produkt:"));
        add(new JLabel(produkt != null ? produkt.getName() : "Kein Produkt ausgewählt"));

        // Buttons in einem zentrierten Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnDelete = new JButton("Löschen");
        btnDelete.addActionListener(e -> deleteProdukt());
        btnCancel = new JButton("Abbrechen");
        btnCancel.addActionListener(e -> dispose());
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCancel);
        add(buttonPanel, "span 2, center");

        if (produkt == null) {
            btnDelete.setEnabled(false);
        }
    }

    private void deleteProdukt() {
        try {
            if (produkt == null) {
                JOptionPane.showMessageDialog(this, "Kein Produkt ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Prüfen, ob das Produkt in Bestellungen verwendet wird
            WarehouseDEO deo = new WarehouseDEO();
            deo.setZone(Zone.BESTELLUNG);
            deo.setCommand(Command.LIST);
            WarehouseReturnDEO ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == Status.OK) {
                List<Bestellung> bestellungList = Cast.safeListCast(ret.getData(), Bestellung.class);
                if (bestellungList != null) {
                    List<Bestellung> blockingOrders = bestellungList.stream()
                            .filter(b -> b.getProdukte().stream()
                                    .anyMatch(bp -> bp.getProdukt().getId() == produkt.getId()))
                            .collect(Collectors.toList());
                    if (!blockingOrders.isEmpty()) {
                        String orderIds = blockingOrders.stream()
                                .map(b -> String.valueOf(b.getId()))
                                .collect(Collectors.joining(", "));
                        String message = "Produkt kann nicht gelöscht werden, da es in Bestellungen verwendet wird (Bestell-IDs: " + orderIds + ").";
                        System.out.println("Produkt " + produkt.getName() + " (ID: " + produkt.getId() + ") wird in Bestellungen verwendet: " + orderIds);
                        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } else {
                System.err.println("Fehler beim Abrufen der Bestellungen: " + ret.getMessage());
                JOptionPane.showMessageDialog(this, "Fehler beim Überprüfen der Bestellungen: " + ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Prüfen, ob das Produkt in Lagerbeständen verwendet wird, und diese löschen
            deo = new WarehouseDEO();
            deo.setZone(Zone.LAGERBESTAND);
            deo.setCommand(Command.LIST);
            ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == Status.OK) {
                List<LagerBestand> bestandList = Cast.safeListCast(ret.getData(), LagerBestand.class);
                if (bestandList != null) {
                    List<LagerBestand> toDelete = bestandList.stream()
                            .filter(b -> b.getProdukt_id().getId() == produkt.getId())
                            .collect(Collectors.toList());
                    System.out.println("Lagerbestand-Einträge für Produkt " + produkt.getName() + " (ID: " + produkt.getId() + ") gefunden: " + toDelete.size());
                    for (LagerBestand bestand : toDelete) {
                        WarehouseDEO deleteDeo = new WarehouseDEO();
                        deleteDeo.setZone(Zone.LAGERBESTAND);
                        deleteDeo.setCommand(Command.DELETE);
                        deleteDeo.setData(bestand);
                        WarehouseReturnDEO deleteRet = ses.getCommunicator().sendRequest(deleteDeo);
                        if (deleteRet.getStatus() != Status.OK) {
                            System.err.println("Fehler beim Löschen des Lagerbestands (ID: " + bestand.getId() + "): " + deleteRet.getMessage());
                            JOptionPane.showMessageDialog(this, "Fehler beim Löschen des Lagerbestands: " + deleteRet.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        System.out.println("Lagerbestand-Eintrag (ID: " + bestand.getId() + ") für Produkt " + produkt.getName() + " erfolgreich gelöscht.");
                    }
                }
            } else {
                System.err.println("Fehler beim Abrufen der Lagerbestände: " + ret.getMessage());
                JOptionPane.showMessageDialog(this, "Fehler beim Überprüfen der Lagerbestände: " + ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie das Produkt '" + produkt.getName() + "' wirklich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                System.out.println("Löschvorgang für Produkt " + produkt.getName() + " (ID: " + produkt.getId() + ") abgebrochen.");
                return;
            }

            // Produkt löschen
            deo = new WarehouseDEO();
            deo.setZone(Zone.PRODUKT);
            deo.setCommand(Command.DELETE);
            deo.setData(produkt);
            ret = ses.getCommunicator().sendRequest(deo);
            if (ret.getStatus() == Status.OK) {
                System.out.println("Produkt " + produkt.getName() + " (ID: " + produkt.getId() + ") erfolgreich gelöscht.");
                JOptionPane.showMessageDialog(this, "Produkt erfolgreich gelöscht.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                System.err.println("Fehler beim Löschen des Produkts: " + ret.getMessage());
                JOptionPane.showMessageDialog(this, ret.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Unerwarteter Fehler beim Löschen des Produkts: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}