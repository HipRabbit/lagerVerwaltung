package thw.edu.javaII.port.warehouse.ui.panels;

import thw.edu.javaII.port.warehouse.model.Bestellung;
import thw.edu.javaII.port.warehouse.ui.common.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Dialog zur Anzeige einer Bestellungszusammenfassung nach erfolgreichem Abschluss.
 *
 * <p>Diese Klasse erweitert JDialog und zeigt eine übersichtliche Zusammenfassung
 * einer abgeschlossenen Bestellung mit Kundeninformationen, Produktdetails
 * und dem Gesamtpreis an.
 *
 * <p>Attribute:
 * <ul>
 *   <li>static final long serialVersionUID – Versionsnummer für Serialisierung</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>BestellungZusammenfassung(Frame, Session, Bestellung) – Konstruktor mit UI-Initialisierung</li>
 *   <li>Anonyme Klassen für Tabellen-Renderer zur Preisformatierung</li>
 * </ul>
 *
 * @author Bjarne von Appen
 */
public class BestellungZusammenfassung extends JDialog {
    private static final long serialVersionUID = 7L;
    
    /**
     * Konstruktor erstellt den Dialog für die Bestellungszusammenfassung.
     *
     * @param parent     übergeordnetes Fenster
     * @param session    aktuelle Benutzersitzung
     * @param bestellung die anzuzeigende Bestellung
     */
    public BestellungZusammenfassung(Frame parent, Session session, Bestellung bestellung) {
        super(parent, "Bestellungszusammenfassung", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Dankesnachricht oben in zwei Zeilen
        JPanel messagePanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLine1 = new JLabel("Vielen Dank für ihre Bestellung");
        titleLine1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel titleLine2 = new JLabel("Hier sind ihre Bestellungsdetails nochmal zusammengefasst");
        titleLine2.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(titleLine1);
        messagePanel.add(titleLine2);
        add(messagePanel, BorderLayout.NORTH);

        // Kundeninformationen in strukturierter Form
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        infoPanel.add(new JLabel("Bestell-ID:"));
        infoPanel.add(new JLabel(String.valueOf(bestellung.getId())));
        infoPanel.add(new JLabel("Kundenname:"));
        infoPanel.add(new JLabel(bestellung.getKunde() != null ?
                bestellung.getKunde().getVorname() + " " + bestellung.getKunde().getNachname() : "Unbekannt"));
        infoPanel.add(new JLabel("Lieferadresse:"));
        infoPanel.add(new JLabel(bestellung.getKunde() != null ? bestellung.getKunde().getLieferadresse() : "Unbekannt"));
        infoPanel.add(new JLabel("Rechnungsadresse:"));
        infoPanel.add(new JLabel(bestellung.getKunde() != null ? bestellung.getKunde().getRechnungsadresse() : "Unbekannt"));
        add(infoPanel, BorderLayout.CENTER);

        // Produkt-Tabelle mit allen bestellten Artikeln
        String[] columns = {"Hersteller", "Produktname", "Anzahl", "Einzelpreis (EUR)", "Gesamtpreis (EUR)"};
        Object[][] data = bestellung.getProdukte().stream().map(bp -> new Object[]{
                bp.getProdukt().getHersteller(), // Hersteller des Produkts
                bp.getProdukt().getName(),        // Produktname
                bp.getAnzahl(),                   // Bestellte Menge
                new DecimalFormat("#,##0.00").format(bp.getProdukt().getPreis()),                    // Einzelpreis formatiert
                new DecimalFormat("#,##0.00").format(bp.getProdukt().getPreis() * bp.getAnzahl())    // Gesamtpreis formatiert
        }).toArray(Object[][]::new);
        
        JTable table = new JTable(data, columns);
        table.setRowHeight(25);
        
        // Rechtsbündige Ausrichtung für Preisspalten
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() { // Einzelpreis (Index 3)
            private static final long serialVersionUID = -3263019462766882288L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() { // Gesamtpreis (Index 4)
            private static final long serialVersionUID = 69371958212004999L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Zentrales Panel für die Produkttabelle
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Footer mit Gesamtpreis und Schließen-Button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        DecimalFormat df = new DecimalFormat("#,##0.00");
        // Berechnung des Gesamtpreises aller Bestellpositionen
        double total = bestellung.getProdukte().stream()
                .mapToDouble(bp -> bp.getProdukt().getPreis() * bp.getAnzahl())
                .sum();
        JLabel gesamtpreisLabel = new JLabel(String.format("Gesamtpreis: %s EUR", df.format(total)));
        gesamtpreisLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerPanel.add(gesamtpreisLabel);

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Schließen");
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);

        // Kombiniertes Panel für Footer und Schließen-Button
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(footerPanel, BorderLayout.NORTH);
        southPanel.add(closePanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
    }
}
