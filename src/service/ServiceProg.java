package service;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Vector;

import personnes.Programmeur;
import service.bri.ServiceRegistry;


public class ServiceProg extends Service {
	private static Vector<Programmeur> programmeurs;

	private static final int PORT_PROG = 11444;

	String pseudo;

	static {
		programmeurs = new Vector<Programmeur>();
	}
	private Socket socket;
	public ServiceProg(Socket socket) {
		this.socket = socket;
		
	}
	PrintWriter out;
	BufferedReader in1;

	@Override
	public void run() {
		try {
			
			in1 = new BufferedReader (new InputStreamReader(socket.getInputStream()));

			out = new PrintWriter (socket.getOutputStream ( ), true);

			
			String line = in1.readLine();
			boolean logged = false;
			if (line.equals("1")) {

				while(!logged) {
					out.println("Taper le pseudo");

					logged = connection();

					if ((logged)) {
						out.println("true");
					} else {
						out.println("false");
					}
				}

			}
			else if(line.equals("2")){
				while(!logged) {
					out.println("Taper le pseudo");
					logged = enregistrer();
					if ((logged)) {
						out.println("true");
					} else {
						out.println("false");
					}
				}
			}
			// deuxième menu
			String choix2 = in1.readLine();
			if (choix2.equals("1")) {
				Programmeur prog = getProgrammer(in1.readLine());
				addService(prog);
			}
			else if (choix2.equals("2")) {
				Programmeur prog = getProgrammer(in1.readLine());
				updateServ(prog);
			}
			else if (choix2.equals("3")) {
				Programmeur prog = getProgrammer(in1.readLine());

				String newFtp = in1.readLine();
				prog.setAdresseFtp("prog");
				out.println("Adresse ftp modifiée");

			}
			else if (choix2.equals("4")) {
				Programmeur prog = getProgrammer(in1.readLine());
				UpdateStateService(prog);
			}
			else if (choix2.equals("5")) {
				Programmeur prog = getProgrammer(in1.readLine());
				deleteService(prog);
			}



			
			
		} catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}

	public void UpdateStateService(Programmeur prog) throws IOException {

		try {
			out.println(ServiceRegistry.toStringue()+"\nRenseigner le numéro du service à démarrer/arrêter :");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		ArrayList<Integer> services = prog.getServices();


		//charger la classe et la déclarer au ServiceRegistry
		Integer serv = Integer.parseInt(in1.readLine());
		boolean isAuthor = false;
		for (Integer service : services) {
			if (serv.equals(service)) {
				isAuthor=true;
				break;
			}
		}
		if (isAuthor) {
			ServiceRegistry.UpdateStateService(serv);
			out.println("L'état du service a été mis à jour");
		}
		else {
			out.println("L'état du service ne peut pas être mis à jour");
		}




	}

	private void updateServ(Programmeur prog) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
		try {
			out.println(ServiceRegistry.toStringue()+"\nRenseigner le numéro du service à modifier :");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		ArrayList<Integer> services = prog.getServices();
		Integer serv = Integer.parseInt(in1.readLine());
		boolean isAuthor = false;



		for (Integer service : services) {
            if (serv.equals(service)) {
                isAuthor = true;
                break;
            }
		}




		if (isAuthor) {
			out.println("Renseigner le chemin vers la classe de service");
			ServiceRegistry.removeService(serv);
			addService(prog);
		}
		else {
			out.println("Impossible de modifier ce service");
		}
	}

	private Programmeur getProgrammer(String pseudo) {
		synchronized (programmeurs) {
			for (Programmeur p : programmeurs) {
				if (pseudo.equals(p.getLogin())) {
					return p;
				}
			}
		}
		return null;
	}

	private boolean connection() throws IOException {
		pseudo = in1.readLine();
		out.println("Taper le mot de passe.");
		String mdp = in1.readLine();
		boolean isLogin = false;
		synchronized (programmeurs) {
			for (Programmeur p : programmeurs) {
				if (pseudo.equals(p.getLogin()) && mdp.equals(p.getMdp())) {
					isLogin = true;
				}
			}
		}

		if (isLogin) {
			out.println("Programmeur connecté.");
		}
		else {
			out.println("Connection échouée, veuillez réessayer.");
		}
		return isLogin;

	}

	private boolean enregistrer() throws IOException{
		pseudo = in1.readLine();
		out.println("Taper le mot de passe.");
		String mdp = in1.readLine();
		out.println("Taper votre adresse ftp.");
		// ex : ftp://localhost:11442/
		String ftp = in1.readLine();


		if (pseudo.equals("") || mdp.equals("") || ftp.equals("")) {
			out.println("Veuillez renseigner tous les champs.");
			return false;
		}



		synchronized (programmeurs) {
			for (Programmeur p : programmeurs) {
				if (pseudo.equals(p.getLogin())) {
					out.println("Pseudo déjà utilisé");
					return false;
				}
			}
		}
		this.programmeurs.add(new Programmeur(pseudo, mdp, ftp));
		out.println("Programmeur bien enregistré à la liste des programmeurs.");
		return true;
	}

	public void addService(Programmeur prog) throws IOException {

		// URLClassLoader sur ftp
		URLClassLoader urlcl = URLClassLoader.newInstance(new URL[] {new URL (prog.getAdresseFtp())});


		try {
			String classeName = in1.readLine();
			//charger la classe et la déclarer au ServiceRegistry
			ServiceRegistry.addService(urlcl.loadClass(classeName).asSubclass(Service.class));
			prog.addService(ServiceRegistry.getServiceLength());
			ServiceRegistry.addServiceState();
			out.println("Le service a été ajouté/modifié");
		}
		catch (ClassCastException e) {
			System.out.println("la classe doit implémenter Service");
		}
		catch (ClassNotFoundException e) {
			System.out.println("la classe n'est pas sur le serveur ftp dans home");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void deleteService(Programmeur prog) throws IOException {

		try {
			out.println(ServiceRegistry.toStringue()+"\nRenseigner le numero du service à supprimer :");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		ArrayList<Integer> services = prog.getServices();


		//charger la classe et la déclarer au ServiceRegistry
		Integer serv = Integer.parseInt(in1.readLine());
		boolean isAuthor = false;
		for (Integer service : services) {
			if (serv.equals(service)) {
				isAuthor=true;
			}
		}
		if (isAuthor) {
			ServiceRegistry.removeService(serv);
			ServiceRegistry.deleteServiceState(serv);
			out.println("Le service a été supprimé");
		}
		else {
			out.println("Impossible de supprimer ce service");
		}




	}

}