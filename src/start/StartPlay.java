package start;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;

import network.Controller;
import network.Host;
import network.NetworkUtility;
import network.RmiServer;
import registration.InterfaceRemoteMethodRegistration;
import registration.RmiServerRegistration;

public class StartPlay {

	private static int PORT = 1234;
	private static Registry registry;
	/**
	 * Metodo di inizio, Main.
	 * @param args
	 * @throws RemoteException 
	 * @throws NumberFormatException 
	 * @throws AlreadyBoundException 
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 * @throws NotBoundException 
	 * @throws InterruptedException 
	 * @throws ServerNotActiveException 
	 */
	public static void main(String[] args) throws NumberFormatException, RemoteException, AlreadyBoundException, UnknownHostException, SocketException, NotBoundException, InterruptedException, ServerNotActiveException {
		String IP = args[0];
		if (IP.equals("SERVER")) {
			int nPlayers = Integer.parseInt(args[1]);
		    Host myHost = new Host(NetworkUtility.getInstance().getHostAddress(), 1234);
		    Controller.getInstance().setMyHost(myHost);
		    startDeamon();
		    startDeamonRegistration(nPlayers, myHost);
		}
		else if (IP.startsWith("192.168")) {
			
			startDeamon();
			InterfaceRemoteMethodRegistration registrationServer = null;
			Registry register = LocateRegistry.getRegistry(IP, PORT);
			registrationServer = (InterfaceRemoteMethodRegistration) register.lookup("RegistrationService");    
			Host myHost = new Host(NetworkUtility.getInstance().getHostAddress(), 1234);
			Controller.getInstance().setMyHost(myHost);
			registrationServer.addPlayer(myHost);
			System.out.println("[HOST REGISTRED]"); 
		}
	}
	
	/**
	 * Inizializzazione del Demone server in ascolto
	 * @throws UnknownHostException
	 * @throws SocketException
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public static void startDeamon() throws UnknownHostException, SocketException, RemoteException, AlreadyBoundException {
		System.setProperty("java.rmi.server.hostname", NetworkUtility.getInstance().getHostAddress());
	    System.setProperty("java.rmi.disableHttp", "true");
		registry = LocateRegistry.createRegistry(PORT);
		System.out.println("[SERVER] IN ASCOLTO...");
		RmiServer server = new RmiServer();
		Controller.getInstance().setCommunication(server);
	    registry.bind("MethodService", server);
	}
	
	/**
	 * Inizializzazione del demone server di registrazione in ascolto
	 * @param nPlayers
	 * @param myHost
	 * @throws AccessException
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 * @throws InterruptedException
	 * @throws ServerNotActiveException
	 */
	public static void startDeamonRegistration(int nPlayers, Host myHost) throws AccessException, RemoteException, AlreadyBoundException, InterruptedException, ServerNotActiveException {
		System.out.println("[REGISTRATION SERVICE] IN ASCOLTO...");
		RmiServerRegistration serverRegistration = new RmiServerRegistration(nPlayers, myHost);
		registry.bind("RegistrationService", serverRegistration);
	}
}
