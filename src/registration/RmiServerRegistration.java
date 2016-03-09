package registration;

import java.awt.Color;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import network.Controller;
import network.RmiMessage;

/**
 * Classe che implementa un servizio base di registrazione centralizzato remoto.
 */
public class RmiServerRegistration extends UnicastRemoteObject implements InterfaceRemoteMethodRegistration {

	private static final long serialVersionUID = 1L;
	private Room room;
	private ArrayList<Color> colors;
	/** variabili ed array per settaggi delle posizioni e direzioni iniziali */
	private static int MARGIN = 40;
	private static int MAX_X = Controller.getInstance().WIDTH - MARGIN;
	private static int MAX_Y = Controller.getInstance().HEIGHT - 36 - MARGIN;
	private static int MIDDLE_Y = (Controller.getInstance().HEIGHT - 36) / 2;
	private int[] startX = {MARGIN, MAX_X, MARGIN, MAX_X, MARGIN, MAX_X };
	private int[] startY = {MAX_Y, MAX_Y, MARGIN, MARGIN, MIDDLE_Y, MIDDLE_Y };
	private String[] startDirections = {"E", "W", "E", "W", "E", "W" };
	private int idPlayer = 1;
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
	public Color addPlayer(Player p) throws InterruptedException, ServerNotActiveException {
		System.out.println("[REGISTRATION SERVICE] AGGIUNTO HOST IP: "+p.getHost().getIP()+"  e PORT:"+p.getHost().getPort());
		Color c = this.colors.remove(0);
		p.setColor(c);
		p.setStartXPos(this.startX[idPlayer-1]);
		p.setStartYPos(this.startY[idPlayer-1]);
		p.setStartDirection(this.startDirections[idPlayer-1]);
		p.setId((char)(idPlayer + '0'));
		idPlayer += 1;
		this.room.addPlayer(p);
		Controller.getInstance().getStartPanel().informServerHostRegistred(this.room);
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
		return c;
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

    /** Costruzione dell'array dei colori disponibili [MAX 6 PLAYER -> 6 COLOR]*/
    public void inizializeColors() {
    	this.colors = new ArrayList<Color>();
    	this.colors.add(Color.red);
    	this.colors.add(Color.cyan);
    	this.colors.add(Color.green);
    	this.colors.add(Color.yellow);
    	this.colors.add(Color.blue);
    	this.colors.add(Color.gray);
    }
}