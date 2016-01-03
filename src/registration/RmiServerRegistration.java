package registration;

import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import network.Controller;
import network.Host;
import network.RmiMessage;

public class RmiServerRegistration extends UnicastRemoteObject implements InterfaceRemoteMethodRegistration {

	private static final long serialVersionUID = 1L;
	private Room room;
	/**
	 * Costruttore di classe che prende in input il numero di giocatori necessari ad iniziare il gioco
	 * e l'host corrente su cui viene implementato il servizio di registrazione centralizzato
	 */
	public RmiServerRegistration(int sp, Host myHost)throws RemoteException, InterruptedException, ServerNotActiveException {
		this.room = new Room(sp);
		this.addPlayer(myHost);
	}
	
	/**
	 * Aggiunta di un host alla stanza di gioco, se la stanza viene completata viene inviato un 
	 * pacchetto nell'anello con la configurazione dello stesso, in modo che ogni nodo riesca 
	 * a settarsela personalmente.
	 */
	public void addPlayer(Host host) throws InterruptedException, ServerNotActiveException {
		System.out.println("[REGISTRATION SERVICE] AGGIUNTO HOST IP: "+host.getIP()+"  e PORT:"+host.getPort());
		this.room.addHost(host);
		System.out.println("[REGISTRATION SERVICE] NUMERO DI GIOCATORI MANCANTI ALL'INIZIO: " + this.room.getMissingPlayers());
		if (this.room.isCompleted()) {
			System.out.println("[REGISTRAZIONE] ROOM COMPLETATA, CONFIGURAZIONE IN SENDING...");
			Controller.getInstance().setRoom(this.room);
			String uuid = Controller.getInstance().getMyHost().getUUID();
			RmiMessage m = new RmiMessage(this.room, uuid);
			try {
				Controller.getInstance().getCommunication().getNextHostInterface().send(m);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
	/**
	 * Ritorna il numeor di giocatori correnti
	 */
    public int getCurrentPlayers() {
        return this.room.getCurrentPlayers();
    }
    
    /**
     * Ritorna la stanza di gioco
     */
    public Room getRoom() {
    	return this.room;
    }
}