package thw.edu.javaII.port.warehouse.ui.panels;

import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.ui.common.Communicator;
import thw.edu.javaII.port.warehouse.ui.model.ProduktComboboxModel;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilterPanel extends JPanel {
    private static final long serialVersionUID = 13L;
    private JTextField startDateField, endDateField, minAmountField;
    private JComboBox<Produkt> productCombo;
    private Communicator communicator;
    private FilterListener listener;

    public FilterPanel(Communicator communicator) {
        this.communicator = communicator;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        initializeUI();
    }

    private void initializeUI() {
        // Eingabefelder
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);
        minAmountField = new JTextField(10);
        productCombo = new JComboBox<>(new ProduktComboboxModel(communicator.getProdukte()));
        productCombo.insertItemAt(null, 0); // Option für "Kein Produkt"
        productCombo.setSelectedIndex(0);

        // Filter-Button
        JButton filterButton = new JButton("Filtern");
        filterButton.addActionListener(e -> applyFilter());

        // Reset-Button
        JButton resetButton = new JButton("Zurücksetzen");
        resetButton.addActionListener(e -> resetFilter());

        // Hinzufügen zur UI
        add(new JLabel("Startdatum (dd.MM.yyyy):"));
        add(startDateField);
        add(new JLabel("Enddatum (dd.MM.yyyy):"));
        add(endDateField);
        add(new JLabel("Mindestbetrag (€):"));
        add(minAmountField);
        add(new JLabel("Produkt:"));
        add(productCombo);
        add(filterButton);
        add(resetButton);
    }

    private void applyFilter() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            sdf.setLenient(false);
            Date startDate = startDateField.getText().isEmpty() ? null : sdf.parse(startDateField.getText());
            Date endDate = endDateField.getText().isEmpty() ? null : sdf.parse(endDateField.getText());
            Double minAmount = minAmountField.getText().isEmpty() ? null : Double.parseDouble(minAmountField.getText());
            Integer productId = productCombo.getSelectedItem() != null ? ((Produkt) productCombo.getSelectedItem()).getId() : null;

            if (minAmount != null && minAmount < 0) {
                throw new NumberFormatException("Mindestbetrag darf nicht negativ sein.");
            }
            if (startDate != null && endDate != null && startDate.after(endDate)) {
                throw new IllegalArgumentException("Startdatum muss vor Enddatum liegen.");
            }

            if (listener != null) {
                listener.onFilter(startDate, endDate, minAmount, productId);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ungültiges Datumsformat. Bitte verwenden Sie dd.MM.yyyy.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültiger Betrag: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFilter() {
        startDateField.setText("");
        endDateField.setText("");
        minAmountField.setText("");
        productCombo.setSelectedIndex(0);
        if (listener != null) {
            listener.onReset();
        }
    }

    public void setFilterListener(FilterListener listener) {
        this.listener = listener;
    }

    public interface FilterListener {
        void onFilter(Date startDate, Date endDate, Double minAmount, Integer productId);
        void onReset();
    }
}