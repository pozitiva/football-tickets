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

public class ProzorLogin extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtPass;

	public ProzorLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtUsername = new JTextField();
		txtUsername.setBounds(170, 26, 96, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);

		txtPass = new JTextField();
		txtPass.setBounds(170, 107, 96, 20);
		contentPane.add(txtPass);
		txtPass.setColumns(10);

		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(24, 20, 83, 33);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(24, 109, 71, 17);
		contentPane.add(lblNewLabel_1);

		JButton btnLogin = new JButton("Uloguj se");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					Poruka poruka = new Poruka();
					poruka.setUsername(txtUsername.getText());
					poruka.setPassword(txtPass.getText());
					poruka.setOperacija(Operacija.LOGIN);

					if (txtUsername.getText() == null || txtUsername.getText().equals("") || txtPass.getText() == null
							|| txtPass.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Morate uneti sva polja prilikom prijavljivanja", "Greska",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					ObjectOutputStream izlaz = new ObjectOutputStream(Main.getSocket().getOutputStream());
					izlaz.writeObject(poruka);

					ObjectInputStream ulaz = new ObjectInputStream(Main.getSocket().getInputStream());
					Odgovor odgovor = (Odgovor) ulaz.readObject();

					if (odgovor.isUspeh()) {
						Main.setUsername(txtUsername.getText());
						GlavniEkran ge = new GlavniEkran();
						ge.setVisible(true);
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Niste uspeli da se prijavite. " + odgovor.getObjasnjenje(),
								"Greska", JOptionPane.ERROR_MESSAGE);
						return;
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnLogin.setBounds(47, 197, 135, 23);
		contentPane.add(btnLogin);

		JButton btnRegistracija = new JButton("Registruj se");
		btnRegistracija.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProzorRegistracije pr = new ProzorRegistracije();
				pr.setVisible(true);
				dispose();
			}
		});
		btnRegistracija.setBounds(264, 197, 135, 23);
		contentPane.add(btnRegistracija);

		JLabel lblNewLabel_2 = new JLabel("Nemas nalog? Registruj se");
		lblNewLabel_2.setBounds(264, 172, 162, 14);
		contentPane.add(lblNewLabel_2);
	}
}
