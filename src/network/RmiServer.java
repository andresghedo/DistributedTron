package network;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer extends UnicastRemoteObject implements InterfaceRemoteMethod {
	
	/* costructor */
	public RmiServer()throws RemoteException {
		
	}
	
	public double calculateSquareRoot(double aNumber) {
        return Math.sqrt(aNumber);
    }
    
    public double calculatePowerTwo(double aNumber) {
        return (aNumber*aNumber);
    }
    
    public static void main(String[] args) throws UnknownHostException, SocketException, RemoteException, AlreadyBoundException {
        
        int PORT = Integer.parseInt(args[0]);
        String ipserver = NetworkUtility.getInstance().getHostAddress();
        System.out.println("[SERVER] IP : " + ipserver + "  in ascolto sulla porta "+PORT+"....");
        System.setProperty("java.rmi.server.hostname", ipserver);
        System.setProperty("java.rmi.disableHttp", "true");
        InterfaceRemoteMethod server = new RmiServer();
        //Registry registry1 = LocateRegistry.getRegistry();
        Registry registry1 = LocateRegistry.createRegistry(PORT);
        registry1.bind("MethodService", server);
    }

}