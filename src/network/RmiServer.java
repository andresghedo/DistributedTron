package network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import registration.Room;


public class RmiServer extends UnicastRemoteObject implements InterfaceRemoteMethod {

	private static final long serialVersionUID = 1L;
	static int PORT = 1234;
	
	public RmiServer()throws RemoteException {}

	public void setRingConfiguration(Room room) {
		// TODO Auto-generated method stub
		Controller.getInstance().setRoom(room);
		Room r = Controller.getInstance().getRoom();
		
		for (int i=0; i<r.getCurrentPlayers();i++) {
			Host corrente = r.getHosts().get(i);
			Host successivo = r.getNext(corrente);
			System.out.println("[SETTING RING CONFIGURAZTION]NODO "+i+" , ip: "+corrente.getIP()+"  ==> next ip: "+successivo.getIP());
		}
	}

	@Override
	public void send(RmiMessage message) {
		System.out.println("[MSG RECEIVED] UUID:"+message.getUuid());
		if (!message.getUuid().equals(Controller.getInstance().getMyHost().getUUID()))
			try {
				processPackage(message);
			} catch (InterruptedException e) {
				System.out.println("[INTERRUPTED EXCEPTION]");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServerNotActiveException e) {
				System.out.println("[SERVERNOTACTIVE EXCEPTION]");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				System.out.println("[REMOTE EXCEPTION]");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(message.getPayload() instanceof Room) {
			System.out.println("[MSG CONFIGURATION RETURN] OGNI HOST HA LA CONFIGURAZIONE DELL'ANELLO UNIDIREZIONALE!");
			(new InputThread()).start();
		}
		else 
			System.out.println("[MSG RECEIVED] IL MESS HA FATTO IL GIRO DELL'ANELLO COMPLETO SENZA ERRORI!");
	}
	
	public void processPackage(RmiMessage message) throws InterruptedException, ServerNotActiveException, RemoteException {
		if (message.getPayload() instanceof Room){ // se il messaggio contiene una configurazione della Room settala
			System.out.println("[MSG RECEIVED] ARRIVATO IL MESSAGGIO DI CONFIGURAZIONE DELL'ANELLO, ORA LO SETTO.");
			this.setRingConfiguration((Room) message.getPayload());
			(new InputThread()).start();
		}
		if (message.getPayload() instanceof String) { // il messaggio contiene una stringa	
			System.out.println("[MSG FROM REMOTE] String: " + (String)message.getPayload());
		}
		
		try {
			this.getNextHostInterface().send(message);
		} catch (NotBoundException e) {
			System.out.println("[NOTBOUND EXCEPTION]");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public InterfaceRemoteMethod getNextHostInterface() throws NotBoundException, ServerNotActiveException {
		Host h = Controller.getInstance().getMyHost();
		Host next = Controller.getInstance().getRoom().getNext(h);	
		InterfaceRemoteMethod remoteServer = null;
		Registry register = null;
		try {
			register = LocateRegistry.getRegistry(next.getIP(), PORT);
			remoteServer = (InterfaceRemoteMethod) register.lookup("MethodService");
		} catch (RemoteException e) {
			System.out.println("[REMOTE EXCEPTION] SU GETNEXTHOSTINTERFACE");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return remoteServer;
	}	
}