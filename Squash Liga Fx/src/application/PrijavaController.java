package application;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.*;
import java.sql.*;
import com.mysql.jdbc.Connection;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class PrijavaController {
	
	
	public void registracija() {
		BorderPane root;
		try {
			
			root=(BorderPane) FXMLLoader.load(getClass().getResource("Registracija.fxml"));
			Main.setMainPage(root);
		}catch(IOException e) {
			e.printStackTrace();
		}

}
}
