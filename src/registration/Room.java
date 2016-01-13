package registration;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe che tiene traccia dei giocatori seduti alla stanza di gioco
 */
public class Room implements Serializable{

	private static final long serialVersionUID = 1L;
	/** numero di giocatori necessari per iniziare il gioco */
	private int startPlayers;
	/** arraylist di Host*/
	private ArrayList<Player> players;
	
	public Room() {
		this.players = new ArrayList<Player>();
	}

	public Room(int sp) {
		this.startPlayers = sp;
		this.players = new ArrayList<Player>();
	}
	
	public int getStartPlayers() {
		return this.startPlayers;
	}
	
	public void setStartPlayers(int sp) {
		this.startPlayers = sp;
	}

	public int getCurrentPlayers() {
		return this.players.size();
	}

	/** Controlla la disponibilità ad accogliere altri host o meno */
	public boolean isCompleted() {
		if ((this.startPlayers - this.players.size())==0)
			return true;
		return false;
	}
	
	/** Ritorna il numero di giocatori mancanti */
	public int getMissingPlayers() {
		return (this.startPlayers - this.getCurrentPlayers());
	}

	public void addPlayer(Player p) {
		this.players.add(p);
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public void removePlayerFromIndex(int index) {
		this.players.remove(index);
	}
	
	/** Ritorna l'Host successivo, nella configurazione dell'anello unidirezionale,
	 *  all'host dato come parametro.
	 */
	public Player getNext(Player p) {
		for (int i = 0; i < this.players.size(); i++) {
			Player current = this.players.get(i);
			if((current.getHost().getIP().equals(p.getHost().getIP())) && (current.getHost().getPort() == p.getHost().getPort()) && (current.getHost().getUUID().equals(p.getHost().getUUID()))) {
				if (i == (this.players.size()-1)){
					return this.players.get(0);
					}
				return this.players.get(i+1);
			}
        }
		return null;
	}
	
	/** 
	 *  Rimuove il player dato in input alla funzione.
	 */
	public void removePlayer(Player p) {
		for (int i = 0; i < this.players.size(); i++) {
			Player current = this.players.get(i);
			if((current.getHost().getIP().equals(p.getHost().getIP())) && (current.getHost().getPort() == p.getHost().getPort()) && (current.getHost().getUUID().equals(p.getHost().getUUID()))) {
				this.players.remove(i);
			}
        }
	}
}
