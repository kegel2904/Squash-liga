package application;

import java.io.IOException;
import java.util.Properties;

import com.mysql.*;
import java.sql.*;
import com.mysql.jdbc.Connection;

import baza.Baza;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class PrijavaController {
	@FXML
	private TextField korisnickoImeUnos;
	@FXML
	private TextField lozinkaUnos;
	
	public void prijaviIgraca() {
		if(provjeriKorisnickoImeILozinka()==true) {
			BorderPane root;
			try {
				
				root=(BorderPane) FXMLLoader.load(getClass().getResource("Liga.fxml"));
				Main.setMainPage(root);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR,
					"Pogrešno korisnièko ime i lozinka!");
			alert.show();
		}
		
	}
	public void registracija() {
		BorderPane root;
		try {
			
			root=(BorderPane) FXMLLoader.load(getClass().getResource("Registracija.fxml"));
			Main.setMainPage(root);
		}catch(IOException e) {
			e.printStackTrace();
		}

}
	private boolean provjeriKorisnickoImeILozinka() {
		int brojac=0;
		Connection connection = null;
		try {
			connection = (Connection) Baza.spajanjeNaBazu();
		} catch (SQLException e1) {
			
		} catch (IOException e1) {
			
		}
		try {
		Statement stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM igraci where korisnickoIme='"+korisnickoImeUnos.getText()+"' AND lozinka='"
	    		+ lozinkaUnos.getText()+"';");
	    while(rs.next()) {
	    	brojac++;
	    	
	    }
		}
		catch(Exception e) {
			
		}
		if (brojac>0){
			return true;
		}
		else {
			return false;
		}
	}
}
