package serveur;

import java.io.IOException;

import service.ServiceAma;
import service.ServiceProg;


public class Application {
	private final static int PORT_PROG = 11444;
	private final static int PORT_AMA = 11443;
	
	public static void main(String[] args) {
		try {
			new Thread(new Serveur(PORT_PROG, ServiceProg.class)).start();
			new Thread(new Serveur(PORT_AMA, ServiceAma.class)).start();

		} catch (IOException e) {
				System.err.println("Pb lors de la création du serveur : " +  e);
		}
	}
}
