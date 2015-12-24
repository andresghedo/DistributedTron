package registration;

import java.util.ArrayList;
import network.Host;

public class Room {
	
	/* numero di giocatori necessari per iniziare il gioco */
	private int startPlayers;
	/* arraylist di host*/
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

	/* controlla la disponibilità ad accogliere altri host o meno */
	public boolean isCompleted() {
		if ((this.startPlayers - this.hosts.size())==0)
			return true;
		return false;
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
}
