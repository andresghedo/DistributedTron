package network;

import graphics.SimpleTronFrame;

import java.awt.Rectangle;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import registration.Player;
import registration.Room;


public class RmiServer extends UnicastRemoteObject implements InterfaceRemoteMethod {

	private static final long serialVersionUID = 1L;
	static int PORT = 1234;
	
	public RmiServer()throws RemoteException {}

	public void setRingConfiguration(Room room) {
		Controller.getInstance().setRoom(room);
		Room r = Controller.getInstance().getRoom();
		
		for (int i=0; i<r.getCurrentPlayers();i++) {
			Player corrente = r.getPlayers().get(i);
			Player successivo = r.getNext(corrente);
			System.out.println("[SETTING RING CONFIGURAZTION]NODO "+i+" , ip: "+corrente.getHost().getIP()+"  ==> next ip: "+successivo.getHost().getIP());
		}
	}

	@Override
	public void send(RmiMessage message) {
		try{
			System.out.println("[MSG RECEIVED] MSG FROM :"+Controller.getInstance().getIpPlayerFromUUid(message.getUuid()) + "  -  TYPE:"+ message.getPayload().getClass());
		} catch (Exception e) {
			System.out.println("[MSG RECEIVED] MSG FROM UUID:" + message.getUuid());
		}
		if (!message.getUuid().equals(Controller.getInstance().getMyHost().getUUID()))
			try {
				processPackage(message);
			} catch (InterruptedException e) {
				System.out.println("########### INTERRUPTED EXCEPTION @ RMISERVER.SEND ###########");
			} catch (ServerNotActiveException e) {
				System.out.println("########### SERVERNOTACTIVE EXCEPTION @ RMISERVER.SEND ###########");
			} catch (RemoteException e) {
				System.out.println("########### REMOTE EXCEPTION @ RMISERVER.SEND ###########");
			}
		else if(message.getPayload() instanceof Room) {
			System.out.println("[MSG CONFIGURATION RETURN] OGNI HOST HA LA CONFIGURAZIONE DELL'ANELLO UNIDIREZIONALE!");
			if (Controller.getInstance().getShowGUI()) {
				Controller.getInstance().setInitalYPlayerFromUUid();
				System.out.println("Y iniziale: "+Controller.getInstance().getMyPlayer().getStartXPos());
				Controller.getInstance().setFrameGUI(new SimpleTronFrame());
				}
			else 
				(new InputThread()).start();
		}
		else 
			System.out.println("[MSG RETURNED] IL MESS HA FATTO IL GIRO DELL'ANELLO COMPLETO SENZA ERRORI!");
	}
	
	public void processPackage(RmiMessage message) throws InterruptedException, ServerNotActiveException, RemoteException {
		if (message.getPayload() instanceof Room) { // se il messaggio contiene una configurazione della Room settala
			System.out.println("[MSG RECEIVED] ARRIVATO IL MESSAGGIO DI CONFIGURAZIONE DELL'ANELLO, ORA LO SETTO.");
			this.setRingConfiguration((Room) message.getPayload());
			if (Controller.getInstance().getShowGUI()) {
				Controller.getInstance().setInitalYPlayerFromUUid();
				System.out.println("[COORDINATE X]: "+Controller.getInstance().getMyPlayer().getStartXPos());
				Controller.getInstance().setFrameGUI(new SimpleTronFrame());
				}
			else 
				(new InputThread()).start();
		}
		if (message.getPayload() instanceof String) { // il messaggio contiene una stringa	
			System.out.println("[MSG FROM REMOTE] String: " + (String)message.getPayload());
		}
		if (message.getPayload() instanceof Rectangle) {
			Controller.getInstance().getFrameGUI().repaint((Rectangle) message.getPayload(), Controller.getInstance().getColorPlayerFromUUid(message.getUuid()));
		}
		
		try {
			this.getNextHostInterface().send(message);
		} catch (NotBoundException e) {
			System.out.println("########### NOTBOUND EXCEPTION @ RMISERVER.PROCESSPACKAGE ###########");
		}
	}
	
	public InterfaceRemoteMethod getNextHostInterface() throws NotBoundException, ServerNotActiveException {
		Player my = Controller.getInstance().getMyPlayer();
		Player next = Controller.getInstance().getRoom().getNext(my);	
		InterfaceRemoteMethod remoteServer = null;
		Registry register = null; 
		try {
			register = LocateRegistry.getRegistry(next.getHost().getIP(), PORT);
			remoteServer = (InterfaceRemoteMethod) register.lookup("MethodService");
		} catch (RemoteException e) {
			System.out.println("########### REMOTE EXCEPTION @ RMISERVER.GETNEXTHOSTINTERFACE ###########");
		} catch (Exception e) {
			System.out.println("########### GENERAL EXCEPTION @ RMISERVER.GETNEXTHOSTINTERFACE ###########");
		}
		return remoteServer;
	}	
}