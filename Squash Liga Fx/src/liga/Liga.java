package liga;

import java.util.List;

public class Liga {
	List<Igrac> igraci;
	List<Mec> mecevi;
	
	public List<Igrac> getIgraci() {
		return igraci;
	}
	public void setIgraci(List<Igrac> igraci) {
		this.igraci = igraci;
	}
	public List<Mec> getMecevi() {
		return mecevi;
	}
	public void setMecevi(List<Mec> mecevi) {
		this.mecevi = mecevi;
	}
	public Liga(List<Igrac> igraci, List<Mec> mecevi) {
		super();
		this.igraci = igraci;
		this.mecevi = mecevi;
	}
	
}
