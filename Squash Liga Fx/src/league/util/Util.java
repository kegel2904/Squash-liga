package league.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Util {

	private Util() {}

	public static <T> List<T> protect(Collection<? extends T> list) {
		List<T> copy = new ArrayList<>(list);
		return Collections.unmodifiableList(copy);
	}

	public static <T, V> List<V> map(Collection<? extends T> list, Function<? super T, ? extends V> map) {
		return list.stream().map(map).collect(Collectors.toList());
	}

	public static <K, V> Map<K, List<V>> groupBy(Collection<? extends V> list,
			Function<? super V, ? extends K> map) {
		return list.stream().collect(Collectors.groupingBy(map));
	}

	public static <T> List<T> filter(Collection<? extends T> list, Predicate<? super T> predicate) {
		return list.stream().filter(predicate).collect(Collectors.toList());
	}
}
