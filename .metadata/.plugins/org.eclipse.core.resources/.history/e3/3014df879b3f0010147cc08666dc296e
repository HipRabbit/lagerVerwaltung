package thw.edu.javaII.port.warehouse.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import thw.edu.javaII.port.warehouse.model.LagerBestand;
import thw.edu.javaII.port.warehouse.model.LagerPlatz;
import thw.edu.javaII.port.warehouse.model.Produkt;
import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.model.LagerPlatzComboboxModel;

import javax.swing.JComboBox;

public class AddProdukt extends JDialog {

	private static final long serialVersionUID = -8118048952794691740L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtName;
	private JTextField txtHersteller;
	private JTextField txtPreis;
	private JTextField txtBestand;

	/**
	 * Create the dialog.
	 */
	public AddProdukt(Session ses) {
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(450, 300);
		setBounds(ss.width / 2 - frameSize.width / 2, ss.height / 2 - frameSize.height / 2, 450, 365);
		getContentPane().setLayout(new BorderLayout());
		JLabel lblNewLabel = new JLabel("Produkt - hinzufügen");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		contentPanel.setLayout(new MigLayout("", "[99.00][grow][][]", "[][][][][][][][][][]"));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblNewLabel_3 = new JLabel("Name:");
		contentPanel.add(lblNewLabel_3, "cell 0 0,alignx trailing");

		txtName = new JTextField();
		contentPanel.add(txtName, "cell 1 0,growx");
		txtName.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Hersteller:");
		contentPanel.add(lblNewLabel_4, "cell 0 1,alignx trailing");

		txtHersteller = new JTextField();
		contentPanel.add(txtHersteller, "cell 1 1,growx");
		txtHersteller.setColumns(10);

		JLabel lblNewLabel_15 = new JLabel("Einzelpreis:");
		contentPanel.add(lblNewLabel_15, "cell 0 2,alignx trailing");

		txtPreis = new JTextField();
		contentPanel.add(txtPreis, "cell 1 2,growx");
		txtPreis.setColumns(10);

		JLabel lblNewLabel_10 = new JLabel("Bestand:");
		contentPanel.add(lblNewLabel_10, "cell 0 3,alignx trailing");

		txtBestand = new JTextField();
		contentPanel.add(txtBestand, "cell 1 3,growx");
		txtBestand.setColumns(10);

		JLabel lblNewLabel_9 = new JLabel("Lagerplatz:");
		contentPanel.add(lblNewLabel_9, "cell 0 4,alignx trailing");

		LagerPlatzComboboxModel model = new LagerPlatzComboboxModel(ses.getCommunicator().getFreeLagerPlatz());
		JComboBox<LagerPlatz> cbLagerPlatz = new JComboBox<>(model);
		contentPanel.add(cbLagerPlatz, "cell 1 4,growx");

		JButton btnSpeichern = new JButton("speichern");
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Produkt p = new Produkt(0, txtName.getText(), txtHersteller.getText(),
						Double.parseDouble(txtPreis.getText()));
				LagerBestand l = new LagerBestand(0, Integer.parseInt(txtBestand.getText()), p,
						cbLagerPlatz.getModel().getElementAt(cbLagerPlatz.getSelectedIndex()));
				boolean okay = ses.getCommunicator().addProdukt(p,l);
				if(okay) {
					dispose();
				} else {
					/// FIXME ggf. ein ICON einfügen das ein Die Info entsprechend darstellt.
					JOptionPane.showMessageDialog(null, "Fehler beim Speichern. Der Datensatz konnte nicht gespeichert werden",
							"Fehler: Speichern", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		contentPanel.add(btnSpeichern, "cell 2 8");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton btnClose = new JButton("Schließen");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setActionCommand("OK");
		buttonPane.add(btnClose);
	}

}
