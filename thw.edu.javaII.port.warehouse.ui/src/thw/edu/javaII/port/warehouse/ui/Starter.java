package thw.edu.javaII.port.warehouse.ui;

import thw.edu.javaII.port.warehouse.ui.common.Session;

/**
 * Startet die Lagerverwaltungs-Benutzeroberfläche, sobald ein Benutzer eingeloggt ist.
 *
 * @author Lennart Höpfner
 */
public class Starter extends Thread {

    private Session ses;

    /**
     * Konstruktor für den Starter.
     *
     * @param ses die {@link Session}, die den Login-Status enthält
     */
    public Starter(Session ses) {
        this.ses = ses;
    }

    /**
     * Wartet, bis der Benutzer eingeloggt ist, und startet dann die {@link LagerUI}.
     */
    @Override
    public void run() {
        while (!ses.isLogin()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LagerUI.run(ses);
    }
}