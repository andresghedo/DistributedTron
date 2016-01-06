package network;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Calendar;
import java.util.Scanner;


/**
 * Classe estensione Thread, viene fatto partire il thread in modo da evitare eventi bloccanti
 * dovuti alle comunicazioni remote e permettere all'utente di inviare e 'parlare' quando vuole, immettendo
 * così un messaggio nella rete ad anello(aspettando ovviamente che il messaggio ritorni, notificando così l'avvenuta consegna
 * ad/ai destinatari).
 * Verrà utilizzata in fase di sviluppo per testing.
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
						System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### SERVERNOTACTIVE EXCEPTION @ INPUTTHREAD.RUN ###########");
					}
				
			} catch (RemoteException e) {
				System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### REMOTE EXCEPTION @ INPUTTHREAD.RUN ###########");
			} catch (NotBoundException e) {
				System.out.println("[" + Calendar.getInstance().getTimeInMillis() + "]########### NOTBOUND EXCEPTION @ INPUTTHREAD.RUN ###########");
			}
	    }
    }

}