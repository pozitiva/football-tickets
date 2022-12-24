package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
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
import java.awt.event.ActionListener;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;

public class ProzorRezervacije extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public ProzorRezervacije() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JComboBox cbObicne = new JComboBox();
		cbObicne.setModel(new DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4" }));
		cbObicne.setBounds(147, 42, 92, 22);
		contentPanel.add(cbObicne);

		JComboBox cbVip = new JComboBox();
		cbVip.setModel(new DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4" }));
		cbVip.setBounds(147, 111, 92, 22);
		contentPanel.add(cbVip);

		JLabel lblNewLabel = new JLabel("Broj obicnih karata");
		lblNewLabel.setBounds(10, 46, 119, 14);
		contentPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Broj vip karata");
		lblNewLabel_1.setBounds(10, 115, 119, 14);
		contentPanel.add(lblNewLabel_1);

		JButton btnRezervisi = new JButton("Rezervisi karte");
		btnRezervisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// pripremi poruku
				Poruka poruka = new Poruka();
				poruka.setUsername(Main.getUsername());
				poruka.setKarte(Integer.parseInt((String) cbObicne.getSelectedItem()));
				poruka.setVipKarte(Integer.parseInt((String) cbVip.getSelectedItem()));
				poruka.setOperacija(Operacija.REZERVACIJA);

				if (poruka.getKarte() == 0 && poruka.getVipKarte() == 0) {
					JOptionPane.showMessageDialog(null, "Niste odabrali broj karata", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (poruka.getKarte() + poruka.getVipKarte() > 4) {
					JOptionPane.showMessageDialog(null, "Mozete odabrati najvise 4 karte", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// posalji poruku
				try {
					ObjectOutputStream izlaz = new ObjectOutputStream(Main.getSocket().getOutputStream());
					izlaz.writeObject(poruka);

					// primi odgovor
					ObjectInputStream ulaz = new ObjectInputStream(Main.getSocket().getInputStream());
					Odgovor odgovor = (Odgovor) ulaz.readObject();

					// obrada odgovora
					if (odgovor.isUspeh()) {
						JOptionPane.showMessageDialog(null, "Uspesno ste izvrsili rezervaciju");

						String brojRezervacije = odgovor.getBrojRezervacije();

						byte[] buffer = new byte[1024*100];
						DataInputStream fajlUlaz = new DataInputStream(Main.getSocket().getInputStream());
						int bytes = fajlUlaz.read(buffer, 0, buffer.length);
						FileOutputStream fileOutputStream = new FileOutputStream(
								"C:\\Users\\Iva\\Downloads\\" + brojRezervacije + ".txt");
						fileOutputStream.write(buffer, 0, bytes);
					} else {
						JOptionPane.showMessageDialog(null, "Rezervacija nije uspela", "Greska",
								JOptionPane.ERROR_MESSAGE);
					}
					dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		btnRezervisi.setBounds(147, 193, 142, 23);
		contentPanel.add(btnRezervisi);
	}
}
