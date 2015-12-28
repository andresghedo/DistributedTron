package network;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

import registration.Room;


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
    
    public void message1(String m) {
    	System.out.println("[MSG]  " + m);
    }

	@Override
	public void setRingConfiguration(Room room) throws RemoteException, UnknownHostException, SocketException {
		// TODO Auto-generated method stub
		Controller.getInstance().setRoom(room);
		Room r = Controller.getInstance().getRoom();
		
		for (int i=0; i<r.getCurrentPlayers();i++) {
			Host corrente = r.getHosts().get(i);
			Host successivo = r.getNext(corrente);
			System.out.println("NODO "+i+" , ip: "+corrente.getIP()+"  ==> next ip: "+successivo.getIP());
		}
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