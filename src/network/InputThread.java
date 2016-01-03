package network;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Scanner;


/**
 * Classe id estensione Thread, viene fatto partire il thread in modo da evitare eventi bloccanti
 * dovuti alle comunicazioni remote e permettere all'utente di inviare e 'parlare' quando vuole, immettendo
 * così un messaggio nella rete ad anello(aspettando ovviamente che il messaggio ritorni, notificando così l'avvenuta consegna
 * ad/ai destinatari).
 * @author andreasd
 *
 */
public class InputThread extends Thread {

    public void run() {
    	Scanner scanner=new Scanner(System.in);
		while (true) {
    		System.out.println("[INPUT] Insert key");
	        String str = scanner.nextLine();
	        String uuid = Controller.getInstance().getMyHost().getUUID();
			RmiMessage m = new RmiMessage(str, uuid);
			try {
					try {
						Controller.getInstance().getCommunication().getNextHostInterface().send(m);
					} catch (ServerNotActiveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }

}