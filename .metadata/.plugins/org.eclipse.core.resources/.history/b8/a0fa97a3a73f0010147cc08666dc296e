package thw.edu.javaII.port.warehouse.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import thw.edu.javaII.port.warehouse.ui.common.Session;

public class LoginScreenHandler implements ActionListener, KeyListener {
	private Session ses;
	private JFrame frame;
	private JTextField txtUser;
	private JPasswordField txtPassword;
	private JLabel lblHint;

	public LoginScreenHandler(Session ses, JFrame frame, JTextField txtUser, JPasswordField txtPassword,
			JLabel lblHint) {
		this.ses = ses;
		this.frame = frame;
		this.txtUser = txtUser;
		this.txtPassword = txtPassword;
		this.lblHint = lblHint;

	}

	public void actionPerformed(ActionEvent e) {
		checkLogin();
	}

	private void checkLogin() {
		if (txtUser.getText().length() > 0 && String.valueOf(txtPassword.getPassword()).length() > 0) {
			if (ses.getUserService().checkLogin(txtUser.getText(), String.valueOf(txtPassword.getPassword()))) {
				ses.setLogin(true);
				frame.dispose();
			} else {
				lblHint.setText("Logindaten fehlerhaft!");
				txtPassword.setText("");
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			checkLogin();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		;
	}
}
