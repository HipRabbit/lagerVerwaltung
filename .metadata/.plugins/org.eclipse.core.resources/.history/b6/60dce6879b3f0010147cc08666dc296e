package thw.edu.javaII.port.warehouse.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import thw.edu.javaII.port.warehouse.ui.common.Session;
import thw.edu.javaII.port.warehouse.ui.panels.BestandPage;
import thw.edu.javaII.port.warehouse.ui.panels.InfoPage;
import thw.edu.javaII.port.warehouse.ui.panels.SearchPage;
import thw.edu.javaII.port.warehouse.ui.panels.StatistikPage;
import thw.edu.javaII.port.warehouse.ui.panels.WelcomePage;

public class LagerUIHandler implements ActionListener {
	private Session ses;
	private JFrame frame, parent;
	private JPanel contentPane;

	public LagerUIHandler(Session ses, JFrame frame, JPanel contentPane, JFrame parent) {
		super();
		this.ses = ses;
		this.frame = frame;
		this.contentPane = contentPane;
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MenuActionCommands command = MenuActionCommands.valueOf(e.getActionCommand());
		switch (command) {
		case BEENDEN:
			ses.close();
			frame.dispose();
			break;
		case STARTSEITE:
			BorderLayout layout = (BorderLayout) contentPane.getLayout();
			contentPane.remove(layout.getLayoutComponent(BorderLayout.CENTER));
			contentPane.add(new WelcomePage(), BorderLayout.CENTER);
			frame.setVisible(true);
			break;
		case SERVERBEENDEN:
			ses.getCommunicator().closeServer();
			frame.dispose();
			break;
		case BESTAND:
			layout = (BorderLayout) contentPane.getLayout();
			contentPane.remove(layout.getLayoutComponent(BorderLayout.CENTER));
			contentPane.add(new BestandPage(ses), BorderLayout.CENTER);
			frame.setVisible(true);
			break;
		case STATISTIK:
			 layout = (BorderLayout) contentPane.getLayout();
			contentPane.remove(layout.getLayoutComponent(BorderLayout.CENTER));
			contentPane.add(new StatistikPage(ses, parent), BorderLayout.CENTER);
			frame.setVisible(true);
			break;
		case SUCHEN:
			layout = (BorderLayout) contentPane.getLayout();
			contentPane.remove(layout.getLayoutComponent(BorderLayout.CENTER));
			contentPane.add(new SearchPage(ses, parent), BorderLayout.CENTER);
			frame.setVisible(true);
			break;
		case INFO:
			layout = (BorderLayout) contentPane.getLayout();
			contentPane.remove(layout.getLayoutComponent(BorderLayout.CENTER));
			contentPane.add(new InfoPage(), BorderLayout.CENTER);
			frame.setVisible(true);
			break;
		default:
			break;
		}

	}

}
