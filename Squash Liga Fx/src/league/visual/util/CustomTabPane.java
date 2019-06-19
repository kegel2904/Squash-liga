package league.visual.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class CustomTabPane extends TabPane {

	public CustomTabPane() {
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}

	public Tab tab(String name, Node node) {
		return tab(new SimpleStringProperty(name), node);
	}

	public Tab tab(ObservableValue<String> name, Node node) {
		Tab tab = new Tab();
		tab.textProperty().bind(name);
		tab.setContent(node);
		getTabs().add(tab);
		return tab;
	}

}
