package main;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.GlavniEkran;

public class Main {
	
	
	private static String username = "iva";
	private static Socket socket;

	public static void main(String[] args) {
		try {
			socket= new Socket("localhost", 8090);
			System.out.println("Povezan na server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		GlavniEkran ge= new GlavniEkran();
		ge.setVisible(true);
		

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
