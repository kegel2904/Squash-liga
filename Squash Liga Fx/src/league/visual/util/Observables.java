package league.visual.util;

import java.util.Collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Observables {
	private Observables() {}

	public static <T> ObservableList<T> from(Collection<T> list) {
		if (list instanceof ObservableList)
			return (ObservableList<T>) list;
		else
			return FXCollections.observableArrayList(list);
	}
}
