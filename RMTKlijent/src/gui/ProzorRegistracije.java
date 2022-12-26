package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Main;
import poruka.Odgovor;
import poruka.Operacija;
import poruka.Poruka;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;

public class ProzorRegistracije extends JFrame {

	private JPanel contentPane;
	private JTextField txtUser;
	private JTextField txtPass;
	private JTextField txtEmail;
	private JTextField txtPrezime;
	private JTextField txtIme;
	private JTextField txtJMBG;

	public ProzorRegistracije() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtUser = new JTextField();
		txtUser.setBounds(95, 14, 96, 20);
		contentPane.add(txtUser);
		txtUser.setColumns(10);

		txtPass = new JTextField();
		txtPass.setBounds(95, 59, 96, 20);
		contentPane.add(txtPass);
		txtPass.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setBounds(95, 112, 96, 20);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);

		txtPrezime = new JTextField();
		txtPrezime.setBounds(282, 59, 96, 20);
		contentPane.add(txtPrezime);
		txtPrezime.setColumns(10);

		txtIme = new JTextField();
		txtIme.setBounds(282, 14, 96, 20);
		contentPane.add(txtIme);
		txtIme.setColumns(10);

		txtJMBG = new JTextField();
		txtJMBG.setBounds(282, 112, 96, 20);
		contentPane.add(txtJMBG);
		txtJMBG.setColumns(10);

		JLabel lblNewLabel = new JLabel("username");
		lblNewLabel.setBounds(10, 8, 63, 27);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("password");
		lblNewLabel_1.setBounds(10, 61, 63, 17);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("email");
		lblNewLabel_2.setBounds(10, 115, 49, 14);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("ime");
		lblNewLabel_3.setBounds(223, 14, 49, 14);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("prezime");
		lblNewLabel_4.setBounds(223, 62, 49, 14);
		contentPane.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("JMBG");
		lblNewLabel_5.setBounds(223, 115, 49, 14);
		contentPane.add(lblNewLabel_5);

		JButton btnRegistracija = new JButton("Registruj se");
		btnRegistracija.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Poruka poruka = new Poruka();
				poruka.setUsername(txtUser.getText());
				poruka.setPassword(txtPass.getText());
				poruka.setEmail(txtEmail.getText());
				poruka.setIme(txtIme.getText());
				poruka.setPrezime(txtPrezime.getText());
				poruka.setJmbg(txtJMBG.getText());
				poruka.setOperacija(Operacija.REGISTRACIJA);
				poruka.setKarte(0);
				poruka.setVipKarte(0);

				if (txtUser.getText() == null || txtUser.getText().equals("") || txtPass.getText() == null
						|| txtPass.getText().equals("") || txtEmail.getText() == null || txtEmail.getText().equals("")
						|| txtIme.getText() == null || txtIme.getText().equals("") || txtPrezime.getText() == null
						|| txtPrezime.getText().equals("") || txtJMBG.getText() == null
						|| txtJMBG.getText().equals("")) {

					JOptionPane.showMessageDialog(null, "Morate uneti sva polja prilikom registracije!", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean jmbgBroj = poruka.getJmbg().chars().allMatch(Character::isDigit);
				if (!jmbgBroj || poruka.getJmbg().length() != 13) {
					JOptionPane.showMessageDialog(null, "Los format JMBG-a", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					ObjectOutputStream izlaz = new ObjectOutputStream(Main.getSocket().getOutputStream());
					izlaz.writeObject(poruka);

					ObjectInputStream ulaz = new ObjectInputStream(Main.getSocket().getInputStream());
					Odgovor odgovor = (Odgovor) ulaz.readObject();

					if (odgovor.isUspeh()) {
						ProzorLogin pl = new ProzorLogin();
						pl.setVisible(true);
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Neuspesna registracija. " + odgovor.getObjasnjenje(),
								"Greska", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		btnRegistracija.setBounds(153, 192, 133, 34);
		contentPane.add(btnRegistracija);
	}
}
