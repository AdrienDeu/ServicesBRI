package service;

import service.bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;


public class ServiceAma extends Service {
	private final Socket socket;
	public ServiceAma(Socket socket) {
		this.socket = socket;
	}


	BufferedReader in1;
	PrintWriter out;

	@Override
	public void run() {
		try {
			in1 = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter (socket.getOutputStream ( ), true);


			out.println(ServiceRegistry.toStringue()+"\nTapez le numéro de service désiré :");
			String clavier = in1.readLine();
			int choix = Integer.parseInt(clavier);

			// instancier le service numéro "choix" en lui passant la socket "client"
			// invoquer run() pour cette instance ou la lancer dans un thread à part
			Class<? extends Service> classe = ServiceRegistry.getServiceClass(choix);
			if(classe!=null) {
				classe.getConstructor(Socket.class).newInstance(socket).run();
			}
			else {
				out.println("Essayer de rentrer un numéro valide");
			}
			in1.close();
			out.close();
			socket.close();

		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {socket.close();} catch (IOException e2) {
            throw new RuntimeException(e2);
        }
	}


}


