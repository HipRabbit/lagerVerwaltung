package thw.edu.javaII.port.warehouse.model.common;

import java.util.List;

/**
 * Bietet Hilfsmethoden für sicheres Casting von Objekten und Listen in spezifische Typen.
 * 
 * @author Lennart Höpfner
 */
public class Cast {
    /**
     * Führt ein sicheres Casting eines Objekts in den angegebenen Typ durch.
     * 
     * @param <T> der Zieltyp
     * @param o das zu castende Objekt
     * @param clazz die Zielklasse
     * @return das gecastete Objekt oder {@code null}, wenn das Casting nicht möglich ist
     */
    public static <T> T safeCast(Object o, Class<T> clazz) {
        return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }

    /**
     * Führt ein sicheres Casting einer Liste in eine Liste des angegebenen Typs durch.
     * 
     * @param <T> der Zieltyp der Listenelemente
     * @param o das zu castende Objekt (erwartet eine Liste)
     * @param clazz die Zielklasse der Listenelemente
     * @return die gecastete Liste oder {@code null}, wenn das Casting nicht möglich ist
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> safeListCast(Object o, Class<T> clazz) {
        if (clazz != null) {
            if (o instanceof List<?>) {
                List<?> list = (List<?>) o;
                if (list.size() > 0) {
                    if (list.get(0).getClass().equals(clazz)) {
                        return (List<T>) o;
                    }
                }
            }
        }
        return null;
    }
}