package network;

import graphics.SimpleTronFrame;

import java.awt.Color;
import java.util.ArrayList;

import registration.Player;
import registration.Room;
/**
 * Classe Controller, implementa il pattern SINGLETON. La classe contiene tutti gli
 * elementi necessari per amministrare l'Host. La seguente soluzione permette di richiamare
 * l'unica istanza del Controller da qualsiasi punto del codice ed accedere tramite i getters 
 * ad elementi fondamentali per amministrare diversi aspetti(ad esempio accedere alla configurazione
 * dell'anello, ai dati personali dell'host, oppure al server in ascolto di client per l'esportazione
 * dei propri metodi remoti).
 * @author andreasd
 *
 */
public class Controller {
	
private static Controller instance;
	
	private Room room;
	private RmiServer communication;
	private Player player;
	private boolean showGUI = false;
	private SimpleTronFrame frameGUI;
	
	/* cerca l'istanza, se la trova la torna altrimenti la crea */
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
	
	public void setInitalYPlayerFromUUid() {
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
}
