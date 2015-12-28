package network;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import registration.InterfaceRemoteMethodRegistration;
import registration.RmiServerRegistration;
import registration.Room;

public class Controller {
	
private static Controller instance;
	
	private Host host;
	private Room room;
	private Registry registry;
	private InterfaceRemoteMethodRegistration serverRegistration;
	private InterfaceRemoteMethod server;
	private static int PORT = 1234;

	/* cerca l'istanza, se la trova la torna altrimenti la crea */
	public static Controller getInstance() throws RemoteException, UnknownHostException, SocketException {
        if(instance == null)
            instance = new Controller();
        return instance;
    }
	
	public Controller() throws RemoteException, UnknownHostException, SocketException {
		System.setProperty("java.rmi.server.hostname", NetworkUtility.getInstance().getHostAddress());
	    System.setProperty("java.rmi.disableHttp", "true");
		this.registry = LocateRegistry.createRegistry(PORT);
	}
	
	public Host getMyHost() {
		return this.host;
	}
	
	public void setMyHost(Host host) {
		this.host = host;
	}
	
	public Room getRoom() {
		return this.room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public RmiServer setDeamon() throws AccessException, RemoteException, AlreadyBoundException {
	    this.server = new RmiServer();
	    this.registry.bind("MethodService", server);
	    return (RmiServer) this.server;
	}
	
	public RmiServerRegistration setDeamonRegistration(int nPlayers) throws RemoteException, AlreadyBoundException {
		this.serverRegistration = new RmiServerRegistration(nPlayers);
		this.registry.bind("RegistrationService", serverRegistration);
		return (RmiServerRegistration) this.serverRegistration;
	}
}
