package liga;

public class Igrac {
	String ime;
	String prezime;
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public Igrac(String ime, String prezime) {
		super();
		this.ime = ime;
		this.prezime = prezime;
	}
	
}
