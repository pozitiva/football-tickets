package main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {

	static List<KlijentNit> klijenti = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		try (ServerSocket serverSocket = new ServerSocket(8090)) {
			while (true) {
				System.out.println("Cekam klijenta...");
				Socket klijentSocket = serverSocket.accept();
				System.out.println("Dosao klijent");
				KlijentNit nit = new KlijentNit(klijentSocket);
				klijenti.add(nit);
				nit.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
