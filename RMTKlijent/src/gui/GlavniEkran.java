package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Main;
import poruka.Odgovor;
import poruka.Operacija;
import poruka.Poruka;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class GlavniEkran extends JFrame {

	private JPanel contentPane;

	public GlavniEkran() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnRezervisi = new JButton("Rezervisi kartu");
		btnRezervisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProzorRezervacije pr = new ProzorRezervacije();
				pr.setVisible(true);

			}
		});
		btnRezervisi.setBounds(29, 11, 144, 36);
		contentPane.add(btnRezervisi);

		JButton btnObrisi = new JButton("Obrisi rezervaciju");
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProzorBrisanja pb = new ProzorBrisanja();
				pb.setVisible(true);

			}
		});
		btnObrisi.setBounds(29, 84, 144, 36);
		contentPane.add(btnObrisi);

		JButton btnVidi = new JButton("Vidi broj preostalih karata");
		btnVidi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Poruka poruka = new Poruka();
				poruka.setOperacija(Operacija.VRATI_BROJ_KARATA);

				Odgovor odgovor;
				try {
					ObjectOutputStream izlaz = new ObjectOutputStream(Main.getSocket().getOutputStream());
					izlaz.writeObject(poruka);

					ObjectInputStream ulaz = new ObjectInputStream(Main.getSocket().getInputStream());
					odgovor = (Odgovor) ulaz.readObject();

					if (odgovor.isUspeh()) {
						JOptionPane.showMessageDialog(null, "Preostali broj slobodnih karata je " + odgovor.getKarte()
								+ ", a vip karata " + odgovor.getVipKarte());

					} else {
						JOptionPane.showMessageDialog(null, "Desila se greska", "Greska", JOptionPane.ERROR_MESSAGE);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		btnVidi.setBounds(29, 155, 175, 44);
		contentPane.add(btnVidi);
		
		JLabel lblUlogovani = new JLabel("Ulogovani korisnik: " + Main.getUsername());
		lblUlogovani.setBounds(29, 222, 49, 14);
		contentPane.add(lblUlogovani);
	}
}
