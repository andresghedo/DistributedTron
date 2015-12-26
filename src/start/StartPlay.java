package start;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import network.Host;
import network.InterfaceRemoteMethod;
import network.NetworkUtility;
import network.RmiServer;
import registration.InterfaceRemoteMethodRegistration;
import registration.RmiServerRegistration;

public class StartPlay {

	private static int PORT = 1234;
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
			System.out.println("[REGISTRATION] Numero di giocatori per iniziare: "+nPlayers);
			String ipserver = NetworkUtility.getInstance().getHostAddress();
			System.out.println("[SERVER] IP : " + ipserver + "  in ascolto sulla porta "+PORT+"....");
		    
			System.setProperty("java.rmi.server.hostname", ipserver);
		    System.setProperty("java.rmi.disableHttp", "true");
		    
		    InterfaceRemoteMethod server = new RmiServer();
		    InterfaceRemoteMethodRegistration serverRegistration = new RmiServerRegistration(nPlayers);
		    //Registry registry1 = LocateRegistry.getRegistry();
		    Registry registry1 = LocateRegistry.createRegistry(PORT);
		    registry1.bind("MethodService", server);
		    registry1.bind("RegistrationService", serverRegistration);
		    Host myHost = new Host(NetworkUtility.getInstance().getHostAddress(), 1234);
		    serverRegistration.addPlayer(myHost);
		}
		else if (IP.startsWith("192.168")){
			System.out.println("[CLIENT]");
			InterfaceRemoteMethod remoteServer = null;
			InterfaceRemoteMethodRegistration registrationServer = null;
			Registry register = LocateRegistry.getRegistry(IP, PORT);
			remoteServer = (InterfaceRemoteMethod) register.lookup("MethodService");
			registrationServer = (InterfaceRemoteMethodRegistration) register.lookup("RegistrationService");    
			Host myHost = new Host(NetworkUtility.getInstance().getHostAddress(), 1234);
			registrationServer.addPlayer(myHost);
			Scanner scanner=new Scanner(System.in);
			while (true) {
	    		System.out.println("[INPUT] Insert 1 or 2 for remote method action1 or action2:");
		        String question = scanner.nextLine();
		        if(question.equals("q")) {
		            break;
		        }
		        else if(question.equals("1")) {
		            remoteServer.action1() ;
		        }
		        else if(question.equals("2")) {
		        	remoteServer.action2() ;
		        }
		    } 
		}
	}
}
