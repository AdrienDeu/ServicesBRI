package client;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


class ApplicationAmateur {
	private static final int PORT = 11443;
	private static final String IPV4 = "localhost";

	public static void main(String[] args) throws IOException {
		//crée la socket
		Socket socket = new Socket(IPV4, PORT);

		//envoie l'entrée clavier au serveur via la socket
		PrintWriter out;
		out = new PrintWriter (socket.getOutputStream(), true);
		//reçoit l'entrée clavier du serveur via la socket
		Scanner input = new Scanner(System.in);

		//reçoit la réponse du serveur
		BufferedReader in1 = new BufferedReader (new InputStreamReader(socket.getInputStream()));
		String output = "";
		while(!output.contains("Tapez le numéro de service désiré :")) {
			output = in1.readLine();
			System.out.println(output);
		}
		String choix1 = input.next();
		out.println(choix1);

			output = in1.readLine();
			System.out.println(output);

			String chaine = input.next();

			out.println(chaine);

			System.out.println(in1.readLine());


		}

	}
