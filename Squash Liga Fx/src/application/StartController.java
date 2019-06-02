package application;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartController {
	
	
	public void registracija() {
		BorderPane root;
		try {
			
			root=(BorderPane) FXMLLoader.load(getClass().getResource("Registracija.fxml"));
			Main.setMainPage(root);
		}catch(IOException e) {
			e.printStackTrace();
		}

}
	
	public void prijava() {
		BorderPane root;
		try {
			
			root=(BorderPane) FXMLLoader.load(getClass().getResource("Prijava.fxml"));
			Main.setMainPage(root);
		}catch(IOException e) {
			e.printStackTrace();
		}

}
}
	
