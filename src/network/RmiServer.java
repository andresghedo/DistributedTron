package network;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;


public class RmiServer extends UnicastRemoteObject implements InterfaceRemoteMethod {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int PORT = 1234;
	
	/* costructor */
	public RmiServer()throws RemoteException {
		
	}
	
	public void action1() {
		// TODO
		System.out.println("ACTION 1");
    }
    
    public void action2() {
    	// TODO
        System.out.println("ACTION 2");
    }
    
    /* prima si lanciava il server separatamente dalla classe client, ora basta lanciare la classe
     * StartPlay decidendo se si è registration server o meno dai parametri in input
    public static void main(String[] args) throws UnknownHostException, SocketException, RemoteException, AlreadyBoundException {
        
    	System.out.println("Numero di giocatori per iniziare: "+Integer.parseInt(args[0]));
    	String ipserver = NetworkUtility.getInstance().getHostAddress();
    	System.out.println("[SERVER] IP : " + ipserver + "  in ascolto sulla porta "+PORT+"....");
        System.setProperty("java.rmi.server.hostname", ipserver);
        System.setProperty("java.rmi.disableHttp", "true");
        InterfaceRemoteMethod server = new RmiServer();
        InterfaceRemoteMethodRegistration serverRegistration = new RmiServerRegistration(Integer.parseInt(args[0]));
        //Registry registry1 = LocateRegistry.getRegistry();
        Registry registry1 = LocateRegistry.createRegistry(PORT);
        registry1.bind("MethodService", server);
        registry1.bind("RegistrationService", serverRegistration);
    }
    */

}