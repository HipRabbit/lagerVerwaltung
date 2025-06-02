package thw.edu.javaII.port.warehouse.ui.common;

/**
 * Verwaltet die Benutzersitzung, einschließlich Login-Status, Kommunikator und Benutzerdienst.
 *
 * @author Lennart Höpfner
 */
public class Session {
    private boolean login;
    private Communicator comm;
    private UserService userService;

    /**
     * Konstruktor für eine neue Sitzung. Initialisiert den Login-Status auf false,
     * erstellt einen neuen {@link Communicator} und einen neuen {@link UserService}.
     */
    public Session() {
        login = false;
        comm = new Communicator();
        userService = new UserService();
    }

    /**
     * Gibt den Login-Status der Sitzung zurück.
     *
     * @return true, wenn der Benutzer eingeloggt ist, sonst false
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * Setzt den Login-Status der Sitzung.
     *
     * @param login der neue Login-Status
     */
    public void setLogin(boolean login) {
        this.login = login;
    }

    /**
     * Gibt den Kommunikator für die Serverkommunikation zurück.
     *
     * @return der {@link Communicator}
     */
    public Communicator getCommunicator() {
        return comm;
    }

    /**
     * Gibt den Benutzerdienst für die Authentifizierung zurück.
     *
     * @return der {@link UserService}
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Schließt die Sitzung und die zugehörige Serververbindung.
     */
    public void close() {
        comm.close();
    }
}