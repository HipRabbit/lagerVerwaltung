package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.model.BestandTableModel;

public class SearchPage extends JPanel {

    private static final long serialVersionUID = 8512898500044030449L;
    private JTable table;
    private JTextField textField;
    private JScrollPane js;
    private BestandTableModel model;

    public SearchPage(Session ses, JFrame parent) {
        setLayout(new BorderLayout(0, 0));

        JLabel lblNewLabel = new JLabel("Suche");
        lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        add(lblNewLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0 };
        gbl_panel.rowHeights = new int[] { 30, 20 };
        gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 1.0 };
        panel.setLayout(gbl_panel);

        JPanel panel_1 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 0;
        panel.add(panel_1, gbc_panel_1);

        textField = new JTextField();
        panel_1.add(textField);
        textField.setColumns(20);

        JButton btnSearch = new JButton("Suchen");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().length() < 3) {
                    JOptionPane.showMessageDialog(null, "Für eine Suche müssen mindestens 3 Zeichen eingegeben werden!",
                            "Hinweis: Eingabefehler", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    List<LagerBestand> searchBestand = ses.getCommunicator().search(textField.getText());
                    if (searchBestand.size() > 0) {
                        model.setData(searchBestand);
                        model.fireTableDataChanged();
                    }
                }
            }
        });
        panel_1.add(btnSearch);

        JLabel lblNewLabel_1 = new JLabel("Ergebnisse");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 1;
        panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

        model = new BestandTableModel(ses.getCommunicator().getBestand());
        table = new JTable(model);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.DARK_GRAY);
        js = new JScrollPane(table);
        js.setVisible(true);
        GridBagConstraints gbc_table = new GridBagConstraints();
        gbc_table.fill = GridBagConstraints.BOTH;
        gbc_table.gridx = 0;
        gbc_table.gridy = 2;
        gbc_table.weighty = 1.0;
        panel.add(js, gbc_table);
    }
}