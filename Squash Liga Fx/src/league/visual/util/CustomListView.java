package league.visual.util;

import java.util.Collection;
import java.util.function.Function;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class CustomListView<T> extends ListView<T> {

	public static <T> CustomListView<T> newInstance() {
		return new CustomListView<>();
	}

	public static <T> CustomListView<T> newInstance(Collection<T> list) {
		ObservableList<T> obs = Observables.from(list);
		CustomListView<T> custom = new CustomListView<>();
		custom.setItems(obs);
		return custom;
	}

	private CustomListView() {}

	public void display(Function<? super T, ? extends String> function) {
		setCellFactory(x -> new ListCell<T>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty)
					setText(null);
				else {
					String value = function.apply(item);
					setText(value);
				}
			}
		});
	}

	public ReadOnlyObjectProperty<T> selectedItem() {
		return getSelectionModel().selectedItemProperty();
	}

	public ObservableList<T> selectedItems() {
		return getSelectionModel().getSelectedItems();
	}

}
