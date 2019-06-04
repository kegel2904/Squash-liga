package baza;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import com.mysql.*;

public class Baza {
	private static final String DATABASE_FILE = "SQL\\Database.properties";
	
	
	public static Connection spajanjeNaBazu() throws SQLException, IOException {

		

		Properties svojstva = new Properties();

		svojstva.load(new FileReader(DATABASE_FILE));

		String bazaPodatakaUrl = svojstva.getProperty("bazaPodatakaUrl");
		String korisnickoIme = svojstva.getProperty("korisnickoIme");
		String lozinka = svojstva.getProperty("lozinka");

		Connection veza = DriverManager.getConnection(bazaPodatakaUrl, korisnickoIme, lozinka);

		

		return veza;
	}
}
