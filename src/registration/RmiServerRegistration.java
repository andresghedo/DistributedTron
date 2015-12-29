package registration;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import network.Controller;
import network.Host;
import network.RmiMessage;

public class RmiServerRegistration extends UnicastRemoteObject implements InterfaceRemoteMethodRegistration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Room room;
	/* costructor */
	public RmiServerRegistration(int sp, Host myHost)throws RemoteException {
		this.room = new Room(sp);
		this.addPlayer(myHost);
	}
	
	public void addPlayer(Host host) {
		System.out.println("[REGISTRAZIONE] Aggiunto il player con host IP: "+host.getIP()+"  e PORT:"+host.getPort());
		this.room.addHost(host);
		System.out.println("[REGISTRAZIONE] Numero di host mancanti: " + this.room.getMissingPlayers());
		if (this.room.isCompleted()) {
			System.out.println("[REGISTRAZIONE] Room completata, sending configuration in ring...");
			Controller.getInstance().setRoom(this.room);
			String uuid = Controller.getInstance().getMyHost().getUUID();
			RmiMessage m = new RmiMessage(this.room, uuid);
			try {
				Controller.getInstance().getCommunication().getNextHostInterface().send(m);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
    public int getCurrentPlayers() {
        return this.room.getCurrentPlayers();
    }

    public Room getRoom() {
    	return this.room;
    }
}