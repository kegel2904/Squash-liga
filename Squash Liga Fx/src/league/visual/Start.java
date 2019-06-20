package league.visual;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import league.results.History;
import league.results.XmlInput;
import league.visual.util.Stretch;

public class Start extends Application {

	private History history;

	@Override
	public void init() throws IOException {
		String filename = getParameters().getUnnamed().get(0);
		this.history = XmlInput.read(filename);
	}

	@Override
	public void start(Stage stage) {

		Overview overview = new Overview(history);

		HBox box = new HBox(overview.view());
		Stretch.horizontal(overview.view());

		Scene scene = new Scene(box);
		stage.setScene(scene);
		stage.setTitle("League statistics");
		stage.setWidth(600);
		stage.setHeight(400);

		stage.show();
	}

	public static void start(String file) {
		launch(file);
	}

}
