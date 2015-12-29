package network;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import registration.InterfaceRemoteMethodRegistration;
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

	@Override
	public void send(RmiMessage message) {
		System.out.println("[MSG IN BOX] Arrivato un msg con uuid:"+message.getUuid());
		// TODO Auto-generated method stub
		if (!message.getUuid().equals(Controller.getInstance().getMyHost().getUUID()))
			processPackage(message);
		else if(message.getPayload() instanceof Room) {
			System.out.println("[MSG IN BOX] Il messaggio ha fatto il giro dell'anello, ora tutti gli host hanno la configurazione dell'anello!");
			this.sendStringInRing();
		}
		else 
			System.out.println("[MSG IN BOX] Il messaggio ha fatto il giro dell'anello e viene scartato!");
	}
	
	public void processPackage(RmiMessage message) {
		if (message.getPayload() instanceof Room){ // se il messaggio contiene una configurazione della Room settala
			Controller.getInstance().setRoom((Room) message.getPayload());
			try {
				this.getNextHostInterface().send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.sendStringInRing();	
		}
		if (message.getPayload() instanceof String) // il messaggio contiene una stringa	
			System.out.println("[MSG FROM REMOTE] String: " + (String)message.getPayload());
	}
	
	public InterfaceRemoteMethod getNextHostInterface() throws RemoteException, NotBoundException {
		Host h = Controller.getInstance().getMyHost();
		Host next = Controller.getInstance().getRoom().getNext(h);	
		InterfaceRemoteMethod remoteServer = null;
		Registry register = LocateRegistry.getRegistry(next.getIP(), PORT);
		remoteServer = (InterfaceRemoteMethod) register.lookup("MethodService");
		return remoteServer;
	}
	
	public void sendStringInRing() {
		Scanner scanner=new Scanner(System.in);
		while (true) {
    		System.out.println("[INPUT] Insert key");
	        String str = scanner.nextLine();
	        String uuid = Controller.getInstance().getMyHost().getUUID();
			RmiMessage m = new RmiMessage(str, uuid);
			try {
				this.getNextHostInterface().send(m);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	

}