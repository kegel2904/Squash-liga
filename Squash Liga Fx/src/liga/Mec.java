package liga;

public class Mec {
	Igrac igrac1;
	Igrac igrac2;
	int rezultatIgrac1;
	int rezultatIgrac2;
	public Igrac getIgrac1() {
		return igrac1;
	}
	public void setIgrac1(Igrac igrac1) {
		this.igrac1 = igrac1;
	}
	public Igrac getIgrac2() {
		return igrac2;
	}
	public void setIgrac2(Igrac igrac2) {
		this.igrac2 = igrac2;
	}
	public int getRezultatIgrac1() {
		return rezultatIgrac1;
	}
	public void setRezultatIgrac1(int rezultatIgrac1) {
		this.rezultatIgrac1 = rezultatIgrac1;
	}
	public int getRezultatIgrac2() {
		return rezultatIgrac2;
	}
	public void setRezultatIgrac2(int rezultatIgrac2) {
		this.rezultatIgrac2 = rezultatIgrac2;
	}
	public Mec(Igrac igrac1, Igrac igrac2, int rezultatIgrac1, int rezultatIgrac2) {
		super();
		this.igrac1 = igrac1;
		this.igrac2 = igrac2;
		this.rezultatIgrac1 = rezultatIgrac1;
		this.rezultatIgrac2 = rezultatIgrac2;
	}
	
}
