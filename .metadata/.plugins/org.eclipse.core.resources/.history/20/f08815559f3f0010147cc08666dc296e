package thw.edu.javaII.port.warehouse.model.common;

import java.util.List;

public class Cast {
	public static <T> T safeCast(Object o, Class<T> clazz) {
		return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
	}

	@SuppressWarnings("unchecked")
	// Es wird geprüft und dann erst gecasted daher eigentlich 99,9% safe
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
