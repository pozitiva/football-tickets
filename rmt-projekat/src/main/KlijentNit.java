package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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

					break;
				case LOGIN:

					break;
				case BRISANJE:

					break;
				case REZERVACIJA:
					rezervacijaKarata(odgovor, poruka);
					break;
				case VRATI_BROJ_KARATA:
					vracanjeBrKarata(odgovor);
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
		upit = "SELECT karte,vipkarte FROM korisnik WHERE username= '" + poruka.getUsername() + "'";
		rs = s.executeQuery(upit);

		rs.next();
		int karteKorisnika = rs.getInt("karte");
		int vipKarteKorisnika = rs.getInt("vipkarte");

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
			odgovor.setUspeh(true);
		}

		s.close();

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
}
