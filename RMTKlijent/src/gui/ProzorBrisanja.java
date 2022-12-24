package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Main;
import poruka.Odgovor;
import poruka.Operacija;
import poruka.Poruka;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;

public class ProzorBrisanja extends JFrame {

	private JPanel contentPane;

	public ProzorBrisanja() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JComboBox cbObicne = new JComboBox();
		cbObicne.setBounds(175, 39, 121, 31);
		contentPane.add(cbObicne);

		JComboBox cbVip = new JComboBox();
		cbVip.setBounds(175, 113, 121, 31);
		contentPane.add(cbVip);

		try {
			Poruka poruka = new Poruka();
			poruka.setUsername(Main.getUsername());
			poruka.setOperacija(Operacija.VRATI_BROJ_KARATA_KORISNIKA);

			ObjectOutputStream izlaz = new ObjectOutputStream(Main.getSocket().getOutputStream());
			izlaz.writeObject(poruka);

			ObjectInputStream ulaz = new ObjectInputStream(Main.getSocket().getInputStream());
			Odgovor odgovor = (Odgovor) ulaz.readObject();

			String[] vrednosti = new String[odgovor.getKarte() + 1];
			for (int i = 0; i <= odgovor.getKarte(); i++) {
				vrednosti[i] = String.valueOf(i);
			}
			cbObicne.setModel(new DefaultComboBoxModel(vrednosti));

			String[] vrednostiVip = new String[odgovor.getVipKarte() + 1];
			for (int i = 0; i <= odgovor.getVipKarte(); i++) {
				vrednostiVip[i] = String.valueOf(i);
			}
			cbVip.setModel(new DefaultComboBoxModel(vrednostiVip));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JLabel lblNewLabel = new JLabel("Koliko obicnih karata brisete");
		lblNewLabel.setBounds(10, 35, 155, 39);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Koliko VIP karata brisete");
		lblNewLabel_1.setBounds(10, 121, 155, 14);
		contentPane.add(lblNewLabel_1);

		JButton btnObrisi = new JButton("Obrisi rezervaciju");
		btnObrisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Poruka poruka = new Poruka();
				poruka.setUsername(Main.getUsername());
				poruka.setKarte(Integer.parseInt((String) cbObicne.getSelectedItem()));
				poruka.setVipKarte(Integer.parseInt((String) cbVip.getSelectedItem()));
				poruka.setOperacija(Operacija.BRISANJE);

				if (poruka.getKarte() == 0 && poruka.getVipKarte() == 0) {
					JOptionPane.showMessageDialog(null, "Niste odabrali broj karata", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					ObjectOutputStream izlaz = new ObjectOutputStream(Main.getSocket().getOutputStream());
					izlaz.writeObject(poruka);

					ObjectInputStream ulaz = new ObjectInputStream(Main.getSocket().getInputStream());
					Odgovor odgovor = (Odgovor) ulaz.readObject();

					if (odgovor.isUspeh()) {
						JOptionPane.showMessageDialog(null, "Uspesno ste obrisali karte");
					} else {
						JOptionPane.showInternalMessageDialog(null, "Niste uspeli da obrisete karte", "Greska",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					dispose();

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		btnObrisi.setBounds(162, 212, 140, 40);
		contentPane.add(btnObrisi);
	}
}
