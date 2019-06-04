package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import liga.Igrac;

public class LigaController implements Initializable {
	@FXML
	private TableView<Igrac> myTable;
	private List<Igrac> igraci;

	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Connection connection=null;
		igraci=new ArrayList<Igrac>();
		
		try {
			connection=baza.Baza.spajanjeNaBazu();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Eror ucitavanje baze!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs=stmt.executeQuery("Select * from igraci");
			System.out.println("Ajmooo");
			while(rs.next()) {
				Igrac noviIgrac=new Igrac(rs.getObject("ime").toString(),rs.getObject("prezime").toString());
				igraci.add(noviIgrac);
				
			}
			for(int i=0;i<igraci.size();i++) {
				System.out.println(igraci.get(i).getIme()+" "+igraci.get(i).getPrezime());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Eror ucitavanje podataka!");
		}
		
	
}

}
