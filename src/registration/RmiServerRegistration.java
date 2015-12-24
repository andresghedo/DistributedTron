package registration;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import network.Host;

public class RmiServerRegistration extends UnicastRemoteObject implements InterfaceRemoteMethodRegistration {

	private Room room;
	/* costructor */
	public RmiServerRegistration(int sp)throws RemoteException {
		this.room = new Room(sp);
	}
	
	public void addPlayer(Host host) {
		//System.out.println("a");
		System.out.println("[DEBUG REGISTRAZIONE] Aggiunto il player con host IP: "+host.getIP()+"  e PORT:"+host.getPort()+"\n");
        this.room.addHost(host);
    }
    
    public int getCurrentPlayers() {
        return this.room.getCurrentPlayers();
    }
}