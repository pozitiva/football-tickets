package poruka;

import java.io.Serializable;

public class Odgovor implements Serializable {

	private boolean uspeh;
	private int karte;
	private int vipKarte;
	private String brojRezervacije;
	private String objasnjenje;

	public Odgovor(boolean uspeh, int karte, int vipKarte) {
		super();
		this.uspeh = uspeh;
		this.karte = karte;
		this.vipKarte = vipKarte;
	}

	public Odgovor() {
		super();
	}

	public boolean isUspeh() {
		return uspeh;
	}

	public void setUspeh(boolean uspeh) {
		this.uspeh = uspeh;
	}

	public int getKarte() {
		return karte;
	}

	public void setKarte(int karte) {
		this.karte = karte;
	}

	public int getVipKarte() {
		return vipKarte;
	}

	public void setVipKarte(int vipKarte) {
		this.vipKarte = vipKarte;
	}

	public String getBrojRezervacije() {
		return brojRezervacije;
	}

	public void setBrojRezervacije(String brojRezervacije) {
		this.brojRezervacije = brojRezervacije;
	}

	public String getObjasnjenje() {
		return objasnjenje;
	}

	public void setObjasnjenje(String objasnjenje) {
		this.objasnjenje = objasnjenje;
	}

}
