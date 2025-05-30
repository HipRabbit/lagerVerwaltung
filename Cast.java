package thw.edu.javaII.port.warehouse.model.common;

import java.util.ArrayList;
import java.util.List;



public class Cast {
    public static <T> T safeCast(Object o, Class<T> clazz) {
        return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }

    public static <T> List<T> safeListCast(Object o, Class<T> clazz) {
        if (o == null) {
            System.err.println("Cast.safeListCast: Eingabeobjekt ist null");
            return new ArrayList<>();
        }
        if (!(o instanceof List<?>)) {
            System.err.println("Cast.safeListCast: Eingabeobjekt ist kein List, sondern " + o.getClass().getName());
            return new ArrayList<>();
        }
        List<?> list = (List<?>) o;
        List<T> result = new ArrayList<>();
        if (list.isEmpty()) {
            System.out.println("Cast.safeListCast: Eingabeliste ist leer");
            return result;
        }
        for (Object item : list) {
            if (item == null) {
                System.err.println("Cast.safeListCast: Listenelement ist null");
                continue;
            }
            if (clazz.isInstance(item)) {
                result.add(clazz.cast(item));
            } else {
                System.err.println("Cast.safeListCast: Element kann nicht gecastet werden, Typ: " + item.getClass().getName() + ", erwartet: " + clazz.getName());
            }
        }
        System.out.println("Cast.safeListCast: Erfolgreich " + result.size() + " Elemente gecastet zu " + clazz.getName());
        return result;
    }
}