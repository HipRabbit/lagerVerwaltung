package thw.edu.javaII.port.warehouse.ui.panels;

import thw.edu.javaII.port.warehouse.model.FilterCriteria;
import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.ui.common.Communicator;
import thw.edu.javaII.port.warehouse.ui.model.ProduktComboboxModel;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Panel zur Filterung von Bestellungen nach verschiedenen Kriterien.
 *
 * <p>Diese Klasse erweitert JPanel und bietet eine Benutzeroberfläche
 * zur Filterung von Bestellungen nach Datum, Mindestbetrag und Produkten
 * mit Eingabevalidierung und Fehlerbehandlung.
 *
 * <p>Attribute:
 * <ul>
 *   <li>JTextField startDateField – Eingabefeld für Startdatum</li>
 *   <li>JTextField endDateField – Eingabefeld für Enddatum</li>
 *   <li>JTextField minAmountField – Eingabefeld für Mindestbetrag</li>
 *   <li>JComboBox<Produkt> productCombo – Dropdown für Produktauswahl</li>
 *   <li>Communicator communicator – Kommunikation mit dem Server</li>
 *   <li>FilterListener listener – Callback für Filteränderungen</li>
 *   <li>Date currentStartDate – aktuelles Startdatum</li>
 *   <li>Date currentEndDate – aktuelles Enddatum</li>
 *   <li>Double currentMinAmount – aktueller Mindestbetrag</li>
 *   <li>Integer currentProductId – aktuelle Produkt-ID</li>
 * </ul>
 *
 * <p>Wesentliche Methoden:
 * <ul>
 *   <li>FilterPanel(Communicator) – Konstruktor mit UI-Initialisierung</li>
 *   <li>initializeUI() – Erstellt die Benutzeroberfläche</li>
 *   <li>applyFilter() – Wendet die eingegebenen Filter an</li>
 *   <li>resetFilter() – Setzt alle Filter zurück</li>
 *   <li>setFilterListener(FilterListener) – Setzt den Filter-Listener</li>
 *   <li>getCurrentFilterCriteria() – Gibt aktuelle Filterkriterien zurück</li>
 *   <li>showCenteredMessageDialog(String, String) – Zeigt zentrierte Fehlermeldungen</li>
 * </ul>
 *
 * @author Bjarne von Appen
 * @author Lennart Höpfner
 */
public class FilterPanel extends JPanel {
    private static final long serialVersionUID = 13L;
    
    /** Eingabefelder für Filterkriterien */
    private JTextField startDateField, endDateField, minAmountField;
    /** Dropdown für Produktauswahl */
    private JComboBox<Produkt> productCombo;
    /** Kommunikation mit dem Server */
    private Communicator communicator;
    /** Callback für Filteränderungen */
    private FilterListener listener;
    
    // Aktuelle Filterkriterien
    /** Aktuelles Startdatum */
    private Date currentStartDate;
    /** Aktuelles Enddatum */
    private Date currentEndDate;
    /** Aktueller Mindestbetrag */
    private Double currentMinAmount;
    /** Aktuelle Produkt-ID */
    private Integer currentProductId;

    /**
     * Konstruktor erstellt das Filter-Panel.
     *
     * @param communicator Kommunikator für Server-Verbindung
     */
    public FilterPanel(Communicator communicator) {
        this.communicator = communicator;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        initializeUI();
    }

    /**
     * Initialisiert die Benutzeroberfläche des Filter-Panels.
     *
     * @author Bjarne von Appen
     */
    private void initializeUI() {
        // Eingabefelder für Filterkriterien
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);
        minAmountField = new JTextField(10);
        
        // Produkt-Dropdown mit null-Option für "Alle Produkte"
        productCombo = new JComboBox<>(new ProduktComboboxModel(communicator.getProdukte()));
        productCombo.insertItemAt(null, 0); // "Alle Produkte" Option
        productCombo.setSelectedIndex(0);

        // Action-Buttons
        JButton filterButton = new JButton("Filtern");
        filterButton.addActionListener(e -> applyFilter());

        JButton resetButton = new JButton("Zurücksetzen");
        resetButton.addActionListener(e -> resetFilter());

        // UI-Komponenten zum Panel hinzufügen
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

    /**
     * Wendet die eingegebenen Filterkriterien an und validiert die Eingaben.
     *
     * @author Bjarne von Appen
     */
    private void applyFilter() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            sdf.setLenient(false); // Strikte Datumsvalidierung
            
            // Parse Eingabefelder
            currentStartDate = startDateField.getText().isEmpty() ? null : sdf.parse(startDateField.getText());
            currentEndDate = endDateField.getText().isEmpty() ? null : sdf.parse(endDateField.getText());
            currentMinAmount = minAmountField.getText().isEmpty() ? null : Double.parseDouble(minAmountField.getText());
            currentProductId = productCombo.getSelectedItem() != null ? ((Produkt) productCombo.getSelectedItem()).getId() : null;

            // Validierung der Eingaben
            if (currentMinAmount != null && currentMinAmount < 0) {
                throw new NumberFormatException("Mindestbetrag darf nicht negativ sein.");
            }
            if (currentStartDate != null && currentEndDate != null && currentStartDate.after(currentEndDate)) {
                throw new IllegalArgumentException("Startdatum muss vor Enddatum liegen.");
            }

            // Filter anwenden über Listener
            if (listener != null) {
                listener.onFilter(currentStartDate, currentEndDate, currentMinAmount, currentProductId);
            }
        } catch (ParseException e) {
            showCenteredMessageDialog("Ungültiges Datumsformat. Bitte verwenden Sie dd.MM.yyyy.", "Fehler");
        } catch (NumberFormatException e) {
            showCenteredMessageDialog("Ungültiger Betrag. Bitte verwenden sie einen gültigen Betrag", "Fehler");
        } catch (IllegalArgumentException e) {
            showCenteredMessageDialog(e.getMessage(), "Fehler");
        }
    }

    /**
     * Setzt alle Filterkriterien zurück und benachrichtigt den Listener.
     *
     * @author Lennart Höpfner
     */
    private void resetFilter() {
        // Eingabefelder leeren
        startDateField.setText("");
        endDateField.setText("");
        minAmountField.setText("");
        productCombo.setSelectedIndex(0); // "Alle Produkte" auswählen
        
        // Filterkriterien zurücksetzen
        currentStartDate = null;
        currentEndDate = null;
        currentMinAmount = null;
        currentProductId = null;
        
        // Reset über Listener mitteilen
        if (listener != null) {
            listener.onReset();
        }
    }

    /**
     * Zeigt eine zentrierte Fehlermeldung als Dialog an.
     *
     * @param message Fehlermeldung
     * @param title   Titel des Dialogs
     * @author Lennart Höpfner
     */
    private void showCenteredMessageDialog(String message, String title) {
        JDialog dialog = new JDialog((JFrame) null, title, true);
        JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{"OK"});
        
        // Aktion für den OK-Button hinzufügen
        pane.addPropertyChangeListener(evt -> {
            if (JOptionPane.VALUE_PROPERTY.equals(evt.getPropertyName())) {
                dialog.dispose();
            }
        });

        dialog.setContentPane(pane);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();

        // Dialog auf dem Bildschirm zentrieren
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = dialog.getSize();
        int x = (screenSize.width - dialogSize.width) / 2;
        int y = (screenSize.height - dialogSize.height) / 2;
        dialog.setLocation(x, y);

        dialog.setVisible(true);
    }

    /**
     * Setzt den Filter-Listener für Callback-Benachrichtigungen.
     *
     * @param listener der zu setzende Filter-Listener
     */
    public void setFilterListener(FilterListener listener) {
        this.listener = listener;
    }

    /**
     * Gibt die aktuellen Filterkriterien als FilterCriteria-Objekt zurück.
     *
     * @return aktuelle Filterkriterien
     * @author Bjarne von Appen
     */
    public FilterCriteria getCurrentFilterCriteria() {
        return new FilterCriteria(currentStartDate, currentEndDate, currentMinAmount, currentProductId);
    }

    /**
     * Interface für Filter-Ereignisse.
     * Implementierende Klassen werden über Filteränderungen benachrichtigt.
     *
     * @author Bjarne von Appen
     * @author Lennart Höpfner
     */
    public interface FilterListener {
        /**
         * Wird aufgerufen, wenn Filter angewendet werden.
         *
         * @param startDate Startdatum für Filter
         * @param endDate   Enddatum für Filter
         * @param minAmount Mindestbetrag für Filter
         * @param productId Produkt-ID für Filter
         */
        void onFilter(Date startDate, Date endDate, Double minAmount, Integer productId);
        
        /**
         * Wird aufgerufen, wenn Filter zurückgesetzt werden.
         */
        void onReset();
    }
}
