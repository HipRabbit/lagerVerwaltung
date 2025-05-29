package thw.edu.javaII.port.warehouse.ui.panels;

import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.model.ReorderTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReorderPage extends JPanel {
    private final ReorderTableModel tableModel;
    private final Session session;

    public ReorderPage(Session session) {
        this.session = session;
        this.tableModel = new ReorderTableModel(session);
        setLayout(new BorderLayout());
        initComponents();
        loadReorders();
    }

    private void initComponents() {
        JLabel title = new JLabel("Nachbestellungen", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Aktualisieren");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReorders();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadReorders() {
        tableModel.setReorders(session.getCommunicator().getAllReorders());
    }
}