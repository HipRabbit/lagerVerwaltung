package thw.edu.javaII.port.warehouse.ui.panels;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;
import java.awt.Component;

public class SortIndicatorHeaderRenderer implements TableCellRenderer {
    private final TableCellRenderer delegate;
    private int sortColumn = -1;
    private boolean ascending = true;

    public SortIndicatorHeaderRenderer(TableCellRenderer delegate) {
        this.delegate = delegate;
    }

    public void setSortColumn(int column, boolean ascending) {
        this.sortColumn = column;
        this.ascending = ascending;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            if (column == sortColumn) {
                label.setText(label.getText() + (ascending ? " ↑" : " ↓"));
            }
        }
        return c;
    }
}