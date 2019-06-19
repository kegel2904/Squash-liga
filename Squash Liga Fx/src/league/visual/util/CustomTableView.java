package league.visual.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class CustomTableView<T> extends TableView<T> {
	public static <T> CustomTableView<T> newInstance() {
		return new CustomTableView<>();
	}

	public static <T> CustomTableView<T> newInstance(Collection<T> col) {
		CustomTableView<T> table = new CustomTableView<>();
		table.setItems(Observables.from(col));
		return table;
	}

	private CustomTableView() {}

	public <V> TableColumn<T, V> column(String name, Function<? super T, ? extends V> extract) {
		return column(name, extract, String::valueOf);
	}

	public <V> TableColumn<T, V> column(String name, Function<? super T, ? extends V> extract,
			Function<? super V, ? extends String> display) {
		return column(new SimpleStringProperty(name), extract, display);
	}

	public <V> TableColumn<T, V> column(ObservableValue<String> name, Function<? super T, ? extends V> extract) {
		return column(name, extract, String::valueOf);
	}

	public <V> TableColumn<T, V> column(ObservableValue<String> name, Function<? super T, ? extends V> extract,
			Function<? super V, ? extends String> display) {

		TableColumn<T, V> column = new TableColumn<>();
		column.textProperty().bind(name);

		column.setCellValueFactory(x -> new SimpleObjectProperty<>(extract.apply(x.getValue())));
		column.setCellFactory(x -> new TableCell<T, V>() {
			@Override
			protected void updateItem(V item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty)
					setText(null);
				else {
					String value = display.apply(item);
					setText(value);
				}
			}
		});

		getColumns().add(column);
		return column;
	}

	public void distribute(int... percentage) {
		int len = percentage.length;
		if (len != getColumns().size())
			throw new IllegalArgumentException(len + "," + getColumns().size());
		int sum = 0;
		for (int percent : percentage) {
			if (percent < 0)
				throw new IllegalArgumentException(String.valueOf(percent));
			sum += percent;
		}
		if (sum == 0 && len > 0)
			throw new IllegalArgumentException();

		for (int i = 0; i < len; i++)
			getColumns().get(i).prefWidthProperty().bind(widthProperty().multiply((double) percentage[i] / sum));
	}

	public void distribute() {
		int len = getColumns().size();
		int[] percentage = new int[len];
		Arrays.fill(percentage, 1);
		distribute(percentage);
	}

	public ReadOnlyObjectProperty<T> selectedItem() {
		return getSelectionModel().selectedItemProperty();
	}

	public ObservableList<T> selectedItems() {
		return getSelectionModel().getSelectedItems();
	}

	public static void align(TableColumn<?, ?> column, Pos position) {
		alignHelper(column, position);
	}

	private static <T, V> void alignHelper(TableColumn<T, V> column, Pos position) {
		Callback<TableColumn<T, V>, TableCell<T, V>> factory = column.getCellFactory();
		column.setCellFactory(x -> {
			TableCell<T, V> cell = factory.call(x);
			cell.setAlignment(position);
			return cell;
		});
	}

}
