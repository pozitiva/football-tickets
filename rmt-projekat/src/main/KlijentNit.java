package main;

import java.io.File;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.swing.JOptionPane;

import poruka.Odgovor;
import poruka.Poruka;

public class KlijentNit extends Thread {

	private Socket socket;
	ObjectInputStream ulaz;
	ObjectOutputStream izlaz;
	Connection kon;

	public KlijentNit(Socket socket) {
		this.socket = socket;
		povezivanjeNaBazu();
	}

	@Override
	public void run() {

		while (!interrupted()) {
			try {
				// primi zahtev tj. poruku
				ulaz = new ObjectInputStream(socket.getInputStream());
				Poruka poruka = (Poruka) ulaz.readObject();

				Odgovor odgovor = new Odgovor();
				// obradi poruku
				switch (poruka.getOperacija()) {
				case REGISTRACIJA:
					registrovanjeKorisnika(odgovor, poruka);
					break;
				case LOGIN:
					login(odgovor, poruka);
					break;
				case BRISANJE:
					brisanjeRezervacije(odgovor, poruka);
					break;
				case REZERVACIJA:
					rezervacijaKarata(odgovor, poruka);
					break;
				case VRATI_BROJ_KARATA:
					vracanjeBrKarata(odgovor);
					break;
				case VRATI_BROJ_KARATA_KORISNIKA:
					vracanjeBrKarataKorisnika(odgovor, poruka);
					break;
				}

				// posaljemo odgovor
				izlaz = new ObjectOutputStream(socket.getOutputStream());
				izlaz.writeObject(odgovor);

			} catch (Exception e) {
//				System.out.println("desila se greska");
				e.printStackTrace();
				interrupt();
			}

		}
	}

	private void povezivanjeNaBazu() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			if (kon == null || kon.isClosed()) {
				kon = DriverManager.getConnection("jdbc:mysql://localhost:3306/rmt", "root", "");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void rezervacijaKarata(Odgovor odgovor, Poruka poruka) throws Exception {
		// provera broja raspolozivih karata

		// uzmi iz baze samo karte
		String upit = "SELECT karte,vipKarte FROM korisnik";
		Statement s = kon.createStatement();
		ResultSet rs = s.executeQuery(upit);

		// saberi trenutno sve rezervisane karte
		int zbirKarata = 0;
		int zbirVipKarata = 0;
		while (rs.next()) {
			int karte = rs.getInt("karte");
			int vipkarte = rs.getInt("vipkarte");
			zbirKarata += karte;
			zbirVipKarata += vipkarte;
		}

		// uporedi sa kapacitetom
		// ako je popunjeno onda prekidam

		if (zbirKarata + poruka.getKarte() > 20 || zbirVipKarata + poruka.getVipKarte() > 5) {
			odgovor.setUspeh(false);
			return;
		}

		// proveri karte tog korisnika
		// uzmi iz baze karte samo ovog korisnika
		upit = "SELECT * FROM korisnik WHERE username= '" + poruka.getUsername() + "'";
		rs = s.executeQuery(upit);

		rs.next();
		int karteKorisnika = rs.getInt("karte");
		int vipKarteKorisnika = rs.getInt("vipkarte");
		String email = rs.getString("email");
		String ime = rs.getString("ime");
		String prezime = rs.getString("prezime");
		String jmbg = rs.getString("jmbg");

		// uporedi sa kapacitetom za jednu osobu
		// ako je prekoracio onda prekidam

		// obavljamo rezervaciju

		// menjamo podatake o kartama za tog korisnika u bazi
		int noveKarte = karteKorisnika + poruka.getKarte();
		int noveVipKarte = vipKarteKorisnika + poruka.getVipKarte();

		if (noveKarte + noveVipKarte > 4) {
			odgovor.setUspeh(false);
			return;
		}
		upit = "UPDATE korisnik SET karte=" + noveKarte + ", vipkarte= " + noveVipKarte + " WHERE username= '"
				+ poruka.getUsername() + "'";

		int uspeh = s.executeUpdate(upit);
		if (uspeh != 1) {
			odgovor.setUspeh(false);
		} else {
			potvrdaRezervacije(odgovor, poruka.getKarte(), poruka.getVipKarte(), ime, prezime, poruka.getUsername(), email, jmbg);
			odgovor.setUspeh(true);
		}

		s.close();

	}

	private void potvrdaRezervacije(Odgovor odgovor, int karteKorisnika, int vipKarteKorisnika, String ime, String prezime, String username, String email, String jmbg) {
		try {
		    String brojRezervacije = String.valueOf(100000 + (new Random()).nextInt(900000));
		    
		    String tekst = "Potvrda rezervacije "
		    		+ "\nBroj rezervacije: " + brojRezervacije
		    		+ "\nBroj rezervisanih obicnih karata: " + karteKorisnika
		    		+ "\nBroj rezervisanih vip karata: " + vipKarteKorisnika
		    		+ "\nKorisnicko ime: " + username
		    		+ "\nIme i prezime: " + ime + " " + prezime
		    		+ "\nEmail: " + email
		    		+ "\nJMBG: " + jmbg
		    		+ "\nHvala na rezervaciji!";
			
			FileWriter myWriter = new FileWriter("rezervacije/" + brojRezervacije +".txt") ;
			myWriter.write(tekst);
			myWriter.close();
			System.out.println("Kreiran fajl!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void vracanjeBrKarata(Odgovor odgovor) {

		try {
			String upit = "SELECT karte,vipkarte FROM korisnik";
			Statement s = kon.createStatement();
			ResultSet rs = s.executeQuery(upit);

			int zbirKarata = 0;
			int zbirVipKarata = 0;
			while (rs.next()) {
				int karte = rs.getInt("karte");
				int vipKarte = rs.getInt("vipkarte");
				zbirKarata += karte;
				zbirVipKarata += vipKarte;
			}
			int preostaleKarte = 20 - zbirKarata;
			int preostaleVipKarte = 5 - zbirVipKarata;

			odgovor.setKarte(preostaleKarte);
			odgovor.setVipKarte(preostaleVipKarte);
			odgovor.setUspeh(true);
			s.close();
		} catch (Exception e) {
			odgovor.setUspeh(false);
		}

	}

	private void brisanjeRezervacije(Odgovor odgovor, Poruka poruka) {

		try {
			String upit = "SELECT karte,vipkarte FROM korisnik WHERE username= '" + poruka.getUsername() + "'";
			Statement s = kon.createStatement();
			ResultSet rs = s.executeQuery(upit);

			rs.next();
			int stareKarte = rs.getInt("karte");
			int stareVipKarte = rs.getInt("vipkarte");

			int noveKarte = stareKarte - poruka.getKarte();
			int noveVipKarte = stareVipKarte - poruka.getVipKarte();

			upit = "UPDATE korisnik SET karte=" + noveKarte + ", vipkarte= " + noveVipKarte + " WHERE username= '"
					+ poruka.getUsername() + "'";

			int uspeh = s.executeUpdate(upit);
			if (uspeh != 1) {
				odgovor.setUspeh(false);
			} else {
				odgovor.setUspeh(true);
			}

			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void vracanjeBrKarataKorisnika(Odgovor odgovor, Poruka poruka) {
		try {
			String upit = "SELECT karte,vipkarte FROM korisnik WHERE username= '" + poruka.getUsername() + "'";
			Statement s = kon.createStatement();
			ResultSet rs = s.executeQuery(upit);

			rs.next();
			int karte = rs.getInt("karte");
			int vipKarte = rs.getInt("vipkarte");

			odgovor.setKarte(karte);
			odgovor.setVipKarte(vipKarte);
			odgovor.setUspeh(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void login(Odgovor odgovor, Poruka poruka) {

		try {
			String upit = "SELECT username,password FROM korisnik WHERE username ='" + poruka.getUsername()
					+ "' AND password = '" + poruka.getPassword() + "'";
			Statement s = kon.createStatement();
			ResultSet rs = s.executeQuery(upit);

			int rsBrojac = 0;
			while (rs.next()) {
				rsBrojac++;
			}

			if (rsBrojac == 1) {
				odgovor.setUspeh(true);
			} else {
				odgovor.setUspeh(false);
				return;
			}

//			String upit = "SELECT username,password FROM korisnik";
//			Statement s = kon.createStatement();
//			ResultSet rs = s.executeQuery(upit);
//
//			while (rs.next()) {
//				if (poruka.getUsername() == rs.getString("username")
//						&& poruka.getPassword() == (rs.getString("password"))) {
//					odgovor.setUspeh(true);
//				} else {
//					odgovor.setUspeh(false);
//				}
//			}

			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void registrovanjeKorisnika(Odgovor odgovor, Poruka poruka) {

		try {
			boolean jmbgBroj = poruka.getJmbg().chars().allMatch(Character::isDigit);
			if (!jmbgBroj || poruka.getJmbg().length() != 13) {
				JOptionPane.showMessageDialog(null, "Los format JMBG-a", "Greska", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String upit = "INSERT INTO korisnik(username, password, ime,prezime,jmbg,email,karte,vipkarte) "
					+ "VALUES ('" + poruka.getUsername() + "','" + poruka.getPassword() + "','" + poruka.getIme()
					+ "','" + poruka.getPrezime() + "','" + poruka.getJmbg() + "','" + poruka.getEmail() + "','"
					+ poruka.getKarte() + "','" + poruka.getVipKarte() + "')";
			Statement s = kon.createStatement();

			int uspeh = s.executeUpdate(upit);

			if (uspeh == 1) {
				odgovor.setUspeh(true);
			} else {
				odgovor.setUspeh(false);
			}

			s.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
