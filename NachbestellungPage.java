package thw.edu.javaII.port.warehouse.ui.panels;

import thw.edu.javaII.port.warehouse.model.Nachbestellung;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.model.NachbestellungTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.util.ArrayList;

public class NachbestellungPage extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private NachbestellungTableModel tableModel;
    private Session ses;

    public NachbestellungPage(Session ses) {
        this.ses = ses;
        setLayout(new BorderLayout(0, 0));
        initializeUI();
    }

    private void initializeUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Nachbestellungen");
        lblTitle.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        topPanel.add(lblTitle, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new NachbestellungTableModel(new ArrayList<>()); // Leere Liste initial
        table = new JTable(tableModel);
        tableModel.setJTableColumnsWidth(table, 800, 10, 25, 20, 15, 15, 15, 10);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.DARK_GRAY);

        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Aktualisieren");
        refreshButton.addActionListener(e -> refresh());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refresh(); // Daten nach Initialisierung laden
    }

    public void refresh() {
        tableModel.setData(null);
        tableModel.setData(ses.getCommunicator().getNachbestellungen());
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
            setLayout(new FlowLayout());
            add(new JButton("Bestätigen"));
            add(new JButton("Abbrechen"));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final JPanel panel;
        private Nachbestellung nachbestellung;

        public ButtonEditor() {
            panel = new JPanel(new FlowLayout());
            JButton confirmButton = new JButton("Bestätigen");
            JButton rejectButton = new JButton("Abbrechen");

            confirmButton.addActionListener(e -> {
                int menge = nachbestellung.getLagerPlatz().getKapazitaet(); // Auffüllen auf Kapazität
                boolean success = ses.getCommunicator().confirmNachbestellung(nachbestellung.getId(), menge);
                if (success) {
                    JOptionPane.showMessageDialog(NachbestellungPage.this, "Nachbestellung bestätigt!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(NachbestellungPage.this, "Fehler beim Bestätigen!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                refresh();
                fireEditingStopped();
            });

            rejectButton.addActionListener(e -> {
                boolean success = ses.getCommunicator().rejectNachbestellung(nachbestellung.getId());
                if (success) {
                    JOptionPane.showMessageDialog(NachbestellungPage.this, "Nachbestellung abgelehnt!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(NachbestellungPage.this, "Fehler beim Ablehnen!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                refresh();
                fireEditingStopped();
            });

            panel.add(confirmButton);
            panel.add(rejectButton);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            nachbestellung = tableModel.getObjectAt(row);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}