package main;

import java.net.Socket;
import gui.ProzorLogin;

public class Main {

	private static String username = "";
	private static Socket socket;

	public static void main(String[] args) {
		try {
			socket = new Socket("localhost", 8090);
			System.out.println("Povezan na server");
			ProzorLogin log = new ProzorLogin();
			log.setVisible(true);
		} catch (Exception e) {
			System.out.println("Server nije pokrenut, gasim aplikaciju");
		}
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		Main.username = username;
	}

	public static Socket getSocket() {
		return socket;
	}

	public static void setSocket(Socket socket) {
		Main.socket = socket;
	}

}
