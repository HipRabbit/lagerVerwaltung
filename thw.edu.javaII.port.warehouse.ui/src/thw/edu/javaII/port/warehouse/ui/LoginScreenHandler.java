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

/**
 * Verarbeitet Benutzeraktionen für den Login-Bildschirm.
 *
 * @author Lennart Höpfner
 */
public class LoginScreenHandler implements ActionListener, KeyListener {
    private Session ses;
    private JFrame frame;
    private JTextField txtUser;
    private JPasswordField txtPassword;
    private JLabel lblHint;

    /**
     * Konstruktor für den LoginScreenHandler.
     *
     * @param ses die {@link Session} für die Benutzer-Authentifizierung
     * @param frame das {@link JFrame} des Login-Bildschirms
     * @param txtUser das Textfeld für den Benutzernamen
     * @param txtPassword das Passwortfeld
     * @param lblHint das Label für Fehlermeldungen
     */
    public LoginScreenHandler(Session ses, JFrame frame, JTextField txtUser, JPasswordField txtPassword,
            JLabel lblHint) {
        this.ses = ses;
        this.frame = frame;
        this.txtUser = txtUser;
        this.txtPassword = txtPassword;
        this.lblHint = lblHint;
    }

    /**
     * Verarbeitet Aktionen wie den Klick auf den Login-Button.
     *
     * @param e das {@link ActionEvent}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        checkLogin();
    }

    /**
     * Überprüft die eingegebenen Login-Daten und aktualisiert die Sitzung oder zeigt eine Fehlermeldung an.
     */
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

    /**
     * Verarbeitet Tastatureingaben, insbesondere die Enter-Taste, um den Login auszulösen.
     *
     * @param e das {@link KeyEvent}
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            checkLogin();
        }
    }

    /**
     * Wird nicht verwendet.
     *
     * @param e das {@link KeyEvent}
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Nicht implementiert
    }

    /**
     * Wird nicht verwendet.
     *
     * @param e das {@link KeyEvent}
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Nicht implementiert
    }
}