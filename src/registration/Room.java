package registration;

import java.io.Serializable;
import java.util.ArrayList;
import network.Host;
import network.RingList;

public class Room implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* numero di giocatori necessari per iniziare il gioco */
	private int startPlayers;
	/* arraylist di host*/
	private RingList<Host> hosts;
	
	public Room() {
		this.hosts = new RingList<Host>();
	}

	public Room(int sp) {
		this.startPlayers = sp;
		this.hosts = new RingList<Host>();
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
	
	public Host getNext(Host host) {
		return this.hosts.getNext(host);
	}
	
	public void printArrayList() {
		for (int i = 0; i < this.hosts.size(); i++) {
			System.out.println("[HOST "+i+"] Con uuid: "+this.hosts.get(i).getUUID());
        }
	}
}
