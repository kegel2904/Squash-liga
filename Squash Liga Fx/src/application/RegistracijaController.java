package application;

import java.io.IOException;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import com.mysql.jdbc.Connection;

import baza.Baza;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class RegistracijaController {
	@FXML
	private TextField imeUnos;
	@FXML
	private TextField prezimeUnos;
	@FXML
	private TextField emailUnos;
	@FXML
	private DatePicker datumUnos;
	@FXML
	private TextField korisnickoImeUnos;
	@FXML
	private PasswordField lozinkaUnos;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy");

	public void registrirajIgraca() {
		datumUnos.setValue(datumUnos.getConverter().fromString(datumUnos.getEditor().getText()));
		if (lozinkaUnos.getText().isEmpty() || imeUnos.getText().isEmpty() || prezimeUnos.getText().isEmpty()
				|| emailUnos.getText().isEmpty() || korisnickoImeUnos.getText().isEmpty()
				|| datumUnos.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR, "Neko od polja je prazno");
			alert.show();
		}

		else {
			if(provjeraPostojecegKorisnickogImena()==false && lozinkaUnos.getText().length()>5) {
				upisIgracaUBazu();
			}
			else if(provjeraPostojecegKorisnickogImena()==true) {
				Alert alert = new Alert(AlertType.ERROR, "Veæ postoji to korisnicko ime, molim odaberite drugo!");
				alert.show();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR, "Lozinka mora imati najmanje 6 znakova!");
				alert.show();
			}

		}
	}

	public void prijava() {
		BorderPane root;
		try {

			root = (BorderPane) FXMLLoader.load(getClass().getResource("Prijava.fxml"));
			Main.setMainPage(root);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void upisIgracaUBazu() {
		Connection connection = null;
		try {
			connection = (Connection) Baza.spajanjeNaBazu();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Isprièavam se, ali trenutno imamo poteškoæa. Isprièavamo se na greški sa naše strane!");
			alert.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Isprièavam se, ali trenutno imamo poteškoæa. Isprièavamo se na greški sa naše strane!");
			alert.show();
		}

		if (connection != null) {
			String query = " INSERT INTO igraci (ime, prezime, datumRodenja, email, korisnickoIme, lozinka)"
					+ " values (?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = null;
			try {
				preparedStmt = connection.prepareStatement(query);
				preparedStmt.setString(1, imeUnos.getText());
				preparedStmt.setString(2, prezimeUnos.getText());
				preparedStmt.setDate(3, Date.valueOf(datumUnos.getValue()));
				preparedStmt.setString(4, emailUnos.getText());
				preparedStmt.setString(5, korisnickoImeUnos.getText());
				preparedStmt.setString(6, lozinkaUnos.getText());
				preparedStmt.execute();
				connection.close();
				Alert alert = new Alert(AlertType.INFORMATION,
						"Èestitamo uspješno ste se prijavili! Voditelj lige æe razmotriti vašu prijavu kroz naredna dva dana!");
				alert.show();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR,
						"Isprièavam se, ali trenutno imamo poteškoæa. Isprièavamo se na greški sa naše strane!");
				alert.show();
			}

		}
	}
	
	private boolean provjeraPostojecegKorisnickogImena() {
		
		int brojac=0;
		Connection connection = null;
		try {
			connection = (Connection) Baza.spajanjeNaBazu();
		} catch (SQLException e1) {
			
		} catch (IOException e1) {
			
		}
		try {
		Statement stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM igraci where korisnickoIme='"+korisnickoImeUnos.getText()+"';");
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
