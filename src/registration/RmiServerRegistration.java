package registration;

import java.awt.Color;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.sql.rowset.spi.SyncResolver;

import network.Controller;
import network.RmiMessage;

/**
 * Classe che implementa un servizio base di registrazione centralizzato remoto.
 */
public class RmiServerRegistration extends UnicastRemoteObject implements InterfaceRemoteMethodRegistration {

	private static final long serialVersionUID = 1L;
	private Room room;
	private ArrayList<Color> colors;
	private int startXGui = 100;
	/**
	 * Costruttore di classe che prende in input il numero di giocatori necessari ad iniziare il gioco
	 * e l'host corrente su cui viene implementato il servizio di registrazione centralizzato
	 */
	public RmiServerRegistration(int sp, Player myPlayer)throws RemoteException, InterruptedException, ServerNotActiveException {
		this.room = new Room(sp);
		this.inizializeColors();
		this.addPlayer(myPlayer);
	}
	
	/**
	 * Aggiunta di un host alla stanza di gioco, se la stanza viene completata viene inviato un 
	 * pacchetto nell'anello con la configurazione dello stesso, in modo che ogni nodo riesca 
	 * a settarsela personalmente.
	 */
	public synchronized void addPlayer(Player p) throws InterruptedException, ServerNotActiveException {
		System.out.println("[REGISTRATION SERVICE] AGGIUNTO HOST IP: "+p.getHost().getIP()+"  e PORT:"+p.getHost().getPort());
		p.setColor(this.colors.remove(0));
		p.setStartXPos(this.startXGui);
		this.startXGui += 100;
		this.room.addPlayer(p);
		System.out.println("[REGISTRATION SERVICE] NUMERO DI GIOCATORI MANCANTI ALL'INIZIO: " + this.room.getMissingPlayers());
		if (this.room.isCompleted()) {
			System.out.println("[REGISTRAZIONE] ROOM COMPLETATA, CONFIGURAZIONE IN SENDING...");
			Controller.getInstance().setRoom(this.room);
			String uuid = Controller.getInstance().getMyHost().getUUID();
			RmiMessage m = new RmiMessage(this.room, uuid);
			try {
				Controller.getInstance().getCommunication().getNextHostInterface().send(m);
			} catch (RemoteException e) {
				System.out.println("########### REMOTE EXCEPTION @ RMISERVERREGISTRATION.ADDPLAYER ###########");
			} catch (NotBoundException e) {
				System.out.println("########### NOTBOUND EXCEPTION @ RMISERVERREGISTRATION.ADDPLAYER ###########");
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
    
    public void inizializeColors() {
    	this.colors = new ArrayList<Color>();
    	this.colors.add(Color.red);
    	this.colors.add(Color.cyan);
    	this.colors.add(Color.green);
    	this.colors.add(Color.yellow);
    	this.colors.add(Color.blue);
    }
}
