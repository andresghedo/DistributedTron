package registration;

import java.io.Serializable;
import java.util.ArrayList;
import network.Host;

public class Room implements Serializable{

	private static final long serialVersionUID = 1L;
	/** numero di giocatori necessari per iniziare il gioco */
	private int startPlayers;
	/** arraylist di Host*/
	private ArrayList<Host> hosts;
	
	public Room() {
		this.hosts = new ArrayList<Host>();
	}

	public Room(int sp) {
		this.startPlayers = sp;
		this.hosts = new ArrayList<Host>();
	}
	
	public int getStartPlayers() {
		return this.startPlayers;
	}
	
	public void setStartPlayers(int sp) {
		this.startPlayers = sp;
	}

	public int getCurrentPlayers() {
		return this.hosts.size();
	}

	/** Controlla la disponibilità ad accogliere altri host o meno */
	public boolean isCompleted() {
		if ((this.startPlayers - this.hosts.size())==0)
			return true;
		return false;
	}
	
	/** Ritorna il numero di giocatori mancanti */
	public int getMissingPlayers() {
		return (this.startPlayers - this.getCurrentPlayers());
	}

	public void addHost(Host host) {
		this.hosts.add(host);
	}

	public ArrayList<Host> getHosts() {
		return this.hosts;
	}

	public void removeHost(Host host) {
		this.hosts.remove(host);
	}

	public void removeHostFromIndex(int index) {
		this.hosts.remove(index);
	}
	
	/** Ritorna l'Host successivo, nella configurazione dell'anello unidirezionale,
	 *  all'host dato come parametro.
	 */
	public Host getNext(Host host) {
		for (int i = 0; i < this.hosts.size(); i++) {
			Host current = this.hosts.get(i);
			if((current.getIP().equals(host.getIP())) && (current.getPort() == host.getPort()) && (current.getUUID().equals(host.getUUID()))){
				if (i == (this.hosts.size()-1)){
					return this.hosts.get(0);
					}
				return this.hosts.get(i+1);
			}
        }
		return null;
	}
}
