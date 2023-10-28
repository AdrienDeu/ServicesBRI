package serveur;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

import service.bri.Service;


public class Serveur implements Runnable {
	private ServerSocket serveur;
	private int port;
	private Class<? extends Service> classe;
	

	
	public Serveur(int p, Class<? extends Runnable> classe) throws IOException {
		this.port = p;
		this.serveur = new ServerSocket(port);
		this.classe = (Class<? extends Service>) classe;
	}


	@Override	
	public void run() {
		try {
			while(true) {
				try {
					new Thread(this.classe.getConstructor(Socket.class).newInstance(this.serveur.accept())).start();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
					
			}
				
		}
		catch (IOException e) { 
			try {this.serveur.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'écoute :"+ e);
		}
	}

	
	
}