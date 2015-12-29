package start;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import network.Controller;
import network.Host;
import network.InterfaceRemoteMethod;
import network.NetworkUtility;
import network.RmiServer;
import registration.InterfaceRemoteMethodRegistration;
import registration.RmiServerRegistration;

public class StartPlay {

	private static int PORT = 1234;
	private static Registry registry;
	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws NumberFormatException 
	 * @throws AlreadyBoundException 
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 * @throws NotBoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, RemoteException, AlreadyBoundException, UnknownHostException, SocketException, NotBoundException {
		// TODO Auto-generated method stub
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
			InterfaceRemoteMethod remoteServer = null;
			InterfaceRemoteMethodRegistration registrationServer = null;
			Registry register = LocateRegistry.getRegistry(IP, PORT);
			remoteServer = (InterfaceRemoteMethod) register.lookup("MethodService");
			registrationServer = (InterfaceRemoteMethodRegistration) register.lookup("RegistrationService");    
			Host myHost = new Host(NetworkUtility.getInstance().getHostAddress(), 1234);
			Controller.getInstance().setMyHost(myHost);
			registrationServer.addPlayer(myHost);
			System.out.println("[REGISTRED]"); 
		}
	}
	
	public static void startDeamon() throws UnknownHostException, SocketException, RemoteException, AlreadyBoundException {
		System.setProperty("java.rmi.server.hostname", NetworkUtility.getInstance().getHostAddress());
	    System.setProperty("java.rmi.disableHttp", "true");
		registry = LocateRegistry.createRegistry(PORT);
		System.out.println("[DEBUG] Ora sono in ascolto da eventuali richieste dall'esterno");
		RmiServer server = new RmiServer();
		Controller.getInstance().setCommunication(server);
	    registry.bind("MethodService", server);
	}
	
	public static void startDeamonRegistration(int nPlayers, Host myHost) throws AccessException, RemoteException, AlreadyBoundException {
		System.out.println("[DEBUG REG] Io implemento il servizio di registrazione");
		RmiServerRegistration serverRegistration = new RmiServerRegistration(nPlayers, myHost);
		registry.bind("RegistrationService", serverRegistration);
	}
}
