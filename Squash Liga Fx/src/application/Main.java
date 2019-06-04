package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	private static Stage stage;
	@Override
	public void start(Stage primaryStage) {
		try {
			stage=primaryStage;
			
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("Start.fxml"));
			Scene scene = new Scene(root,600,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		launch(args);
		
	}
	public static void setMainPage(BorderPane root) {
		Scene scene=new Scene(root,600,500);
		stage.setScene(scene);
		stage.show();
	}
}
