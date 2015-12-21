package registration;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServerRegistration extends UnicastRemoteObject implements InterfaceRemoteMethodRegistration {

	private Room room;
	/* costructor */
	public RmiServerRegistration(int sp)throws RemoteException {
		this.room = new Room(sp);
	}
	
	public void incrementPlayers() {
		System.out.println("[DEBUG REGISTRAZIONE] Incremento del numero di players");
        this.room.incrementCurrentPlayers();
    }
    
    public int getCurrentPlayers() {
        return this.room.getCurrentPlayers();
    }
}