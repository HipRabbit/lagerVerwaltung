package thw.edu.javaII.port.warehouse.ui.model;

import javax.swing.DefaultComboBoxModel;

import thw.edu.javaII.port.warehouse.model.LagerPlatz;

public class LagerPlatzComboboxModel extends DefaultComboBoxModel<LagerPlatz> {
	private static final long serialVersionUID = 4492813848639274499L;

	public LagerPlatzComboboxModel(LagerPlatz[] items) {
		super(items);
	}

	@Override
	public LagerPlatz getSelectedItem() {
		LagerPlatz selectedJob = (LagerPlatz) super.getSelectedItem();

		return selectedJob;
	}
}
