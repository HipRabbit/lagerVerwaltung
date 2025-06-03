package thw.edu.javaII.port.warehouse.ui.panels;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;
import java.awt.Component;

/**
 * Custom TableCellRenderer für Tabellenköpfe mit Sortierungsanzeige.
 *
 * <p>Diese Klasse implementiert TableCellRenderer und erweitert einen bestehenden
 * Renderer um die Funktionalität, Sortierungsindikatoren (Pfeile) in den
 * Spaltenköpfen anzuzeigen, um dem Benutzer die aktuelle Sortierrichtung zu signalisieren.
 *
 * <p>Attribute:
 * <ul>
 *   <li>TableCellRenderer delegate – der ursprüngliche Header-Renderer</li>
 *   <li>int sortColumn – Index der aktuell sortierten Spalte (-1 für keine)</li>
 *   <li>boolean ascending – true für aufsteigende, false für absteigende Sortierung</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>SortIndicatorHeaderRenderer(TableCellRenderer) – Konstruktor mit Delegate-Renderer</li>
 *   <li>setSortColumn(int, boolean) – Setzt die sortierte Spalte und Richtung</li>
 *   <li>getTableCellRendererComponent(...) – Rendert Header-Zelle mit Sortierungsindikator</li>
 * </ul>
 *
 * @author Lennart Höpfner
 */
public class SortIndicatorHeaderRenderer implements TableCellRenderer {
    /** Der ursprüngliche Header-Renderer, der erweitert wird */
    private final TableCellRenderer delegate;
    /** Index der aktuell sortierten Spalte (-1 für keine Sortierung) */
    private int sortColumn = -1;
    /** true für aufsteigende, false für absteigende Sortierung */
    private boolean ascending = true;

    /**
     * Konstruktor erstellt einen neuen Sortierungsindikator-Renderer.
     *
     * @param delegate der ursprüngliche TableCellRenderer, der erweitert werden soll
     */
    public SortIndicatorHeaderRenderer(TableCellRenderer delegate) {
        this.delegate = delegate;
    }

    /**
     * Setzt die aktuell sortierte Spalte und die Sortierrichtung.
     *
     * @param column    Index der sortierten Spalte
     * @param ascending true für aufsteigende, false für absteigende Sortierung
     * @author Lennart Höpfner
     */
    public void setSortColumn(int column, boolean ascending) {
        this.sortColumn = column;
        this.ascending = ascending;
    }

    /**
     * Rendert eine Header-Zelle mit optionalem Sortierungsindikator.
     *
     * @param table      die JTable, für die gerendert wird
     * @param value      der Wert der Zelle (Spaltenname)
     * @param isSelected ob die Zelle ausgewählt ist
     * @param hasFocus   ob die Zelle den Fokus hat
     * @param row        Zeilenindex (bei Headern immer 0)
     * @param column     Spaltenindex
     * @return Component zur Darstellung der Header-Zelle
     * @author Lennart Höpfner
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Delegiere die grundlegende Renderung an den ursprünglichen Renderer
        Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Füge Sortierungsindikator hinzu, falls diese Spalte sortiert ist
        if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            if (column == sortColumn) {
                // Füge Pfeil-Symbol je nach Sortierrichtung hinzu
                label.setText(label.getText() + (ascending ? " ↑" : " ↓"));
            }
        }
        return c;
    }
}
