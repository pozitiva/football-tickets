package poruka;

import java.io.Serializable;

public class Poruka implements Serializable {
	
	private Operacija operacija;
	private String username;
	private String password;
	private String ime;
	private String prezime;
	private String jmbg;
	private String email;
	private int karte;
	private int vipKarte;
	
	public Poruka() {
		// TODO Auto-generated constructor stub
	}
	
	public Poruka(Operacija operacija, String username, String password, String ime, String prezime, String jmbg,
			String email, int karte, int vipKarte) {
		super();
		this.operacija = operacija;
		this.username = username;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.jmbg = jmbg;
		this.email = email;
		this.karte = karte;
		this.vipKarte = vipKarte;
	}


	public Operacija getOperacija() {
		return operacija;
	}
	public void setOperacija(Operacija operacija) {
		this.operacija = operacija;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
	public String getJmbg() {
		return jmbg;
	}
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	
	
}
