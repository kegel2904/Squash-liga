package league.visual.util;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Stretch {
	private Stretch() {}

	public static void horizontal(Node... nodes) {
		for (Node node : nodes)
			HBox.setHgrow(node, Priority.ALWAYS);
	}

	public static void vertical(Node... nodes) {
		for (Node node : nodes)
			VBox.setVgrow(node, Priority.ALWAYS);
	}

}
