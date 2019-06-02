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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
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
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy");
		 
	public void registrirajIgraca() {
		datumUnos.setValue(datumUnos.getConverter()
			    .fromString(datumUnos.getEditor().getText()));
		if(imeUnos.getText().isEmpty() || prezimeUnos.getText().isEmpty() || emailUnos.getText().isEmpty() || korisnickoImeUnos.getText().isEmpty() || datumUnos.getValue()==null) {
			Alert alert=new Alert(AlertType.ERROR, "Neko od polja je prazno");
			alert.show();
		}
		else {
			Connection connection=null;
			try {
				connection=getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(connection!=null) {
				String query = " INSERT INTO igraci (ime, prezime, datumRodenja, email, korisnickoIme)"
				        + " values (?, ?, ?, ?, ?)";
				PreparedStatement preparedStmt = null;
				try {
					preparedStmt = connection.prepareStatement(query);
					preparedStmt.setString (1, imeUnos.getText());
				    preparedStmt.setString (2, prezimeUnos.getText());
				    preparedStmt.setDate   (3, Date.valueOf(datumUnos.getValue()));
				    preparedStmt.setString(4, emailUnos.getText());
				    preparedStmt.setString    (5, korisnickoImeUnos.getText());
				    preparedStmt.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
	public Connection getConnection() throws SQLException {

	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", "root");
	    connectionProps.put("password", "");

	    
	        conn = (Connection) DriverManager.getConnection(
	                   "jdbc:mysql://localhost:3306/squashliga",
	                   connectionProps);
	    if(conn != null) {
	    System.out.println("Connected to database");
	    }
	    return conn;
	    
	}
}
