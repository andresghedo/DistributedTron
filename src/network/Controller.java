package network;

import graphics.SimpleTronFrame;

import java.awt.Color;
import java.util.ArrayList;

import registration.Player;
import registration.Room;
/**
 *  Classe Controller, implementa il pattern SINGLETON. La classe contiene tutti gli
 *  elementi necessari per amministrare l'Host. La seguente soluzione permette di richiamare
 *  l'unica istanza del Controller da qualsiasi punto del codice ed accedere tramite i getters 
 *  ad elementi fondamentali per amministrare diversi aspetti(ad esempio accedere alla configurazione
 *  dell'anello, ai dati personali dell'host, oppure al server in ascolto di client per l'esportazione
 *  dei propri metodi remoti).
 *  @author andreasd
 *
 */
public class Controller {
	/** istanza singleton */
	private static Controller instance;
	/** istanza della classe Room */
	private Room room;
	/** istanza della classe RmiServer dell'host corrente */
	private RmiServer communication;
	/** istanza della classe Player che identifica il giocatore della macchina */
	private Player player;
	/** variabile booleana per mostrare GUI o meno [utilizzata in fase di sviluppo] */
	private boolean showGUI = false;
	/** istanza della classe SimpleTronFrame che fornisce il JFrame della GUI */
	private SimpleTronFrame frameGUI;
	/** booleano che notifica se il player sta già giocando o meno */
	private boolean isGaming = false;
	
	/** cerca l'istanza singleton, se la trova la torna altrimenti la crea */
	public static Controller getInstance() {
        if(instance == null)
            instance = new Controller();
        return instance;
    }
	
	public Player getMyPlayer() {
		return this.player;
	}
	
	public void setMyPlayer(Player p) {
		this.player = p;
	}
	
	public Host getMyHost() {
		return this.player.getHost();
	}
	
	public Room getRoom() {
		return this.room;
	}
	
	/** 
	 *  Quando setto la Room che mi arriva dall'host che implementa il servizi di registrazione
	 *  centralizzato, vado a cercare e settare che colore ha impostato per il mio Player.
	 */
	public void setRoom(Room room) {
		this.room = room;
		this.setMyColor();
		System.out.println("[COLOR DEBUG] MY COLOR:"+this.player.getColor().toString());
	}
	
	public RmiServer getCommunication() {
		return this.communication;
	}
	
	public void setCommunication(RmiServer c) {
		this.communication = c;
	}
	
	/**
	 * Cerco nella Room l'host che mi rappresenta e ne prelevo il colore e lo setto. 
	 */
	public void setMyColor() {
		ArrayList<Player> p = this.room.getPlayers();
		for (int i=0; i<p.size();i++) {
			if(p.get(i).getHost().getUUID().equals(this.player.getHost().getUUID()))
				this.player.setColor(p.get(i).getColor());
		}
	}
	
	public void setShowGUI(boolean b) {
		this.showGUI = b;
	}
	
	public boolean getShowGUI() {
		return this.showGUI;
	}
	
	public Color getColorPlayerFromUUid(String uuid) {
		for (int i=0; i<this.room.getPlayers().size(); i++) {
			if(this.room.getPlayers().get(i).getHost().getUUID().equals(uuid))
				return this.room.getPlayers().get(i).getColor();
		}
		return null;
	}
	
	//prende in input un color e ti ritorna il giocatore che equivale a quell'colore
	public Player getPlayerByColor(Color c){
		for (int i =0; i < this.room.getPlayers().size(); i++){
			if(this.room.getPlayers().get(i).getColor().equals(c)){
				return this.room.getPlayers().get(i);
			}
		}
		return null;
	}
	
	public String getIpPlayerFromUUid(String uuid) {
		for (int i=0; i<this.room.getPlayers().size(); i++) {
			if(this.room.getPlayers().get(i).getHost().getUUID().equals(uuid))
				return this.room.getPlayers().get(i).getHost().getIP();
		}
		return null;
	}
	
	/** 
	 * Setto la posizione iniziale che l'host di registrazione ha previsto per il mio Player. 
	 */
	public void setInitalXPlayerFromUUid() {
		for (int i=0; i<this.room.getPlayers().size(); i++) {
			if(this.room.getPlayers().get(i).getHost().getUUID().equals(this.player.getHost().getUUID()))
				this.player.setStartXPos(this.room.getPlayers().get(i).getStartXPos());
		}
	}
	
	public void setFrameGUI(SimpleTronFrame frame) {
		this.frameGUI = frame;
	}
	
	public SimpleTronFrame getFrameGUI() {
		return this.frameGUI;
	}

	public void setIsGaming(boolean b) {
		this.isGaming = b;
	}

	public boolean getIsGaming() {
		return this.isGaming;
	}
}
