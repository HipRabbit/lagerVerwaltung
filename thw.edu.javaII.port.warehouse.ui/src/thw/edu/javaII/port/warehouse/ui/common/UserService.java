package thw.edu.javaII.port.warehouse.ui.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet Benutzer und überprüft Login-Daten für das Lagerverwaltungssystem.
 *
 * @author Lennart Höpfner
 */
public class UserService {
    private List<User> users;

    /**
     * Konstruktor für den UserService. Initialisiert eine Liste mit vordefinierten Benutzern.
     */
    public UserService() {
        users = new ArrayList<User>();
        users.add(new User("user", "pass"));
        users.add(new User("jsh", "jsh123"));
        users.add(new User("mmn", "mmn123"));
    }

    /**
     * Überprüft die Login-Daten eines Benutzers.
     *
     * @param user der Benutzername
     * @param pass das Passwort
     * @return true, wenn die Login-Daten korrekt sind, sonst false
     */
    public boolean checkLogin(String user, String pass) {
        for (User u : users) {
            if (u.getUserName().equals(user) && u.getPassword().equals(pass)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * Repräsentiert einen Benutzer mit Benutzername und Passwort.
 *
 * @author Lennart Höpfner
 */
class User {
    private String userName;
    private String password;

    /**
     * Gibt den Benutzernamen zurück.
     *
     * @return der Benutzername
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setzt den Benutzernamen.
     *
     * @param userName der neue Benutzername
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gibt das Passwort zurück.
     *
     * @return das Passwort
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt das Passwort.
     *
     * @param password das neue Passwort
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Konstruktor für einen Benutzer.
     *
     * @param userName der Benutzername
     * @param password das Passwort
     */
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}