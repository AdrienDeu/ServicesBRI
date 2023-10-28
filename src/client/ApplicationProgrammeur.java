package client;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import java.util.Scanner;


class ApplicationProgrammeur{

	private static final int PORT = 11444;
	private static String IPV4 = "localhost";

	
	public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
		//crée la socket
		Socket socket = new Socket(IPV4, PORT);

		//envoie l'entrée clavier au serveur via la socket
		PrintWriter out;
		out = new PrintWriter (socket.getOutputStream(), true);

		//reçoit l'entrée clavier du serveur via la socket
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		//reçoit la réponse du serveur
		BufferedReader in1 = new BufferedReader (new InputStreamReader(socket.getInputStream()));






		//System.out.println("Bienvenue sur le service programmeur de BRiLaunch");
		String commandes = "Bienvenue sur le service programmeur de BRiLaunch\nTaper :\n - 1 pour se connecter.\n - 2 pour s'enregistrer.\n - exit pour quitter";
		System.out.println(commandes);
		int choix1 = Integer.parseInt(input.readLine());
		out.println(choix1);
		int nbIterations = 0;
		if(choix1 == 1){
			nbIterations = 2;
		} else if (choix1 == 2) {
			nbIterations = 3;
		}
		String isLogged = "";
		String pseudo = null;
		while (!isLogged.equals("true")) {
			System.out.println(in1.readLine());


			for(int i = 0; i<nbIterations; ++i){
				if (i==0) {
					 pseudo = input.readLine();
					 out.println(pseudo);
				}
				else {
					out.println(input.readLine());
				}

				System.out.println(in1.readLine());
			}
			isLogged = in1.readLine();
		}

		String commandes2 = "Taper :\n" +
				"- 1 Pour fournir un nouveau service\n" +
				"- 2 Pour mettre-à-jour un service\n" +
				"- 3 Pour déclarer un changement d’adresse de son serveur ftp\n" +
				"- 4 Pour démarrer/arrêter un service\n" +
				"- 5 Pour désinstaller un service";

		System.out.println(commandes2);
		int cmd2 = Integer.parseInt(input.readLine());
		out.println(cmd2);

		if (cmd2==1) {
			addService(out, in1, pseudo);
		}
		else if (cmd2==2) {
			updateService(out, in1, pseudo);
		}
		else if (cmd2==3) {
			updateFtpAdress(out, in1, pseudo);
		}
		else if (cmd2==4) {
			startOrStopService(out, in1, pseudo);
		}
		else if (cmd2==5) {
			deleteService(out, in1, pseudo);
		}
		else {
			System.out.println("Veuillez saisir un service valide : 1 ou 2 ou 3 ou 4 ou 5");
		}
		in1.close();
		out.close();
		input.close();
		socket.close();
				
		

	}

	private static void startOrStopService(PrintWriter out, BufferedReader in1, String pseudo) {

	}

	private static void deleteService(PrintWriter out, BufferedReader in1, String pseudo) {

	}

	private static void updateFtpAdress(PrintWriter out, BufferedReader in1, String pseudo) throws IOException {
		System.out.println("Renseigner votre nouvelle adresse FTP :");
		Scanner clavier = new Scanner(System.in);
		out.println(pseudo);
		String adresseFtp = "";
		try {
			// ex : classes.ServiceInversion
			adresseFtp = clavier.next();
			//charger la classe et la déclarer au ServiceRegistry
			//ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
			out.println(adresseFtp);
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(in1.readLine());
	}

	private static void updateService(PrintWriter out, BufferedReader in1, String pseudo) throws IOException {
		System.out.println("Renseigner le numéro du service à modifier");
		Scanner clavier = new Scanner(System.in);
		out.println(pseudo);
		String numService = "";
		try {
			// ex : classes.ServiceInversion
			numService = clavier.next();
			//charger la classe et la déclarer au ServiceRegistry
			//ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
			out.println(numService);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println(in1.readLine());
		out.println(clavier.next());
		System.out.println(in1.readLine());


	}

	private static void addService(PrintWriter out, BufferedReader in1, String pseudo) throws IOException {
		System.out.println("Renseigner le chemin vers la classe de service");
		Scanner clavier = new Scanner(System.in);
		out.println(pseudo);
		try {
			// ex : classes.ServiceInversion
			String classeName = clavier.next();
			//charger la classe et la déclarer au ServiceRegistry
			//ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
			out.println(classeName);
		}
		catch (ClassCastException e) {
			System.out.println("la classe doit implémenter service.bri.Service");
		}

		System.out.println(in1.readLine());
	}
}