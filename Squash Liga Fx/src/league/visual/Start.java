package league.visual;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import league.results.History;
import league.results.PointCriteria;
import league.results.PointCriteria.Item;
import league.results.XmlInput;

public class Start extends Application {

	private History history;

	@Override
	public void init() throws IOException {
		String filename = getParameters().getUnnamed().get(0);
		this.history = XmlInput.read(filename);
	}

	@Override
	public void start(Stage stage) {

		PointCriteria criteria = PointCriteria.newBuilder()
				.set(Item.GAME_WON, 1)
				.set(Item.MATCH_PLAYED, 1)
				.set(Item.MATCH_WON, 1)
				.build();

		Overview overview = new Overview(history, criteria);

		Scene scene = new Scene((Parent) overview.view());
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
