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
	/** FINESTRA DI GIOCO */
	public final int WIDTH = 1200, HEIGHT = 1000;
	/** GRANDEZZA QUADRATO DELLA MOTO */
	public final int SIZE_MOTO = 4;
	/** UPDATE DELLA FINESTRA */
	public final int TIMER_UPDATE = 50;
	/** VELOCITA DELLA MOTO*/
	public final int SPEED = 4;
	/** X MASSIMA DELLA MATRICE DI GIOCO DELLE MOTO */
	public final int MATRIX_X_SIZE = WIDTH / SPEED;
	/** Y MASSIMA DELLA MATRICE DI GIOCO DELLE MOTO */
	public final int MATRIX_Y_SIZE = (HEIGHT - 32) / SPEED;

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

	public String getIpPlayerFromUUid(String uuid) {
		for (int i=0; i<this.room.getPlayers().size(); i++) {
			if(this.room.getPlayers().get(i).getHost().getUUID().equals(uuid))
				return this.room.getPlayers().get(i).getHost().getIP();
		}
		return null;
	}

	/** 
	 * Setto la posizione iniziale (X,Y) e la direzione che l'host di registrazione ha previsto per il mio Player.
	 */
	public void setInitalSettingsPlayerFromUUid() {
		for (int i=0; i<this.room.getPlayers().size(); i++) {
			if(this.room.getPlayers().get(i).getHost().getUUID().equals(this.player.getHost().getUUID())) {
				this.player.setStartXPos(this.room.getPlayers().get(i).getStartXPos());
				this.player.setStartYPos(this.room.getPlayers().get(i).getStartYPos());
				this.player.setStartDirection(this.room.getPlayers().get(i).getStartDirection());
				}
		}
	}

	public void setIdPlayerFromUUid() {
		for (int i=0; i<this.room.getPlayers().size(); i++) {
			if(this.room.getPlayers().get(i).getHost().getUUID().equals(this.player.getHost().getUUID()))
				this.player.setId(this.room.getPlayers().get(i).getId());
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
