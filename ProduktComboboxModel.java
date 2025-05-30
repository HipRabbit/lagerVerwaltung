package thw.edu.javaII.port.warehouse.ui.model;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import thw.edu.javaII.port.warehouse.model.Produkt;

public class ProduktComboboxModel extends DefaultComboBoxModel<Produkt> {
    private static final long serialVersionUID = 1L;

    public ProduktComboboxModel(List<Produkt> produkte) {
        for (Produkt produkt : produkte) {
            addElement(produkt);
        }
    }
}