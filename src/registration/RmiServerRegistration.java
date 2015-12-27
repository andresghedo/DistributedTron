package registration;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import network.Host;

public class RmiServerRegistration extends UnicastRemoteObject implements InterfaceRemoteMethodRegistration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Room room;
	/* costructor */
	public RmiServerRegistration(int sp)throws RemoteException {
		this.room = new Room(sp);
	}
	
	public void addPlayer(Host host) {
		System.out.println("[REGISTRAZIONE] Aggiunto il player con host IP: "+host.getIP()+"  e PORT:"+host.getPort());
		this.room.addHost(host);
		System.out.println("[REGISTRAZIONE] Numero di host mancanti: " + this.room.getMissingPlayers());
    }
    
    public int getCurrentPlayers() {
        return this.room.getCurrentPlayers();
    }
    
    public void printArrayList() {
    	this.room.printArrayList();
    }
    
    public Room getRoom() {
    	return this.room;
    }
}